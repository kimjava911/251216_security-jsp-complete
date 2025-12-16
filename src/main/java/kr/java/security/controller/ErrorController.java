package kr.java.security.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    /**
     * 403 Forbidden 에러 페이지
     */
    @GetMapping("/403")
    // org.springframework.ui.Model
    public String forbidden(HttpServletRequest request, Model model) {
        // SecurityConfig에서 설정한 에러 메시지 가져오기
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (errorMessage == null) {
            errorMessage = "이 페이지에 접근할 권한이 없습니다.";
        }
        model.addAttribute("errorMessage", errorMessage);
        return "error/403";
    }
}

