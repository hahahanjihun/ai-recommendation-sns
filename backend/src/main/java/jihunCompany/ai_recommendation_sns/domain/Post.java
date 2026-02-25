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
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    연관관계 주인 FK
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Author_id", nullable = false)
    private User author;

    /*
    게시글
     */
    @Column(nullable = false, length = 1000)
    private String content;

    /*
    댓글
     */
    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    /*
    행동 로그
     */
    @OneToMany(mappedBy = "post")
    private List<UserAction> userActions = new ArrayList<>();


    public Post(User author, String content) {
        this.author = author;
        this.content = content;
    }
}
