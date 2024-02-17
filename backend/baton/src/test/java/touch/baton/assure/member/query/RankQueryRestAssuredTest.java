package touch.baton.assure.member.query;

import org.junit.jupiter.api.Test;
import touch.baton.assure.member.support.query.RankQueryAssuredSupport;
import touch.baton.config.AssuredTestConfig;
import touch.baton.config.infra.auth.oauth.authcode.FakeAuthCodes;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.SocialId;

import java.util.List;

import static touch.baton.assure.member.support.query.RankQueryAssuredSupport.RankQueryResponseBuilder.서포터_리뷰_랭킹_응답;

@SuppressWarnings("NonAsciiCharacters")
class RankQueryRestAssuredTest extends AssuredTestConfig {

    @Test
    void 코드_리뷰를_가장_많이_한_5명을_리뷰_숫자를_기준으로_내림차순으로_반환한다() {
        // given
        final String 에단_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ethanAuthCode());
        final SocialId 에단_소셜_아이디 = jwtTestManager.parseToSocialId(에단_액세스_토큰);
        final Supporter 서포터_에단 = supporterRepository.getBySocialId(에단_소셜_아이디);
        rankQueryRepository.updateReviewCount(서포터_에단.getId(), 10);

        final String 디투_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.ditooAuthCode());
        final SocialId 디투_소셜_아이디 = jwtTestManager.parseToSocialId(디투_액세스_토큰);
        final Supporter 서포터_디투 = supporterRepository.getBySocialId(디투_소셜_아이디);
        rankQueryRepository.updateReviewCount(서포터_디투.getId(), 5);

        final String 헤나_액세스_토큰 = oauthLoginTestManager.소셜_회원가입을_진행한_후_액세스_토큰을_반환한다(FakeAuthCodes.hyenaAuthCode());
        final SocialId 헤나_소셜_아이디 = jwtTestManager.parseToSocialId(헤나_액세스_토큰);
        final Supporter 서포터_헤나 = supporterRepository.getBySocialId(헤나_소셜_아이디);
        rankQueryRepository.updateReviewCount(서포터_헤나.getId(), 20);

        // when, then
        RankQueryAssuredSupport.클라이언트_요청()
                .서포터_리뷰_랭킹을_조회한다(2)

                .서버_응답()
                .서포터_리뷰_랭킹_조회_성공을_검증한다(List.of(
                        서포터_리뷰_랭킹_응답(1, 서포터_헤나, 20),
                        서포터_리뷰_랭킹_응답(2, 서포터_에단, 10)
                ));
    }
}
