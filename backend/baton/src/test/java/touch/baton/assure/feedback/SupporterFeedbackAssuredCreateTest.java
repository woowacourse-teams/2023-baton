package touch.baton.assure.feedback;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import touch.baton.assure.common.AssuredSupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.common.vo.Grade;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.HttpStatus.CREATED;
import static touch.baton.fixture.domain.SupporterFixture.create;
import static touch.baton.fixture.vo.DeadlineFixture.deadline;
import static touch.baton.fixture.vo.ReviewCountFixture.reviewCount;
import static touch.baton.fixture.vo.StarCountFixture.starCount;
import static touch.baton.fixture.vo.TotalRatingFixture.totalRating;

@SuppressWarnings("NonAsciiCharacters")
class SupporterFeedbackAssuredCreateTest extends AssuredTestConfig {

    @Test
    void 러너가_서포터_피드백을_등록한다() {
        // given
        final Member memberHyena = memberRepository.save(MemberFixture.createHyena());
        final Runner runnerHyena = runnerRepository.save(RunnerFixture.create(totalRating(0), Grade.BARE_FOOT, memberHyena));
        final Member memberEthan = memberRepository.save(MemberFixture.createEthan());
        final Supporter supporterEthan = supporterRepository.save(create(reviewCount(0), starCount(0), totalRating(0), Grade.BARE_FOOT, memberEthan, new ArrayList<>()));
        final RunnerPost runnerPost = runnerPostRepository.save(RunnerPostFixture.create(runnerHyena, supporterEthan, deadline(LocalDateTime.now().plusHours(100))));

        final SupporterFeedBackCreateRequest request = new SupporterFeedBackCreateRequest("GOOD", List.of("코드리뷰가 맛있어요.", "말투가 친절해요."), supporterEthan.getId(), runnerPost.getId());

        // when
        final ExtractableResponse<Response> result = AssuredSupport.post("/api/v1/feedback/supporter", request);

        // then
        assertAll(
                () -> assertThat(result.statusCode()).isEqualTo(CREATED.value()),
                () -> assertThat(result.header(HttpHeaders.LOCATION)).contains("/api/v1/feedback/supporter")
        );
    }
}
