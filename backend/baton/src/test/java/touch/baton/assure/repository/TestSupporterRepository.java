package touch.baton.assure.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.member.vo.SocialId;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.repository.SupporterRepository;

import java.util.Optional;

public interface TestSupporterRepository extends SupporterRepository {

    default Supporter getBySocialId(final SocialId socialId) {
        return joinMemberBySocialId(socialId)
                .orElseThrow(() -> new IllegalArgumentException("테스트에서 Supporter 를 SocialId 로 조회할 수 없습니다."));
    }

    @Query("""
            select s, m
            from Supporter s
            join fetch Member m on m.id = s.member.id
            where m.socialId = :socialId
            """)
    Optional<Supporter> joinMemberBySocialId(@Param("socialId") final SocialId socialId);
}
