package jihunCompany.ai_recommendation_sns.repository;

import jihunCompany.ai_recommendation_sns.domain.Comment;
import jihunCompany.ai_recommendation_sns.domain.Post;
import jihunCompany.ai_recommendation_sns.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class CommentRepositoryTest {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    User user = new User("jihun", "password");
    Post post = new Post(user, "This is a sample post content.");
    Comment comment = new Comment(user,post, "댓글댓글댓글");

    @Test
    void saveCommentTest() {
        //given
        userRepository.save(user);
        postRepository.save(post);
        //when
        Comment savedComment = commentRepository.save(comment);

        //then
        assertNotNull(comment);
        assertEquals("댓글댓글댓글", savedComment.getContent());
        assertEquals(user, savedComment.getUser());
        assertEquals(post, savedComment.getPost());
    }



}