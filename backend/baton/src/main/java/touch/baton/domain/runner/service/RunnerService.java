package touch.baton.domain.runner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.exception.RunnerBusinessException;
import touch.baton.domain.runner.repository.RunnerRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerService {

    private final RunnerRepository runnerRepository;

    public Runner readRunnerById(final Long runnerId) {
        return runnerRepository.joinMemberByRunnerId(runnerId)
                .orElseThrow(() -> new RunnerBusinessException("Runner가 존재하지 않습니다."));
    }
}
