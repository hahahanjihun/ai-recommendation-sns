package jihunCompany.ai_recommendation_sns.domain;

import jakarta.persistence.*;
import jihunCompany.ai_recommendation_sns.domain.action.ActionType;
import jihunCompany.ai_recommendation_sns.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access =  AccessLevel.PROTECTED)
@Table(name = "user_actions",
       uniqueConstraints = {
               @UniqueConstraint(
                       name = "uk_user_post_action",
                       columnNames = {"user_id", "post_id", "actionType"}
               )
       }
)
public class UserAction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 행동한 유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 대상 게시글
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActionType actionType;


    public UserAction(User user, Post post, ActionType actionType) {
        this.user = user;
        this.post = post;
        this.actionType = actionType;

    }
}
