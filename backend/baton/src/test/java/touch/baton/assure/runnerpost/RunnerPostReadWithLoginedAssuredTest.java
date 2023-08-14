package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.controller.response.SupporterRunnerPostResponse;
import touch.baton.domain.runnerpost.controller.response.SupporterRunnerPostResponses;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static touch.baton.fixture.domain.MessageFixture.message;
import static java.time.LocalDateTime.now;
import static touch.baton.assure.runnerpost.RunnerPostAssuredSupport.러너_게시글_Detail_응답;
import static touch.baton.assure.runnerpost.RunnerPostAssuredSupport.클라이언트_요청;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.IntroductionFixture.introduction;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostReadWithLoginedAssuredTest extends AssuredTestConfig {

    @Test
    void 러너의_게시글_식별자값으로_러너_게시글_상세_정보_조회에_성공한다() {
        final Member 사용자_헤나 = memberRepository.save(MemberFixture.createHyena());
        final Runner 러너_헤나 = runnerRepository.save(RunnerFixture.createRunner(introduction("안녕하세요"), 사용자_헤나));
        final RunnerPost 러너_게시글 = runnerPostRepository.save(RunnerPostFixture.create(러너_헤나, deadline(now().plusHours(100))));
        final String 로그인용_토큰 = login(사용자_헤나.getSocialId().getValue());

        final RunnerPostResponse.Detail 러너_게시글_detail_응답 = 러너_게시글_Detail_응답을_생성한다(러너_헤나, 러너_게시글, ReviewStatus.NOT_STARTED, 러너_게시글.getId(), 1, 0, false);

        클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .러너_게시글_식별자값으로_러너_게시글을_조회한다(러너_게시글.getId())

                .서버_응답()
                .러너_게시글_단건_조회_성공을_검증한다(러너_게시글_detail_응답);
    }

    private RunnerPostResponse.Detail 러너_게시글_Detail_응답을_생성한다(final Runner 로그인한_러너,
                                                             final RunnerPost 러너_게시글,
                                                             final ReviewStatus 리뷰_상태,
                                                             final Long 러너_게시글_식별자값,
                                                             final int 조회수,
                                                             final long 서포터_지원자수,
                                                             final boolean 서포터_지원_여부
    ) {
        return 러너_게시글_Detail_응답(
                러너_게시글_식별자값,
                러너_게시글.getTitle().getValue(),
                러너_게시글.getContents().getValue(),
                러너_게시글.getPullRequestUrl().getValue(),
                러너_게시글.getDeadline().getValue(),
                조회수,
                서포터_지원자수,
                리뷰_상태,
                !러너_게시글.isNotOwner(로그인한_러너),
                서포터_지원_여부,
                러너_게시글.getRunner(),
                러너_게시글.getRunnerPostTags().getRunnerPostTags().stream()
                        .map(runnerPostTag -> runnerPostTag.getTag().getTagName().getValue())
                        .toList()
        );
    }

    @Test
    void 러너의_게시글_식별자값으로_서포터_러너_게시글_조회에_성공한다() {
        final Member 사용자_헤나 = memberRepository.save(MemberFixture.createJudy());
        final Runner 러너_헤나 = runnerRepository.save(RunnerFixture.createRunner(introduction("안녕하세요"), 사용자_헤나));
        final RunnerPost 러너_게시글 = runnerPostRepository.save(RunnerPostFixture.create(러너_헤나, deadline(LocalDateTime.now().plusHours(100))));
        final String 로그인용_토큰 = login(사용자_헤나.getSocialId().getValue());

        final Member 사용자_주디 = memberRepository.save(MemberFixture.createJudy());
        final Supporter 서포터_주디 = supporterRepository.save(SupporterFixture.create(사용자_주디));
        final SupporterRunnerPost 서포터_러너_게시글 = supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(러너_게시글, 서포터_주디));

        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(로그인용_토큰)
                .러너_게시글_식별자값으로_서포터_러너_게시글을_조회한다(러너_게시글.getId())

                .서버_응답()
                .서포터_러너_게시글_조회_성공을_검증한다(new SupporterRunnerPostResponses.All(
                        List.of(new SupporterRunnerPostResponse.Detail(
                                서포터_주디.getId(),
                                서포터_러너_게시글.getSupporter().getMember().getMemberName().getValue(),
                                서포터_러너_게시글.getSupporter().getMember().getCompany().getValue(),
                                서포터_러너_게시글.getSupporter().getReviewCount().getValue(),
                                서포터_러너_게시글.getSupporter().getMember().getImageUrl().getValue(),
                                서포터_러너_게시글.getMessage().getValue(),
                                서포터_러너_게시글.getSupporter().getSupporterTechnicalTags().getSupporterTechnicalTags().stream()
                                        .map(supporterTechnicalTag -> supporterTechnicalTag.getTechnicalTag().getTagName().getValue())
                                        .toList()
                        ))
                ));
    }
}
