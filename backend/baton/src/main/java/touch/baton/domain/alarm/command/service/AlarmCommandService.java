package touch.baton.domain.alarm.command.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.alarm.command.repository.AlarmCommandRepository;
import touch.baton.domain.alarm.exception.AlarmBusinessException;
import touch.baton.domain.member.command.Member;

@RequiredArgsConstructor
@Transactional
@Service
public class AlarmCommandService {

    private final AlarmCommandRepository alarmCommandRepository;

    public void updateAlarmIsReadTrueByMember(final Member member, final Long alarmId) {
        if (getAlarmByAlarmId(alarmId).isNotOwner(member)) {
            throw new AlarmBusinessException("Alarm 의 주인(사용자)가 아니므로 알람의 읽은 여부를 수정할 수 없습니다.");
        }

        alarmCommandRepository.updateIsReadTrueByMemberId(alarmId);
    }

    private Alarm getAlarmByAlarmId(final Long alarmId) {
        return alarmCommandRepository.findById(alarmId)
                .orElseThrow(() -> new AlarmBusinessException("Alarm 식별자값으로 알람을 조회할 수 없습니다."));
    }

    public void deleteAlarmByMember(final Member member, final Long alarmId) {
        if (getAlarmByAlarmId(alarmId).isNotOwner(member)) {
            throw new AlarmBusinessException("Alarm 의 주인(사용자)가 아니므로 알람을 삭제할 수 없습니다.");
        }

        alarmCommandRepository.deleteById(alarmId);
    }
}
