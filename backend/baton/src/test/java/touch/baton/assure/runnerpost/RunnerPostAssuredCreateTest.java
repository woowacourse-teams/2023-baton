package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.service.dto.RunnerPostCreateRequest;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.util.List;

import static java.time.LocalDateTime.now;
import static touch.baton.assure.runnerpost.RunnerPostAssuredCreateSupport.러너_게시글_생성_요청;
import static touch.baton.assure.runnerpost.RunnerPostAssuredSupport.러너_게시글_Detail_응답;
import static touch.baton.domain.runnerpost.vo.ReviewStatus.NOT_STARTED;
import static touch.baton.fixture.domain.TechnicalTagFixture.createJava;
import static touch.baton.fixture.domain.TechnicalTagFixture.createSpring;
import static touch.baton.fixture.vo.IntroductionFixture.introduction;
import static touch.baton.fixture.vo.ReviewCountFixture.reviewCount;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostAssuredCreateTest extends AssuredTestConfig {

    @Test
    void 러너가_러너_게시글을_생성하고_서포터가_러너_게시글에_리뷰를_신청한다() {
        final Runner 러너_에단 = 러너를_저장한다(MemberFixture.createEthan());
        final Supporter 서포터_헤나 = 서포터_헤나를_저장한다();

        final String 에단_로그인_액세스_토큰 = login(러너_에단.getMember().getSocialId().getValue());
        final String 헤나_로그인_액세스_토큰 = login(서포터_헤나.getMember().getSocialId().getValue());

        final RunnerPostCreateRequest 러너_게시글_생성_요청 = 러너_게시글_생성_요청을_생성한다();
        final Long 에단의_러너_게시글_식별자값 = RunnerPostAssuredCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(에단_로그인_액세스_토큰)
                .러너가_러너_게시글을_작성한다(러너_게시글_생성_요청)

                .서버_응답()
                .러너_게시글_생성_성공을_검증한다()
                .생성한_러너_게시글의_식별자값을_반환한다();

        final RunnerPostResponse.Detail 리뷰가_시작되지_않은_에단의_러너_게시글_Detail_응답 = 러너_게시글_Detail_응답을_생성한다(러너_에단, 러너_게시글_생성_요청, NOT_STARTED, 에단의_러너_게시글_식별자값, 1, 0L, false);
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(에단_로그인_액세스_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_조회한다(에단의_러너_게시글_식별자값)

                .서버_응답()
                .러너_게시글_단건_조회_성공을_검증한다(리뷰가_시작되지_않은_에단의_러너_게시글_Detail_응답);

        RunnerPostAssuredCreateSupport
                .클라이언트_요청()
                .액세스_토큰으로_로그인한다(헤나_로그인_액세스_토큰)
                .서포터가_러너_게시글에_리뷰를_신청한다(에단의_러너_게시글_식별자값, "안녕하세요. 서포터 헤나입니다.")

                .서버_응답()
                .서포터가_러너_게시글에_리뷰_신청_성공을_검증한다(에단의_러너_게시글_식별자값);
    }

    private Runner 러너를_저장한다(final Member 사용자) {
        final Member 저장된_사용자 = memberRepository.save(사용자);

        return runnerRepository.save(RunnerFixture.createRunner(introduction("안녕하세요"), 저장된_사용자));
    }

    private Supporter 서포터_헤나를_저장한다() {
        final TechnicalTag 자바_기술_태그 = technicalTagRepository.save(createJava());
        final TechnicalTag 스프링_기술_태그 = technicalTagRepository.save(createSpring());
        final List<TechnicalTag> 기술_태그_목록 = List.of(자바_기술_태그, 스프링_기술_태그);
        final Member 저장된_사용자_헤나 = memberRepository.save(MemberFixture.createHyena());

        return supporterRepository.save(SupporterFixture.create(reviewCount(0), 저장된_사용자_헤나, 기술_태그_목록));
    }

    private RunnerPostCreateRequest 러너_게시글_생성_요청을_생성한다() {
        return 러너_게시글_생성_요청(
                "러너 게시글 테스트 제목",
                List.of("java", "spring"),
                "https://github.com",
                now().plusHours(10),
                "러너 게시글 내용",
                "게시글 궁금한 내용",
                "참고 사항");
    }

    private RunnerPostResponse.Detail 러너_게시글_Detail_응답을_생성한다(final Runner 러너,
                                                             final RunnerPostCreateRequest 러너_게시글_생성_요청,
                                                             final ReviewStatus 리뷰_상태,
                                                             final Long 러너_게시글_식별자값,
                                                             final int 조회수,
                                                             final long 서포터_지원자수,
                                                             final boolean 서포터_지원_여부
    ) {
        return 러너_게시글_Detail_응답(
                러너_게시글_식별자값,
                러너_게시글_생성_요청.title(),
                러너_게시글_생성_요청.implementedContents(),
                러너_게시글_생성_요청.curiousContents(),
                러너_게시글_생성_요청.postscriptContents(),
                러너_게시글_생성_요청.pullRequestUrl(),
                러너_게시글_생성_요청.deadline(),
                조회수,
                서포터_지원자수,
                리뷰_상태,
                true,
                서포터_지원_여부,
                러너,
                러너_게시글_생성_요청.tags()
        );
    }
}
