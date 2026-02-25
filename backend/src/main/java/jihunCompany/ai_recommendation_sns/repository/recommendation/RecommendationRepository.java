package jihunCompany.ai_recommendation_sns.repository.recommendation;

import jihunCompany.ai_recommendation_sns.domain.Post;
import jihunCompany.ai_recommendation_sns.domain.recommendation.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    //
    boolean existsByUserId(Long userId);


    // 사용자별 추천 삭제
    void deleteByUserId(Long userId);

    // 상위 5개 추천 가져오기
    List<Recommendation> findTop5ByUserIdOrderByRankAsc(Long userId);


    // 가장 최근 추천 시각
    @Query("""
        select max(r.recommendedAt)
        from Recommendation r
        where r.userId = :userId
    """)
    LocalDateTime findLatestCreatedAt(Long userId);

    // 좋아요(UserAction - LIKE)가 많은 순서대로 게시글 상위 N개 가져오기
    @Query("""
        select p from Post p 
        left join UserAction ua on ua.post = p and ua.actionType = 'LIKE'
        group by p
        order by count(ua) desc, p.id desc
    """)
    List<Post> findTopPostsByLikeCount(Pageable pageable);
}
