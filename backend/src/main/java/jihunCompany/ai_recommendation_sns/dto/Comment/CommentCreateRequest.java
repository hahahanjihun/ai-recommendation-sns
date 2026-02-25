package jihunCompany.ai_recommendation_sns.dto.Comment;

import lombok.Getter;

@Getter
public class CommentCreateRequest {
    private Long userId;
    private Long postId;
    private String content;
}
