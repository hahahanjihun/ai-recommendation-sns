package jihunCompany.ai_recommendation_sns.service.recommendation;

import jihunCompany.ai_recommendation_sns.dto.Post.PostDetail.PostDetailResponse;
import jihunCompany.ai_recommendation_sns.dto.recommendation.RecommendationViewResponse;
import jihunCompany.ai_recommendation_sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationOrchestrator {

    private final RecommendationService recommendationService;
    private final RecommendationPolicyService policyService;
    private final PostService postService; // 🔥 최신 카운트 조회를 위해 주입

    public List<RecommendationViewResponse> recommend(Long userId, int topK) {
        List<RecommendationViewResponse> recommendations;

        // 추천 데이터 가져오기 (이 데이터 안의 Count는 과거의 값일 수 있음)
        // 1) 처음 방문한 유저: 콜드스타트 로직으로 추천 생성 (DB 저장 포함)
        if (!policyService.hasAny(userId)) {
            recommendations = recommendationService.generateColdStartRecommendations(topK);
            recommendationService.saveToDB(userId, recommendations);
        } else if (policyService.shouldRegenerate(userId)) { // 2) 기존에 추천이 있었지만, 재생성이 필요한 유저: AI 모델로 재생성 (DB 업데이트 포함)
            recommendations = recommendationService.regenerate(userId, topK);
        } else { // 3) 기존 추천이 유효한 유저: DB에서 기존 추천 가져오기
            recommendations = recommendationService.getExisting(userId);
        }

        // 추천된 글들에 최신 '좋아요수/댓글수' 정보를 덮어쓰기 (Data Hydration)
        return recommendations.stream()
                .map(rec -> {
                    // 명확하게 타입을 명시 (실무 스타일)
                    PostDetailResponse detail = postService.getPostDetail(rec.getPostId(), userId);

                    return new RecommendationViewResponse(
                            rec.getPostId(),
                            rec.getContent(),
                            rec.getAuthorName(),
                            rec.getScore(),
                            rec.getRank(),
                            detail.getLikeCount(),
                            detail.getViewCount(),
                            detail.getCommentCount(),
                            detail.isLikedByCurrentUser()
                    );
                }).toList();
    }
}
