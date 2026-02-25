package jihunCompany.ai_recommendation_sns.service.recommendation;

import jihunCompany.ai_recommendation_sns.dto.recommendation.ActionDto;
import jihunCompany.ai_recommendation_sns.dto.recommendation.CandidatePostDto;
import jihunCompany.ai_recommendation_sns.dto.recommendation.RecommendationSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PythonRecommendationClient {

    private final RestTemplate restTemplate;

    // 환경변수 AI_URL이 있으면 사용, 없으면 기본값 localhost
    @Value("${AI_URL:http://localhost:8000}")
    private String aiUrl;

    public RecommendationSaveRequest recommend(
            Long userId,
            List<ActionDto> actions,
            List<CandidatePostDto> candidates,
            int topK) {

        Map<String, Object> body = new HashMap<>();
        body.put("userId", userId);
        body.put("topK", topK);
        body.put("candidates", candidates);
        body.put("actions", actions);

        return restTemplate.postForObject(
                aiUrl + "/recommend",  // 하드코딩 제거
                body,
                RecommendationSaveRequest.class
        );
    }
}
