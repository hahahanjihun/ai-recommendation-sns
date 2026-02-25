package jihunCompany.ai_recommendation_sns.service.recommendation;

import jihunCompany.ai_recommendation_sns.domain.Post;
import jihunCompany.ai_recommendation_sns.domain.UserAction;
import jihunCompany.ai_recommendation_sns.domain.action.ActionType;
import jihunCompany.ai_recommendation_sns.domain.recommendation.Recommendation;
import jihunCompany.ai_recommendation_sns.dto.recommendation.ActionDto;
import jihunCompany.ai_recommendation_sns.dto.recommendation.CandidatePostDto;
import jihunCompany.ai_recommendation_sns.dto.recommendation.RecommendationViewResponse;
import jihunCompany.ai_recommendation_sns.dto.recommendation.RecommendationSaveRequest;
import jihunCompany.ai_recommendation_sns.repository.PostRepository;
import jihunCompany.ai_recommendation_sns.repository.UserActionRepository;
import jihunCompany.ai_recommendation_sns.repository.recommendation.RecommendationRepository;
import jihunCompany.ai_recommendation_sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final PostRepository postRepository;
    private final PythonRecommendationClient pythonClient;
    private final UserActionRepository userActionRepository;

    // ------------------ 콜드스타트 계산만 (DB 저장 제거) ------------------
    public List<RecommendationViewResponse> generateColdStartRecommendations(int topK) {
        List<Post> allPosts = postRepository.findAll();
        if (allPosts.isEmpty()) return List.of();
        //한번에 집계
        List<Object[]> likeResults =
                userActionRepository.countByActionTypeGrouped(ActionType.LIKE);

        Map<Long, Long> likeCounts = new HashMap<>();

        for (Object[] row : likeResults) {
            likeCounts.put((Long) row[0], (Long) row[1]);
        }
        //좋아요수로 정렬
        allPosts.sort((a, b) -> {
            long likeA = likeCounts.getOrDefault(a.getId(), 0L);
            long likeB = likeCounts.getOrDefault(b.getId(), 0L);

            if (likeA != likeB) return Long.compare(likeB, likeA);
            return b.getCreatedAt().compareTo(a.getCreatedAt());
        });

        List<Long> postIds = allPosts.stream().map(Post::getId).toList();
        List<Object[]> counts = userActionRepository.countActionsByPostIds(postIds);

        Map<Long, Long> likeCount = new HashMap<>();
        Map<Long, Long> viewCount = new HashMap<>();
        Map<Long, Long> commentCount = new HashMap<>();

        for (Object[] row : counts) {
            Long postId = (Long) row[0];
            ActionType type = (ActionType) row[1];
            Long count = (Long) row[2];

            switch (type) {
                case LIKE -> likeCount.put(postId, count);
                case VIEW -> viewCount.put(postId, count);
                case COMMENT -> commentCount.put(postId, count);
            }
        }

        List<RecommendationViewResponse> result = new ArrayList<>();
        int rank = 1;
        for (Post post : allPosts) {
            if (rank > topK) break;

            result.add(new RecommendationViewResponse(
                    post.getId(),
                    post.getContent(),
                    post.getAuthor().getUsername(),
                    0.0,                // score 콜드스타트용
                    rank++,
                    likeCount.getOrDefault(post.getId(), 0L),
                    viewCount.getOrDefault(post.getId(), 0L),
                    commentCount.getOrDefault(post.getId(), 0L),
                    false               // likedByUser는 콜드스타트 무조건 false
            ));
        }

        return result;
    }

    // ------------------ DB 저장 헬퍼 ------------------
    public void saveToDB(Long userId, List<RecommendationViewResponse> list) {
        recommendationRepository.deleteByUserId(userId); // 기존 추천 삭제
        List<Recommendation> entities = new ArrayList<>();
        for (RecommendationViewResponse dto : list) {
            entities.add(Recommendation.of(userId, dto.getPostId(), dto.getScore(), dto.getRank()));
        }
        recommendationRepository.saveAll(entities);
    }

    // ------------------ Python 기반 추천 생성 + DB 저장 ------------------
    @Transactional
    public List<RecommendationViewResponse> regenerate(Long userId, int topK) {
        // 1️⃣ 유저 행동 조회
        List<UserAction> actions =
                userActionRepository.findTop5ByUser_IdOrderByCreatedAtDesc(userId);


        //행동없는 유저는 여전히 콜드스타트
        if(actions.isEmpty()){
            List<RecommendationViewResponse> cold =
                    generateColdStartRecommendations(topK);
            recommendationRepository.deleteByUserId(userId);
            saveToDB(userId, cold);
            return cold;
        }


        // 2️⃣ 추천 후보 게시글
        List<Post> candidates = postRepository.findAll();

        // 3️⃣ Python용 DTO 변환
        List<ActionDto> actionDtos = actions.stream()
                .map(ActionDto::from)
                .toList();

        List<CandidatePostDto> candidateDtos = candidates.stream()
                .map(CandidatePostDto::from)
                .toList();

        // 4️⃣ Python 추천 호출
        RecommendationSaveRequest result =
                pythonClient.recommend(
                        userId,
                        actionDtos,
                        candidateDtos,
                        topK
                );

        recommendationRepository.deleteByUserId(userId);

        save(result);


        return getExisting(userId);

    }

    // ------------------ 기존 추천 조회 ------------------
    public List<RecommendationViewResponse> getExisting(Long userId) {
        return mapToResponse(userId,recommendationRepository.findTop5ByUserIdOrderByRankAsc(userId));
    }

    public void save(RecommendationSaveRequest request) {
        List<Recommendation> list = new ArrayList<>();
        int rank = 1;
        for (var item : request.getRecommendations()) {
            list.add(Recommendation.of(
                    request.getUserId(),
                    item.getPostId(),
                    item.getScore(),
                    rank++));
        }
        recommendationRepository.saveAll(list);
    }

    private List<RecommendationViewResponse> mapToResponse(Long userId, List<Recommendation> recommendations) {
        if (recommendations.isEmpty()) return List.of();

        List<Long> postIds = recommendations.stream()
                .map(Recommendation::getPostId)
                .toList();

        List<Post> posts = postRepository.findAllWithAuthorByIdIn(postIds);

        // 한 번에 like/view/comment 수 집계
        List<Object[]> counts = userActionRepository.countActionsByPostIds(postIds);

        Map<Long, Long> likeCounts = new HashMap<>();
        Map<Long, Long> viewCounts = new HashMap<>();
        Map<Long, Long> commentCounts = new HashMap<>();

        for (Object[] row : counts) {
            Long postId = (Long) row[0];
            ActionType type = (ActionType) row[1];
            Long count = (Long) row[2];

            switch (type) {
                case LIKE -> likeCounts.put(postId, count);
                case VIEW -> viewCounts.put(postId, count);
                case COMMENT -> commentCounts.put(postId, count);
            }
        }

        List<RecommendationViewResponse> result = new ArrayList<>();
        for (Recommendation r : recommendations) {
            Post post = posts.stream()
                    .filter(p -> p.getId().equals(r.getPostId()))
                    .findFirst()
                    .orElseThrow();

            boolean likedByUser = userActionRepository.existsByUserIdAndPostIdAndActionType(
                    userId, post.getId(), ActionType.LIKE
            );

            result.add(new RecommendationViewResponse(
                    post.getId(),
                    post.getContent(),
                    post.getAuthor().getUsername(),
                    r.getScore(),
                    r.getRank(),
                    likeCounts.getOrDefault(post.getId(), 0L),
                    viewCounts.getOrDefault(post.getId(), 0L),
                    commentCounts.getOrDefault(post.getId(), 0L),
                    likedByUser
            ));
        }

        return result;
    }
}
