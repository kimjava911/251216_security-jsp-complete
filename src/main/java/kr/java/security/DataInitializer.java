package kr.java.security;

import kr.java.security.model.entity.Memo;
import kr.java.security.model.entity.UserAccount;
import kr.java.security.model.repository.MemoRepository;
import kr.java.security.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository userAccountRepository;
    private final MemoRepository memoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    // org.springframework.transaction.annotation.Transactional
    @Transactional
    public void run(String... args) {
        // 이미 데이터가 있으면 스킵
        if (userAccountRepository.count() > 0) {
            return;
        }

        // 1. 관리자 계정 생성
        // DelegatingPasswordEncoder 사용 시 {bcrypt}$2a$10$... 형태로 저장됨
        UserAccount admin = new UserAccount(
                "admin",
                passwordEncoder.encode("admin1234"),
                "ROLE_ADMIN"
        );
        userAccountRepository.save(admin);

        // 2. 일반 사용자 계정 생성
        UserAccount user = new UserAccount(
                "user",
                passwordEncoder.encode("user1234"),
                "ROLE_USER"
        );
        userAccountRepository.save(user);

        // 3. 테스트 메모 생성
        Memo memo1 = new Memo();
        memo1.setTitle("첫 번째 메모");
        memo1.setContent("Spring Security 학습 중입니다.");
        memo1.setAuthor(user);
        memoRepository.save(memo1);

        Memo memo2 = new Memo();
        memo2.setTitle("관리자 메모");
        memo2.setContent("관리자가 작성한 메모입니다.");
        memo2.setAuthor(admin);
        memoRepository.save(memo2);

        System.out.println("=== 초기 데이터 생성 완료 ===");
        System.out.println("관리자: admin / admin1234");
        System.out.println("사용자: user / user1234");
        System.out.println("비밀번호 저장 형식: " + admin.getPassword().substring(0, 20) + "...");
    }
}