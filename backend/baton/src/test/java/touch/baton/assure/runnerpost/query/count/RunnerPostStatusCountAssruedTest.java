package touch.baton.assure.runnerpost.query.count;

import org.junit.jupiter.api.Test;
import touch.baton.assure.runnerpost.support.query.count.RunnerPostStatusCountAssuredSupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.FakeAuthCodes;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.SocialId;

import static touch.baton.assure.runnerpost.RunnerPostSteps.러너_게시글을_생성하고_게시을글_식별자를_반환한다;
import static touch.baton.assure.runnerpost.RunnerPostSteps.러너가_게시글을_작성하고_리뷰를_받은_뒤_리뷰완료로_변경한다;
import static touch.baton.assure.runnerpost.RunnerPostSteps.러너가_게시글을_작성하고_서포터가_선택된다;

@SuppressWarnings("NonAsciiCharacters")
class RunnerPostStatusCountAssruedTest extends AssuredTestConfig {

    @Test
    void 리뷰_상태_별_게시글의_총_개수를_조회한다() {
        // given
        final String 러너_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.hyenaAuthCode());
        final String 서포터_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ethanAuthCode());
        final SocialId 서포터_소셜_아이디 = jwtTestManager.parseToSocialId(서포터_액세스_토큰);
        final Supporter 서포터 = supporterRepository.getBySocialId(서포터_소셜_아이디);

        final Long 리뷰_진행중_글_개수 = 1L;
        러너가_게시글을_작성하고_서포터가_선택된다(러너_액세스_토큰, 서포터_액세스_토큰, 서포터);

        final Long 리뷰_완료_글_개수 = 2L;
        러너가_게시글을_작성하고_리뷰를_받은_뒤_리뷰완료로_변경한다(러너_액세스_토큰, 서포터_액세스_토큰, 서포터);
        러너가_게시글을_작성하고_리뷰를_받은_뒤_리뷰완료로_변경한다(러너_액세스_토큰, 서포터_액세스_토큰, 서포터);

        final Long 리뷰_대기중_글_개수 = 3L;
        러너_게시글을_생성하고_게시을글_식별자를_반환한다(러너_액세스_토큰);
        러너_게시글을_생성하고_게시을글_식별자를_반환한다(러너_액세스_토큰);
        러너_게시글을_생성하고_게시을글_식별자를_반환한다(러너_액세스_토큰);

        final Long 기간_만료_글_개수 = 4L;
        러너_게시글을_생성하고_마감기한이_지나_마감된다(러너_액세스_토큰);
        러너_게시글을_생성하고_마감기한이_지나_마감된다(러너_액세스_토큰);
        러너_게시글을_생성하고_마감기한이_지나_마감된다(러너_액세스_토큰);
        러너_게시글을_생성하고_마감기한이_지나_마감된다(러너_액세스_토큰);

        // when, then
        RunnerPostStatusCountAssuredSupport
                .클라이언트_요청()
                .게시글의_상태별로_게시글_총_수를_반환한다()

                .서버_응답()
                .게시글_상태별_게시글_총_수_반환_성공을_확인한다(리뷰_대기중_글_개수, 리뷰_진행중_글_개수, 리뷰_완료_글_개수, 기간_만료_글_개수);
    }

    private void 러너_게시글을_생성하고_마감기한이_지나_마감된다(final String 러너_액세스_토큰) {
        final Long 만료된_게시글 = 러너_게시글을_생성하고_게시을글_식별자를_반환한다(러너_액세스_토큰);
        runnerPostCommandRepository.expireRunnerPost(만료된_게시글);
    }
}
