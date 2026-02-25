package jihunCompany.ai_recommendation_sns.domain;

import jakarta.persistence.*;
import jihunCompany.ai_recommendation_sns.domain.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    /*작성한 게시글*/
    @OneToMany(mappedBy = "author")
    private List<Post> posts = new ArrayList<>();

    /*내가 작성한 댓글*/
    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    /*내가 한 행동들(조회, 좋아요 등)*/
    @OneToMany(mappedBy = "user")
    private List<UserAction> actions = new ArrayList<>();

    // 생성자 (의미 있는 상태만 허용)
    public User(String username, String encodedPassword) {
        this.username = username;
        this.password = encodedPassword;
    }
}
