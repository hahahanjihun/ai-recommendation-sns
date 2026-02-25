package jihunCompany.ai_recommendation_sns.service.recommendation;

import jihunCompany.ai_recommendation_sns.repository.UserActionRepository;
import jihunCompany.ai_recommendation_sns.repository.recommendation.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RecommendationPolicyService {

    private final RecommendationRepository recommendationRepository;
    private final UserActionRepository userActionRepository;

    /*
    유저에 대한 추천이 존재하는지 여부
     */
    public boolean hasAny(Long userId) {
        return recommendationRepository.existsByUserId(userId);
    }

    /*
    추천 재생성 필요 여부 확인
     */
    public boolean shouldRegenerate(Long userId) {
        LocalDateTime last =
                recommendationRepository.findLatestCreatedAt(userId);

        if (last == null) return false;

        boolean timeExpired =
                last.isBefore(LocalDateTime.now().minusMinutes(50));

        long recentActions =
                userActionRepository.countByUser_IdAndCreatedAtAfter(
                        userId, last
                );

        return timeExpired || (recentActions >= 5);
    }
}
