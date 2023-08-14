package touch.baton.assure.runnerpost;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import touch.baton.assure.common.HttpStatusAndLocationHeader;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.supporter.Supporter;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;
import touch.baton.fixture.domain.RunnerPostFixture;
import touch.baton.fixture.domain.SupporterFixture;

import static touch.baton.domain.runnerpost.vo.ReviewStatus.IN_PROGRESS;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerPostAssuredUpdateTest extends AssuredTestConfig {

    @Test
    void 서포터_리뷰완료_후_리뷰상태를_완료로_변경할_수_있다() {
        // given
        final Member 사용자_에단 = memberRepository.save(MemberFixture.createEthan());
        final Runner 글_쓴_러너 = runnerRepository.save(RunnerFixture.createRunner(사용자_에단));

        final String 디투_소셜_아이디 = "hongSile";
        final Member 사용자_디투 = memberRepository.save(MemberFixture.createWithSocialId(디투_소셜_아이디));
        final Supporter 선택된_서포터 = supporterRepository.save(SupporterFixture.create(사용자_디투));
        final String 서포터_디투_토큰 = login(디투_소셜_아이디);

        final RunnerPost 서포터가_배정된_게시글 = runnerPostRepository.save(RunnerPostFixture.create(글_쓴_러너, 선택된_서포터, IN_PROGRESS));

        // when, then
        RunnerPostAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(서포터_디투_토큰)
                .서포터가_리뷰를_완료하고_리뷰완료_버튼을_누른다(서포터가_배정된_게시글.getId())

                .서버_응답()
                .러너_게시글이_성공적으로_리뷰_완료_상태인지_확인한다(new HttpStatusAndLocationHeader(HttpStatus.NO_CONTENT, "/api/v1/posts/runner"));
    }
}
