package touch.baton.domain.runnerpost.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.data.domain.Sort.Order.desc;

class RunnerPostRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private SupporterRepository supporterRepository;

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @DisplayName("Supporter 식별자값과 ReviewStatus 로 연관된 RunnerPost 를 페이징하여 조회한다.")
    @Test
    void findBySupporterIdAndReviewStatus() {
        // given
        final Member savedMemberDitoo = memberRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberRepository.save(MemberFixture.createDitoo());
        final Supporter savedSupporterHyena = supporterRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost runnerPost = RunnerPostFixture.create(savedRunnerDitoo, new Deadline(LocalDateTime.now().plusHours(100)));
        final RunnerPost savedRunnerPost = runnerPostRepository.save(runnerPost);

        savedRunnerPost.assignSupporter(savedSupporterHyena);

        // when
        final PageRequest pageRequest = PageRequest.of(1, 10, Sort.by(desc("createdAt")));
        final Page<RunnerPost> pageRunnerPost
                = runnerPostRepository.findBySupporterIdAndReviewStatus(pageRequest, savedSupporterHyena.getId(), ReviewStatus.IN_PROGRESS);

        // then
        assertAll(
                () -> assertThat(pageRunnerPost.getPageable()).isEqualTo(pageRequest),
                () -> assertThat(pageRunnerPost.getContent()).containsExactly(runnerPost)
        );
    }
}
