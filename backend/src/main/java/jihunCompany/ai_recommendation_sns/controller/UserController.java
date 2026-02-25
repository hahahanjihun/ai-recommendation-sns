package jihunCompany.ai_recommendation_sns.controller;

import jihunCompany.ai_recommendation_sns.dto.User.UserLoginRequest;
import jihunCompany.ai_recommendation_sns.dto.User.UserRegisterRequest;
import jihunCompany.ai_recommendation_sns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Long> register(@RequestBody UserRegisterRequest req) {
        System.out.println("rigister called"+req.getUsername());
        return ResponseEntity.ok(
                userService.register(req.getUsername(), req.getPassword())
        );
    }

    @PostMapping("/login")
    public ResponseEntity<Long> login(@RequestBody UserLoginRequest req) {
        Long userId = userService.login(req.getUsername(), req.getPassword()).getId();
        return ResponseEntity.ok(userId);
    }

}
