package kr.java.security.service;

import kr.java.security.model.entity.UserAccount;
import kr.java.security.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 처리
     *
     * @param username 사용자가 입력한 아이디
     * @param rawPassword 암호화 전 평문 비밀번호
     */
    public void signup(String username, String rawPassword) {
        // 1. 아이디 중복 검사
        if (userAccountRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        // 2. 비밀번호 암호화
        // DelegatingPasswordEncoder는 기본적으로 BCrypt를 사용
        // 결과: {bcrypt}$2a$10$N9qo8uLOickgx2ZMRZoMy.MqAVqWk... 형태로 저장
        //
        // 이 접두사({bcrypt}) 덕분에:
        // - 나중에 다른 암호화 방식으로 전환해도 기존 사용자 로그인 가능
        // - 여러 암호화 방식이 혼재해도 각각 올바르게 검증됨
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // 3. 사용자 생성 및 저장
        // 일반 사용자는 ROLE_USER 권한 부여
        UserAccount user = new UserAccount(
                username,
                encodedPassword,
                "ROLE_USER"  // Spring Security에서 권한은 "ROLE_" 접두사 필요
        );

        userAccountRepository.save(user);
    }
}
