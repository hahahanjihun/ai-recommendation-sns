package jihunCompany.ai_recommendation_sns.dto.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {
    private String username;
    private String password;
}
