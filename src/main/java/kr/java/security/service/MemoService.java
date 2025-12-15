package kr.java.security.service;

import kr.java.security.model.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemoService {
    private final MemoRepository memoRepository;
}
