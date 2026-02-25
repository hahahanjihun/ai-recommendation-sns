package jihunCompany.ai_recommendation_sns.repository;

import jihunCompany.ai_recommendation_sns.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저 저장 및 조회")
    void saveAndFindUser() {
        // given
        User user = new User("jihuns", "password123");
        userRepository.save(user);

        // when
        User foundUser = userRepository.findByUsername("jihuns").orElseThrow();

        // then
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }
}
