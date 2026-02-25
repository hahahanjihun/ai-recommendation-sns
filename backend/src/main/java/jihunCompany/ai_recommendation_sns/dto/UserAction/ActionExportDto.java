package jihunCompany.ai_recommendation_sns.dto.UserAction;

import jihunCompany.ai_recommendation_sns.domain.UserAction;
import jihunCompany.ai_recommendation_sns.domain.action.ActionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor // 기본 생성자는 프레임워크를 위해 유지
public class ActionExportDto {
    private Long userId;
    private Long postId;
    private String action;
    private LocalDateTime timestamp;

    // 핵심: JPQL의 'select new'가 호출할 생성자를 직접 만듭니다.
    // 세 번째 파라미터를 ActionType으로 받아야 쿼리와 일치합니다!
    public ActionExportDto(Long userId, Long postId, ActionType actionType, LocalDateTime timestamp) {
        this.userId = userId;
        this.postId = postId;
        this.action = actionType.name(); // 여기서 Enum을 String으로 변환
        this.timestamp = timestamp;
    }

    // 기존의 from 메서드도 그대로 유지 (서비스 단에서 쓸 때 편함)
    public static ActionExportDto from(UserAction action) {
        return new ActionExportDto(
                action.getUser().getId(),
                action.getPost().getId(),
                action.getActionType(), // 위에서 만든 생성자를 호출하도록 수정
                action.getCreatedAt()
        );
    }
}
