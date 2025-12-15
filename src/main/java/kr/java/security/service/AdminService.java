package kr.java.security.service;

import kr.java.security.model.entity.UserAccount;
import kr.java.security.model.repository.MemoRepository;
import kr.java.security.model.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final UserAccountRepository userAccountRepository;
    private final MemoRepository memoRepository;

    /**
     * 모든 사용자 목록 조회 - 관리자만 가능
     */
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    /**
     * 사용자 계정 비활성화 - 관리자만 가능
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void disableUser(Long userId) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.setEnabled(false);
    }

    /**
     * 사용자 계정 활성화 - 관리자만 가능
     */
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void enableUser(Long userId) {
        UserAccount user = userAccountRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        user.setEnabled(true);
    }
}
