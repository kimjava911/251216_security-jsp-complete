package kr.java.security.service;

import kr.java.security.model.entity.CustomUserDetails;
import kr.java.security.model.entity.UserAccount;
import kr.java.security.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    /**
     * Spring Security가 로그인 시 자동으로 호출하는 메서드
     *
     * 사용자가 입력한 username으로 DB에서 사용자를 찾아
     * Spring Security가 이해할 수 있는 UserDetails 객체로 변환합니다.
     *
     * @param username 사용자가 로그인 폼에 입력한 아이디
     * @return UserDetails - Spring Security의 사용자 정보 인터페이스
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때
     */
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        // 1. DB에서 사용자 조회
//        UserAccount user = userAccountRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException(
//                        "사용자를 찾을 수 없습니다: " + username
//                ));
//
//        // 2. UserDetails 객체 생성
//        // Spring Security가 제공하는 User 클래스 사용
//        // 비밀번호는 {bcrypt}$2a$10$... 형태로 저장되어 있음
//        // DelegatingPasswordEncoder가 자동으로 적절한 인코더 선택
//        return User.builder()
//                .username(user.getUsername())
//                .password(user.getPassword())  // {bcrypt} 접두사 포함된 암호화 비밀번호
//                .roles(user.getRole().replace("ROLE_", ""))  // "ROLE_USER" -> "USER"
//                .disabled(!user.getEnabled())  // 계정 비활성화 여부
//                .build();
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "사용자를 찾을 수 없습니다: " + username
                ));

        // CustomUserDetails 사용 시 - 추가 정보 접근 가능
        return new CustomUserDetails(user);
    }
}
