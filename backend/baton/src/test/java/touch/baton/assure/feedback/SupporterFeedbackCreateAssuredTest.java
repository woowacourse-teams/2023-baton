package touch.baton.assure.feedback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.feedback.service.SupporterFeedBackCreateRequest;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.supporter.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static touch.baton.fixture.domain.SupporterFixture.create;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;

@SuppressWarnings("NonAsciiCharacters")
class SupporterFeedbackCreateAssuredTest extends AssuredTestConfig {

    private static String 토큰;
    private Runner 피드백할_러너;

    @BeforeEach
    void setUp() {
        final String 소셜_아이디 = "hongSile";
        final Member 사용자 = memberRepository.save(MemberFixture.createWithSocialId(소셜_아이디));
        피드백할_러너 = runnerRepository.save(RunnerFixture.createRunner(사용자));
        토큰 = login(소셜_아이디);
    }

    @Test
    void 러너가_서포터_피드백을_등록한다() {
        // given
        final Member 사용자_에단 = memberRepository.save(MemberFixture.createEthan());
        final Supporter 리뷰해준_서포터 = supporterRepository.save(create(사용자_에단));
        final RunnerPost 리뷰_완료한_게시글 = runnerPostRepository.save(RunnerPostFixture.create(피드백할_러너, 리뷰해준_서포터, deadline(LocalDateTime.now().plusHours(100))));

        final SupporterFeedBackCreateRequest 서포터_피드백_요청 = new SupporterFeedBackCreateRequest("GOOD", List.of("코드리뷰가 맛있어요.", "말투가 친절해요."), 리뷰해준_서포터.getId(), 리뷰_완료한_게시글.getId());

        // when, then
        SupporterFeedbackAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(토큰)
                .서포터_피드백을_등록한다(서포터_피드백_요청)

                .서버_응답()
                .서포터_피드백_등록_성공을_검증한다(new HttpStatusAndLocationHeader(CREATED, "/api/v1/feedback/supporter"));
    }
}
