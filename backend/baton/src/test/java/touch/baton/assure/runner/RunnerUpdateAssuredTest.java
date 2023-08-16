package touch.baton.assure.runner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import touch.baton.config.AssuredTestConfig;
import touch.baton.domain.member.Member;
import touch.baton.domain.runner.Runner;
import touch.baton.domain.runner.service.dto.RunnerUpdateRequest;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.fixture.domain.RunnerFixture;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static touch.baton.domain.common.exception.ClientErrorCode.COMPANY_IS_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.NAME_IS_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.RUNNER_TECHNICAL_TAGS_ARE_NULL;

@SuppressWarnings("NonAsciiCharacters")
public class RunnerUpdateAssuredTest extends AssuredTestConfig {

    private String 디투_액세스_토큰;

    private Runner 러너_디투;

    @BeforeEach
    void setUp() {
        final String 소셜_id = "judySocialId";
        final Member 사용자_주디 = memberRepository.save(MemberFixture.createWithSocialId(소셜_id));
        러너_디투 = runnerRepository.save(RunnerFixture.createRunner(사용자_주디));
        디투_액세스_토큰 = login(소셜_id);
    }

    @Test
    void 러너_정보를_수정한다() {
        final RunnerUpdateRequest 러너_업데이트_요청 = new RunnerUpdateRequest("업데이트된 이름", "업데이트된 소속", "업데이트된 자기소개", List.of("Java", "React"));

        RunnerAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(디투_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_업데이트_요청)

                .서버_응답()
                .러너_본인_프로필_수정_성공을_검증한다(NO_CONTENT, 러너_디투.getId());
    }

    @Test
    void 러너_정보_수정_시에_이름이_없으면_예외가_발생한다() {
        final RunnerUpdateRequest 러너_업데이트_요청 = new RunnerUpdateRequest(null, "업데이트된 소속", "업데이트된 자기소개", List.of("Java", "React"));

        RunnerAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(디투_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_업데이트_요청)

                .서버_응답()
                .러너_본인_프로필_수정_실패를_검증한다(NAME_IS_NULL);
    }

    @Test
    void 서포터_정보_수정_시에_소속이_없으면_예외가_발생한다() {
        final RunnerUpdateRequest 러너_업데이트_요청 = new RunnerUpdateRequest("업데이트된 이름", null, "업데이트된 자기소개", List.of("Java", "React"));

        RunnerAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(디투_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_업데이트_요청)

                .서버_응답()
                .러너_본인_프로필_수정_실패를_검증한다(COMPANY_IS_NULL);
    }

    @Test
    void 러너_정보_수정_시에_소개글이_없어도_된다() {
        final RunnerUpdateRequest 러너_업데이트_요청 = new RunnerUpdateRequest("업데이트된 이름", "업데이트된 소속", null, List.of("Java", "React"));

        RunnerAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(디투_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_업데이트_요청)

                .서버_응답()
                .러너_본인_프로필_수정_성공을_검증한다(NO_CONTENT, 러너_디투.getId());
    }

    @Test
    void 러너_정보_수정_시에_기술_태그가_없으면_예외가_발생한다() {
        final RunnerUpdateRequest 러너_업데이트_요청 = new RunnerUpdateRequest("업데이트된 이름", "업데이트된 소속", "업데이트된 자기소개", null);

        RunnerAssuredSupport
                .클라이언트_요청()
                .토큰으로_로그인한다(디투_액세스_토큰)
                .러너_본인_프로필을_수정한다(러너_업데이트_요청)

                .서버_응답()
                .러너_본인_프로필_수정_실패를_검증한다(RUNNER_TECHNICAL_TAGS_ARE_NULL);
    }
}
