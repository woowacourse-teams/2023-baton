package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.runnerpost.service.dto.RunnerPostUpdateRequest;
import touch.baton.domain.runnerpost.vo.Deadline;
import touch.baton.domain.supporter.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;
import touch.baton.fixture.domain.SupporterRunnerPostFixture;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostAssuredUpdateTest extends AssuredTestConfig {

    @Test
    void 러너가_서포터_목록에서_서포터를_선택할_수_있다() {
        // given
        final String 디투_소셜_아이디 = "hongSile";
        final Member 사용자_디투 = memberRepository.save(MemberFixture.createWithSocialId(디투_소셜_아이디));
        final Runner 러너_디투 = runnerRepository.save(RunnerFixture.createRunner(사용자_디투));
        final String 디투_토큰 = login(디투_소셜_아이디);

        final RunnerPost 디투_게시글 = runnerPostRepository.save(RunnerPostFixture.create(러너_디투,
                new Deadline(LocalDateTime.now().plusDays(10))));

        final Member 사용자_에단 = memberRepository.save(MemberFixture.createEthan());
        final Supporter 서포터_에단 = supporterRepository.save(SupporterFixture.create(사용자_에단));

        서포터가_리뷰_게시글에_리뷰_제안을_한다(서포터_에단, 디투_게시글);

        final RunnerPostUpdateRequest.AppliedSupporter 서포터_선택_요청_정보 = new RunnerPostUpdateRequest.AppliedSupporter(서포터_에단.getId());

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(디투_토큰)
                .러너가_서포터를_선택한다(디투_게시글.getId(), 서포터_선택_요청_정보)

                .서버_응답()
                .러너_게시글에_서포터가_성공적으로_선택되었는지_확인한다(new HttpStatusAndLocationHeader(HttpStatus.NO_CONTENT, "/api/v1/posts/runner"));
    }

    private void 서포터가_리뷰_게시글에_리뷰_제안을_한다(final Supporter 지원한_서포터, final RunnerPost 지원할_게시글) {
        supporterRunnerPostRepository.save(SupporterRunnerPostFixture.create(지원한_서포터, 지원할_게시글));
    }
}
