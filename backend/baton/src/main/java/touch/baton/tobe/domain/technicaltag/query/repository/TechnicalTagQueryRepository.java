package touch.baton.tobe.domain.technicaltag.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.common.vo.TagName;
import touch.baton.tobe.domain.technicaltag.command.TechnicalTag;

import java.util.Optional;

public interface TechnicalTagQueryRepository extends JpaRepository<TechnicalTag, Long> {

    Optional<TechnicalTag> findByTagName(final TagName tagName);
}
