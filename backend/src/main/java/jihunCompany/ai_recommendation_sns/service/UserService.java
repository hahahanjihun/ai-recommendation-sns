package jihunCompany.ai_recommendation_sns.service;

import jihunCompany.ai_recommendation_sns.domain.User;
import jihunCompany.ai_recommendation_sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*
    회원가입
     */
    public Long register(String username, String password) {
        if(userRepository.findByUsername(username).isPresent()){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        String encoded = passwordEncoder.encode(password);
        User user = new User(username, encoded);

        userRepository.save(user);
        return user.getId();
    }

    /*
    로그인
     */
    public User login(String username, String rawpassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        //user.getPassword() : 인코딩된 비밀번호
        if(!passwordEncoder.matches(rawpassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return user;
    }

    /*
    아이디로 회원번호 조회
     */
    public Long getUserId(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));
        return user.getId();
    }

    /*
    회원 탈퇴
     */
    public void deleteUser(String username, String rawpassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));
        if(!passwordEncoder.matches(rawpassword, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        userRepository.delete(user);
    }
}
