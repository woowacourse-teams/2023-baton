package touch.baton.domain.runnerpost.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import touch.baton.config.ServiceTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponses;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.tag.Tag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.TagFixture;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.TagNameFixture.tagName;

class RunnerPostReadServiceTest extends ServiceTestConfig {

    private RunnerPostReadService runnerPostReadService;

    @BeforeEach
    void setUp() {
        runnerPostReadService = new RunnerPostReadService(runnerPostRepository);
    }

    @DisplayName("러너 게시글을 태그 이름과 리뷰 상태를 조건으로 이용하여 첫 페이지 조회에 성공한다.")
    @Test
    void readLatestByLimitAndTagNameAndReviewStatus() {
        // given
        final Member hyenaMember = memberRepository.save(MemberFixture.createHyena());
        final Runner hyenaRunner = runnerRepository.save(RunnerFixture.createRunner(hyenaMember));

        final Tag javaTag = tagRepository.save(TagFixture.create(tagName("자바")));
        final Tag springTag = tagRepository.save(TagFixture.create(tagName("스프링")));

        final RunnerPost expectedRunnerPostOne = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag, springTag)
        ));

        final RunnerPost expectedRunnerPostTwo = runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(javaTag)
        ));

        runnerPostRepository.save(RunnerPostFixture.create(
                hyenaRunner,
                deadline(LocalDateTime.now().plusHours(100)),
                List.of(springTag)
        ));

        // when
        final RunnerPostResponses.Simple actual = runnerPostReadService.readLatestByLimitAndTagNameAndReviewStatus(
                javaTag.getTagName().getValue(),
                10,
                ReviewStatus.NOT_STARTED
        );

        // then
        assertSoftly(softly -> {
            softly.assertThat(actual.data()).hasSize(2);
            softly.assertThat(actual.data().get(0).runnerPostId()).isEqualTo(expectedRunnerPostTwo.getId());
            softly.assertThat(actual.data().get(1).runnerPostId()).isEqualTo(expectedRunnerPostOne.getId());
        });
    }

//    @DisplayName("러너 게시글 식별자값 목록으로 서포터 지원자 수 카운트(count)에 성공한다.")
//    @Test
//    void readApplicantCountsByRunnerPostIds() {
//        // given
//        final Member hyenaMember = memberRepository.save(MemberFixture.createHyena());
//        final Runner hyenaRunner = runnerRepository.save(RunnerFixture.createRunner(hyenaMember));
//
//        final Member ditooMember = memberRepository.save(MemberFixture.createDitoo());
//        final Supporter supporterDitoo = supporterRepository.save(SupporterFixture.create(ditooMember));
//
//        final Member ethanMember = memberRepository.save(MemberFixture.createEthan());
//        final Supporter supporterEthan = supporterRepository.save(SupporterFixture.create(ethanMember));
//
//        final Member judyMember = memberRepository.save(MemberFixture.createJudy());
//        final Supporter supporterJudy = supporterRepository.save(SupporterFixture.create(judyMember));
//
//        final Tag javaTag = tagRepository.save(TagFixture.create(tagName("자바")));
//        final Tag springTag = tagRepository.save(TagFixture.create(tagName("스프링")));
//
//        final RunnerPost savedRunnerPostOne = runnerPostRepository.save(RunnerPostFixture.create(
//                hyenaRunner,
//                deadline(LocalDateTime.now().plusHours(100)),
//                List.of(javaTag, springTag)
//        ));
//
//        final RunnerPost savedRunnerPostTwo = runnerPostRepository.save(RunnerPostFixture.create(
//                hyenaRunner,
//                deadline(LocalDateTime.now().plusHours(100)),
//                List.of(javaTag)
//        ));
//
//        final RunnerPost savedRunnerPostThree = runnerPostRepository.save(RunnerPostFixture.create(
//                hyenaRunner,
//                deadline(LocalDateTime.now().plusHours(100)),
//                List.of(springTag)
//        ));
//
//        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(savedRunnerPostOne, supporterDitoo));
//        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(savedRunnerPostOne, supporterEthan));
//        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(savedRunnerPostOne, supporterJudy));
//
//        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(savedRunnerPostThree, supporterDitoo));
//        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(savedRunnerPostThree, supporterEthan));
//
//        // when
//        final List<Long> runnerPostIds = List.of(
//                savedRunnerPostOne.getId(),
//                savedRunnerPostTwo.getId(),
//                savedRunnerPostThree.getId()
//        );
//
//        final ApplicantCountMappingDto actual = runnerPostReadService.readApplicantCountMappingByRunnerPostIds(runnerPostIds);
//
//        // then
//        final ApplicantCountMappingDto expected = new ApplicantCountMappingDto(Map.of(
//                savedRunnerPostOne.getId(), 3L,
//                savedRunnerPostTwo.getId(), 0L,
//                savedRunnerPostThree.getId(), 2L
//        ));
//
//        assertThat(actual).isEqualTo(expected);
//    }
}
