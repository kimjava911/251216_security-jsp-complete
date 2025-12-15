package kr.java.security.model.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security의 UserDetails를 확장한 커스텀 클래스
 *
 * 기본 UserDetails에 없는 추가 정보(사용자 ID 등)가
 * 필요할 때 이 클래스를 사용합니다.
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;           // DB의 사용자 ID
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;

    public CustomUserDetails(UserAccount user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();  // {bcrypt}... 형태
        this.enabled = user.getEnabled();
        // 권한 문자열을 GrantedAuthority 객체로 변환
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    // UserDetails 인터페이스 필수 메서드들
    @Override
    public boolean isAccountNonExpired() {
        return true;  // 계정 만료 여부 (true = 만료되지 않음)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // 계정 잠금 여부 (true = 잠기지 않음)
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // 비밀번호 만료 여부 (true = 만료되지 않음)
    }
}
