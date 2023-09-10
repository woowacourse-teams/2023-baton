package touch.baton.domain.runnerpost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.common.vo.Title;
import touch.baton.domain.common.vo.WatchedCount;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.service.dto.RunnerPostApplicantCreateRequest;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.ImplementedContents;
import touch.baton.domain.runnerpost.vo.PullRequestUrl;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertAll;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class RunnerPostServiceCreateTest extends ServiceTestConfig {

    private static final String TITLE = "코드 리뷰 해주세요.";
    private static final String TAG = "Java";
    private static final String OTHER_TAG = "Spring";
    private static final String PULL_REQUEST_URL = "https://github.com/cookienc";
    private static final LocalDateTime DEADLINE = LocalDateTime.now().plusDays(10);
    private static final String CONTENTS = "싸게 부탁드려요.";

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
    }

    @DisplayName("Runner post 저장에 성공한다.")
    @Test
    void success() {
        // given
        final RunnerPostCreateRequest request = new RunnerPostCreateRequest(TITLE,
                List.of(TAG, OTHER_TAG),
                PULL_REQUEST_URL,
                DEADLINE,
                CONTENTS);
        final Member ethanMember = memberRepository.save(MemberFixture.createEthan());
        final Runner runner = runnerRepository.save(RunnerFixture.createRunner(ethanMember));

        // when
        final Long savedId = runnerPostService.createRunnerPost(runner, request);

        // then
        assertThat(savedId).isNotNull();
        final Optional<RunnerPost> maybeActual = runnerPostRepository.findById(savedId);
        assertThat(maybeActual).isPresent();
        final RunnerPost actual = maybeActual.get();
        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo(new Title(TITLE)),
                () -> assertThat(actual.getImplementedContents()).isEqualTo(new ImplementedContents(CONTENTS)),
                () -> assertThat(actual.getPullRequestUrl()).isEqualTo(new PullRequestUrl(PULL_REQUEST_URL)),
                () -> assertThat(actual.getDeadline()).isEqualTo(new Deadline(DEADLINE)),
                () -> assertThat(actual.getWatchedCount()).isEqualTo(new WatchedCount(0)),
                () -> assertThat(actual.getRunner()).isEqualTo(runner)
        );
    }

    @DisplayName("Supporter 가 RunnerPost 에 리뷰를 지원한다.")
    @Test
    void success_createRunnerPostApplicant() {
        // given
        final Member savedMemberDitoo = memberRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitto = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPost = runnerPostRepository.save(RunnerPostFixture.create(savedRunnerDitto, deadline(now().plusHours(100))));

        // when
        final RunnerPostApplicantCreateRequest request = new RunnerPostApplicantCreateRequest("안녕하세요. 서포터 헤나입니다.");
        final Long savedRunnerPostApplicantId = runnerPostService.createRunnerPostApplicant(savedSupporterHyena, request, savedRunnerPost.getId());

        final Optional<SupporterRunnerPost> maybeRunnerPostApplicant = supporterRunnerPostRepository.findById(savedRunnerPostApplicantId);

        // then
        assertSoftly(softly -> {
            softly.assertThat(maybeRunnerPostApplicant).isPresent();
            softly.assertThat(maybeRunnerPostApplicant.get().getId())
                    .isNotNull()
                    .isEqualTo(maybeRunnerPostApplicant.get().getId());
            softly.assertThat(maybeRunnerPostApplicant.get().getSupporter()).isEqualTo(savedSupporterHyena);
            softly.assertThat(maybeRunnerPostApplicant.get().getRunnerPost()).isEqualTo(savedRunnerPost);
            softly.assertThat(maybeRunnerPostApplicant.get().getMessage().getValue()).isEqualTo(request.message());
        });
    }

    @DisplayName("Supporter 가 RunnerPost 에 리뷰를 지원할 때 RunnerPost 가 존재하지 않을 경우 예외가 발생한다.")
    @Test
    void fail_createRunnerPostApplicant_if_runnerPost_is_null() {
        // given
        final Member savedMemberDitoo = memberRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitto = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterRepository.save(SupporterFixture.create(savedMemberHyena));

        // when
        final RunnerPostApplicantCreateRequest request = new RunnerPostApplicantCreateRequest("안녕하세요. 서포터 헤나입니다.");

        // then
        assertThatThrownBy(() -> runnerPostService.createRunnerPostApplicant(savedSupporterHyena, request, 0L))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("Supporter 가 RunnerPost 에 리뷰를 지원할 때 이미 지원한 이력이 있는 경우 예외가 발생한다.")
    @Test
    void fail_createRunnerPostApplicant_if_supporter_already_applied() {
        // given
        final Member savedMemberDitoo = memberRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitto = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPost = runnerPostRepository.save(RunnerPostFixture.create(savedRunnerDitto, deadline(now().plusHours(100))));

        // when
        final RunnerPostApplicantCreateRequest request = new RunnerPostApplicantCreateRequest("안녕하세요. 서포터 헤나입니다.");

        // then
        assertSoftly(softly -> {
            softly.assertThatCode(() -> runnerPostService.createRunnerPostApplicant(savedSupporterHyena, request, savedRunnerPost.getId()))
                    .doesNotThrowAnyException();
            softly.assertThatThrownBy(() -> runnerPostService.createRunnerPostApplicant(savedSupporterHyena, request, savedRunnerPost.getId()))
                    .isInstanceOf(RunnerPostBusinessException.class);
        });
    }
}
