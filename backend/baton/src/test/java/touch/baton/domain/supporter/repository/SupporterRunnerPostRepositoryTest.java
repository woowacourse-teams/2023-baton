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

    @DisplayName("러너 게시글 식별자값으로 서포터가 지원한 수를 count 한다.")
    @Test
    void countByRunnerPostIdIn() {
        // given
        final Member savedMemberDitoo = memberRepository.save(MemberFixture.createDitoo());
        final Runner savedRunnerDitoo = runnerRepository.save(RunnerFixture.createRunner(savedMemberDitoo));

        final Member savedMemberHyena = memberRepository.save(MemberFixture.createDitoo());
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
        final List<Integer> foundRunnerPostsApplicantCounts = supporterRunnerPostRepository.countByRunnerPostIdIn(runnerPostIds);

        // then
        assertThat(foundRunnerPostsApplicantCounts).containsExactly(1, 1, 1, 1);
    }

    private SupporterRunnerPost createSupporterRunnerPost(final Supporter supporter, final RunnerPost runnerPost) {
        return SupporterRunnerPost.builder()
                .runnerPost(runnerPost)
                .supporter(supporter)
                .message(new Message("안녕하세요. 서포터 헤나입니다."))
                .build();
    }
}
