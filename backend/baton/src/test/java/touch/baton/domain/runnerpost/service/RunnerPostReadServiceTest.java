package touch.baton.domain.runnerpost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponses;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

class RunnerPostReadServiceTest extends ServiceTestConfig {

    private RunnerPostReadService runnerPostReadService;

    @BeforeEach
    void setUp() {
        runnerPostReadService = new RunnerPostReadService(runnerPostRepository);
    }

    @DisplayName("태그 이름과 리뷰 상태를 조건으로 러너 게시글 첫 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfoAndTagNameAndReviewStatus_firstPage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagRepository.save(TagFixture.create(tagName("스프링")));

        final RunnerPost expectedRunnerPostOne = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag, springTag),
                ReviewStatus.NOT_STARTED
        ));

        runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().minusHours(100)),
                List.of(javaTag),
                ReviewStatus.OVERDUE
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag),
                ReviewStatus.NOT_STARTED
        ));

        runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(springTag),
                ReviewStatus.NOT_STARTED
        ));

        // when
        final RunnerPostResponses.Simple actual = runnerPostReadService.readRunnerPostByPageInfoAndTagNameAndReviewStatus(
                javaTag.getTagName().getValue(),
                null,
                10,
                ReviewStatus.NOT_STARTED
        );

        final RunnerPostResponses.Simple expected = RunnerPostResponses.Simple.from(List.of(
                RunnerPostResponse.Simple.from(expectedRunnerPostTwo, 0),
                RunnerPostResponse.Simple.from(expectedRunnerPostOne, 0)
        ));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("태그 이름과 리뷰 상태를 조건으로 러너 게시글 중간 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfoAndTagNameAndReviewStatus_middlePage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagRepository.save(TagFixture.create(tagName("스프링")));

        final RunnerPost expectedRunnerPostOne = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag, springTag),
                ReviewStatus.NOT_STARTED
        ));

        runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().minusHours(100)),
                List.of(javaTag, springTag),
                ReviewStatus.OVERDUE
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag),
                ReviewStatus.NOT_STARTED
        ));

        runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                ReviewStatus.NOT_STARTED
        ));

        final RunnerPost previousRunnerPost = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(javaTag),
                ReviewStatus.NOT_STARTED
        ));

        // when
        final RunnerPostResponses.Simple actual = runnerPostReadService.readRunnerPostByPageInfoAndTagNameAndReviewStatus(
                javaTag.getTagName().getValue(),
                previousRunnerPost.getId(),
                10,
                ReviewStatus.NOT_STARTED
        );

        final RunnerPostResponses.Simple expected = RunnerPostResponses.Simple.from(List.of(
                RunnerPostResponse.Simple.from(expectedRunnerPostTwo, 0),
                RunnerPostResponse.Simple.from(expectedRunnerPostOne, 0)
        ));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("리뷰 상태를 조건으로 러너 게시글 첫 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfoAndReviewStatus_firstPage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagRepository.save(TagFixture.create(tagName("스프링")));

        final RunnerPost expectedRunnerPostOne = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag, springTag),
                ReviewStatus.NOT_STARTED
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag),
                ReviewStatus.NOT_STARTED
        ));

        final RunnerPost expectedRunnerPostThree = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                ReviewStatus.NOT_STARTED
        ));

        runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().minusHours(100)),
                List.of(springTag),
                ReviewStatus.OVERDUE
        ));

        // when
        final RunnerPostResponses.Simple actual = runnerPostReadService.readRunnerPostByPageInfoAndTagNameAndReviewStatus(
                null,
                null,
                10,
                ReviewStatus.NOT_STARTED
        );

        final RunnerPostResponses.Simple expected = RunnerPostResponses.Simple.from(List.of(
                RunnerPostResponse.Simple.from(expectedRunnerPostThree, 0),
                RunnerPostResponse.Simple.from(expectedRunnerPostTwo, 0),
                RunnerPostResponse.Simple.from(expectedRunnerPostOne, 0)
        ));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("리뷰 상태를 조건으로 러너 게시글 중간 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfoAndReviewStatus_middlePage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagRepository.save(TagFixture.create(tagName("스프링")));

        final RunnerPost expectedRunnerPostOne = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag, springTag),
                ReviewStatus.NOT_STARTED
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag),
                ReviewStatus.NOT_STARTED
        ));

        final RunnerPost expectedRunnerPostThree = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                ReviewStatus.NOT_STARTED
        ));

        runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().minusHours(100)),
                List.of(springTag),
                ReviewStatus.OVERDUE
        ));

        final RunnerPost previousRunnerPost = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                ReviewStatus.NOT_STARTED
        ));

        // when
        final RunnerPostResponses.Simple actual = runnerPostReadService.readRunnerPostByPageInfoAndTagNameAndReviewStatus(
                null,
                previousRunnerPost.getId(),
                10,
                ReviewStatus.NOT_STARTED
        );

        final RunnerPostResponses.Simple expected = RunnerPostResponses.Simple.from(List.of(
                RunnerPostResponse.Simple.from(expectedRunnerPostThree, 0),
                RunnerPostResponse.Simple.from(expectedRunnerPostTwo, 0),
                RunnerPostResponse.Simple.from(expectedRunnerPostOne, 0)
        ));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("태그 이름을 조건으로 러너 게시글 첫 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfoAndTagName_firstPage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagRepository.save(TagFixture.create(tagName("스프링")));

        final RunnerPost expectedRunnerPostOne = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now()),
                List.of(javaTag, springTag),
                ReviewStatus.OVERDUE
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag),
                ReviewStatus.NOT_STARTED
        ));

        runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                ReviewStatus.IN_PROGRESS
        ));

        // when
        final RunnerPostResponses.Simple actual = runnerPostReadService.readRunnerPostByPageInfoAndTagNameAndReviewStatus(
                javaTag.getTagName().getValue(),
                null,
                10,
                null
        );

        final RunnerPostResponses.Simple expected = RunnerPostResponses.Simple.from(List.of(
                RunnerPostResponse.Simple.from(expectedRunnerPostTwo, 0),
                RunnerPostResponse.Simple.from(expectedRunnerPostOne, 0)
        ));

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("태그 이름을 조건으로 러너 게시글 중간 페이지 조회에 성공한다.")
    @Test
    void readRunnerPostByPageInfoAndTagName_middlePage() {
        // given
        final Member hyenaMember = memberCommandRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagRepository.save(TagFixture.create(tagName("스프링")));

        final RunnerPost expectedRunnerPostOne = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag, springTag),
                ReviewStatus.NOT_STARTED
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now()),
                List.of(javaTag),
                ReviewStatus.OVERDUE
        ));

        runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(springTag),
                ReviewStatus.IN_PROGRESS
        ));

        final RunnerPost previousRunnerPost = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(now().plusHours(100)),
                List.of(javaTag),
                ReviewStatus.NOT_STARTED
        ));

        // when
        final RunnerPostResponses.Simple actual = runnerPostReadService.readRunnerPostByPageInfoAndTagNameAndReviewStatus(
                javaTag.getTagName().getValue(),
                previousRunnerPost.getId(),
                10,
                null
        );

        final RunnerPostResponses.Simple expected = RunnerPostResponses.Simple.from(List.of(
                RunnerPostResponse.Simple.from(expectedRunnerPostTwo, 0),
                RunnerPostResponse.Simple.from(expectedRunnerPostOne, 0)
        ));

        // then
        assertThat(actual).isEqualTo(expected);
    }
}
