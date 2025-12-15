package kr.java.security.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // Spring Security 활성화
public class SecurityConfig {

    /**
     * SecurityFilterChain - Spring Security의 모든 보안 규칙을 정의
     *
     * 이 빈(Bean)이 HTTP 요청에 대한 보안 처리를 담당합니다.
     * 람다(Lambda) DSL 문법으로 설정을 작성합니다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // JSP 렌더링은 내부 FORWARD로 동작하므로, forward 디스패치 자체는 허용
                        // jakarta.servlet.DispatcherType
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        // 누구나 접근 가능한 경로
                        .requestMatchers("/", "/auth/**", "/css/**", "/js/**").permitAll()
                        // 메모 관련 경로는 로그인 필요
                        .requestMatchers("/memo/**").authenticated()
                        // 관리자 전용 경로
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 그 외 모든 요청은 로그인 필요
                        .anyRequest().authenticated()
                )

                // 2. 폼 로그인 설정
                .formLogin(form -> form
                        .loginPage("/auth/login")           // 커스텀 로그인 페이지 경로
                        .loginProcessingUrl("/auth/login")  // 로그인 처리 URL (POST)
                        .usernameParameter("username")      // 폼의 아이디 필드명
                        .passwordParameter("password")      // 폼의 비밀번호 필드명
                        .defaultSuccessUrl("/", true)       // 로그인 성공 시 이동할 페이지
                        .failureUrl("/auth/login?error=true")  // 로그인 실패 시 이동할 페이지
                        .permitAll()
                )

                // 3. 로그아웃 설정
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")          // 로그아웃 처리 URL
                        .logoutSuccessUrl("/")              // 로그아웃 성공 시 이동할 페이지
                        .invalidateHttpSession(true)        // 세션 무효화
                        .deleteCookies("JSESSIONID")        // 세션 쿠키 삭제
                        .permitAll()
                );

        return http.build();
    }

    /**
     * PasswordEncoder - 비밀번호 암호화/검증 담당
     *
     * DelegatingPasswordEncoder: 여러 암호화 방식을 지원하는 "위임" 인코더
     * - 저장된 비밀번호 앞의 {id} 접두사를 보고 어떤 방식으로 암호화되었는지 판단
     * - 예: {bcrypt}$2a$10$... → BCrypt로 암호화됨
     * - 예: {noop}plaintext → 암호화 없음 (테스트용)
     *
     * 기본 암호화 방식: BCrypt (새로 저장하는 비밀번호에 적용)
     * - 단방향 해시: 원본 비밀번호를 알 수 없음
     * - Salt 자동 처리: 같은 비밀번호도 매번 다른 결과 생성
     * - 느린 해시: 무차별 대입 공격(Brute Force)에 강함
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // 실무 권장 방식: DelegatingPasswordEncoder
        // - 여러 암호화 방식 호환 (마이그레이션 용이)
        // - Spring Security 5.x 이상 기본값
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
