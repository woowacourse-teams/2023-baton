package touch.baton.domain.supporter.query.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.SupporterRunnerPost;
import touch.baton.domain.member.query.repository.SupporterRunnerPostQueryRepository;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.fixture.domain.MemberFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SupporterRunnerPostQueryRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private SupporterRunnerPostQueryRepository supporterRunnerPostQueryRepository;

    @DisplayName("Member 가 SupporterRunnerPost 에 지원한 이력이 있을 경우 true 를 반환한다.")
    @Test
    void existsByRunnerPostIdAndMemberId_return_true() {
        // given
        final Runner runner = persistRunner(MemberFixture.createDitoo());
        final RunnerPost runnerPost = persistRunnerPost(runner);
        final Supporter supporter = persistSupporter(MemberFixture.createEthan());
        final SupporterRunnerPost supporterRunnerPost = persistApplicant(supporter, runnerPost);

        supporterRunnerPostQueryRepository.save(supporterRunnerPost);

        // when
        final boolean isApplicantHistoryExist = supporterRunnerPostQueryRepository.existsByRunnerPostIdAndMemberId(
                runnerPost.getId(),
                supporter.getMember().getId()
        );

        // then
        assertThat(isApplicantHistoryExist).isTrue();
    }

    @DisplayName("Member 가 SupporterRunnerPost 에 지원한 이력이 없을 경우 false 를 반환한다.")
    @Test
    void existsByRunnerPostIdAndMemberId_if_supporterRunnerPost_is_not_exist_then_return_false() {
        // given
        final Runner runner = persistRunner(MemberFixture.createDitoo());
        final RunnerPost runnerPost = persistRunnerPost(runner);
        final Supporter supporter = persistSupporter(MemberFixture.createEthan());

        // when
        final boolean isApplicantHistoryNotExist = supporterRunnerPostQueryRepository.existsByRunnerPostIdAndMemberId(
                runnerPost.getId(),
                supporter.getMember().getId()
        );

        // then
        assertThat(isApplicantHistoryNotExist).isFalse();
    }

    @DisplayName("Member 가 SupporterRunnerPost 에 지원한 이력을 조회할 때 RunnerPost 자체가 없으면 false 를 반환한다.")
    @Test
    void existsByRunnerPostIdAndMemberId_if_runnerPost_is_not_exist_then_return_false() {
        // given
        final Supporter supporter = persistSupporter(MemberFixture.createDitoo());

        // when
        final Long notExistRunnerPostId = -1L;
        final boolean isApplicantHistoryNotExist = supporterRunnerPostQueryRepository.existsByRunnerPostIdAndMemberId(
                notExistRunnerPostId,
                supporter.getMember().getId()
        );

        // then
        assertThat(isApplicantHistoryNotExist).isFalse();
    }

    @DisplayName("RunnerPostId 로 SupporterRunnerPost 목록을 조회한다.")
    @Test
    void readByRunnerPostId() {
        // given
        final Runner runner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(runner);
        final Supporter supporterDitoo = persistSupporter(MemberFixture.createDitoo());
        final SupporterRunnerPost supporterDitooRunnerPost = persistApplicant(supporterDitoo, runnerPost);
        final Supporter supporterEthan = persistSupporter(MemberFixture.createEthan());
        final SupporterRunnerPost supporterEthanRunnerPost = persistApplicant(supporterEthan, runnerPost);

        // when
        final List<SupporterRunnerPost> actual = supporterRunnerPostQueryRepository.readByRunnerPostId(runnerPost.getId());

        // then
        assertThat(actual).containsExactly(supporterDitooRunnerPost, supporterEthanRunnerPost);
    }

    @DisplayName("SupporterId 와 Not Started 인 ReviewStatus 로 RunnerPost 개수를 센다.")
    @Test
    void countRunnerPostBySupporterIdByReviewStatusNotStarted() {
        // given
        final Runner runner = persistRunner(MemberFixture.createHyena());
        final RunnerPost runnerPost = persistRunnerPost(runner);
        final Supporter supporter = persistSupporter(MemberFixture.createDitoo());
        persistApplicant(supporter, runnerPost);

        // when
        final long count = supporterRunnerPostQueryRepository.countRunnerPostBySupporterIdByReviewStatusNotStarted(supporter.getId());

        // then
        assertThat(count).isEqualTo(1);
    }
}
