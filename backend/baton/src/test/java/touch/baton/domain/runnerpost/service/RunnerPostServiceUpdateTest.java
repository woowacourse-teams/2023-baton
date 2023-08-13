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
import touch.baton.domain.runnerpost.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    private static Runner runnerPostOwner;
    private static RunnerPost targetRunnerPost;
    private static Supporter applySupporter;

    private RunnerPostService runnerPostService;

    @BeforeEach
    void setUp() {
        runnerPostService = new RunnerPostService(
                runnerPostRepository,
                runnerPostTagRepository,
                tagRepository,
                supporterRepository,
                supporterRunnerPostRepository
        );

        final Member ehtanMember = memberRepository.save(MemberFixture.createEthan());
        runnerPostOwner = runnerRepository.save(RunnerFixture.createRunner(ehtanMember));
        targetRunnerPost = runnerPostRepository.save(RunnerPostFixture.create(runnerPostOwner,
                deadline(LocalDateTime.now().plusDays(10))));

        final Member hyenaMember = memberRepository.save(MemberFixture.createHyena());
        applySupporter = supporterRepository.save(SupporterFixture.create(hyenaMember));
        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(applySupporter, targetRunnerPost));
    }

    @DisplayName("Runner Post 수정에 성공한다.")
    @Test
    void success() {
        // given
        final RunnerPostUpdateRequest.Post request = new RunnerPostUpdateRequest.Post(
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

    @DisplayName("러너는 자신의 글에 제안한 서포터를 서포터로 선택할 수 있다.")
    @Test
    void updateRunnerPostAppliedSupporter() {
        // given
        final RunnerPostUpdateRequest.AppliedSupporter request = new RunnerPostUpdateRequest.AppliedSupporter(applySupporter.getId());

        // when
        runnerPostService.updateRunnerPostAppliedSupporter(runnerPostOwner, targetRunnerPost.getId(), request);

        // then
        final Optional<RunnerPost> maybeRunnerPost = runnerPostRepository.findById(targetRunnerPost.getId());
        assertThat(maybeRunnerPost).isPresent();

        final RunnerPost actualRunnerPost = maybeRunnerPost.get();
        assertAll(
                () -> assertThat(actualRunnerPost.getSupporter().getId()).isEqualTo(applySupporter.getId()),
                () -> assertThat(actualRunnerPost.getReviewStatus()).isEqualTo(ReviewStatus.IN_PROGRESS)
        );
    }

    @DisplayName("러너는 가입되어 있지 않는 서포터를 선택할 수 없다.")
    @Test
    void fail_updateRunnerPostAppliedSupporter_if_not_join_supporter() {
        // given
        final Long notJoinSupporterId = 1000000L;
        final RunnerPostUpdateRequest.AppliedSupporter request = new RunnerPostUpdateRequest.AppliedSupporter(notJoinSupporterId);

        // when, then
        assertThatThrownBy(() -> runnerPostService.updateRunnerPostAppliedSupporter(runnerPostOwner, targetRunnerPost.getId(), request))
                .isInstanceOf(RunnerPostBusinessException.class)
                .hasMessage("해당하는 식별자값의 서포터를 찾을 수 없습니다.");
    }

    @DisplayName("러너는 자신의 글에 제안한 서포터가 아니면 서포터로 선택할 수 없다.")
    @Test
    void fail_updateRunnerPostAppliedSupporter_if_not_apply_supporter() {
        // given
        final Member ditooMember = memberRepository.save(MemberFixture.createDitoo());
        final Supporter notApplySupporter = supporterRepository.save(SupporterFixture.create(ditooMember));

        final RunnerPostUpdateRequest.AppliedSupporter request = new RunnerPostUpdateRequest.AppliedSupporter(notApplySupporter.getId());

        // when, then
        assertThatThrownBy(() -> runnerPostService.updateRunnerPostAppliedSupporter(runnerPostOwner, targetRunnerPost.getId(), request))
                .isInstanceOf(RunnerPostBusinessException.class)
                .hasMessage("RunnerPost 에 리뷰를 제안한 서포터가 아닙니다.");
    }

    @DisplayName("러너는 작성된 글이 아니면 서포터를 선택할 수 없다.")
    @Test
    void fail_updateRunnerPostAppliedSupporter_if_is_not_written_runnerPost() {
        // given
        final RunnerPostUpdateRequest.AppliedSupporter request = new RunnerPostUpdateRequest.AppliedSupporter(applySupporter.getId());
        final Long notWrittenRunnerPostId = 1000000L;

        // when, then
        assertThatThrownBy(() -> runnerPostService.updateRunnerPostAppliedSupporter(runnerPostOwner, notWrittenRunnerPostId, request))
                .isInstanceOf(RunnerPostBusinessException.class)
                .hasMessage("RunnerPost 에 리뷰를 제안한 서포터가 아닙니다.");
    }

    @DisplayName("러너는 자신의 글이 아니면 서포터를 선택할 수 없다.")
    @Test
    void fail_updateRunnerPostAppliedSupporter_if_is_not_owner_of_runnerPost() {
        // given
        final Member ditooMember = memberRepository.save(MemberFixture.createDitoo());
        final Runner notOwnerRunner = runnerRepository.save(RunnerFixture.createRunner(ditooMember));

        final RunnerPostUpdateRequest.AppliedSupporter request = new RunnerPostUpdateRequest.AppliedSupporter(applySupporter.getId());

        // when, then
        assertThatThrownBy(() -> runnerPostService.updateRunnerPostAppliedSupporter(notOwnerRunner, targetRunnerPost.getId(), request))
                .isInstanceOf(RunnerPostBusinessException.class)
                .hasMessage("RunnerPost 의 글쓴이와 다른 사용자입니다.");
    }
}
