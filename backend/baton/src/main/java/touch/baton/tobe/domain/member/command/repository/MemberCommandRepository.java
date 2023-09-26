package touch.baton.tobe.domain.member.command.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.tobe.domain.member.command.Member;

public interface MemberCommandRepository extends JpaRepository<Member, Long> {
}
