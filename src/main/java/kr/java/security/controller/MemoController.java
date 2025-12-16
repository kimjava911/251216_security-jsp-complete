package kr.java.security.controller;

import kr.java.security.model.entity.Memo;
import kr.java.security.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

//    /**
//     * 내 메모 목록
//     *
//     * @Principal - Spring Security가 주입하는 현재 로그인한 사용자 정보
//     */
//    @GetMapping
//    // org.springframework.ui.Model;
//    public String list(Principal principal, Model model) {
//        // principal.getName() = 로그인한 사용자의 username
//        List<Memo> memos = memoService.getMyMemos(principal.getName());
//        model.addAttribute("memos", memos);
//        return "memo/list";
//    }

    /**
     * 메모 상세 보기
     */
//    @GetMapping("/{id}")
//    public String detail(@PathVariable Long id, Principal principal, Model model) {
//        Memo memo = memoService.getMemo(id);
//
//        // 본인 글인지 확인하여 수정/삭제 버튼 표시 여부 결정
//        boolean isOwner = memo.getAuthor().getUsername().equals(principal.getName());
//
//        model.addAttribute("memo", memo);
//        model.addAttribute("isOwner", isOwner);
//        return "memo/detail";
//    }

    /**
     * 메모 작성 폼
     */
//    @GetMapping("/new")
//    public String createForm() {
//        return "memo/form";
//    }

    /**
     * 메모 작성 처리
     */
    @PostMapping("/new")
    public String create(
            @RequestParam String title,
            @RequestParam String content,
            Principal principal) {

        memoService.createMemo(title, content, principal.getName());
        return "redirect:/memo";
    }

    /**
     * 메모 수정 폼
     */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Principal principal, Model model) {
        Memo memo = memoService.getMemo(id);

        // 본인 글이 아니면 목록으로 리다이렉트
        if (!memo.getAuthor().getUsername().equals(principal.getName())) {
            return "redirect:/memo";
        }

        model.addAttribute("memo", memo);
        return "memo/edit";
    }

    /**
     * 메모 수정 처리
     */
    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String content,
            Principal principal) {

//        boolean success = memoService.updateMemo(id, title, content, principal.getName());
//
//        if (success) {
//            return "redirect:/memo/" + id;
//        } else {
//            return "redirect:/memo";  // 권한 없으면 목록으로
//        }

        memoService.updateMemo(id, title, content, principal.getName());
        return "redirect:/memo/" + id;
    }

    /**
     * 메모 삭제 처리
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal) {
        memoService.deleteMemo(id, principal.getName());
        return "redirect:/memo";
    }

    /**
     * 방법 1: Principal 인터페이스 사용
     * - 가장 간단한 방법
     * - getName()으로 username만 얻을 수 있음
     */
    @GetMapping
    public String list(Principal principal, Model model) {
        String username = principal.getName();
        List<Memo> memos = memoService.getMyMemos(username);
        model.addAttribute("memos", memos);
        return "memo/list";
    }

    /**
     * 방법 2: @AuthenticationPrincipal 어노테이션 사용
     * - UserDetails 구현체를 직접 주입받음
     * - CustomUserDetails 사용 시 추가 정보(id, email 등) 접근 가능
     */
    @GetMapping("/{id}")
    public String detail(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        Memo memo = memoService.getMemo(id);

        // UserDetails에서 username 가져오기
        String username = userDetails.getUsername();
        boolean isOwner = memo.getAuthor().getUsername().equals(username);

        model.addAttribute("memo", memo);
        model.addAttribute("isOwner", isOwner);
        return "memo/detail";
    }

    /**
     * 방법 3: Authentication 객체 직접 사용
     * - 권한 정보까지 필요할 때 유용
     */
    @GetMapping("/new")
    public String createForm(Authentication authentication, Model model) {
        // 현재 사용자의 권한 목록 확인
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);
        return "memo/form";
    }

}