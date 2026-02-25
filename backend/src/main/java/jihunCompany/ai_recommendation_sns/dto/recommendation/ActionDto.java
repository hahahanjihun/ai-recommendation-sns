package jihunCompany.ai_recommendation_sns.dto.recommendation;

import jihunCompany.ai_recommendation_sns.domain.UserAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
/*
파이썬한테 보내는 유저 액션 정보 DTO
 */
@Getter
@AllArgsConstructor
public class ActionDto {
    private Long postId;
    private String action;
    private String content;

    public static ActionDto from(UserAction ua) {
        return new ActionDto(
                ua.getPost().getId(),
                ua.getActionType().name(),
                ua.getPost().getContent()
        );
    }
}
