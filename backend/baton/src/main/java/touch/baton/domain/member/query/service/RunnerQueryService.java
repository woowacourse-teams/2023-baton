package touch.baton.domain.member.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.exception.RunnerBusinessException;
import touch.baton.domain.member.query.repository.RunnerQueryRepository;

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
