package jihunCompany.ai_recommendation_sns.repository;

import jihunCompany.ai_recommendation_sns.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p join fetch p.author where p.id in :ids")
    List<Post> findAllWithAuthorByIdIn(@Param("ids") List<Long> ids);

    List<Post> findByAuthor_idOrderByCreatedAtDesc(Long userId);
}
