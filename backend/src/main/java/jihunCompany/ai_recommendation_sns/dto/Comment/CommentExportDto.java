package jihunCompany.ai_recommendation_sns.dto.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentExportDto {
    private Long postId;
    private String content;
}
