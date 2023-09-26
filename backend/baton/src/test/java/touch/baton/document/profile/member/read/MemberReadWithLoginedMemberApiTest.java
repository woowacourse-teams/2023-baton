package touch.baton.document.profile.member.read;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import touch.baton.config.RestdocsConfig;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.member.query.controller.MemberQueryController;
import touch.baton.fixture.domain.MemberFixture;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static touch.baton.fixture.vo.CompanyFixture.company;
import static touch.baton.fixture.vo.GithubUrlFixture.githubUrl;
import static touch.baton.fixture.vo.ImageUrlFixture.imageUrl;
import static touch.baton.fixture.vo.MemberNameFixture.memberName;
import static touch.baton.fixture.vo.OauthIdFixture.oauthId;
import static touch.baton.fixture.vo.SocialIdFixture.socialId;

@WebMvcTest(MemberQueryController.class)
public class MemberReadWithLoginedMemberApiTest extends RestdocsConfig {

    @BeforeEach
    void setUp() {
        restdocsSetUp(new MemberQueryController());
    }

    @DisplayName("로그인 한 맴버 정보 조회 API")
    @Test
    void readLoginMemberByAccessToken() throws Exception {
        // given
        final String socialId = "ditooSocialId";
        final Member member = MemberFixture.create(
                memberName("디투"),
                socialId(socialId),
                oauthId("abcd"),
                githubUrl("naver.com"),
                company("우아한테크코스"),
                imageUrl("profile.jpg")
        );
        final String token = getAccessTokenBySocialId(socialId);

        // when
        when(oauthMemberRepository.findBySocialId(any())).thenReturn(Optional.ofNullable(member));

        // then
        mockMvc.perform(get("/api/v1/profile/me").header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        requestHeaders(headerWithName(AUTHORIZATION).description("Bearer JWT")),
                        responseFields(
                                fieldWithPath("name").type(STRING).description("사용자 이름"),
                                fieldWithPath("imageUrl").type(STRING).description("사용자 프로필 이미지 url")
                        )
                ))
                .andDo(print());
    }
}
