package kr.java.security.model.repository;

import kr.java.security.model.entity.Memo;
import kr.java.security.model.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
