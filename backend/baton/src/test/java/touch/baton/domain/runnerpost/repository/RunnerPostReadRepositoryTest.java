package touch.baton.domain.runnerpost.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.dto.ApplicantCountDto;
import touch.baton.domain.runnerpost.repository.dto.ApplicantCountMappingDto;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagReducedName;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.RunnerPostTagFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

class RunnerPostReadRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private RunnerPostReadRepository runnerPostReadRepository;

    @Autowired
    private EntityManager em;

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
        final List<ApplicantCountDto> actual = runnerPostReadRepository.countApplicantsByRunnerPostIds(List.of(
                runnerPostTwo.getId(),
                runnerPostThree.getId(),
                runnerPostFour.getId(),
                runnerPostFive.getId(),
                runnerPostSix.getId(),
                runnerPostOne.getId()
        ));

        // then
        final List<ApplicantCountDto> expected = List.of(
                new ApplicantCountDto(runnerPostTwo.getId(), 2L),
                new ApplicantCountDto(runnerPostThree.getId(), 1L),
                new ApplicantCountDto(runnerPostFour.getId(), 0L),
                new ApplicantCountDto(runnerPostFive.getId(), 0L),
                new ApplicantCountDto(runnerPostSix.getId(), 0L),
                new ApplicantCountDto(runnerPostOne.getId(), 3L)
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
        final ApplicantCountMappingDto actual = runnerPostReadRepository.findApplicantCountMappingByRunnerPostIds(List.of(
                runnerPostTwo.getId(),
                runnerPostThree.getId(),
                runnerPostFour.getId(),
                runnerPostFive.getId(),
                runnerPostSix.getId(),
                runnerPostOne.getId()
        ));

        // then
        final ApplicantCountMappingDto expected = new ApplicantCountMappingDto(Map.of(
                runnerPostTwo.getId(), 2L,
                runnerPostThree.getId(), 1L,
                runnerPostFour.getId(), 0L,
                runnerPostFive.getId(), 0L,
                runnerPostSix.getId(), 0L,
                runnerPostOne.getId(), 3L
        ));

        assertThat(actual).isEqualTo(expected);
    }

    private Runner persistRunner(final Member member) {
        em.persist(member);
        final Runner runner = RunnerFixture.createRunner(member);
        em.persist(runner);

        return runner;
    }

    private Supporter persistSupporter(final Member member) {
        em.persist(member);
        final Supporter supporter = SupporterFixture.create(member);
        em.persist(supporter);

        return supporter;
    }

    private RunnerPost persistRunnerPost(final Runner runner) {
        final RunnerPost runnerPostOne = RunnerPostFixture.create(runner, deadline(LocalDateTime.now().plusHours(100)));
        em.persist(runnerPostOne);

        return runnerPostOne;
    }

    private SupporterRunnerPost persistApplicant(final Supporter supporter, final RunnerPost runnerPost) {
        final SupporterRunnerPost applicant = SupporterRunnerPostFixture.create(runnerPost, supporter);
        em.persist(applicant);

        return applicant;
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

        final Page<RunnerPost> foundRunnerPosts = runnerPostReadRepository.findByTagReducedNameAndReviewStatus(
                pageOne,
                TagReducedName.from("자바"),
                ReviewStatus.NOT_STARTED
        );

        // then
        final List<RunnerPost> expected = List.of(runnerPostOne, runnerPostTwo, runnerPostThree, runnerPostFive);

        assertThat(foundRunnerPosts.getContent()).isEqualTo(expected);
    }

    private Tag persistTag(final String tagName) {
        final Tag javaTag = TagFixture.create(tagName(tagName));
        em.persist(javaTag);

        return javaTag;
    }

    private RunnerPostTag persistRunnerPostTag(final RunnerPost runnerPost, final Tag tag) {
        final RunnerPostTag runnerPostTag = RunnerPostTagFixture.create(runnerPost, tag);
        em.persist(runnerPostTag);

        return runnerPostTag;
    }
}
