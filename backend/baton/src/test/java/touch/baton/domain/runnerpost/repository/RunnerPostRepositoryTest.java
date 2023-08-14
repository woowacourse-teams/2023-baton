package touch.baton.domain.runnerpost.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.repository.RunnerRepository;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.repository.SupporterRepository;
import touch.baton.domain.supporter.repository.SupporterRunnerPostRepository;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

class RunnerPostRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private SupporterRepository supporterRepository;

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @Autowired
    private SupporterRunnerPostRepository supporterRunnerPostRepository;

    @DisplayName("Supporter 식별자값과 ReviewStatus 로 연관된 RunnerPost 를 페이징하여 조회한다.")
    @Test
    void findBySupporterIdAndReviewStatus() {
        // given
        final Member savedMemberDitoo = memberRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));
        final Member savedMemberEthan = memberRepository.save(MemberFixture.createEthan());
        final Runner savedRunnerEthan = runnerRepository.save(RunnerFixture.createRunner(savedMemberEthan));
        final Member savedMemberJudy = memberRepository.save(MemberFixture.createJudy());
        final Runner savedRunnerJudy = runnerRepository.save(RunnerFixture.createRunner(savedMemberJudy));

        final Member savedMemberHyena = memberRepository.save(MemberFixture.createHyena());
        final Supporter savedSupporterHyena = supporterRepository.save(SupporterFixture.create(savedMemberHyena));

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

        assertSoftly(softly -> {
            softly.assertThat(pageOneRunnerPosts.getContent()).containsExactly(savedRunnerPostOne, savedRunnerPostTwo);
            softly.assertThat(pageTwoRunnerPosts.getContent()).containsExactly(savedRunnerPostThree);
        });
    }
}
