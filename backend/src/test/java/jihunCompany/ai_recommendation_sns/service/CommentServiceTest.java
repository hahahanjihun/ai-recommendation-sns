package jihunCompany.ai_recommendation_sns.service;

import jihunCompany.ai_recommendation_sns.domain.Comment;
import jihunCompany.ai_recommendation_sns.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 작성")
    void createComment() {
        // given
        Long userId = userService.register("jihun", "1234");
        Long postId = postService.createPost(userId, "게시글 내용요요요요용");

        // when
        Long commentId = commentService.createComment(
                userId,
                postId,
                "이거 댓글임"
        );

        // then
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글이 없습니다."));

        assertThat(comment.getContent()).isEqualTo("이거 댓글임");
        assertThat(comment.getPost().getId()).isEqualTo(postId);
        assertThat(comment.getUser().getId()).isEqualTo(userId);
    }
}