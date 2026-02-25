package jihunCompany.ai_recommendation_sns.service;

import jihunCompany.ai_recommendation_sns.domain.UserAction;
import jihunCompany.ai_recommendation_sns.domain.action.ActionType;
import jihunCompany.ai_recommendation_sns.repository.UserActionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserActionServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @Autowired
    UserActionService userActionService;

    @Autowired
    UserActionRepository userActionRepository;

    @Test
    @DisplayName("게시글 조회 로그 저장")
    void 조회수_로그() {
        // given
        // 자기가 쓴글 자기가 본거임
        Long userId = userService.register("viewer", "1234");
        Long postId = postService.createPost(userId, "게시글 내용ㅇ요용ㅇ");

        // when
        userActionService.viewPost(userId, postId);

        // then
        List<UserAction> actions = userActionRepository.findAll();

        assertThat(actions).hasSize(1);
        assertThat(actions.get(0).getActionType()).isEqualTo(ActionType.VIEW);
    }


}