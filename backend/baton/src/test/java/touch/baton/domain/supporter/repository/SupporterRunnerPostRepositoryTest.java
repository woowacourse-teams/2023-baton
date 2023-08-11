package touch.baton.domain.supporter.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import touch.baton.config.RepositoryTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.member.repository.MemberRepository;
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

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;

class SupporterRunnerPostRepositoryTest extends RepositoryTestConfig {

    @Autowired
    private SupporterRunnerPostRepository supporterRunnerPostRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RunnerRepository runnerRepository;

    @Autowired
    private SupporterRepository supporterRepository;

    @Autowired
    private RunnerPostRepository runnerPostRepository;

    @DisplayName("서포터와 연관된 러너 게시글을 조인하여 조회한다.")
    @Test
    void findBySupporterIdAndRunnerPostReviewStatusOrderByCreatedAtDesc() {
        // given
        final Member savedMemberDitoo = memberRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberRepository.save(MemberFixture.createDitoo());
        final Supporter savedSupporterHyena = supporterRepository.save(SupporterFixture.create(savedMemberHyena));

        final RunnerPost runnerPost = RunnerPostFixture.create(savedRunnerDitoo, new Deadline(now().plusHours(100)));
        final RunnerPost savedRunnerPost = runnerPostRepository.save(runnerPost);

        savedRunnerPost.assignSupporter(savedSupporterHyena);

        final SupporterRunnerPost supporterRunnerPost = SupporterRunnerPost.builder()
                .runnerPost(savedRunnerPost)
                .supporter(savedSupporterHyena)
                .message(new Message("안녕하세요. 서포터 헤나입니다."))
                .build();
        supporterRunnerPostRepository.save(supporterRunnerPost);

        // when
        final List<Long> runnerPostIds = List.of(savedRunnerPost.getId());
        final List<Integer> foundRunnerPostsApplicantCounts = supporterRunnerPostRepository.countByRunnerPostIdIn(runnerPostIds);

        // then
        assertThat(foundRunnerPostsApplicantCounts).containsExactly(1);
    }
}
