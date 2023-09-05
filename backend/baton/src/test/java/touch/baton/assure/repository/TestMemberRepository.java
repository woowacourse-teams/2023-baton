package touch.baton.assure.repository;

import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.member.vo.SocialId;

import java.util.Optional;

public interface TestMemberRepository extends MemberRepository {

    default Member getBySocialId(final SocialId socialId) {
        return findBySocialId(socialId)
                .orElseThrow(() -> new IllegalArgumentException("테스트에서 Runner 를 SocialId 로 조회할 수 없습니다."));
    };

    Optional<Member> findBySocialId(final SocialId socialId);
}
