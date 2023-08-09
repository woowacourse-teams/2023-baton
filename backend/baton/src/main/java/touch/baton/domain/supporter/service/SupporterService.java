package touch.baton.domain.supporter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.exception.SupporterBusinessException;
import touch.baton.domain.supporter.repository.SupporterRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SupporterService {

    private final SupporterRepository supporterRepository;

    public List<Supporter> readAllSupporters() {
        return supporterRepository.findAll();
    }

    public Supporter readBySupporterId(final Long supporterId) {
        return supporterRepository.joinMemberBySupporterId(supporterId)
                .orElseThrow(() -> new SupporterBusinessException("존재하지 않는 서포터 식별자값으로 조회할 수 없습니다."));
    }
}
