package jihunCompany.ai_recommendation_sns.controller;

import jihunCompany.ai_recommendation_sns.dto.UserAction.ActionExportDto;
import jihunCompany.ai_recommendation_sns.dto.UserAction.ActionRequest;
import jihunCompany.ai_recommendation_sns.dto.UserAction.UserActionExportDto;
import jihunCompany.ai_recommendation_sns.service.UserActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/actions")
public class UserActionController {

    private final UserActionService userActionService;

    @PostMapping("/like")
    public ResponseEntity<Boolean> like(@RequestBody ActionRequest req){
        boolean liked =
                userActionService.toggleLike(req.getUserId(), req.getPostId());
        return ResponseEntity.ok(liked);
    }


    // ---------------- 전체 export ----------------
    @GetMapping("/export")
    public List<ActionExportDto> exportAll() {
        return userActionService.exportAll();
    }

    // ---------------- 유저별 export ----------------
    @GetMapping("/export/user")
    public List<UserActionExportDto> exportByUser(
            @RequestParam Long userId
    ) {
        return userActionService.exportByUser(userId);
    }

}
