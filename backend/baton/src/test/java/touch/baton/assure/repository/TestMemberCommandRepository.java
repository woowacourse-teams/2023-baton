package touch.baton.assure.repository;

import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.repository.MemberCommandRepository;
import touch.baton.tobe.domain.member.command.vo.SocialId;

import java.util.Optional;

public interface TestMemberCommandRepository extends MemberCommandRepository {

    default Member getBySocialId(final SocialId socialId) {
        return findBySocialId(socialId)
                .orElseThrow(() -> new IllegalArgumentException("테스트에서 Runner 를 SocialId 로 조회할 수 없습니다."));
    };

    Optional<Member> findBySocialId(final SocialId socialId);
}
