package jihunCompany.ai_recommendation_sns.service;

import jihunCompany.ai_recommendation_sns.domain.Post;
import jihunCompany.ai_recommendation_sns.domain.User;
import jihunCompany.ai_recommendation_sns.domain.UserAction;
import jihunCompany.ai_recommendation_sns.domain.action.ActionType;
import jihunCompany.ai_recommendation_sns.dto.UserAction.ActionExportDto;
import jihunCompany.ai_recommendation_sns.dto.UserAction.UserActionExportDto;
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
public class UserActionService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserActionRepository userActionRepository;




    /*
    게시글 조회 로직
     */
    public void viewPost(Long userId, Long postId) {
        if (userActionRepository.existsByUserIdAndPostIdAndActionType(
                userId, postId, ActionType.VIEW)) {
            return;
        }

        //유저와 게시글 엔티티 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("유저 없음"));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

        userActionRepository.save(
                new UserAction(user, post, ActionType.VIEW)
        );
    }




    /*
    좋아요 토글
     */
    @Transactional
    public boolean toggleLike(Long userId, Long postId) {

        boolean exists =
                userActionRepository.existsByUserIdAndPostIdAndActionType(
                        userId, postId, ActionType.LIKE
                );

        if (exists) {
            userActionRepository.deleteByUserIdAndPostIdAndActionType(
                    userId, postId, ActionType.LIKE
            );
            return false; // 좋아요 취소 ->빈하트
        }

        User user = userRepository.findById(userId).orElseThrow();
        Post post = postRepository.findById(postId).orElseThrow();

        userActionRepository.save(
                new UserAction(user, post, ActionType.LIKE)
        );
        return true; // 좋아요 추가 ->빨간하트
    }




    /*
    모든 데이터 내보내기(DB에서 쿼리를 통해 바로 DTO로 변환)
     */
    // ---------------- 전체 export ----------------
    public List<ActionExportDto> exportAll() {
        return userActionRepository.findAll()
                .stream()
                .map(ActionExportDto::from)
                .toList();
    }

    // ---------------- 유저별 export ----------------
    public List<UserActionExportDto> exportByUser(Long userId) {
        return userActionRepository.findAllByUser_Id(userId)
                .stream()
                .map(UserActionExportDto::from)
                .toList();
    }

}
