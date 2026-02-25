package jihunCompany.ai_recommendation_sns.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    PostService postService;

    @Test
    void 게시글_작성() {
        Long userId = userService.register("user1", "1234");

        Long postId = postService.createPost(userId, "hello");

        assertThat(postId).isNotNull();
    }
}