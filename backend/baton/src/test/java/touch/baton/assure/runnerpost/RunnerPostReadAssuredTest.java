package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.common.response.PageResponse;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;
import touch.baton.domain.runnerpost.vo.ReviewStatus;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.domain.technicaltag.TechnicalTag;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.util.List;

import static java.time.LocalDateTime.now;
import static touch.baton.assure.runnerpost.RunnerPostAssuredSupport.러너_게시글_전체_조회_응답;
import static touch.baton.fixture.domain.TechnicalTagFixture.createJava;
import static touch.baton.fixture.domain.TechnicalTagFixture.createSpring;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.IntroductionFixture.introduction;
import static touch.baton.fixture.vo.MessageFixture.message;
import static touch.baton.fixture.vo.ReviewCountFixture.reviewCount;

public class RunnerPostReadAssuredTest extends AssuredTestConfig {

    @Test
    void 러너_게시글_전체_조회에_성공한다() {
        final Runner 러너_에단 = 러너를_저장한다(MemberFixture.createEthan());
        final RunnerPost 러너_에단의_게시글 = 러너_게시글을_등록한다(러너_에단);
        runnerPostRepository.save(러너_에단의_게시글);

        final String 헤나_액세스_토큰 = login(러너_에단.getMember().getSocialId().getValue());

        final PageRequest 페이징_정보 = PageRequest.of(1, 10);
        final RunnerPostResponse.Simple 게시글_응답
                = RunnerPostResponse.Simple.from(러너_에단의_게시글, 0);
        final PageResponse<RunnerPostResponse.Simple> 페이징된_게시글_응답
                = 러너_게시글_전체_조회_응답(페이징_정보, List.of(게시글_응답));

        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(헤나_액세스_토큰)
                .전체_러너_게시글_페이징을_조회한다(페이징_정보)

                .서버_응답()
                .전체_러너_게시글_페이징_조회_성공을_검증한다(페이징된_게시글_응답);
    }

    private RunnerPost 러너_게시글에_서포터를_할당한다(final Supporter 서포터_헤나, final RunnerPost 러너_에단의_게시글) {
        러너_에단의_게시글.assignSupporter(서포터_헤나);
        return runnerPostRepository.save(러너_에단의_게시글);
    }

    private RunnerPost 러너_게시글을_등록한다(final Runner 러너) {

        return runnerPostRepository.save(RunnerPostFixture.create(러너, deadline(now().plusHours(100))));
    }

    private Runner 러너를_저장한다(final Member member) {
        final Member 저장된_사용자 = memberRepository.save(member);

        return runnerRepository.save(RunnerFixture.createRunner(introduction("안녕하세요"), 저장된_사용자));
    }

    private Supporter 서포터_헤나를_저장한다() {
        final TechnicalTag 자바_기술_태그 = technicalTagRepository.save(createJava());
        final TechnicalTag 스프링_기술_태그 = technicalTagRepository.save(createSpring());
        final List<TechnicalTag> 기술_태그_목록 = List.of(자바_기술_태그, 스프링_기술_태그);
        final Member 저장된_사용자_헤나 = memberRepository.save(MemberFixture.createHyena());

        return supporterRepository.save(SupporterFixture.create(reviewCount(0), 저장된_사용자_헤나, 기술_태그_목록));
    }

    private SupporterRunnerPost 서포터를_러너_게시글에_저장한다(final Supporter 서포터, final RunnerPost 러너_게시글) {
        final SupporterRunnerPost 서포터_러너_게시글 = SupporterRunnerPost.builder()
                .runnerPost(러너_게시글)
                .supporter(서포터)
                .message(message("안녕하세요. 서포터 헤나입니다."))
                .build();

        return supporterRunnerPostRepository.save(서포터_러너_게시글);
    }
}
