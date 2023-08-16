package touch.baton.assure.supporter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.supporter.Supporter;
import touch.baton.domain.supporter.service.dto.SupporterUpdateRequest;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.SupporterFixture;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static touch.baton.domain.common.exception.ClientErrorCode.*;

@SuppressWarnings("NonAsciiCharacters")
public class SupporterProfileAssuredUpdateTest extends AssuredTestConfig {

    private String 디투_액세스_토큰;

    @BeforeEach
    void setUp() {
        final String 디투_소셜_id = "ditooSocialId";
        final Member 사용자_디투 = memberRepository.save(MemberFixture.createWithSocialId(디투_소셜_id));
        final Supporter 서포터_디투 = supporterRepository.save(SupporterFixture.create(사용자_디투));
        디투_액세스_토큰 = login(디투_소셜_id);
    }

    @Test
    void 서포터_정보를_수정한다() {
        final SupporterUpdateRequest supporterUpdateRequest = new SupporterUpdateRequest("디투랜드", "우아한테크코스", "안녕하세요.", List.of("java", "spring"));

        SupporterAssuredSupport
                .클라이언트_요청()
                .로그인_한다(디투_액세스_토큰)
                .서포터_본인_프로필을_수정한다(supporterUpdateRequest)

                .서버_응답()
                .서포터_본인_프로필_수정_성공을_검증한다(NO_CONTENT);
    }

    @Test
    void 서포터_정보_수정_시에_이름이_없으면_예외가_발생한다() {
        final SupporterUpdateRequest supporterUpdateRequest = new SupporterUpdateRequest(null, "우아한테크코스", "안녕하세요.", List.of("java", "spring"));

        SupporterAssuredSupport
                .클라이언트_요청()
                .로그인_한다(디투_액세스_토큰)
                .서포터_본인_프로필을_수정한다(supporterUpdateRequest)

                .서버_응답()
                .서포터_본인_프로필_수정_실패를_검증한다(NAME_IS_NULL);
    }

    @Test
    void 서포터_정보_수정_시에_소속이_없으면_예외가_발생한다() {
        final SupporterUpdateRequest supporterUpdateRequest = new SupporterUpdateRequest("디투랜드", null, "안녕하세요.", List.of("java", "spring"));

        SupporterAssuredSupport
                .클라이언트_요청()
                .로그인_한다(디투_액세스_토큰)
                .서포터_본인_프로필을_수정한다(supporterUpdateRequest)

                .서버_응답()
                .서포터_본인_프로필_수정_실패를_검증한다(COMPANY_IS_NULL);
    }

    @Test
    void 서포터_정보_수정_시에_소개글이_없으면_예외가_발생한다() {
        final SupporterUpdateRequest supporterUpdateRequest = new SupporterUpdateRequest("디투랜드", "배달의민족", null, List.of("java", "spring"));

        SupporterAssuredSupport
                .클라이언트_요청()
                .로그인_한다(디투_액세스_토큰)
                .서포터_본인_프로필을_수정한다(supporterUpdateRequest)

                .서버_응답()
                .서포터_본인_프로필_수정_실패를_검증한다(SUPPORTER_INTRODUCTION_IS_NULL);
    }

    @Test
    void 서포터_정보_수정_시에_기술_태그가_없으면_예외가_발생한다() {
        final SupporterUpdateRequest supporterUpdateRequest = new SupporterUpdateRequest("디투랜드", "배달의민족", "배달왕이 될거에요.", null);

        SupporterAssuredSupport
                .클라이언트_요청()
                .로그인_한다(디투_액세스_토큰)
                .서포터_본인_프로필을_수정한다(supporterUpdateRequest)

                .서버_응답()
                .서포터_본인_프로필_수정_실패를_검증한다(SUPPORTER_TECHNICAL_TAGS_ARE_NULL);
    }
}
