package jihunCompany.ai_recommendation_sns.controller.recommendation;

import jihunCompany.ai_recommendation_sns.dto.recommendation.RecommendationViewResponse;
import jihunCompany.ai_recommendation_sns.dto.recommendation.RecommendationSaveRequest;
import jihunCompany.ai_recommendation_sns.service.recommendation.RecommendationOrchestrator;
import jihunCompany.ai_recommendation_sns.service.recommendation.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationOrchestrator orchestrator;
    private final RecommendationService service; // 수동 저장용

    // ------------------ 추천 피드 조회 ------------------
    @GetMapping
    public List<RecommendationViewResponse> getRecommendations(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "5") int topK
    ) {
        return orchestrator.recommend(userId, topK);
    }

   // ------------------ 수동 저장 (필요할 때만 사용) ------------------
    @PostMapping("/save")
    public void save(@RequestBody RecommendationSaveRequest request) {
        service.save(request);
    }
}
