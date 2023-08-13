package touch.baton.domain.runnerpost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.common.exception.ClientRequestException;
import touch.baton.domain.common.vo.Contents;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
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

    @DisplayName("Supporter 의 RunnerPost 리뷰 제안 철회")
    @Nested
    class DeleteSupporterRunnerPost {

        private Supporter applicantSupporter;
        private Runner revieweeRunner;

        @BeforeEach
        void setUp() {
            final Member applicantMember = memberRepository.save(MemberFixture.createDitoo());
            applicantSupporter = supporterRepository.save(SupporterFixture.create(applicantMember));

            final Member revieweeMember = memberRepository.save(MemberFixture.createJudy());
            revieweeRunner = runnerRepository.save(RunnerFixture.createRunner(revieweeMember));
        }

        @DisplayName("성공한다.")
        @Test
        void success() {
            // given
            final RunnerPost runnerPost = runnerPostRepository.save(
                    RunnerPostFixture.create(
                            revieweeRunner,
                            applicantSupporter,
                            new Deadline(LocalDateTime.now().plusHours(100))
                    ));
            final SupporterRunnerPost supporterRunnerPost = SupporterRunnerPostFixture.create(applicantSupporter, runnerPost);
            supporterRunnerPostRepository.save(supporterRunnerPost);

            // when
            runnerPostService.deleteSupporterRunnerPost(applicantSupporter, runnerPost.getId());

            // then
            assertThat(supporterRunnerPostRepository.findById(supporterRunnerPost.getId())).isNotPresent();
        }

        @DisplayName("RunnerPost 가 존재하지 않으면 실패한다.")
        @Test
        void fail_when_runnerPost_not_found() {
            // given
            final RunnerPost runnerPost = runnerPostRepository.save(
                    RunnerPostFixture.create(
                            revieweeRunner,
                            applicantSupporter,
                            new Deadline(LocalDateTime.now().plusHours(100))
                    ));
            final SupporterRunnerPost supporterRunnerPost = SupporterRunnerPostFixture.create(applicantSupporter, runnerPost);
            supporterRunnerPostRepository.save(supporterRunnerPost);
            runnerPostRepository.delete(runnerPost);

            // when & then
            assertThatThrownBy(() -> runnerPostService.deleteSupporterRunnerPost(applicantSupporter, runnerPost.getId()))
                    .isInstanceOf(ClientRequestException.class);
        }

        @DisplayName("RunnerPost 의 리뷰 상태가 대기중이 아니면 실패한다.")
        @Test
        void fail_when_runnerPost_reviewStatus_is_not_NOT_STARTED() {
            // given
            final RunnerPost runnerPost = runnerPostRepository.save(
                    RunnerPostFixture.create(
                            revieweeRunner,
                            applicantSupporter,
                            new Deadline(LocalDateTime.now().plusHours(100)),
                            ReviewStatus.IN_PROGRESS
                    ));
            final SupporterRunnerPost supporterRunnerPost = SupporterRunnerPostFixture.create(applicantSupporter, runnerPost);
            supporterRunnerPostRepository.save(supporterRunnerPost);


            // when & then
            assertThatThrownBy(() -> runnerPostService.deleteSupporterRunnerPost(applicantSupporter, runnerPost.getId()))
                    .isInstanceOf(ClientRequestException.class);
        }

        @DisplayName("SupporterRunnerPost 가 존재하지 않으면 실패한다.")
        @Test
        void fail_when_supporterRunnerPost_not_found() {
            // given
            final RunnerPost runnerPost = runnerPostRepository.save(
                    RunnerPostFixture.create(
                            revieweeRunner,
                            applicantSupporter,
                            new Deadline(LocalDateTime.now().plusHours(100))
                    ));

            // when & then
            assertThatThrownBy(() -> runnerPostService.deleteSupporterRunnerPost(applicantSupporter, runnerPost.getId()))
                    .isInstanceOf(ClientRequestException.class);
        }
    }
}
