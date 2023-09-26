package touch.baton.domain.runner.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.repository.MemberCommandRepository;
import touch.baton.tobe.domain.member.command.vo.Company;
import touch.baton.tobe.domain.member.command.vo.GithubUrl;
import touch.baton.tobe.domain.member.command.vo.ImageUrl;
import touch.baton.tobe.domain.member.command.vo.MemberName;
import touch.baton.tobe.domain.member.command.vo.OauthId;
import touch.baton.tobe.domain.member.command.vo.SocialId;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.fixture.domain.RunnerTechnicalTagsFixture;
import touch.baton.tobe.domain.member.query.repository.RunnerQueryRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class RunnerQueryRepositoryTest extends RepositoryTestConfig {

    private static final MemberName memberName = new MemberName("헤에디주");
    private static final SocialId socialId = new SocialId("testSocialId");
    private static final OauthId oauthId = new OauthId("dsigjh98gh230gn2oinv913bcuo23nqovbvu93b12voi3bc31j");
    private static final GithubUrl githubUrl = new GithubUrl("github.com/hyena0608");
    private static final Company company = new Company("우아한형제들");
    private static final ImageUrl imageUrl = new ImageUrl("김석호");

    @Autowired
    private RunnerQueryRepository runnerQueryRepository;

    @Autowired
    private MemberCommandRepository memberCommandRepository;

    private Runner runner;

    @BeforeEach
    void setUp() {
        final Member member = Member.builder()
                .memberName(memberName)
                .socialId(socialId)
                .oauthId(oauthId)
                .githubUrl(githubUrl)
                .company(company)
                .imageUrl(imageUrl)
                .build();
        memberCommandRepository.save(member);

        runner = Runner.builder()
                .member(member)
                .runnerTechnicalTags(RunnerTechnicalTagsFixture.create(new ArrayList<>()))
                .build();
    }

    @DisplayName("Runner 를 Member 와 조인해서 조회할 수 있다.")
    @Test
    void findByIdJoinMember() {
        // given
        final Runner expected = runnerQueryRepository.save(runner);

        // when
        final Optional<Runner> actual = runnerQueryRepository.joinMemberByRunnerId(expected.getId());

        // then
        assertThat(actual).isPresent();
        final Member actualMember = actual.get().getMember();
        assertAll(
                () -> assertThat(actualMember.getId()).isNotNull(),
                () -> assertThat(actualMember.getMemberName()).isEqualTo(memberName),
                () -> assertThat(actualMember.getCompany()).isEqualTo(company),
                () -> assertThat(actualMember.getSocialId()).isEqualTo(socialId),
                () -> assertThat(actualMember.getOauthId()).isEqualTo(oauthId),
                () -> assertThat(actualMember.getGithubUrl()).isEqualTo(githubUrl)
        );
    }

    @DisplayName("식별자가 없으면 Optional.empty()가 반환된다.")
    @Test
    void findByIdJoinMember_if_id_is_not_exists() {
        // when
        final Optional<Runner> actual = runnerQueryRepository.joinMemberByRunnerId(999L);

        // then
        assertThat(actual).isEmpty();
    }
}
