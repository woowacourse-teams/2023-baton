package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostDeleteAssuredTest extends AssuredTestConfig {

    private Runner 러너_디투;
    private String 로그인용_토큰;

    @BeforeEach
    void setUp() {
        final Member 사용자_디투 = memberRepository.save(MemberFixture.createDitoo());
        러너_디투 = runnerRepository.save(RunnerFixture.createRunner(사용자_디투));
        로그인용_토큰 = login(사용자_디투.getSocialId().getValue());
    }

    @Test
    void 리뷰가_대기중이고_리뷰_지원자가_없다면_러너의_게시글_식별자값으로_러너_게시글_삭제에_성공한다() {
        final RunnerPost 러너_게시글 = runnerPostRepository.save(RunnerPostFixture.create(러너_디투, deadline(LocalDateTime.now().plusHours(100))));

        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_삭제한다(러너_게시글.getId())

                .서버_응답()
                .러너_게시글_삭제_성공을_검증한다(NO_CONTENT);
    }

    @Test
    void 러너_게시글이_존재하지_않으면_러너의_게시글_식별자값으로_러너_게시글_삭제에_실패한다() {
        final Long 존재하지_않는_러너_게시글의_식별자값 = -1L;

        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_삭제한다(존재하지_않는_러너_게시글의_식별자값)

                .서버_응답()
                .러너_게시글_삭제_실패를_검증한다(NOT_FOUND);
    }

    @Test
    void 리뷰가_진행중인_상태라면_러너의_게시글_식별자값으로_러너_게시글_삭제에_실패한다() {
        final RunnerPost 러너_게시글 = runnerPostRepository.save(RunnerPostFixture.create(
                러너_디투,
                deadline(LocalDateTime.now().plusHours(100)),
                ReviewStatus.IN_PROGRESS
        ));

        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_삭제한다(러너_게시글.getId())

                .서버_응답()
                .러너_게시글_삭제_실패를_검증한다(BAD_REQUEST);
    }

    @Test
    void 리뷰가_완료된_상태라면_러너의_게시글_식별자값으로_러너_게시글_삭제에_실패한다() {
        final RunnerPost 러너_게시글 = runnerPostRepository.save(RunnerPostFixture.create(
                러너_디투,
                deadline(LocalDateTime.now().plusHours(100)),
                ReviewStatus.DONE
        ));

        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_삭제한다(러너_게시글.getId())

                .서버_응답()
                .러너_게시글_삭제_실패를_검증한다(BAD_REQUEST);
    }

    @Test
    void 리뷰_요청_대기중인_상태이고_리뷰_지원자가_있는_상태라면_러너의_게시글_식별자값으로_러너_게시글_삭제에_실패한다() {
        final RunnerPost 러너_게시글 = runnerPostRepository.save(RunnerPostFixture.create(
                러너_디투,
                deadline(LocalDateTime.now().plusHours(100)),
                ReviewStatus.NOT_STARTED
        ));
        final Member 지원자_맴버 = memberRepository.save(MemberFixture.createHyena());
        final Supporter 지원자_서포터 = supporterRepository.save(SupporterFixture.create(지원자_맴버));
        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(러너_게시글, 지원자_서포터));

        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_삭제한다(러너_게시글.getId())

                .서버_응답()
                .러너_게시글_삭제_실패를_검증한다(BAD_REQUEST);
    }
}
