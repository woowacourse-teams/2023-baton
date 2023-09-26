package touch.baton.tobe.domain.member.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.member.exception.SupporterBusinessException;
import touch.baton.tobe.domain.member.query.repository.SupporterQueryRepository;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SupporterQueryService {

    private final SupporterQueryRepository supporterQueryRepository;

    public Supporter readBySupporterId(final Long supporterId) {
        return supporterQueryRepository.joinMemberBySupporterId(supporterId)
                .orElseThrow(() -> new SupporterBusinessException("존재하지 않는 서포터 식별자값으로 조회할 수 없습니다."));
    }
}
