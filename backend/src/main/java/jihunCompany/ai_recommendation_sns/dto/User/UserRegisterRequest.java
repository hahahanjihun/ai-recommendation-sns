package jihunCompany.ai_recommendation_sns.dto.User;

import lombok.Getter;

@Getter
public class UserRegisterRequest {
    private String username; //json에서 username으로 받음
    private String password; //json에서 password로 받음
}
