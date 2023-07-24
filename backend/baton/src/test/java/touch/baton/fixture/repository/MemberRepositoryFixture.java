package touch.baton.fixture.repository;

import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;

public class MemberRepositoryFixture {

    private final MemberRepository memberRepository;

    public MemberRepositoryFixture(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member save(final Member member) {
        return memberRepository.save(member);
    }
}
