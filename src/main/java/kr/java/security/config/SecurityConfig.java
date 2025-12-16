package kr.java.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/auth/**", "/css/**", "/js/**", "/error/**").permitAll()
                        .requestMatchers("/memo/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth/login?error=true")
                        // 로그인 실패 시 커스텀 핸들러 사용 (선택사항)
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                // 예외 처리 설정
                .exceptionHandling(ex -> ex
                        // 인증되지 않은 사용자가 보호된 리소스 접근 시
                        .authenticationEntryPoint(authenticationEntryPoint())
                        // 인증은 되었지만 권한이 없을 때
                        .accessDeniedHandler(accessDeniedHandler())
                );

        return http.build();
    }

    /**
     * AuthenticationEntryPoint - 인증되지 않은 사용자 처리
     *
     * 로그인하지 않은 사용자가 보호된 페이지에 접근했을 때
     * 어떻게 처리할지 정의합니다.
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            // 요청한 URL을 세션에 저장 (로그인 후 원래 페이지로 돌아가기 위해)
            String requestedUrl = request.getRequestURI();
            if (request.getQueryString() != null) {
                requestedUrl += "?" + request.getQueryString();
            }
            request.getSession().setAttribute("REDIRECT_URL", requestedUrl);

            // 로그인 페이지로 리다이렉트
            response.sendRedirect("/auth/login?redirect=true");
        };
    }

    /**
     * AccessDeniedHandler - 권한 없는 사용자 처리
     *
     * 로그인은 했지만 해당 리소스에 접근 권한이 없을 때
     * 어떻게 처리할지 정의합니다.
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            // 403 에러 페이지로 포워드
            request.setAttribute("errorMessage", "접근 권한이 없습니다.");
            request.getRequestDispatcher("/error/403").forward(request, response);
        };
    }

    /**
     * AuthenticationFailureHandler - 로그인 실패 처리
     *
     * 아이디/비밀번호 오류, 계정 비활성화 등
     * 다양한 실패 원인에 따라 다른 메시지를 보여줄 수 있습니다.
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            String errorMessage;

            if (exception instanceof BadCredentialsException) {
                errorMessage = "아이디 또는 비밀번호가 올바르지 않습니다.";
            } else if (exception instanceof DisabledException) {
                errorMessage = "비활성화된 계정입니다. 관리자에게 문의하세요.";
            } else if (exception instanceof LockedException) {
                errorMessage = "잠긴 계정입니다. 관리자에게 문의하세요.";
            } else {
                errorMessage = "로그인에 실패했습니다.";
            }

            // URL 인코딩하여 파라미터로 전달
            String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
            response.sendRedirect("/auth/login?error=true&message=" + encodedMessage);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}