package touch.baton.tobe.domain.member.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.tobe.domain.member.exception.RunnerBusinessException;
import touch.baton.tobe.domain.member.query.repository.RunnerQueryRepository;
import touch.baton.tobe.domain.member.command.Runner;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RunnerQueryService {

    private final RunnerQueryRepository runnerQueryRepository;

    public Runner readByRunnerId(final Long runnerId) {
        return runnerQueryRepository.joinMemberByRunnerId(runnerId)
                .orElseThrow(() -> new RunnerBusinessException("Runner 가 존재하지 않습니다."));
    }
}
