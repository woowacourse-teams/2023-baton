package touch.baton.domain.runner.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.exception.RunnerDomainException;
import touch.baton.domain.runner.repository.RunnerRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerService {

    private final RunnerRepository runnerRepository;

    public Runner readRunnerWithMember(final Long runnerId) {
        return runnerRepository.joinMemberByRunnerId(runnerId)
                .orElseThrow(() -> new RunnerDomainException("해당하는 식별자의 Runner 를 찾을 수 없습니다. 식별자를 다시 확인해주세요."));
    }
}
