package touch.baton.domain.runner.service;

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
import touch.baton.domain.member.vo.ImageUrl;
import touch.baton.domain.member.vo.MemberName;
import touch.baton.domain.member.vo.OauthId;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.exception.OldRunnerException;
import touch.baton.domain.runner.repository.RunnerRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(JpaConfig.class)
@DataJpaTest
class RunnerServiceReadTest {

    private RunnerService runnerService;
    
    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private MemberRepository memberRepository;
    private static final MemberName memberName = new MemberName("헤에디주");
    private static final Email email = new Email("test@test.co.kr");
    private static final OauthId oauthId = new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j");
    private static final GithubUrl githubUrl = new GithubUrl("github.com/hyena0608");
    private static final Company company = new Company("우아한형제들");
    private static final ImageUrl imageUrl = new ImageUrl("홍혁준");
    private static final TotalRating totalRating = new TotalRating(100);
    private static final Grade grade = Grade.BARE_FOOT;

    private Runner runner;

    @BeforeEach
    void setUp() {
        runnerService = new RunnerService(runnerRepository);

        final Member member = Member.builder()
                .memberName(memberName)
                .email(email)
                .oauthId(oauthId)
                .githubUrl(githubUrl)
                .company(company)
                .imageUrl(imageUrl)
                .build();
        memberRepository.save(member);

        runner = Runner.builder()
                .totalRating(totalRating)
                .grade(grade)
                .member(member)
                .build();
        runnerRepository.save(runner);
    }

    @DisplayName("Runner 를 Member 와 조인해서 조회할 수 있다.")
    @Test
    void success_readRunnerWithMember() {
        // when
        final Runner actual = runnerService.readRunnerWithMember(runner.getId());

        // then
        assertThat(actual).isEqualTo(runner);
    }

    @DisplayName("식별자로 Runner 와 Member 를 조인해서 조회할 때, 식별자에 해당하는 데이터가 없으면 예외를 던진다.")
    @Test
    void fail_readRunnerWithMember_if_id_is_invalid() {
        // when, then
        assertThatThrownBy(() -> runnerService.readRunnerWithMember(999L))
                .isInstanceOf(OldRunnerException.NotFound.class);
    }
}
