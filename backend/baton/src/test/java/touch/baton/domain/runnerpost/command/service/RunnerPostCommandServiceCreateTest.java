package touch.baton.domain.runnerpost.command.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.alarm.command.Alarm;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.Runner;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.SupporterRunnerPost;
import touch.baton.domain.runnerpost.command.RunnerPost;
import touch.baton.domain.runnerpost.command.exception.RunnerPostBusinessException;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostApplicantCreateRequest;
import touch.baton.domain.runnerpost.command.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.command.vo.CuriousContents;
import touch.baton.domain.runnerpost.command.vo.Deadline;
import touch.baton.domain.runnerpost.command.vo.ImplementedContents;
import touch.baton.domain.runnerpost.command.vo.PostscriptContents;
import touch.baton.domain.runnerpost.command.vo.PullRequestUrl;
import touch.baton.domain.runnerpost.command.vo.Title;
import touch.baton.domain.runnerpost.command.vo.WatchedCount;
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

class RunnerPostCommandServiceCreateTest extends ServiceTestConfig {

    private static final Logger log = LoggerFactory.getLogger(RunnerPostCommandServiceCreateTest.class);

    private static final String TITLE = "코드 리뷰 해주세요.";
    private static final String TAG = "Java";
    private static final String OTHER_TAG = "Spring";
    private static final String PULL_REQUEST_URL = "https://github.com/cookienc";
    private static final LocalDateTime DEADLINE = LocalDateTime.now().plusDays(10);
    private static final String IMPLEMENTED_CONTENTS = "이것 구현했어요.";
    private static final String CURIOUS_CONTENTS = "궁금해.";
    private static final String POSTSCRIPT_CONTENTS = "싸게 부탁드려요.";

    private RunnerPostCommandService runnerPostCommandService;

    @BeforeEach
    void setUp() {
        runnerPostCommandService = new RunnerPostCommandService(
                runnerPostCommandRepository,
                tagCommandRepository,
                supporterCommandRepository,
                supporterRunnerPostCommandRepository,
                publisher
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
                IMPLEMENTED_CONTENTS,
                CURIOUS_CONTENTS,
                POSTSCRIPT_CONTENTS);
        final Member ethanMember = memberCommandRepository.save(MemberFixture.createEthan());
        final Runner runner = runnerQueryRepository.save(RunnerFixture.createRunner(ethanMember));

        // when
        final Long savedId = runnerPostCommandService.createRunnerPost(runner, request);

        // then
        assertThat(savedId).isNotNull();
        final Optional<RunnerPost> maybeActual = runnerPostQueryRepository.findById(savedId);
        assertThat(maybeActual).isPresent();
        final RunnerPost actual = maybeActual.get();
        assertAll(
                () -> assertThat(actual.getTitle()).isEqualTo(new Title(TITLE)),
                () -> assertThat(actual.getImplementedContents()).isEqualTo(new ImplementedContents(IMPLEMENTED_CONTENTS)),
                () -> assertThat(actual.getCuriousContents()).isEqualTo(new CuriousContents(CURIOUS_CONTENTS)),
                () -> assertThat(actual.getPostscriptContents()).isEqualTo(new PostscriptContents(POSTSCRIPT_CONTENTS)),
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
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitto = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterQueryRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitto, deadline(now().plusHours(100))));

        // when
        final RunnerPostApplicantCreateRequest request = new RunnerPostApplicantCreateRequest("안녕하세요. 서포터 헤나입니다.");
        final Long savedRunnerPostApplicantId = runnerPostCommandService.createRunnerPostApplicant(savedSupporterHyena, request, savedRunnerPost.getId());

        final Optional<SupporterRunnerPost> maybeRunnerPostApplicant = supporterRunnerPostQueryRepository.findById(savedRunnerPostApplicantId);

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

    @DisplayName("Supporter 가 RunnerPost 에 리뷰를 지원하면 RunnerPost 작성자에게 서포터 지원 알림이 저장된다.")
    @Test
    void event() throws InterruptedException {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitto = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterQueryRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitto, deadline(now().plusHours(100))));

        // when
        final RunnerPostApplicantCreateRequest request = new RunnerPostApplicantCreateRequest("안녕하세요. 서포터 헤나입니다.");
        final Long savedRunnerPostApplicantId = runnerPostCommandService.createRunnerPostApplicant(savedSupporterHyena, request, savedRunnerPost.getId());

        final Optional<SupporterRunnerPost> maybeRunnerPostApplicant = supporterRunnerPostQueryRepository.findById(savedRunnerPostApplicantId);

        Thread.sleep(2000L);

        final Optional<Alarm> maybeAlarm = alarmCommandRepository.findById(1L);

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
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitto = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterQueryRepository.save(SupporterFixture.create(savedMemberHyena));

        // when
        final RunnerPostApplicantCreateRequest request = new RunnerPostApplicantCreateRequest("안녕하세요. 서포터 헤나입니다.");

        // then
        assertThatThrownBy(() -> runnerPostCommandService.createRunnerPostApplicant(savedSupporterHyena, request, 0L))
                .isInstanceOf(RunnerPostBusinessException.class);
    }

    @DisplayName("Supporter 가 RunnerPost 에 리뷰를 지원할 때 이미 지원한 이력이 있는 경우 예외가 발생한다.")
    @Test
    void fail_createRunnerPostApplicant_if_supporter_already_applied() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitto = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterQueryRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitto, deadline(now().plusHours(100))));

        // when
        final RunnerPostApplicantCreateRequest request = new RunnerPostApplicantCreateRequest("안녕하세요. 서포터 헤나입니다.");

        // then
        assertSoftly(softly -> {
            softly.assertThatCode(() -> runnerPostCommandService.createRunnerPostApplicant(savedSupporterHyena, request, savedRunnerPost.getId()))
                    .doesNotThrowAnyException();
            softly.assertThatThrownBy(() -> runnerPostCommandService.createRunnerPostApplicant(savedSupporterHyena, request, savedRunnerPost.getId()))
                    .isInstanceOf(RunnerPostBusinessException.class);
        });
    }
}
