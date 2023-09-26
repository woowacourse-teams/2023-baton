package touch.baton.domain.supporter.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.member.command.SupporterRunnerPost;
import touch.baton.tobe.domain.member.command.repository.MemberCommandRepository;
import touch.baton.tobe.domain.member.command.vo.Message;
import touch.baton.tobe.domain.member.query.repository.RunnerQueryRepository;
import touch.baton.tobe.domain.member.query.repository.SupporterQueryRepository;
import touch.baton.tobe.domain.member.query.repository.SupporterRunnerPostQueryRepository;
import touch.baton.tobe.domain.runnerpost.command.RunnerPost;
import touch.baton.tobe.domain.runnerpost.command.vo.Deadline;
import touch.baton.tobe.domain.runnerpost.query.repository.RunnerPostQueryRepository;

import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

class SupporterRunnerPostQueryRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private SupporterRunnerPostQueryRepository supporterRunnerPostRepository;

    @Autowired
    private MemberCommandRepository memberCommandRepository;

    @Autowired
    private RunnerQueryRepository runnerQueryRepository;

    @Autowired
    private SupporterQueryRepository supporterQueryRepository;

    @Autowired
    private RunnerPostQueryRepository runnerPostQueryRepository;

    @DisplayName("러너 게시글 식별자값으로 서포터가 지원한 수를 count 한다.")
    @Test
    void countByRunnerPostIdIn() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createDitoo());
        final Supporter savedSupporterHyena = supporterQueryRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPostOne = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));
        final RunnerPost savedRunnerPostTwo = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));
        final RunnerPost savedRunnerPostThree = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));
        final RunnerPost savedRunnerPostFour = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));

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
        final Runner savedRunnerDitoo = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterQueryRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost savedRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));

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
        final Runner savedRunnerDitoo = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());

        final RunnerPost savedRunnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));

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
        final Runner savedRunnerDitoo = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createDitoo());
        final Supporter savedSupporterHyena = supporterQueryRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost runnerPostOfApplicantExist = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));
        final RunnerPost runnerPostOfApplicantNotExist = runnerPostQueryRepository.save(RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100))));
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
        final Supporter reviewerSupporter = supporterQueryRepository.save(SupporterFixture.create(reviewerMember));

        final Member revieweeMember = memberCommandRepository.save(MemberFixture.createJudy());
        final Runner revieweeRunner = runnerQueryRepository.save(RunnerFixture.createRunner(revieweeMember));

        final RunnerPost runnerPost = runnerPostQueryRepository.save(RunnerPostFixture.create(
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
