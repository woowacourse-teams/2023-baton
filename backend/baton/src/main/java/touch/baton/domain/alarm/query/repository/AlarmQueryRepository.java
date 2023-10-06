package touch.baton.domain.alarm.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.alarm.command.Alarm;

import java.util.List;

public interface AlarmQueryRepository extends JpaRepository<Alarm, Long> {

    @Query("""
        select a
        from Alarm a
        where a.member.id = :memberId
        order by a.id desc
        limit :limit
        """)
    List<Alarm> findByMemberIdLimit(@Param("memberId") final Long memberId, @Param("limit") final int limit);
}
