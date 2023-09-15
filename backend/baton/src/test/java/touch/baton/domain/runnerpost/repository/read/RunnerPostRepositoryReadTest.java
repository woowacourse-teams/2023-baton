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

    @DisplayName("생성 시간이 같다면 다음 기준인 id 오름차순으로 게시글 페이징 조회한다")
    @Test
    void findByReviewStatus_when_createAt_is_same() {
        // given
        final Member ditoo = MemberFixture.createDitoo();
        em.persist(ditoo);
        final Runner runner = RunnerFixture.createRunner(ditoo);
        em.persist(runner);
        final Long runnerId = runner.getId();
        final LocalDateTime createdAt = LocalDateTime.now();

        em.createNativeQuery("""
                        insert into runner_post (id, title, implemented_contents, curious_contents, postscript_contents, 
                            pull_request_url, deadline, review_status, created_at, updated_at, runner_id)
                        values (:id, :title, :implemented_contents, :curious_contents, :postscript_contents, 
                            :pull_request_url, :deadline, :review_status, :created_at, :updated_at, :runner_id)
                        """)
                .setParameter("id", 1L)
                .setParameter("title", "제목1")
                .setParameter("implemented_contents", "구현 내용1")
                .setParameter("curious_contents", "궁금한 내용1")
                .setParameter("postscript_contents", "참고 사항1")
                .setParameter("pull_request_url", "pr url1")
                .setParameter("deadline", LocalDateTime.now().plusHours(100))
                .setParameter("review_status", NOT_STARTED.name())
                .setParameter("created_at", createdAt)
                .setParameter("updated_at", createdAt)
                .setParameter("runner_id", runnerId)
                .executeUpdate();

        em.createNativeQuery("""
                        insert into runner_post (id, title, implemented_contents, curious_contents, postscript_contents, 
                            pull_request_url, deadline, review_status, created_at, updated_at, runner_id)
                        values (:id, :title, :implemented_contents, :curious_contents, :postscript_contents, 
                            :pull_request_url, :deadline, :review_status, :created_at, :updated_at, :runner_id)
                        """)
                .setParameter("id", 3L)
                .setParameter("title", "제목3")
                .setParameter("implemented_contents", "구현 내용3")
                .setParameter("curious_contents", "궁금한 내용3")
                .setParameter("postscript_contents", "참고 사항3")
                .setParameter("pull_request_url", "pr url3")
                .setParameter("deadline", LocalDateTime.now().plusHours(100))
                .setParameter("review_status", NOT_STARTED.name())
                .setParameter("created_at", createdAt)
                .setParameter("updated_at", createdAt)
                .setParameter("runner_id", runnerId)
                .executeUpdate();

        em.createNativeQuery("""
                        insert into runner_post (id, title, implemented_contents, curious_contents, postscript_contents, 
                            pull_request_url, deadline, review_status, created_at, updated_at, runner_id)
                        values (:id, :title, :implemented_contents, :curious_contents, :postscript_contents, 
                            :pull_request_url, :deadline, :review_status, :created_at, :updated_at, :runner_id)
                        """)
                .setParameter("id", 2L)
                .setParameter("title", "제목2")
                .setParameter("implemented_contents", "구현 내용2")
                .setParameter("curious_contents", "궁금한 내용2")
                .setParameter("postscript_contents", "참고 사항2")
                .setParameter("pull_request_url", "pr url2")
                .setParameter("deadline", LocalDateTime.now().plusHours(100))
                .setParameter("review_status", NOT_STARTED.name())
                .setParameter("created_at", createdAt)
                .setParameter("updated_at", createdAt)
                .setParameter("runner_id", runnerId)
                .executeUpdate();

        // when
        final PageRequest pageable = PageRequest.of(0, 10, Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("id")
        ));
        final Page<RunnerPost> actual = runnerPostRepository.findByReviewStatus(pageable, NOT_STARTED);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.getTotalElements()).isEqualTo(3);

            List<RunnerPost> contents = actual.getContent();
            softly.assertThat(contents.get(0).getId()).isEqualTo(3L);
            softly.assertThat(contents.get(1).getId()).isEqualTo(2L);
            softly.assertThat(contents.get(2).getId()).isEqualTo(1L);
        });
    }
}
