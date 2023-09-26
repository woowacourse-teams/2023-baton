package touch.baton.domain.supporter.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.repository.MemberCommandRepository;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.repository.RunnerPostRepository;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.domain.supporter.vo.Message;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class SupporterRunnerPostRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private SupporterRunnerPostRepository supporterRunnerPostRepository;

    @Autowired
    private MemberCommandRepository memberCommandRepository;

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private SupporterRepository supporterRepository;

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @DisplayName("러너 게시글 식별자값으로 서포터가 지원한 수를 count 한다.")
    @Test
    void countByRunnerPostIdIn() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createDitoo());
        final Supporter savedSupporterHyena = supporterRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPostOne = runnerPostRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));
        final RunnerPost savedRunnerPostTwo = runnerPostRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));
        final RunnerPost savedRunnerPostThree = runnerPostRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));
        final RunnerPost savedRunnerPostFour = runnerPostRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));

        savedRunnerPostOne.assignSupporter(savedSupporterHyena);
        savedRunnerPostTwo.assignSupporter(savedSupporterHyena);
        savedRunnerPostThree.assignSupporter(savedSupporterHyena);
        savedRunnerPostFour.assignSupporter(savedSupporterHyena);

        supporterRunnerPostRepository.save(createSupporterRunnerPost(savedSupporterHyena, savedRunnerPostOne));
        supporterRunnerPostRepository.save(createSupporterRunnerPost(savedSupporterHyena, savedRunnerPostTwo));
        supporterRunnerPostRepository.save(createSupporterRunnerPost(savedSupporterHyena, savedRunnerPostThree));
        supporterRunnerPostRepository.save(createSupporterRunnerPost(savedSupporterHyena, savedRunnerPostFour));

        // when
        final List<Long> runnerPostIds = List.of(
                savedRunnerPostOne.getId(),
                savedRunnerPostTwo.getId(),
                savedRunnerPostThree.getId(),
                savedRunnerPostFour.getId()
        );
        final List<Long> foundRunnerPostsApplicantCounts = supporterRunnerPostRepository.countByRunnerPostIdIn(runnerPostIds);

        // then
        assertThat(foundRunnerPostsApplicantCounts).containsExactly(1L, 1L, 1L, 1L);
    }

    private SupporterRunnerPost createSupporterRunnerPost(final Supporter supporter, final RunnerPost runnerPost) {
        return SupporterRunnerPost.builder()
                .runnerPost(runnerPost)
                .supporter(supporter)
                .message(new Message("안녕하세요. 서포터 헤나입니다."))
                .build();
    }

    @DisplayName("Member 가 SupporterRunnerPost 에 지원한 이력이 있을 경우 true 를 반환한다.")
    @Test
    void existsByRunnerPostIdAndMemberId_return_true() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPost = runnerPostRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));

        final SupporterRunnerPost runnerPostApplicant = createSupporterRunnerPost(savedSupporterHyena, savedRunnerPost);
        supporterRunnerPostRepository.save(runnerPostApplicant);

        // when
        final Long notExistMemberId = -1L;
        final boolean isApplicantHistoryExist = supporterRunnerPostRepository.existsByRunnerPostIdAndMemberId(
                savedRunnerPost.getId(),
                savedMemberHyena.getId()
        );

        // then
        assertThat(isApplicantHistoryExist).isTrue();
    }

    @DisplayName("Member 가 SupporterRunnerPost 에 지원한 이력이 없을 경우 false 를 반환한다.")
    @Test
    void existsByRunnerPostIdAndMemberId_if_supporterRunnerPost_is_not_exist_then_return_false() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());

        final RunnerPost savedRunnerPost = runnerPostRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));

        // when
        final boolean isApplicantHistoryNotExist = supporterRunnerPostRepository.existsByRunnerPostIdAndMemberId(
                savedRunnerPost.getId(),
                savedMemberHyena.getId()
        );

        // then
        assertThat(isApplicantHistoryNotExist).isFalse();
    }

    @DisplayName("Member 가 SupporterRunnerPost 에 지원한 이력을 조회할 때 RunnerPost 자체가 없으면 false 를 반환한다.")
    @Test
    void existsByRunnerPostIdAndMemberId_if_runnerPost_is_not_exist_then_return_false() {
        // given
        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createDitoo());

        // when
        final Long notExistRunnerPostId = -1L;
        final boolean isApplicantHistoryNotExist = supporterRunnerPostRepository.existsByRunnerPostIdAndMemberId(
                notExistRunnerPostId,
                savedMemberHyena.getId()
        );

        // then
        assertThat(isApplicantHistoryNotExist).isFalse();
    }

    @DisplayName("RunnerPost 외래키로 된 SupporterRunnerPost 가 존재하는지 확인한다.")
    @Test
    void existsByRunnerPostId() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createDitoo());
        final Supporter savedSupporterHyena = supporterRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost runnerPostOfApplicantExist = runnerPostRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));
        final RunnerPost runnerPostOfApplicantNotExist = runnerPostRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));
        runnerPostOfApplicantExist.assignSupporter(savedSupporterHyena);
        supporterRunnerPostRepository.save(createSupporterRunnerPost(savedSupporterHyena, runnerPostOfApplicantExist));

        // when
        final boolean actualOfExist = supporterRunnerPostRepository.existsByRunnerPostId(runnerPostOfApplicantExist.getId());
        final boolean actualOfNotExist = supporterRunnerPostRepository.existsByRunnerPostId(runnerPostOfApplicantNotExist.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(actualOfExist).isTrue();
            softly.assertThat(actualOfNotExist).isFalse();
        });
    }

    @DisplayName("서포터의 러너 게시글 리뷰 제안을 철회하는데 성공한다")
    @Test
    void deleteBySupporterAndRunnerPostId() {
        // given
        final Member reviewerMember = memberCommandRepository.save(MemberFixture.createDitoo());
        final Supporter reviewerSupporter = supporterRepository.save(SupporterFixture.create(reviewerMember));

        final Member revieweeMember = memberCommandRepository.save(MemberFixture.createJudy());
        final Runner revieweeRunner = runnerRepository.save(RunnerFixture.createRunner(revieweeMember));

        final RunnerPost runnerPost = runnerPostRepository.save(RunnerPostFixture.create(
                revieweeRunner,
                reviewerSupporter,
                deadline(LocalDateTime.now().plusHours(100))
        ));

        final SupporterRunnerPost deletedSupporterRunnerPost = supporterRunnerPostRepository.save(
                SupporterRunnerPostFixture.create(runnerPost, reviewerSupporter));

        // when
        supporterRunnerPostRepository.deleteBySupporterIdAndRunnerPostId(reviewerSupporter.getId(), runnerPost.getId());

        // then
        assertThat(supporterRunnerPostRepository.findById(deletedSupporterRunnerPost.getId())).isNotPresent();
    }
}
