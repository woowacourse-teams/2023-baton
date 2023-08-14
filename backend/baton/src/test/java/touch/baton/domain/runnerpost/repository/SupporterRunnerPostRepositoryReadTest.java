package touch.baton.domain.runnerpost.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.repository.SupporterRepository;
import touch.baton.domain.supporter.repository.SupporterRunnerPostRepository;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class SupporterRunnerPostRepositoryReadTest extends RepositoryTestConfig {

    @Autowired
    private SupporterRunnerPostRepository supporterRunnerPostRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private SupporterRepository supporterRepository;

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @DisplayName("RunnerPostId 와 SupporterId 로 존재 유무를 확인할 수 있다.")
    @Test
    void existsByRunnerPostIdAndSupporterId() {
        // given
        final Member ehtanMember = memberRepository.save(MemberFixture.createEthan());
        final Runner runnerPostOwner = runnerRepository.save(RunnerFixture.createRunner(ehtanMember));
        final RunnerPost runner = runnerPostRepository.save(RunnerPostFixture.create(runnerPostOwner,
                deadline(LocalDateTime.now().plusDays(10))));

        final Member hyenaMember = memberRepository.save(MemberFixture.createHyena());
        final Supporter supporter = supporterRepository.save(SupporterFixture.create(hyenaMember));
        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(runner, supporter));

        final Long notSavedRunnerPostId = -1L;
        final Long notSavedSupporter = -1L;

        // when, then
        assertSoftly(softly -> {
                    softly.assertThat(supporterRunnerPostRepository.existsByRunnerPostIdAndSupporterId(runner.getId(), supporter.getId())).isTrue();
                    softly.assertThat(supporterRunnerPostRepository.existsByRunnerPostIdAndSupporterId(notSavedRunnerPostId, supporter.getId())).isFalse();
                    softly.assertThat(supporterRunnerPostRepository.existsByRunnerPostIdAndSupporterId(runner.getId(), notSavedSupporter)).isFalse();
                }
        );
    }
}
