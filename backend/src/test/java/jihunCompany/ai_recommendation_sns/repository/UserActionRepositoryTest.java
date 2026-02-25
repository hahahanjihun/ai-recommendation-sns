package jihunCompany.ai_recommendation_sns.repository;

import jihunCompany.ai_recommendation_sns.domain.Post;
import jihunCompany.ai_recommendation_sns.domain.User;
import jihunCompany.ai_recommendation_sns.domain.UserAction;
import jihunCompany.ai_recommendation_sns.domain.action.ActionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class UserActionRepositoryTest {

    @Autowired
    private UserActionRepository userActionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired private PostRepository postRepository;

    @Test
    @DisplayName("사용자 행동 저장 및 조회 테스트")
    void saveAndFindUserAction() {
        // 1. Given: 유저와 포스트가 DB에 존재해야 함
        User user = new User("jihun", "pass123");
        userRepository.save(user);

        Post post = new Post(user, "테스트 게시글입니다.");
        postRepository.save(post);

        UserAction action = new UserAction(user, post, ActionType.LIKE); // 좋아요 액션
        userActionRepository.save(action);

        // 2. When: 다양한 조건으로 조회
        List<UserAction> userActions = userActionRepository.findAllByUser_Id(user.getId());
        List<UserAction> postActions = userActionRepository.findAllByPost_Id(post.getId());
        boolean isExists = userActionRepository.existsByUserIdAndPostIdAndActionType(
                user.getId(), post.getId(), ActionType.LIKE
        );

        // 3. Then: 검증
        assertEquals(1, userActions.size());
        assertEquals(ActionType.LIKE, userActions.get(0).getActionType());
        assertEquals(1, postActions.size());
        assertTrue(isExists);
    }

    @Test
    @DisplayName("존재하지 않는 액션 확인")
    void checkNonExistsAction() {
        // Given
        User user = new User("tester", "pass");
        userRepository.save(user);
        Post post = new Post(user, "content");
        postRepository.save(post);

        // When
        boolean isExists = userActionRepository.existsByUserIdAndPostIdAndActionType(
                user.getId(), post.getId(), ActionType.VIEW
        );

        // Then
        assertFalse(isExists, "저장한 적 없는 VIEW 액션은 false여야 합니다.");
    }
}