package kr.java.security.model.repository;

import kr.java.security.model.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    // 로그인 시 사용자 조회에 사용
    Optional<UserAccount> findByUsername(String username);

    // 회원가입 시 중복 체크에 사용
    boolean existsByUsername(String username);
}