package jihunCompany.ai_recommendation_sns.dto.recommendation;

import jihunCompany.ai_recommendation_sns.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
/*
파이썬한테 보내는 후보 게시글 정보 DTO
 */
@Getter
@AllArgsConstructor
public class CandidatePostDto {
    private Long postId;
    private String content;

    public static CandidatePostDto from(Post post) {
        return new CandidatePostDto(
                post.getId(),
                post.getContent()
        );
    }
}
