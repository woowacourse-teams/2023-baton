package touch.baton.domain.alarm.query.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.alarm.query.repository.AlarmQueryRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AlarmQueryService {

    private final AlarmQueryRepository alarmQueryRepository;

    public List<Alarm> readAlarmsByMemberId(final Long memberId, final int limit) {
        return alarmQueryRepository.findByMemberIdLimit(memberId, limit);
    }
}
