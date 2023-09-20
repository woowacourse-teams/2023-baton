package touch.baton.assure.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;

import java.util.Optional;

public interface TestRunnerRepository extends RunnerRepository {

    default Runner getBySocialId(final SocialId socialId) {
        return joinMemberBySocialId(socialId)
                .orElseThrow(() -> new IllegalArgumentException("테스트에서 Runner 를 SocialId 로 조회할 수 없습니다."));
    }

    @Query("""
            select r, m
            from Runner r
            join fetch Member m on m.id = r.member.id
            where m.socialId = :socialId
            """)
    Optional<Runner> joinMemberBySocialId(@Param("socialId") final SocialId socialId);
}
