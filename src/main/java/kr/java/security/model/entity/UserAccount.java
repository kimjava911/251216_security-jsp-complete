package kr.java.security.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserAccount extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String username;  // 로그인할 때 사용하는 ID

    @Column(nullable = false)
    private String password;  // 암호화되어 저장됨 (예: {bcrypt}$2a$10$...)

    @Column(nullable = false)
    private String role;  // "ROLE_USER" 또는 "ROLE_ADMIN"

    private Boolean enabled = true;  // 계정 활성화 여부

    // 회원가입용 생성자
    public UserAccount(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = true;
    }
}