package jihunCompany.ai_recommendation_sns.repository;

import jihunCompany.ai_recommendation_sns.domain.Post;
import jihunCompany.ai_recommendation_sns.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {
    //given
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Test
    void savePostTest() {
        User user = new User("jihun", "password123");
        userRepository.save(user);
        Post post = new Post(user, "This is a sample post content.");
        //when
        Post savedPost = postRepository.save(post);
        //then
        assertNotNull(savedPost.getId());
        assertEquals("This is a sample post content.", savedPost.getContent());
        assertEquals(user, savedPost.getAuthor());
    }


}