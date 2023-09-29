package touch.baton.domain.runnerpost.command.repository.read;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.vo.IsReviewed;
import touch.baton.domain.runnerpost.query.repository.RunnerPostQueryRepository;
import touch.baton.domain.tag.command.RunnerPostTag;
import touch.baton.domain.tag.command.Tag;
import touch.baton.domain.tag.query.repository.RunnerPostTagQueryRepository;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.RunnerPostTagFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static touch.baton.domain.runnerpost.command.vo.ReviewStatus.NOT_STARTED;
import static touch.baton.fixture.domain.RunnerPostTagsFixture.runnerPostTags;
import static touch.baton.fixture.vo.CuriousContentsFixture.curiousContents;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.ImplementedContentsFixture.implementedContents;
import static touch.baton.fixture.vo.PostscriptContentsFixture.postscriptContents;
import static touch.baton.fixture.vo.PullRequestUrlFixture.pullRequestUrl;
import static touch.baton.fixture.vo.TitleFixture.title;
import static touch.baton.fixture.vo.WatchedCountFixture.watchedCount;

class RunnerPostQueryRepositoryReadTest extends RepositoryTestConfig {

    @Autowired
    private RunnerPostQueryRepository runnerPostQueryRepository;

    @Autowired
    private RunnerPostTagQueryRepository runnerPostTagQueryRepository;

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
                IsReviewed.notReviewed(),
                runner,
                null,
                runnerPostTags(new ArrayList<>()));
        runnerPostQueryRepository.save(runnerPost);

        final Tag java = TagFixture.createJava();
        em.persist(java);
        final Tag spring = TagFixture.createSpring();
        em.persist(spring);
        final RunnerPostTag javaRunnerPostTag = RunnerPostTagFixture.create(runnerPost, java);
        final RunnerPostTag springRunnerPostTag = RunnerPostTagFixture.create(runnerPost, spring);

        runnerPost.addAllRunnerPostTags(List.of(javaRunnerPostTag, springRunnerPostTag));

        em.flush();
        em.close();

        // when
        final List<RunnerPostTag> expected = runnerPostTagQueryRepository.joinTagByRunnerPostId(runnerPost.getId());

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

        em.flush();
        em.close();

        final RunnerPost runnerPost = RunnerPostFixture.create(title("제 코드를 리뷰해주세요"),
                implementedContents("제 코드의 내용은 이렇습니다."),
                curiousContents("저는 이것이 궁금합니다."),
                postscriptContents("잘 부탁드립니다."),
                pullRequestUrl("https://"),
                deadline(LocalDateTime.now().plusHours(10)),
                watchedCount(0),
                NOT_STARTED,
                IsReviewed.notReviewed(),
                runner,
                null,
                runnerPostTags(new ArrayList<>()));
        runnerPostQueryRepository.save(runnerPost);

        // when
        final List<RunnerPost> actual = runnerPostQueryRepository.findByRunnerId(runner.getId());

        // then
        assertThat(actual).containsExactly(runnerPost);
    }
}
