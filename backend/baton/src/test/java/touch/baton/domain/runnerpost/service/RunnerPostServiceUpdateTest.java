package touch.baton.domain.runnerpost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.domain.tag.RunnerPostTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.RunnerPostTagsFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static touch.baton.domain.runnerpost.vo.ReviewStatus.NOT_STARTED;
import static touch.baton.fixture.vo.ContentsFixture.contents;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.PullRequestUrlFixture.pullRequestUrl;
import static touch.baton.fixture.vo.TitleFixture.title;
import static touch.baton.fixture.vo.WatchedCountFixture.watchedCount;

class RunnerPostServiceUpdateTest extends ServiceTestConfig {

    private static final String TITLE = "코드 리뷰 해주세요.";
    private static final String TAG = "java";
    private static final String OTHER_TAG = "spring";
    private static final String PULL_REQUEST_URL = "https://github.com/shb03323";
    private static final LocalDateTime DEADLINE = LocalDateTime.now().plusHours(100);
    private static final String CONTENTS = "싸게 부탁드려요.";

    private RunnerPostService runnerPostService;

    @BeforeEach
    void setUp() {
        runnerPostService = new RunnerPostService(runnerPostRepository, runnerPostTagRepository, tagRepository, supporterRepository, supporterRunnerPostRepository);
    }

    @DisplayName("Runner Post 수정에 성공한다.")
    @Test
    void success() {
        // given
        final RunnerPostUpdateRequest request = new RunnerPostUpdateRequest(
                TITLE, List.of(TAG, OTHER_TAG), PULL_REQUEST_URL, DEADLINE, CONTENTS);
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
        final Long savedId = runnerPostService.updateRunnerPost(runnerPost.getId(), runner, request);

        // then
        assertThat(savedId).isNotNull();
        final Optional<RunnerPost> maybeActual = runnerPostRepository.findById(savedId);
        assertThat(maybeActual).isPresent();
        final RunnerPost actual = maybeActual.get();
        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo(new Title(TITLE)),
                () -> assertThat(actual.getContents()).isEqualTo(new Contents(CONTENTS)),
                () -> assertThat(actual.getPullRequestUrl()).isEqualTo(new PullRequestUrl(PULL_REQUEST_URL)),
                () -> assertThat(actual.getDeadline()).isEqualTo(new Deadline(request.deadline()))
        );

        final List<RunnerPostTag> runnerPostTags = runnerPostTagRepository.joinTagByRunnerPostId(savedId);
        assertThat(
                runnerPostTags.stream()
                        .map(runnerPostTag -> runnerPostTag.getTag().getTagName().getValue())
                        .toList()
        ).containsExactly(TAG, OTHER_TAG);
    }

    @DisplayName("Supporter 의 RunnerPost 제안을 철회하는데 성공한다")
    @Test
    void deleteBySupporterAndRunnerPostId() {
        // given
        final Member reviewerMember = MemberFixture.createDitoo();
        memberRepository.save(reviewerMember);
        final Supporter reviewerSupporter = SupporterFixture.create(reviewerMember);
        supporterRepository.save(reviewerSupporter);

        final Member revieweeMember = MemberFixture.createJudy();
        memberRepository.save(revieweeMember);
        final Runner revieweeRunner = RunnerFixture.createRunner(revieweeMember);
        runnerRepository.save(revieweeRunner);
        final RunnerPost runnerPost = RunnerPostFixture.create(revieweeRunner, reviewerSupporter, new Deadline(LocalDateTime.now().plusHours(100)));
        runnerPostRepository.save(runnerPost);
        final SupporterRunnerPost supporterRunnerPost = SupporterRunnerPostFixture.create(reviewerSupporter, runnerPost);
        supporterRunnerPostRepository.save(supporterRunnerPost);

        // when
        runnerPostService.deleteSupporterRunnerPost(reviewerSupporter, runnerPost.getId());

        // then
        assertThat(supporterRunnerPostRepository.findById(supporterRunnerPost.getId())).isNotPresent();
    }
}
