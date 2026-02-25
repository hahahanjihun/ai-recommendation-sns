package jihunCompany.ai_recommendation_sns.dto.recommendation;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
/*
DB에 추천게시글 저장용도 DTO, 파이썬에서 추천결과를 받을 때 사용하는 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationSaveRequest {

    private Long userId;
    private List<RecommendationItem> recommendations;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendationItem {
        @JsonProperty("postId")
        @JsonAlias({"post_id", "postID"})
        private Long postId;

        private Double score;
        private Integer rank;
    }
}
