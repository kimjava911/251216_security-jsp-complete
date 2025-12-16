package kr.java.security.controller;

import kr.java.security.model.entity.Memo;
import kr.java.security.service.MemoService;
import lombok.RequiredArgsConstructor;
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

    /**
     * 내 메모 목록
     *
     * @Principal - Spring Security가 주입하는 현재 로그인한 사용자 정보
     */
    @GetMapping
    // org.springframework.ui.Model;
    public String list(Principal principal, Model model) {
        // principal.getName() = 로그인한 사용자의 username
        List<Memo> memos = memoService.getMyMemos(principal.getName());
        model.addAttribute("memos", memos);
        return "memo/list";
    }

    /**
     * 메모 상세 보기
     */
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Principal principal, Model model) {
        Memo memo = memoService.getMemo(id);

        // 본인 글인지 확인하여 수정/삭제 버튼 표시 여부 결정
        boolean isOwner = memo.getAuthor().getUsername().equals(principal.getName());

        model.addAttribute("memo", memo);
        model.addAttribute("isOwner", isOwner);
        return "memo/detail";
    }

    /**
     * 메모 작성 폼
     */
    @GetMapping("/new")
    public String createForm() {
        return "memo/form";
    }

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
            @RequestParam String content) {

        String authorName = memoService.getMemo(id).getAuthor().getUsername();
        memoService.updateMemo(id, title, content, authorName);
        return "redirect:/memo/" + id;
    }

    /**
     * 메모 삭제 처리
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        String authorName = memoService.getMemo(id).getAuthor().getUsername();
        memoService.deleteMemo(id, authorName);
        return "redirect:/memo";
    }
}