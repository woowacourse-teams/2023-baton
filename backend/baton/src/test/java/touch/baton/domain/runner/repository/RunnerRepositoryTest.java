package touch.baton.domain.runner.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import touch.baton.config.JpaConfig;
import touch.baton.domain.common.vo.Grade;
import touch.baton.domain.common.vo.TotalRating;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.member.vo.Company;
import touch.baton.domain.member.vo.Email;
import touch.baton.domain.member.vo.GithubUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.runner.Runner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Import(JpaConfig.class)
@DataJpaTest
class RunnerRepositoryTest {

    private static final MemberName memberName = new MemberName("헤에디주");
    private static final Email email = new Email("test@test.co.kr");
    private static final OauthId oauthId = new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j");
    private static final GithubUrl githubUrl = new GithubUrl("github.com/hyena0608");
    private static final Company company = new Company("우아한형제들");
    private static final TotalRating totalRating = new TotalRating(100);
    private static final Grade grade = Grade.BARE_FOOT;

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Runner runner;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .memberName(memberName)
                .email(email)
                .oauthId(oauthId)
                .githubUrl(githubUrl)
                .company(company)
                .build();
        memberRepository.save(member);

        runner = Runner.builder()
                .totalRating(totalRating)
                .grade(grade)
                .member(member)
                .build();
    }

    @DisplayName("Runner 를 Member 와 조인해서 조회할 수 있다.")
    @Test
    void findByIdJoinMember() {
        // given
        final Runner expected = runnerRepository.save(runner);

        // when
        final Optional<Runner> actual = runnerRepository.joinMemberByRunnerId(expected.getId());

        // then
        assertThat(actual).isPresent();
        final Member actualMember = actual.get().getMember();
        assertAll(
                () -> assertThat(actualMember.getId()).isNotNull(),
                () -> assertThat(actualMember.getMemberName()).isEqualTo(memberName),
                () -> assertThat(actualMember.getCompany()).isEqualTo(company),
                () -> assertThat(actualMember.getEmail()).isEqualTo(email),
                () -> assertThat(actualMember.getOauthId()).isEqualTo(oauthId),
                () -> assertThat(actualMember.getGithubUrl()).isEqualTo(githubUrl)
        );
    }

    @DisplayName("식별자가 없으면 Optional.empty()가 반환된다.")
    @Test
    void findByIdJoinMember_if_id_is_not_exists() {
        // when
        final Optional<Runner> actual = runnerRepository.joinMemberByRunnerId(999L);

        // then
        assertThat(actual).isEmpty();
    }
}
