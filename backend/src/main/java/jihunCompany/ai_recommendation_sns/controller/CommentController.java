package jihunCompany.ai_recommendation_sns.controller;

import jihunCompany.ai_recommendation_sns.dto.Comment.CommentCreateRequest;
import jihunCompany.ai_recommendation_sns.dto.Comment.CommentExportDto;
import jihunCompany.ai_recommendation_sns.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
/*
댓글은 게시글에 종속적이므로 /posts/comments로 통일
 */
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CommentCreateRequest req) {
        return ResponseEntity.ok(commentService.createComment(
                req.getUserId(),
                req.getPostId(),
                req.getContent()
        ));
    }

    @GetMapping("/export")
    public ResponseEntity<List<CommentExportDto>> export() {
        return ResponseEntity.ok(
                commentService.exportComments()
        );
    }


}
