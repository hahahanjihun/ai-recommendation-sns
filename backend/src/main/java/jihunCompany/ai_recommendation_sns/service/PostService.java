package jihunCompany.ai_recommendation_sns.service;

import jihunCompany.ai_recommendation_sns.domain.Post;
import jihunCompany.ai_recommendation_sns.domain.User;
import jihunCompany.ai_recommendation_sns.domain.action.ActionType;
import jihunCompany.ai_recommendation_sns.dto.Comment.CommentResponse;
import jihunCompany.ai_recommendation_sns.dto.Post.PostDetail.LikeToggleResponse;
import jihunCompany.ai_recommendation_sns.dto.Post.PostDetail.PostDetailResponse;
import jihunCompany.ai_recommendation_sns.dto.Post.PostExportDto;
import jihunCompany.ai_recommendation_sns.dto.Post.MyPostResponse;
import jihunCompany.ai_recommendation_sns.repository.CommentRepository;
import jihunCompany.ai_recommendation_sns.repository.PostRepository;
import jihunCompany.ai_recommendation_sns.repository.UserActionRepository;
import jihunCompany.ai_recommendation_sns.repository.UserRepository;
import jihunCompany.ai_recommendation_sns.service.recommendation.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final UserActionRepository userActionRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserActionService userActionService;
    private final RecommendationService recommendationService;
    private final CommentRepository commentRepository;
    /*
    게시글 만들기
     */
    public Long createPost(Long userId, String content) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));
        Post post = new Post(user, content);
        postRepository.save(post);
        recommendationService.regenerate(userId, 5);
        return post.getId();
    }

    /*
    자기 게시물 보기
     */
    @Transactional(readOnly = true)
    public List<MyPostResponse> getPostsByAuthor(Long userId) {
        List<Post> posts = postRepository.findByAuthor_idOrderByCreatedAtDesc(userId);
        if (posts.isEmpty()) return List.of();

        List<Long> postIds = posts.stream().map(Post::getId).toList();

        // 1️⃣ 좋아요/조회수는 UserAction 집계
        List<Object[]> counts = userActionRepository.countActionsByPostIds(postIds);
        Map<Long, Long> likeCounts = new HashMap<>();
        Map<Long, Long> viewCounts = new HashMap<>();
        for (Object[] row : counts) {
            Long postId = (Long) row[0];
            ActionType type = (ActionType) row[1];
            Long count = (Long) row[2];
            switch (type) {
                case LIKE -> likeCounts.put(postId, count);
                case VIEW -> viewCounts.put(postId, count);
            }
        }

        // 2️⃣ 댓글 수는 CommentRepository에서 집계
        List<Object[]> commentResults = commentRepository.countByPostIds(postIds);
        Map<Long, Long> commentCounts = new HashMap<>();
        for (Object[] row : commentResults) {
            Long postId = (Long) row[0];
            Long count = (Long) row[1];
            commentCounts.put(postId, count);
        }

        // 3️⃣ DTO 생성
        List<MyPostResponse> result = new ArrayList<>();
        for (Post post : posts) {
            result.add(new MyPostResponse(
                    post.getId(),
                    post.getContent(),
                    likeCounts.getOrDefault(post.getId(), 0L),
                    viewCounts.getOrDefault(post.getId(), 0L),
                    commentCounts.getOrDefault(post.getId(), 0L),
                    post.getCreatedAt()
            ));
        }
        return result;
    }





    /*
    게시글 좋아요수, 조회수, 댓글수 조회
     */
    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetail(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        List<Long> postIds = List.of(postId);
        List<Object[]> counts = userActionRepository.countActionsByPostIds(postIds);

        Map<Long, Long> likeCountMap = new HashMap<>();
        Map<Long, Long> viewCountMap = new HashMap<>();
        Map<Long, Long> commentCountMap = new HashMap<>();

        for (Object[] row : counts) {
            Long id = (Long) row[0];
            ActionType type = (ActionType) row[1];
            Long count = (Long) row[2];

            switch (type) {
                case LIKE -> likeCountMap.put(id, count);
                case VIEW -> viewCountMap.put(id, count);
                case COMMENT -> commentCountMap.put(id, count);
            }
        }

        long likeCount = likeCountMap.getOrDefault(postId, 0L);
        long viewCount = viewCountMap.getOrDefault(postId, 0L);

        List<CommentResponse> commentResponse = post.getComments().stream()
                .map(c -> new CommentResponse(
                        c.getId(),
                        c.getUser().getUsername(),
                        c.getContent(),
                        c.getCreatedAt()
                ))
                .toList();

        boolean likedByCurrentUser = userActionRepository.existsByUserIdAndPostIdAndActionType(
                userId, postId, ActionType.LIKE
        );

        return new PostDetailResponse(
                post.getId(),
                post.getContent(),
                likeCount,
                viewCount,
                commentResponse.size(),
                likedByCurrentUser,
                commentResponse
        );
    }

    /*
    게시글 클릭해서 들어갔을때 좋아요 누르거나 할때 실시간으로 반영해주려고
     */
    public long getLikeCount(Long postId) {
        Map<Long, Long> likeCountsMap = new HashMap<>();
        List<Object[]> results = userActionRepository.countByActionTypeGrouped(ActionType.LIKE);
        for (Object[] row : results) {
            likeCountsMap.put((Long) row[0], (Long) row[1]);
        }

        return likeCountsMap.getOrDefault(postId, 0L);
    }

    /*
    postController에서 postDetail에서 좋아요 토글 요청 처리
     */
    @Transactional
    public LikeToggleResponse toggleLike(Long postId, Long userId) {

        boolean liked = userActionService.toggleLike(userId, postId);

        long likeCount = getLikeCount(postId);

        return new LikeToggleResponse(liked, likeCount);
    }


    /*
    모든 게시글 내보내기 export
     */
    public List<PostExportDto> exportPosts() {
        return postRepository.findAll()
                .stream()
                .map(p -> new PostExportDto(
                        p.getId(),
                        p.getContent()
                ))
                .toList();
    }
}
