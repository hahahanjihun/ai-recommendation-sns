package jihunCompany.ai_recommendation_sns.controller;

import jihunCompany.ai_recommendation_sns.dto.Post.MyPostResponse;
import jihunCompany.ai_recommendation_sns.dto.Post.PostCreateRequest;
import jihunCompany.ai_recommendation_sns.dto.Post.PostDetail.LikeToggleResponse;
import jihunCompany.ai_recommendation_sns.dto.Post.PostDetail.PostDetailResponse;
import jihunCompany.ai_recommendation_sns.dto.Post.PostExportDto;
import jihunCompany.ai_recommendation_sns.service.PostService;
import jihunCompany.ai_recommendation_sns.service.UserActionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final UserActionService userActionService;

    /*
    게시글 생성 api
     */
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody PostCreateRequest req){
        return ResponseEntity.ok(
                postService.createPost(req.getUserId(), req.getContent())
        );
    }
    /*
    자기 게시물 보기 api
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MyPostResponse>> getMyPosts(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                postService.getPostsByAuthor(userId)
        );
    }
    /*
    게시글 클릭했을때 상세조회 api
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> detail(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {
        userActionService.viewPost(userId, postId); // 조회 기록
        return ResponseEntity.ok(
                postService.getPostDetail(postId,userId)   // 상세 데이터
        );
    }


    /*
     postdetail에 들어갔을때 좋아요 토글 API
     */
    @PostMapping("/{postId}/like")
    public ResponseEntity<LikeToggleResponse> toggleLike(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {

       return ResponseEntity.ok(
               postService.toggleLike(postId, userId)
       );
    }





    /*
    모든 게시글 내보내기 export
     */
    @GetMapping("/export")
    public ResponseEntity<List<PostExportDto>> export() {
        return ResponseEntity.ok(
                postService.exportPosts()
        );
    }

}
