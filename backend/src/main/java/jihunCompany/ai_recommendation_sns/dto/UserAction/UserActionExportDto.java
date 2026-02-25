package jihunCompany.ai_recommendation_sns.dto.UserAction;

import jihunCompany.ai_recommendation_sns.domain.UserAction;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserActionExportDto {

    private Long userId;
    private Long postId;
    private String action;
    private LocalDateTime timestamp;

    public static UserActionExportDto from(UserAction ua) {
        return new UserActionExportDto(
                ua.getUser().getId(),
                ua.getPost().getId(),
                ua.getActionType().name(),
                ua.getCreatedAt()
        );
    }
}
