package kr.java.security.controller;

import kr.java.security.model.entity.UserAccount;
import kr.java.security.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * 관리자 대시보드
     */
    @GetMapping
    public String dashboard(Model model) {
        List<UserAccount> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/dashboard";
    }

    /**
     * 사용자 계정 상태 토글 (활성화/비활성화)
     */
    @PostMapping("/users/{id}/toggle")
    public String toggleUserStatus(@PathVariable Long id, @RequestParam boolean enabled) {
        if (enabled) {
            adminService.disableUser(id);
        } else {
            adminService.enableUser(id);
        }
        return "redirect:/admin";
    }
}
