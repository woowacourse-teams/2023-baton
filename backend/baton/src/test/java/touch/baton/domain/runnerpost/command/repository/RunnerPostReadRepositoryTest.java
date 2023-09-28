package touch.baton.domain.runnerpost.command.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.repository.dto.RunnerPostApplicantCountDto;
import touch.baton.domain.runnerpost.command.vo.ReviewStatus;
import touch.baton.domain.runnerpost.query.repository.RunnerPostQueryRepository;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.command.vo.TagReducedName;
import touch.baton.fixture.domain.MemberFixture;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RunnerPostReadRepositoryTest extends RepositoryTestConfig {

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

    @DisplayName("축약된 태그 이름과 리뷰 상태로 러너 게시글 페이징 조회에 성공한다.")
    @Test
    void findByTagReducedNameAndReviewStatus() {
        // given
        final Runner hyenaRunner = persistRunner(MemberFixture.createHyena());

        final RunnerPost runnerPostOne = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostTwo = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostThree = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostFour = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostFive = persistRunnerPost(hyenaRunner);
        final RunnerPost runnerPostSix = persistRunnerPost(hyenaRunner);

        final Tag javaTag = persistTag("자바");
        final Tag springTag = persistTag("스프링");

        persistRunnerPostTag(runnerPostOne, javaTag);
        persistRunnerPostTag(runnerPostTwo, javaTag);
        persistRunnerPostTag(runnerPostThree, javaTag);
        persistRunnerPostTag(runnerPostFour, springTag);
        persistRunnerPostTag(runnerPostFive, javaTag);
        persistRunnerPostTag(runnerPostSix, springTag);

        em.flush();
        em.close();

        // when
        final PageRequest pageOne = PageRequest.of(0, 10);

        final Page<RunnerPost> foundRunnerPosts = runnerPostQueryRepository.findByTagReducedNameAndReviewStatus(
                pageOne,
                TagReducedName.from("자바"),
                ReviewStatus.NOT_STARTED
        );

        // then
        final List<RunnerPost> expected = List.of(runnerPostOne, runnerPostTwo, runnerPostThree, runnerPostFive);

        assertThat(foundRunnerPosts.getContent()).isEqualTo(expected);
    }
}
