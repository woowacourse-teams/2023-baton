package touch.baton.domain.runnerpost.repository.read;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import touch.baton.fixture.domain.RunnerPostTagsFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.domain.runnerpost.vo.ReviewStatus.NOT_STARTED;
import static touch.baton.fixture.vo.ContentsFixture.contents;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
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

    @DisplayName("RunnerPost 식별자로 RunnerPostTag 목록을 조회할 때 Tag 가 있으면 조회된다.")
    @Test
    void findRunnerPostTagsById_exist() {
        // given
        final Member ditoo = MemberFixture.createDitoo();
        memberRepository.save(ditoo);
        final Runner runner = RunnerFixture.createRunner(ditoo);
        runnerRepository.save(runner);

        final RunnerPost runnerPost = RunnerPostFixture.create(title("제 코드를 리뷰해주세요"),
                contents("제 코드의 내용은 이렇습니다."),
                pullRequestUrl("https://"),
                deadline(LocalDateTime.now().plusHours(10)),
                watchedCount(0),
                NOT_STARTED,
                runner,
                null,
                RunnerPostTagsFixture.runnerPostTags(new ArrayList<>()));
        runnerPostRepository.save(runnerPost);

        final Tag java = TagFixture.createJava();
        tagRepository.save(java);
        final Tag spring = TagFixture.createSpring();
        tagRepository.save(spring);
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
        memberRepository.save(ditoo);
        final Runner runner = RunnerFixture.createRunner(ditoo);
        runnerRepository.save(runner);

        final RunnerPost runnerPost = RunnerPostFixture.create(title("제 코드를 리뷰해주세요"),
                contents("제 코드의 내용은 이렇습니다."),
                pullRequestUrl("https://"),
                deadline(LocalDateTime.now().plusHours(10)),
                watchedCount(0),
                NOT_STARTED,
                runner,
                null,
                RunnerPostTagsFixture.runnerPostTags(new ArrayList<>()));
        runnerPostRepository.save(runnerPost);

        // when
        final List<RunnerPost> actual = runnerPostRepository.findByRunnerId(runner.getId());

        // then
        assertThat(actual).containsExactly(runnerPost);
    }

    @DisplayName("RunnerPost 최신 순으로 전체 조회한다.")
    @Test
    void findAllByOrderByCreatedAt() {
        // given
        final Member ditoo = MemberFixture.createDitoo();
        memberRepository.save(ditoo);
        final Runner runner = RunnerFixture.createRunner(ditoo);
        runnerRepository.save(runner);

        final RunnerPost previousRunnerPost = RunnerPostFixture.create(title("제 코드를 리뷰해주세요"),
                contents("제 코드의 내용은 이렇습니다."),
                pullRequestUrl("https://"),
                deadline(LocalDateTime.now().plusHours(10)),
                watchedCount(0),
                NOT_STARTED,
                runner,
                null,
                RunnerPostTagsFixture.runnerPostTags(new ArrayList<>()));
        runnerPostRepository.save(previousRunnerPost);

        final RunnerPost nextRunnerPost = RunnerPostFixture.create(title("제 코드를 리뷰해주세요"),
                contents("제 코드의 내용은 이렇습니다."),
                pullRequestUrl("https://"),
                deadline(LocalDateTime.now().plusHours(10)),
                watchedCount(0),
                NOT_STARTED,
                runner,
                null,
                RunnerPostTagsFixture.runnerPostTags(new ArrayList<>()));
        runnerPostRepository.save(nextRunnerPost);

        // when
        final List<RunnerPost> actual = runnerPostRepository.findAllByOrderByCreatedAtDesc();

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual).hasSize(2);
            softly.assertThat(actual.get(0)).isEqualTo(nextRunnerPost);
            softly.assertThat(actual.get(1)).isEqualTo(previousRunnerPost);
        });
    }
}
