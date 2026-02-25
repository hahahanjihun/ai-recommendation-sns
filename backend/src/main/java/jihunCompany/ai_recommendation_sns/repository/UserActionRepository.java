package jihunCompany.ai_recommendation_sns.repository;

import jihunCompany.ai_recommendation_sns.domain.UserAction;
import jihunCompany.ai_recommendation_sns.domain.action.ActionType;
import jihunCompany.ai_recommendation_sns.dto.UserAction.ActionExportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface UserActionRepository extends JpaRepository<UserAction, Long> {

    List<UserAction> findAllByUser_Id(Long userId);

    List<UserAction> findAllByPost_Id(Long postId);

    boolean existsByUserIdAndPostIdAndActionType(
            Long userId, Long postId, ActionType actionType
    );

    void deleteByUserIdAndPostIdAndActionType(
            Long userId, Long postId, ActionType actionType
    );


    /*
    모든 유저 액션 내보내기 export
     */
    @Query("""
            select new jihunCompany.ai_recommendation_sns.dto.UserAction.ActionExportDto(
                ua.user.id,
                ua.post.id,
                ua.actionType,
                ua.createdAt
            )
            from UserAction ua
            """)
    List<ActionExportDto> exportAll();


    /*
    특정 행동 유형별로 게시글 한번에 가져오기
     */
    @Query("""
            select ua.post.id, count(ua)
            from UserAction ua
            where ua.actionType = :actionType
            group by ua.post.id
            """)
    List<Object[]> countByActionTypeGrouped(@Param("actionType") ActionType actionType);


    /*
    좋아요 수 = countByPost_IdAndActionType(postId, LIKE)
    조회 수 = countByPost_IdAndActionType(postId, VIEW)
    댓글 수 = countByPost_IdAndActionType(postId, COMMENT)
    이런식으로 할 수있음
     */
    //long countByPost_IdAndActionType(Long postId, ActionType actionType);

    @Query("""
                SELECT ua.post.id, ua.actionType, COUNT(ua)
                FROM UserAction ua
                WHERE ua.post.id IN :postIds
                GROUP BY ua.post.id, ua.actionType
            """)
    List<Object[]> countActionsByPostIds(List<Long> postIds);

    /*
    이건 특정 유저가 특정 시간 이후로 한 행동 수 조회
    SELECT COUNT(*)
    FROM user_actions ua
    WHERE ua.user_id = ?
    AND ua.created_at > ?
    랑 같은거임
     */
    long countByUser_IdAndCreatedAtAfter(Long userId, LocalDateTime lastTime);

    List<UserAction> findTop5ByUser_IdOrderByCreatedAtDesc(Long userId);

}
