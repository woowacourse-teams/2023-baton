package touch.baton.assure.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.repository.MemberCommandRepository;
import touch.baton.domain.member.command.vo.SocialId;

import java.util.Optional;

public interface TestMemberQueryRepository extends MemberCommandRepository {

    default Member getBySocialId(final SocialId socialId) {
        return findBySocialId(socialId)
                .orElseThrow(() -> new IllegalArgumentException("테스트에서 Member 를 SocialId 로 조회할 수 없습니다."));
    };

    Optional<Member> findBySocialId(final SocialId socialId);

    default Member getAsRunnerByRunnerPostId(final Long runnerPostId) {
        return findAsRunnerByRunnerPostId(runnerPostId)
                .orElseThrow(() -> new IllegalArgumentException("테스트에서 Member 를 runnerPostId로 러너 게시글의 작성자(Runner)로서 조회할 수 없습니다."));
    };

    @Query("""
        select rp.runner.member
        from RunnerPost rp
        join fetch Runner r on r.id = rp.runner.id
        join fetch Member m on m.id = r.member.id
        where rp.id = :runnerPostId
        """)
    Optional<Member> findAsRunnerByRunnerPostId(@Param("runnerPostId") final Long runnerPostId);
}
