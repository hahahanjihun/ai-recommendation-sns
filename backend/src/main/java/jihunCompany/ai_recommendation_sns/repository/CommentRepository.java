package jihunCompany.ai_recommendation_sns.repository;

import jihunCompany.ai_recommendation_sns.domain.Comment;
import jihunCompany.ai_recommendation_sns.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c.post.id, COUNT(c) FROM Comment c WHERE c.post.id IN :postIds GROUP BY c.post.id")
    List<Object[]> countByPostIds(List<Long> postIds);
}
