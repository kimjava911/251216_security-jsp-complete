package kr.java.security.controller;

import kr.java.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * 로그인 페이지 표시
     *
     * @param error - 로그인 실패 시 true가 전달됨
     * @param logout - 로그아웃 성공 시 true가 전달됨
     */
    @GetMapping("/login")
    public String loginPage(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            // org.springframework.ui.Model
            Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "로그아웃되었습니다.");
        }

        return "auth/login";
    }

    /**
     * 회원가입 페이지 표시
     */
    @GetMapping("/signup")
    public String signupPage() {
        return "auth/signup";
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/signup")
    public String signup(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        try {
            authService.signup(username, password);
            // 회원가입 성공 시 로그인 페이지로 이동
            return "redirect:/auth/login?signup=success";
        } catch (IllegalArgumentException e) {
            // 중복 아이디 등 오류 발생 시 다시 회원가입 페이지로
            model.addAttribute("errorMessage", e.getMessage());
            return "auth/signup";
        }
    }
}
