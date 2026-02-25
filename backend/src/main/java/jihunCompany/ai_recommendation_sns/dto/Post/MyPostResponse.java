package jihunCompany.ai_recommendation_sns.dto.Post;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class MyPostResponse {
    private Long postId;
    private String content;

    private long likeCount;
    private long viewCount;
    private long commentCount;

    private LocalDateTime createdAt;
}
