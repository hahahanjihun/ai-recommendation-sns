package jihunCompany.ai_recommendation_sns.service;

import jihunCompany.ai_recommendation_sns.domain.Comment;
import jihunCompany.ai_recommendation_sns.domain.Post;
import jihunCompany.ai_recommendation_sns.domain.User;
import jihunCompany.ai_recommendation_sns.domain.UserAction;
import jihunCompany.ai_recommendation_sns.domain.action.ActionType;
import jihunCompany.ai_recommendation_sns.dto.Comment.CommentExportDto;
import jihunCompany.ai_recommendation_sns.repository.CommentRepository;
import jihunCompany.ai_recommendation_sns.repository.PostRepository;
import jihunCompany.ai_recommendation_sns.repository.UserActionRepository;
import jihunCompany.ai_recommendation_sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserActionRepository userActionRepository;

    /*
    댓글작성
     */
    public Long createComment(Long userId, Long postId, String content) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저없음"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글없음"));

        Comment comment = new Comment(user, post, content);
        commentRepository.save(comment);

        UserAction commentAction = new UserAction(user, post, ActionType.COMMENT);
        userActionRepository.save(commentAction);

        return comment.getId();
    }

    /*
    모든 댓글 내보내기 export
     */
    public List<CommentExportDto> exportComments() {
        return commentRepository.findAll()
                .stream()
                .map(c -> new CommentExportDto(
                        c.getPost().getId(),
                        c.getContent()
                ))
                .toList();
    }

}
