package jihunCompany.ai_recommendation_sns.domain.recommendation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


/*
-----------------추천한거 저장해놓음--------------------
 */
@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "recommendation",
        indexes = {
                @Index(name = "idx_user_recommended_at", columnList = "userId, recommendedAt")
        }
)
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 추천 받은 유저
    @Column(nullable = false)
    private Long userId;

    // 추천된 게시글
    @Column(nullable = false)
    private Long postId;

    // 추천 점수 (선택이지만 강력 추천)
    private Double score;

    // 게시글 추천 순위
    @Column(name = "recommendation_rank")
    private Integer rank;

    // 추천 시점
    @Column(nullable = false)
    private LocalDateTime recommendedAt;

    // 생성 메서드 (중요)
    public static Recommendation of(Long userId, Long postId, Double score, Integer rank) {
        Recommendation r = new Recommendation();
        r.userId = userId;
        r.postId = postId;
        r.score = score;
        r.rank = rank;
        r.recommendedAt = LocalDateTime.now();
        return r;
    }
}
