package touch.baton.domain.runnerpost.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.command.repository.MemberCommandRepository;
import touch.baton.tobe.domain.member.command.Runner;
import touch.baton.tobe.domain.member.query.repository.RunnerQueryRepository;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.tobe.domain.member.command.Supporter;
import touch.baton.tobe.domain.member.query.repository.SupporterQueryRepository;
import touch.baton.tobe.domain.member.query.repository.SupporterRunnerPostQueryRepository;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class RunnerPostRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private MemberCommandRepository memberCommandRepository;

    @Autowired
    private RunnerQueryRepository runnerQueryRepository;

    @Autowired
    private SupporterQueryRepository supporterQueryRepository;

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @Autowired
    private SupporterRunnerPostQueryRepository supporterRunnerPostRepository;

    @DisplayName("Supporter 식별자값과 ReviewStatus 로 연관된 RunnerPost 를 페이징하여 조회한다.")
    @Test
    void findBySupporterIdAndReviewStatus() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));
        final Member savedMemberEthan = memberCommandRepository.save(MemberFixture.createEthan());
        final Runner savedRunnerEthan = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberEthan));
        final Member savedMemberJudy = memberCommandRepository.save(MemberFixture.createJudy());
        final Runner savedRunnerJudy = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberJudy));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterQueryRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost runnerPostOne = RunnerPostFixture.create(savedRunnerDitoo, new Deadline(LocalDateTime.now().plusHours(100)));
        final RunnerPost savedRunnerPostOne = runnerPostRepository.save(runnerPostOne);
        final RunnerPost runnerPostTwo = RunnerPostFixture.create(savedRunnerEthan, new Deadline(LocalDateTime.now().plusHours(100)));
        final RunnerPost savedRunnerPostTwo = runnerPostRepository.save(runnerPostTwo);
        final RunnerPost runnerPostThree = RunnerPostFixture.create(savedRunnerJudy, new Deadline(LocalDateTime.now().plusHours(100)));
        final RunnerPost savedRunnerPostThree = runnerPostRepository.save(runnerPostThree);

        savedRunnerPostOne.assignSupporter(savedSupporterHyena);
        savedRunnerPostOne.updateReviewStatus(ReviewStatus.DONE);
        savedRunnerPostTwo.assignSupporter(savedSupporterHyena);
        savedRunnerPostTwo.updateReviewStatus(ReviewStatus.DONE);
        savedRunnerPostThree.assignSupporter(savedSupporterHyena);
        savedRunnerPostThree.updateReviewStatus(ReviewStatus.DONE);

        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(savedRunnerPostOne, savedSupporterHyena));
        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(savedRunnerPostTwo, savedSupporterHyena));
        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(savedRunnerPostThree, savedSupporterHyena));

        // when
        final PageRequest pageOne = PageRequest.of(0, 2);
        final PageRequest pageTwo = PageRequest.of(1, 2);

        final Page<RunnerPost> pageOneRunnerPosts
                = runnerPostRepository.findBySupporterIdAndReviewStatus(pageOne, savedSupporterHyena.getId(), ReviewStatus.DONE);
        final Page<RunnerPost> pageTwoRunnerPosts
                = runnerPostRepository.findBySupporterIdAndReviewStatus(pageTwo, savedSupporterHyena.getId(), ReviewStatus.DONE);

        // then
        assertSoftly(softly -> {
            softly.assertThat(pageOneRunnerPosts.getContent()).containsExactly(savedRunnerPostOne, savedRunnerPostTwo);
            softly.assertThat(pageTwoRunnerPosts.getContent()).containsExactly(savedRunnerPostThree);
        });
    }

    @DisplayName("join 한 SupporterRunnerPost의 Supporter 외래키가 Supporter 식별자값과 같고, ReviewStatus 로 연관된 RunnerPost 를 페이징하여 조회한다.")
    @Test
    void joinSupporterRunnerPostBySupporterIdAndReviewStatus() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));
        final Member savedMemberEthan = memberCommandRepository.save(MemberFixture.createEthan());
        final Runner savedRunnerEthan = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberEthan));
        final Member savedMemberJudy = memberCommandRepository.save(MemberFixture.createJudy());
        final Runner savedRunnerJudy = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberJudy));

        final Member savedMemberHyena = memberCommandRepository.save(MemberFixture.createHyena());
        final Supporter savedApplicantHyena = supporterQueryRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost runnerPostOne = RunnerPostFixture.create(savedRunnerDitoo, new Deadline(LocalDateTime.now().plusHours(100)));
        final RunnerPost savedRunnerPostOne = runnerPostRepository.save(runnerPostOne);
        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(savedRunnerPostOne, savedApplicantHyena));
        final RunnerPost runnerPostTwo = RunnerPostFixture.create(savedRunnerEthan, new Deadline(LocalDateTime.now().plusHours(100)));
        final RunnerPost savedRunnerPostTwo = runnerPostRepository.save(runnerPostTwo);
        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(savedRunnerPostTwo, savedApplicantHyena));
        final RunnerPost runnerPostThree = RunnerPostFixture.create(savedRunnerJudy, new Deadline(LocalDateTime.now().plusHours(100)));
        final RunnerPost savedRunnerPostThree = runnerPostRepository.save(runnerPostThree);
        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(savedRunnerPostThree, savedApplicantHyena));

        // when
        final PageRequest pageOne = PageRequest.of(0, 2);
        final PageRequest pageTwo = PageRequest.of(1, 2);

        final Page<RunnerPost> pageOneRunnerPosts
                = runnerPostRepository.joinSupporterRunnerPostBySupporterIdAndReviewStatus(pageOne, savedApplicantHyena.getId(), ReviewStatus.NOT_STARTED);
        final Page<RunnerPost> pageTwoRunnerPosts
                = runnerPostRepository.joinSupporterRunnerPostBySupporterIdAndReviewStatus(pageTwo, savedApplicantHyena.getId(), ReviewStatus.NOT_STARTED);

        // then
        assertSoftly(softly -> {
            softly.assertThat(pageOneRunnerPosts.getContent()).containsExactly(savedRunnerPostOne, savedRunnerPostTwo);
            softly.assertThat(pageTwoRunnerPosts.getContent()).containsExactly(savedRunnerPostThree);
        });
    }

    @DisplayName("Runner 식별자값과 ReviewStatus 로 연관된 RunnerPost 를 페이징하여 조회한다.")
    @Test
    void findByRunnerIdAndReviewStatus() {
        // given
        final Member savedMemberDitoo = memberCommandRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerQueryRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final RunnerPost runnerPostOne = RunnerPostFixture.create(savedRunnerDitoo, new Deadline(LocalDateTime.now().plusHours(100)));
        final RunnerPost savedRunnerPostOne = runnerPostRepository.save(runnerPostOne);
        final RunnerPost runnerPostTwo = RunnerPostFixture.create(savedRunnerDitoo, new Deadline(LocalDateTime.now().plusHours(100)));
        final RunnerPost savedRunnerPostTwo = runnerPostRepository.save(runnerPostTwo);
        final RunnerPost runnerPostThree = RunnerPostFixture.create(savedRunnerDitoo, new Deadline(LocalDateTime.now().plusHours(100)));
        final RunnerPost savedRunnerPostThree = runnerPostRepository.save(runnerPostThree);

        // when
        final PageRequest pageOne = PageRequest.of(0, 2);
        final PageRequest pageTwo = PageRequest.of(1, 2);

        final Page<RunnerPost> pageOneRunnerPosts
                = runnerPostRepository.findByRunnerIdAndReviewStatus(pageOne, savedRunnerDitoo.getId(), ReviewStatus.NOT_STARTED);
        final Page<RunnerPost> pageTwoRunnerPosts
                = runnerPostRepository.findByRunnerIdAndReviewStatus(pageTwo, savedRunnerDitoo.getId(), ReviewStatus.NOT_STARTED);

        assertSoftly(softly -> {
            softly.assertThat(pageOneRunnerPosts.getContent()).containsExactly(savedRunnerPostOne, savedRunnerPostTwo);
            softly.assertThat(pageTwoRunnerPosts.getContent()).containsExactly(savedRunnerPostThree);
        });
    }
}
