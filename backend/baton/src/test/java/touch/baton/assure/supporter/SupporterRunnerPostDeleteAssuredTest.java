package touch.baton.assure.supporter;

import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.SupporterRunnerPost;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@SuppressWarnings("NonAsciiCharacters")
public class SupporterRunnerPostDeleteAssuredTest extends AssuredTestConfig {

    @Test
    void 러너_게시글에_보낸_리뷰_제안을_취소한다() {
        final String 로그인한_서포터의_소셜_id = "ditooSocialId";
        final Supporter 로그인한_서포터 = 로그인한_서포터를_저장한다(로그인한_서포터의_소셜_id);
        final String 로그인한_서포터의_액세스_토큰 = login(로그인한_서포터의_소셜_id);

        final Runner 리뷰_받고_싶은_러너 = 리뷰_받고_싶은_러너를_저장한다();
        final RunnerPost 리뷰_받을_게시글 = 리뷰_받을_게시글을_생성한다(리뷰_받고_싶은_러너);
        서포터가_러너_게시글에_리뷰_제안한다(로그인한_서포터, 리뷰_받을_게시글);

        final String 응답_헤더_이름 = LOCATION;
        final String 응답_헤더_값 = "/api/v1/posts/runner/" + 리뷰_받을_게시글.getId();

        SupporterRunnerPostAssuredSupport
                .클라이언트_요청()
                .로그인_한다(로그인한_서포터의_액세스_토큰)
                .서포터가_리뷰_제안을_취소한다(리뷰_받을_게시글.getId())

                .서버_응답()
                .서포터의_리뷰_제안_철회를_검증한다(NO_CONTENT, 응답_헤더_이름, 응답_헤더_값);
    }

    private Supporter 로그인한_서포터를_저장한다(final String 소셜_id) {
        final Member 사용자 = memberRepository.save(MemberFixture.createWithSocialId(소셜_id));
        return supporterRepository.save(SupporterFixture.create(사용자));
    }

    private Runner 리뷰_받고_싶은_러너를_저장한다() {
        final Member 사용자 = memberRepository.save(MemberFixture.createEthan());
        return runnerRepository.save(RunnerFixture.createRunner(사용자));
    }

    private RunnerPost 리뷰_받을_게시글을_생성한다(final Runner 리뷰_받을_러너) {
        final RunnerPost 러너_게시글 = RunnerPostFixture.create(리뷰_받을_러너, new Deadline(LocalDateTime.now().plusHours(100)));
        return runnerPostRepository.save(러너_게시글);
    }

    private void 서포터가_러너_게시글에_리뷰_제안한다(final Supporter 서포터, final RunnerPost 러너_게시글) {
        final SupporterRunnerPost 리뷰_제안 = SupporterRunnerPostFixture.create(러너_게시글, 서포터);
        supporterRunnerPostRepository.save(리뷰_제안);
    }
}
