package jihunCompany.ai_recommendation_sns.dto.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/*
게시글 상세조회할때 댓글 긁어오는거
 */
@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long commentId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
}
