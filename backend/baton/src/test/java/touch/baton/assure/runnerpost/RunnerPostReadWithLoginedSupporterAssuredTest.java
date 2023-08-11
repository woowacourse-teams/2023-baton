package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostReadResponses;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.time.LocalDateTime;
import java.util.List;

import static touch.baton.fixture.vo.DeadlineFixture.deadline;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostReadWithLoginedSupporterAssuredTest extends AssuredTestConfig {

    private Supporter 로그인된_서포터;
    private String 로그인용_토큰;
    private RunnerPost 대기중인_게시글;
    private RunnerPost 진행중인_게시글;
    private RunnerPost 완료된_게시글;
    private RunnerPost 만료된_게시글;

    @BeforeEach
    void setUp() {
        final Member 로그인된_사용자 = memberRepository.save(MemberFixture.createHyena());
        로그인된_서포터 = supporterRepository.save(SupporterFixture.create(로그인된_사용자));
        로그인용_토큰 = login(로그인된_사용자.getSocialId().getValue());
        로그인된_서포터의_러너_게시글을_모든_리뷰_상태로_저장한다(로그인된_서포터);
    }

    private void 로그인된_서포터의_러너_게시글을_모든_리뷰_상태로_저장한다(final Supporter 로그인된_서포터) {
        final Member 사용자_누군가 = memberRepository.save(MemberFixture.createEthan());
        final Runner 러너_누군가 = runnerRepository.save(RunnerFixture.createRunner(사용자_누군가));
        대기중인_게시글 = runnerPostRepository.save(RunnerPostFixture.create(
                러너_누군가,
                로그인된_서포터,
                deadline(LocalDateTime.now().plusHours(100)),
                ReviewStatus.NOT_STARTED
        ));
        진행중인_게시글 = runnerPostRepository.save(RunnerPostFixture.create(
                러너_누군가,
                로그인된_서포터,
                deadline(LocalDateTime.now().plusHours(100)),
                ReviewStatus.IN_PROGRESS
        ));
        완료된_게시글 = runnerPostRepository.save(RunnerPostFixture.create(
                러너_누군가,
                로그인된_서포터,
                deadline(LocalDateTime.now().plusHours(100)),
                ReviewStatus.DONE
        ));
        만료된_게시글 = runnerPostRepository.save(RunnerPostFixture.create(
                러너_누군가,
                로그인된_서포터,
                deadline(LocalDateTime.now().plusHours(100)),
                ReviewStatus.OVERDUE
        ));
    }

    @Test
    void 로그인된_서포터의_대기중인_러너_게시글_목록_조회에_성공한다() {
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .로그인한_서포터의_토큰과_리뷰_진행_상태로_러너_게시글을_조회한다(ReviewStatus.NOT_STARTED)

                .서버_응답()
                .로그인한_서포터의_리뷰_상태에_따른_러너_게시글_조회_성공을_검증한다(RunnerPostReadResponses.LoginedSupporter.from(
                        List.of(RunnerPostResponse.LoginedSupporter.from(대기중인_게시글, 1))
                ));
    }

    @Test
    void 로그인된_서포터의_진행중인_러너_게시글_목록_조회에_성공한다() {
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .로그인한_서포터의_토큰과_리뷰_진행_상태로_러너_게시글을_조회한다(ReviewStatus.IN_PROGRESS)

                .서버_응답()
                .로그인한_서포터의_리뷰_상태에_따른_러너_게시글_조회_성공을_검증한다(RunnerPostReadResponses.LoginedSupporter.from(
                        List.of(RunnerPostResponse.LoginedSupporter.from(진행중인_게시글, 1))
                ));
    }

    @Test
    void 로그인된_서포터의_완료된_러너_게시글_목록_조회에_성공한다() {
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .로그인한_서포터의_토큰과_리뷰_진행_상태로_러너_게시글을_조회한다(ReviewStatus.DONE)

                .서버_응답()
                .로그인한_서포터의_리뷰_상태에_따른_러너_게시글_조회_성공을_검증한다(RunnerPostReadResponses.LoginedSupporter.from(
                        List.of(RunnerPostResponse.LoginedSupporter.from(완료된_게시글, 1))
                ));
    }

    @Test
    void 로그인된_서포터의_만료된_러너_게시글_목록_조회에_성공한다() {
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .로그인한_서포터의_토큰과_리뷰_진행_상태로_러너_게시글을_조회한다(ReviewStatus.OVERDUE)

                .서버_응답()
                .로그인한_서포터의_리뷰_상태에_따른_러너_게시글_조회_성공을_검증한다(RunnerPostReadResponses.LoginedSupporter.from(
                        List.of(RunnerPostResponse.LoginedSupporter.from(만료된_게시글, 1))
                ));
    }
}
