package touch.baton.assure.feedback;

import org.junit.jupiter.api.Disabled;
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
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static touch.baton.fixture.domain.SupporterFixture.create;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.IntroductionFixture.introduction;
import static touch.baton.fixture.vo.ReviewCountFixture.reviewCount;

@SuppressWarnings("NonAsciiCharacters")
class SupporterFeedbackAssuredCreateTest extends AssuredTestConfig {

    // TODO: 2023/08/03 로그인 기능 테스트 추가
    @Disabled
    @Test
    void 러너가_서포터_피드백을_등록한다() {
        // given
        final Member memberHyena = memberRepository.save(MemberFixture.createHyena());
        final Runner runnerHyena = runnerRepository.save(RunnerFixture.create(introduction("안녕하세요"), memberHyena));
        final Member memberEthan = memberRepository.save(MemberFixture.createEthan());
        final Supporter supporterEthan = supporterRepository.save(create(reviewCount(0), memberEthan, new ArrayList<>()));
        final RunnerPost runnerPost = runnerPostRepository.save(RunnerPostFixture.create(runnerHyena, supporterEthan, deadline(LocalDateTime.now().plusHours(100))));

        final SupporterFeedBackCreateRequest request = new SupporterFeedBackCreateRequest("GOOD", List.of("코드리뷰가 맛있어요.", "말투가 친절해요."), supporterEthan.getId(), runnerPost.getId());

        // when, then
        SupporterFeedbackAssuredSupport
                .클라이언트_요청().서포터_피드백을_등록한다(request)
                .서버_응답().서포터_피드백_등록_성공을_검증한다(new HttpStatusAndLocationHeader(CREATED, "/api/v1/feedback/supporter"));
    }
}
