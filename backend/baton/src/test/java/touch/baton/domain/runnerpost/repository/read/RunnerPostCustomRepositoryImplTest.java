package touch.baton.domain.runnerpost.repository.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.vo.TagReducedName;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerPostFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.domain.runnerpost.vo.ReviewStatus.NOT_STARTED;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class RunnerPostCustomRepositoryImplTest extends RepositoryTestConfig {

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @DisplayName("리뷰 상태로 러너 게시글을 페이지 조회한다 (중간 페이지 조회)")
    @Test
    void findByPageInfoAndReviewStatus() {
        // given
        final Runner runner = persistRunner(MemberFixture.createDitoo());
        final ReviewStatus reviewStatus = NOT_STARTED;
        final List<Long> runnerPostIds = new ArrayList<>();
        final int persistSize = 30;
        for (int i = 0; i < persistSize; i++) {
            runnerPostIds.add(persistRunnerPost(runner, reviewStatus).getId());
        }
        final int lastIndex = persistSize - 1;
        final Long previousLastId = runnerPostIds.get(lastIndex);
        final int limit = 10;

        // when
        final List<RunnerPost> runnerPosts = runnerPostRepository.findByPageInfoAndReviewStatusAndTagReducedName(previousLastId, limit, null, reviewStatus);
        runnerPostIds.sort(Comparator.reverseOrder());
        final List<Long> expected = runnerPostIds.subList(1, 1 + limit);

        // then
        assertSoftly(softly -> {
           softly.assertThat(runnerPosts).hasSize(limit);
           softly.assertThat(runnerPosts.stream().mapToLong(RunnerPost::getId))
                   .isEqualTo(expected);
        });
    }

    @DisplayName("리뷰 상태로 러너 게시글을 페이지 조회한다 (첫 페이지 조회)")
    @Test
    void findLatestByLimitAndReviewStatus() {
        // given
        final Runner runner = persistRunner(MemberFixture.createDitoo());
        final ReviewStatus reviewStatus = NOT_STARTED;
        final int runnerPostCount = 10;
        final List<Long> runnerPostIds = new ArrayList<>();
        for (int i = 0; i < runnerPostCount; i++) {
            runnerPostIds.add(persistRunnerPost(runner, reviewStatus).getId());
        }
        final int limit = 10;

        // when
        final List<RunnerPost> runnerPosts = runnerPostRepository.findByPageInfoAndReviewStatusAndTagReducedName(null, limit, null, reviewStatus);
        runnerPostIds.sort(Comparator.reverseOrder());
        final List<Long> expected = runnerPostIds;

        // then
        assertSoftly(softly -> {
            softly.assertThat(runnerPosts).hasSize(limit);
            softly.assertThat(runnerPosts.stream().mapToLong(RunnerPost::getId))
                    .isEqualTo(expected);
        });
    }

    @DisplayName("축약된 태그 이름과 리뷰 상태로 러너 게시글을 페이지 조회한다 (중간 페이지 조회)")
    @Test
    void findByPageInfoAndReviewStatusAndTagReducedName() {
        // given
        final String tagName = "Javascript";
        final ReviewStatus reviewStatus = NOT_STARTED;

        final Runner runner = persistRunner(MemberFixture.createDitoo());
        final Tag tag = persistTag(tagName);

        final List<Long> runnerPostIds = new ArrayList<>();
        final int persistSize = 30;
        for (int i = 0; i < persistSize; i++) {
            final RunnerPost runnerPost = persistRunnerPost(runner, reviewStatus);
            persistRunnerPostTag(runnerPost, tag);
            runnerPostIds.add(runnerPost.getId());
        }
        final int lastIndex = persistSize - 1;
        final Long previousLastId = runnerPostIds.get(lastIndex);
        final int limit = 10;

        // when
        final TagReducedName tagReducedName = TagReducedName.from(tagName);
        final List<RunnerPost> runnerPosts = runnerPostRepository.findByPageInfoAndReviewStatusAndTagReducedName(previousLastId, limit, tagReducedName, reviewStatus);
        runnerPostIds.sort(Comparator.reverseOrder());
        final List<Long> expected = runnerPostIds.subList(1, 1 + limit);

        // then
        assertSoftly(softly -> {
            softly.assertThat(runnerPosts).hasSize(limit);
            softly.assertThat(runnerPosts.stream().mapToLong(RunnerPost::getId))
                    .isEqualTo(expected);
        });
    }

    @DisplayName("축약된 태그 이름과 리뷰 상태로 러너 게시글을 페이지 조회한다 (첫 페이지 조회)")
    @Test
    void findLatestByLimitAndTagNameAndReviewStatus() {
        // given
        final String tagName = "Java";
        final ReviewStatus reviewStatus = NOT_STARTED;

        final Runner runner = persistRunner(MemberFixture.createDitoo());
        final Tag tag = persistTag(tagName);

        final List<Long> runnerPostIds = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final RunnerPost runnerPost = persistRunnerPost(runner, reviewStatus);
            persistRunnerPostTag(runnerPost, tag);
            runnerPostIds.add(runnerPost.getId());
        }
        final int limit = 10;

        // when
        final TagReducedName tagReducedName = TagReducedName.from(tagName);
        final List<RunnerPost> runnerPosts = runnerPostRepository.findByPageInfoAndReviewStatusAndTagReducedName(null, limit, tagReducedName, reviewStatus);
        runnerPostIds.sort(Comparator.reverseOrder());
        final List<Long> expected = runnerPostIds;

        // then
        assertSoftly(softly -> {
            softly.assertThat(runnerPosts).hasSize(limit);
            softly.assertThat(runnerPosts.stream().mapToLong(RunnerPost::getId))
                    .isEqualTo(expected);
        });
    }

    @DisplayName("RunnerPost 목록으로 RunnerPostTag 목록을 조회한다.")
    @Test
    void findRunnerPostTagsByRunnerPosts() {
        // given
        final Tag tagReact = persistTag("react");
        final Tag tagJava = persistTag("java");

        final List<RunnerPost> runnerPosts = new ArrayList<>();
        final List<RunnerPostTag> expected = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            final Runner runner = persistRunner(MemberFixture.createDitoo());
            final RunnerPost runnerPost = persistRunnerPost(runner);
            final RunnerPostTag runnerPostTagReact = persistRunnerPostTag(runnerPost, tagReact);
            final RunnerPostTag runnerPostTagJava = persistRunnerPostTag(runnerPost, tagJava);
            runnerPosts.add(runnerPost);
            expected.addAll(List.of(runnerPostTagReact, runnerPostTagJava));
        }

        em.flush();
        em.close();

        // when
        final List<RunnerPostTag> actual = runnerPostRepository.findRunnerPostTagsByRunnerPosts(runnerPosts);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    private RunnerPost persistRunnerPost(final Runner runner, final ReviewStatus reviewStatus) {
        final RunnerPost runnerPost = RunnerPostFixture.create(runner, deadline(LocalDateTime.now().plusHours(100)), reviewStatus);
        em.persist(runnerPost);
        return runnerPost;
    }
}
