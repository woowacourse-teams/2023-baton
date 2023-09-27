package touch.baton.tobe.domain.technicaltag.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.technicaltag.command.SupporterTechnicalTag;

public interface SupporterTechnicalTagCommandRepository extends JpaRepository<SupporterTechnicalTag, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM SupporterTechnicalTag st WHERE st.supporter = :supporter")
    int deleteBySupporter(@Param("supporter") final Supporter supporter);
}
