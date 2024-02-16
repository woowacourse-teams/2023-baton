package touch.baton.assure.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.member.query.repository.SupporterQueryRepository;

import java.util.Optional;

public interface TestSupporterQueryRepository extends SupporterQueryRepository {

    default Supporter getBySocialId(final SocialId socialId) {
        return joinMemberBySocialId(socialId)
                .orElseThrow(() -> new IllegalArgumentException("테스트에서 Supporter 를 SocialId 로 조회할 수 없습니다."));
    }

    @Query("""
            select s
            from Supporter s
            join fetch s.member m
            join fetch s.supporterTechnicalTags stt
            where m.socialId = :socialId
            """)
    Optional<Supporter> joinMemberBySocialId(@Param("socialId") final SocialId socialId);
}
