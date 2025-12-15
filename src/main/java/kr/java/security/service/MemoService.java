package kr.java.security.service;

import kr.java.security.model.entity.Memo;
import kr.java.security.model.entity.UserAccount;
import kr.java.security.model.repository.MemoRepository;
import kr.java.security.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
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
//    @Transactional
//    @PreAuthorize("#username == authentication.name")
//    public boolean updateMemo(Long id, String title, String content, String username) {
//        Memo memo = getMemo(id);
//
//        // 본인 글인지 확인
//        if (!memo.getAuthor().getUsername().equals(username)) {
//            return false;
//        }
//
//        memo.setTitle(title);
//        memo.setContent(content);
//        return true;
//    }

    /**
     * 메모 수정 - 본인 글만 수정 가능
     *
     * @PreAuthorize: 메서드 실행 전에 권한 검사
     * #username == authentication.name:
     *   - #username은 메서드 파라미터
     *   - authentication.name은 현재 로그인한 사용자
     */
    @Transactional
    @PreAuthorize("#username == authentication.name")
    public void updateMemo(Long id, String title, String content, String username) {
        Memo memo = getMemo(id);

        // 추가 보안: 메모 작성자와 요청자 일치 확인
        if (!memo.getAuthor().getUsername().equals(username)) {
            throw new AccessDeniedException("본인의 메모만 수정할 수 있습니다.");
        }

        memo.setTitle(title);
        memo.setContent(content);
    }


    /**
     * 메모 삭제
     *
     * @return 삭제 성공 여부 (본인 글만 삭제 가능)
     */
//    @Transactional
//    public boolean deleteMemo(Long id, String username) {
//        Memo memo = getMemo(id);
//
//        // 본인 글인지 확인
//        if (!memo.getAuthor().getUsername().equals(username)) {
//            return false;
//        }
//
//        memoRepository.delete(memo);
//        return true;
//    }


    /**
     * 메모 삭제 - 본인 글 또는 관리자만 삭제 가능
     *
     * hasRole('ADMIN'): ROLE_ADMIN 권한 보유 시 허용
     * #username == authentication.name: 본인인 경우 허용
     * or: 둘 중 하나라도 만족하면 허용
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    public void deleteMemo(Long id, String username) {
        Memo memo = getMemo(id);

        // 관리자가 아닌 경우 본인 글인지 확인
        if (!memo.getAuthor().getUsername().equals(username)) {
            // 여기 도달 = 관리자가 다른 사람 글 삭제 (허용)
        }

        memoRepository.delete(memo);
    }
}