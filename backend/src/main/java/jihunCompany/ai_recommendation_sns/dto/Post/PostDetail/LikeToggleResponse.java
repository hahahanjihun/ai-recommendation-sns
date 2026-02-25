package jihunCompany.ai_recommendation_sns.dto.Post.PostDetail;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*
게시글 클릭했을때의 좋아요 토글 응답 DTO
 */
@Getter
@AllArgsConstructor
public class LikeToggleResponse {
    private boolean likedByCurrentUser;
    private long likeCount;
}
