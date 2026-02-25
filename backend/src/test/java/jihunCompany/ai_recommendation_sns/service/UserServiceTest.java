package jihunCompany.ai_recommendation_sns.service;

import jihunCompany.ai_recommendation_sns.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void 회원가입_로그인() {
        Long userId = userService.register("jihun", "1234");

        User user = userService.login("jihun", "1234");

        assertThat(user.getId()).isEqualTo(userId);
    }
}