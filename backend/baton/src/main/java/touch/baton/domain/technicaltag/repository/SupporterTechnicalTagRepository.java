package touch.baton.domain.technicaltag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.technicaltag.SupporterTechnicalTag;

public interface SupporterTechnicalTagRepository extends JpaRepository<SupporterTechnicalTag, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM SupporterTechnicalTag st WHERE st.supporter = :supporter")
    int deleteBySupporter(@Param("supporter") final Supporter supporter);
}
