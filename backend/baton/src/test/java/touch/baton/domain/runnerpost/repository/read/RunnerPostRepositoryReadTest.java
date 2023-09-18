package touch.baton.domain.runnerpost.repository.read;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.domain.tag.Tag;
import touch.baton.domain.tag.repository.RunnerPostTagRepository;
import touch.baton.domain.tag.repository.TagRepository;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.RunnerPostTagFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.domain.runnerpost.vo.ReviewStatus.NOT_STARTED;
import static touch.baton.fixture.domain.RunnerPostTagsFixture.runnerPostTags;
import static touch.baton.fixture.vo.CuriousContentsFixture.curiousContents;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.ImplementedContentsFixture.implementedContents;
import static touch.baton.fixture.vo.PostscriptContentsFixture.postscriptContents;
import static touch.baton.fixture.vo.PullRequestUrlFixture.pullRequestUrl;
import static touch.baton.fixture.vo.TitleFixture.title;
import static touch.baton.fixture.vo.WatchedCountFixture.watchedCount;
import static touch.baton.util.TestDateFormatUtil.createExpireDate;

class RunnerPostRepositoryReadTest extends RepositoryTestConfig {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private RunnerPostTagRepository runnerPostTagRepository;

    @Autowired
    private EntityManager em;

    @DisplayName("RunnerPost 식별자로 RunnerPostTag 목록을 조회할 때 Tag 가 있으면 조회된다.")
    @Test
    void findRunnerPostTagsById_exist() {
        // given
        final Member ditoo = MemberFixture.createDitoo();
        em.persist(ditoo);
        final Runner runner = RunnerFixture.createRunner(ditoo);
        em.persist(runner);

        final RunnerPost runnerPost = RunnerPostFixture.create(title("제 코드를 리뷰해주세요"),
                implementedContents("제 코드의 내용은 이렇습니다."),
                curiousContents("저는 이것이 궁금합니다."),
                postscriptContents("잘 부탁드립니다."),
                pullRequestUrl("https://"),
                deadline(LocalDateTime.now().plusHours(10)),
                watchedCount(0),
                NOT_STARTED,
                runner,
                null,
                runnerPostTags(new ArrayList<>()));
        runnerPostRepository.save(runnerPost);

        final Tag java = TagFixture.createJava();
        em.persist(java);
        final Tag spring = TagFixture.createSpring();
        em.persist(spring);
        final RunnerPostTag javaRunnerPostTag = RunnerPostTagFixture.create(runnerPost, java);
        final RunnerPostTag springRunnerPostTag = RunnerPostTagFixture.create(runnerPost, spring);

        runnerPost.addAllRunnerPostTags(List.of(javaRunnerPostTag, springRunnerPostTag));

        // when
        final List<RunnerPostTag> expected = runnerPostTagRepository.joinTagByRunnerPostId(runnerPost.getId());

        // then
        assertThat(expected).containsExactly(javaRunnerPostTag, springRunnerPostTag);
    }

    @DisplayName("Runner 식별자로 RunnerPost 목록을 조회한다.")
    @Test
    void findByRunnerId() {
        // given
        final Member ditoo = MemberFixture.createDitoo();
        em.persist(ditoo);
        final Runner runner = RunnerFixture.createRunner(ditoo);
        em.persist(runner);

        final RunnerPost runnerPost = RunnerPostFixture.create(title("제 코드를 리뷰해주세요"),
                implementedContents("제 코드의 내용은 이렇습니다."),
                curiousContents("저는 이것이 궁금합니다."),
                postscriptContents("잘 부탁드립니다."),
                pullRequestUrl("https://"),
                deadline(LocalDateTime.now().plusHours(10)),
                watchedCount(0),
                NOT_STARTED,
                runner,
                null,
                runnerPostTags(new ArrayList<>()));
        runnerPostRepository.save(runnerPost);

        // when
        final List<RunnerPost> actual = runnerPostRepository.findByRunnerId(runner.getId());

        // then
        assertThat(actual).containsExactly(runnerPost);
    }

    @DisplayName("id 오름차순으로 게시글 페이징 조회한다")
    @Test
    void findByReviewStatus_sort_by_id() {
        // given
        final Member ditoo = MemberFixture.createDitoo();
        em.persist(ditoo);
        final Runner runner = RunnerFixture.createRunner(ditoo);
        em.persist(runner);
        final Long runnerId = runner.getId();
        final LocalDateTime createdAt = createExpireDate(LocalDateTime.now());

        insertRunnerPostByNativeQuery(1L, createdAt, runnerId);
        insertRunnerPostByNativeQuery(3L, createdAt, runnerId);
        insertRunnerPostByNativeQuery(2L, createdAt, runnerId);

        // when
        final PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("id")));

        final Page<RunnerPost> actual = runnerPostRepository.findByReviewStatus(pageable, NOT_STARTED);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getTotalElements()).isEqualTo(3);

            List<RunnerPost> runnerPosts = actual.getContent();
            softly.assertThat(runnerPosts.get(0).getId()).isEqualTo(3L);
            softly.assertThat(runnerPosts.get(1).getId()).isEqualTo(2L);
            softly.assertThat(runnerPosts.get(2).getId()).isEqualTo(1L);

            softly.assertThat(runnerPosts.stream()
                    .filter(runnerPost -> runnerPost.getCreatedAt().isEqual(createdAt))
                    .count()).isEqualTo(3);
        });
    }

    private void insertRunnerPostByNativeQuery(final long value, final LocalDateTime createdAt, final Long runnerId) {
        em.createNativeQuery("""
                        insert into runner_post (id, title, implemented_contents, curious_contents, postscript_contents, 
                            pull_request_url, deadline, review_status, created_at, updated_at, runner_id)
                        values (:id, :title, :implemented_contents, :curious_contents, :postscript_contents, 
                            :pull_request_url, :deadline, :review_status, :created_at, :updated_at, :runner_id)
                        """)
                .setParameter("id", value)
                .setParameter("title", "제목")
                .setParameter("implemented_contents", "구현 내용")
                .setParameter("curious_contents", "궁금한 내용")
                .setParameter("postscript_contents", "참고 사항")
                .setParameter("pull_request_url", "pr url")
                .setParameter("deadline", LocalDateTime.now().plusHours(100))
                .setParameter("review_status", NOT_STARTED.name())
                .setParameter("created_at", createdAt)
                .setParameter("updated_at", createdAt)
                .setParameter("runner_id", runnerId)
                .executeUpdate();
    }
}
