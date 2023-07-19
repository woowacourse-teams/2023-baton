package touch.baton.domain.runnerpost.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerPostService {

    private final RunnerPostRepository runnerPostRepository;

}
