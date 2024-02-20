package touch.baton.domain.runnerpost.query.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.repository.dto.RunnerPostApplicantCountDto;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerPostFixture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class RunnerPostQueryRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private RunnerPostQueryRepository runnerPostQueryRepository;

    @DisplayName("러너 게시글 식별자값 목록으로 서포터 지원자 수를 조회에 성공한다.")
    @Test
    void countApplicantsByRunnerPostIds() {
        // given
        final Runner hyenaRunner = persistRunner(MemberFixture.createHyena());
        final Supporter ditooSupporter = persistSupporter(MemberFixture.createDitoo());
        final Supporter ethanSupporter = persistSupporter(MemberFixture.createEthan());
        final Supporter judySupporter = persistSupporter(MemberFixture.createJudy());

        final RunnerPost runnerPostOne = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostTwo = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostThree = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostFour = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostFive = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostSix = persistRunnerPost(hyenaRunner);

        persistApplicant(ditooSupporter, runnerPostOne);
        persistApplicant(ethanSupporter, runnerPostOne);
        persistApplicant(judySupporter, runnerPostOne);

        persistApplicant(ditooSupporter, runnerPostTwo);
        persistApplicant(ethanSupporter, runnerPostTwo);

        persistApplicant(ditooSupporter, runnerPostThree);

        em.flush();
        em.close();

        // when
        final List<RunnerPostApplicantCountDto> actual = runnerPostQueryRepository.countApplicantsByRunnerPostIds(List.of(
                runnerPostOne.getId(),
                runnerPostTwo.getId(),
                runnerPostThree.getId(),
                runnerPostFour.getId(),
                runnerPostFive.getId(),
                runnerPostSix.getId(),
                runnerPostOne.getId()
        ));

        // then
        final List<RunnerPostApplicantCountDto> expected = List.of(
                new RunnerPostApplicantCountDto(runnerPostTwo.getId(), 2L),
                new RunnerPostApplicantCountDto(runnerPostThree.getId(), 1L),
                new RunnerPostApplicantCountDto(runnerPostFour.getId(), 0L),
                new RunnerPostApplicantCountDto(runnerPostFive.getId(), 0L),
                new RunnerPostApplicantCountDto(runnerPostSix.getId(), 0L),
                new RunnerPostApplicantCountDto(runnerPostOne.getId(), 3L)
        );

        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
    }

    @DisplayName("러너 게시글 식별자값 목록으로 서포터 지원자 수 매핑 정보 조회에 성공한다.")
    @Test
    void findApplicantCountMappingByRunnerPostIds() {
        // given
        final Runner hyenaRunner = persistRunner(MemberFixture.createHyena());
        final Supporter ditooSupporter = persistSupporter(MemberFixture.createDitoo());
        final Supporter ethanSupporter = persistSupporter(MemberFixture.createEthan());
        final Supporter judySupporter = persistSupporter(MemberFixture.createJudy());

        final RunnerPost runnerPostOne = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostTwo = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostThree = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostFour = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostFive = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostSix = persistRunnerPost(hyenaRunner);

        persistApplicant(ditooSupporter, runnerPostOne);
        persistApplicant(ethanSupporter, runnerPostOne);
        persistApplicant(judySupporter, runnerPostOne);

        persistApplicant(ditooSupporter, runnerPostTwo);
        persistApplicant(ethanSupporter, runnerPostTwo);

        persistApplicant(ditooSupporter, runnerPostThree);

        em.flush();
        em.close();

        // when
        final List<RunnerPostApplicantCountDto> actual = runnerPostQueryRepository.countApplicantsByRunnerPostIds(List.of(
                runnerPostOne.getId(),
                runnerPostTwo.getId(),
                runnerPostThree.getId(),
                runnerPostFour.getId(),
                runnerPostFive.getId(),
                runnerPostSix.getId()
        ));

        // then
        final List<RunnerPostApplicantCountDto> expected = new ArrayList<>(List.of(
                new RunnerPostApplicantCountDto(runnerPostOne.getId(), 3L),
                new RunnerPostApplicantCountDto(runnerPostTwo.getId(), 2L),
                new RunnerPostApplicantCountDto(runnerPostThree.getId(), 1L),
                new RunnerPostApplicantCountDto(runnerPostFour.getId(), 0L),
                new RunnerPostApplicantCountDto(runnerPostFive.getId(), 0L),
                new RunnerPostApplicantCountDto(runnerPostSix.getId(), 0L)
        ));

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("러너 게시글 식별자값으로 러너 게시글과 서포터, 사용자를 조인하여 조회한다.")
    @Test
    void joinSupporterByRunnerPostId() {
        // given
        final Runner hyenaRunner = persistRunner(MemberFixture.createHyena());
        final Supporter ditooSupporter = persistSupporter(MemberFixture.createDitoo());
        final RunnerPost runnerPost = persistRunnerPost(hyenaRunner);
        persistApplicant(ditooSupporter, runnerPost);
        runnerPost.assignSupporter(ditooSupporter);

        em.flush();
        em.close();

        // when
        final Optional<RunnerPost> maybeActual = runnerPostQueryRepository.joinSupporterByRunnerPostId(runnerPost.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(maybeActual).isPresent();
            final RunnerPost actual = maybeActual.get();

            softly.assertThat(actual.getSupporter()).isEqualTo(ditooSupporter);
            softly.assertThat(actual).isEqualTo(runnerPost);
        });
    }

    @DisplayName("러너 관련 게시글 개수를 조회하는데 성공한다.")
    @Test
    void countByRunnerIdAndReviewStatus() {
        // given
        final Runner runner = persistRunner(MemberFixture.createDitoo());
        final int expected = 5;
        for (int i = 0; i < expected; i++) {
            persistRunnerPost(runner);
        }

        em.flush();
        em.close();

        // when
        final Long actual = runnerPostQueryRepository.countByRunnerIdAndReviewStatus(runner.getId(), ReviewStatus.NOT_STARTED);

        // then
        assertThat(actual.intValue()).isEqualTo(expected);
    }

    @DisplayName("서포터 관련 게시글 개수를 조회하는데 성공한다.")
    @Test
    void countBySupporterIdAndReviewStatus() {
        // given
        final Runner runner = persistRunner(MemberFixture.createEthan());
        final Supporter supporter = persistSupporter(MemberFixture.createDitoo());
        final int expected = 3;
        for (int i = 0; i < expected; i++) {
            final RunnerPost runnerPost = persistRunnerPost(runner);
            runnerPost.assignSupporter(supporter);
            runnerPost.finishReview();
        }

        em.flush();
        em.close();

        // when
        final Long actual = runnerPostQueryRepository.countBySupporterIdAndReviewStatus(supporter.getId(), ReviewStatus.DONE);

        // then
        assertThat(actual.intValue()).isEqualTo(expected);
    }

    @DisplayName("전체 글에서 리뷰 상태별로 게시글 개수를 조회할 수 있다.")
    @MethodSource("provideCountByReviewStatus")
    @ParameterizedTest
    void countByReviewStatus(final Long notStartedCount,
                             final Long inProgressCount,
                             final Long doneCount,
                             final Long overdueCount,
                             final ReviewStatus targetReviewStatus,
                             final Long expected
    ) {
        // given
        final Member member = persistMember(MemberFixture.createEthan());
        final Runner runner = persistRunner(member);

        for (int i = 0; i < notStartedCount; i++) {
            persistRunnerPost(RunnerPostFixture.create(runner, ReviewStatus.NOT_STARTED));
        }
        for (int i = 0; i < inProgressCount; i++) {
            persistRunnerPost(RunnerPostFixture.create(runner, ReviewStatus.IN_PROGRESS));
        }
        for (int i = 0; i < doneCount; i++) {
            persistRunnerPost(RunnerPostFixture.create(runner, ReviewStatus.DONE));
        }
        for (int i = 0; i < overdueCount; i++) {
            persistRunnerPost(RunnerPostFixture.create(runner, ReviewStatus.OVERDUE));
        }

        // when, then
        assertThat(runnerPostQueryRepository.countByReviewStatus(targetReviewStatus)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideCountByReviewStatus() {
        return Stream.of(
                Arguments.of(4L, 3L, 2L, 1L, ReviewStatus.NOT_STARTED, 4L),
                Arguments.of(4L, 3L, 2L, 1L, ReviewStatus.IN_PROGRESS, 3L),
                Arguments.of(4L, 3L, 2L, 1L, ReviewStatus.DONE, 2L),
                Arguments.of(4L, 3L, 2L, 1L, ReviewStatus.OVERDUE, 1L)
        );
    }
}
