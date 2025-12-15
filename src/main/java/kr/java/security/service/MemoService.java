package kr.java.security.service;

import kr.java.security.model.entity.Memo;
import kr.java.security.model.entity.UserAccount;
import kr.java.security.model.repository.MemoRepository;
import kr.java.security.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
// org.springframework.transaction.annotation.Transactional;
@Transactional(readOnly = true)
public class MemoService {

    private final MemoRepository memoRepository;
    private final UserAccountRepository userAccountRepository;

    /**
     * 현재 로그인한 사용자의 메모 목록 조회
     */
    public List<Memo> getMyMemos(String username) {
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        return memoRepository.findByAuthorIdWithAuthor(user.getId());
    }

    /**
     * 메모 상세 조회
     */
    public Memo getMemo(Long id) {
        return memoRepository.findByIdWithAuthor(id)
                .orElseThrow(() -> new IllegalArgumentException("메모를 찾을 수 없습니다."));
    }

    /**
     * 메모 작성
     */
    @Transactional
    public Memo createMemo(String title, String content, String username) {
        UserAccount author = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Memo memo = new Memo();
        memo.setTitle(title);
        memo.setContent(content);
        memo.setAuthor(author);

        return memoRepository.save(memo);
    }

    /**
     * 메모 수정
     *
     * @return 수정 성공 여부 (본인 글만 수정 가능)
     */
    @Transactional
    public boolean updateMemo(Long id, String title, String content, String username) {
        Memo memo = getMemo(id);

        // 본인 글인지 확인
        if (!memo.getAuthor().getUsername().equals(username)) {
            return false;
        }

        memo.setTitle(title);
        memo.setContent(content);
        return true;
    }

    /**
     * 메모 삭제
     *
     * @return 삭제 성공 여부 (본인 글만 삭제 가능)
     */
    @Transactional
    public boolean deleteMemo(Long id, String username) {
        Memo memo = getMemo(id);

        // 본인 글인지 확인
        if (!memo.getAuthor().getUsername().equals(username)) {
            return false;
        }

        memoRepository.delete(memo);
        return true;
    }
}