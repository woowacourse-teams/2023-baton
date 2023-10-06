package touch.baton.domain.alarm.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.alarm.command.Alarm;

public interface AlarmCommandRepository extends JpaRepository<Alarm, Long> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
        update Alarm a
        set a.isRead.value = true
        where a.id = :alarmId
        """)
    void updateIsReadTrueByMemberId(@Param("alarmId") Long alarmId);
}
