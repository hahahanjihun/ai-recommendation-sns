package jihunCompany.ai_recommendation_sns.dto.Post.PostDetail;

import jihunCompany.ai_recommendation_sns.dto.Comment.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/*
게시글 눌렀을때 보여주는 정보 DTO
 */
@Getter
@AllArgsConstructor
public class PostDetailResponse {
    private Long postId;
    private String content;

    private long likeCount;
    private long viewCount;
    private long commentCount;

    private boolean likedByCurrentUser;

    private List<CommentResponse> comments;
}
