package jihunCompany.ai_recommendation_sns.dto.recommendation;

import lombok.AllArgsConstructor;
import lombok.Getter;
/*
화면에 추천게시글 보여주기용 DTO(프론트에 보내는 용도)
 */
@Getter
@AllArgsConstructor
public class RecommendationViewResponse {
    private Long postId;
    private String content;

    private String authorName;

    private Double score;
    private Integer rank;

    private long likeCount;
    private long viewCount;
    private long commentCount;

    private boolean likedByCurrentUser;
}
