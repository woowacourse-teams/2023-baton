package touch.baton.tobe.domain.member.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.tobe.domain.member.command.Supporter;

public interface SupporterCommandRepository extends JpaRepository<Supporter, Long> {
}
