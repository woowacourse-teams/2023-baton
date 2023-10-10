package touch.baton.domain.supporter.command.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.SupporterRunnerPost;
import touch.baton.domain.member.command.repository.SupporterRunnerPostCommandRepository;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.fixture.domain.MemberFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class SupporterRunnerPostCommandRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private SupporterRunnerPostCommandRepository supporterRunnerPostCommandRepository;

    @DisplayName("RunnerPost 외래키로 된 SupporterRunnerPost 가 존재하는지 확인한다.")
    @Test
    void existsByRunnerPostId() {
        // given
        final Runner runner = persistRunner(MemberFixture.createDitoo());
        final Supporter supporter = persistSupporter(MemberFixture.createHyena());
        final RunnerPost runnerPostOfApplicantExist = persistRunnerPost(runner);
        final RunnerPost runnerPostOfApplicantNotExist = persistRunnerPost(runner);
        persistApplicant(supporter, runnerPostOfApplicantExist);

        // when
        final boolean actualOfExist = supporterRunnerPostCommandRepository.existsByRunnerPostId(runnerPostOfApplicantExist.getId());
        final boolean actualOfNotExist = supporterRunnerPostCommandRepository.existsByRunnerPostId(runnerPostOfApplicantNotExist.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actualOfExist).isTrue();
            softly.assertThat(actualOfNotExist).isFalse();
        });
    }

    @DisplayName("RunnerPostId 와 SupporterId 로 존재 유무를 확인할 수 있다.")
    @Test
    void existsByRunnerPostIdAndSupporterId() {
        // given
        final Runner runner = persistRunner(MemberFixture.createEthan());
        final RunnerPost runnerPost = persistRunnerPost(runner);
        final Supporter supporter = persistSupporter(MemberFixture.createHyena());
        persistApplicant(supporter, runnerPost);

        final Long notSavedRunnerPostId = -1L;
        final Long notSavedSupporter = -1L;

        // when, then
        assertSoftly(softly -> {
                    softly.assertThat(supporterRunnerPostCommandRepository.existsByRunnerPostIdAndSupporterId(runnerPost.getId(), supporter.getId())).isTrue();
                    softly.assertThat(supporterRunnerPostCommandRepository.existsByRunnerPostIdAndSupporterId(notSavedRunnerPostId, supporter.getId())).isFalse();
                    softly.assertThat(supporterRunnerPostCommandRepository.existsByRunnerPostIdAndSupporterId(runnerPost.getId(), notSavedSupporter)).isFalse();
                }
        );
    }

    @DisplayName("서포터의 러너 게시글 리뷰 제안을 철회하는데 성공한다")
    @Test
    void deleteBySupporterAndRunnerPostId() {
        // given
        final Supporter supporter = persistSupporter(MemberFixture.createDitoo());
        final Runner runner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(runner);
        final SupporterRunnerPost supporterRunnerPost = persistApplicant(supporter, runnerPost);

        // when
        supporterRunnerPostCommandRepository.deleteBySupporterIdAndRunnerPostId(supporterRunnerPost.getId(), runnerPost.getId());

        // then
        assertThat(supporterRunnerPostCommandRepository.findById(runnerPost.getId())).isNotPresent();
    }
}
