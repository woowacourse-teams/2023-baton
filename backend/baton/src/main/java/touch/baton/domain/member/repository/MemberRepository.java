package touch.baton.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import touch.baton.domain.member.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
