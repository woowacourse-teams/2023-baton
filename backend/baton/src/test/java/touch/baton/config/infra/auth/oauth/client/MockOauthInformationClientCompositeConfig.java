package touch.baton.config.infra.auth.oauth.client;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import touch.baton.config.infra.auth.oauth.authcode.MockAuthCodes;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.tobe.domain.member.command.Member;
import touch.baton.tobe.domain.oauth.command.OauthInformation;
import touch.baton.tobe.domain.oauth.command.OauthType;
import touch.baton.tobe.domain.oauth.command.client.OauthInformationClientComposite;
import touch.baton.tobe.domain.oauth.command.token.SocialToken;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.when;

@TestConfiguration
public abstract class MockOauthInformationClientCompositeConfig {

    @Bean
    OauthInformationClientComposite oauthInformationClientComposite() {
        final OauthInformationClientComposite mock = Mockito.mock(OauthInformationClientComposite.class);

        when(mock.fetchInformation(any(OauthType.class), eq(MockAuthCodes.ditooAuthCode())))
                .thenReturn(oauthInformation(MemberFixture.createDitoo(), "ditoo_access_token"));

        when(mock.fetchInformation(any(OauthType.class), eq(MockAuthCodes.ethanAuthCode())))
                .thenReturn(oauthInformation(MemberFixture.createEthan(), "ethan_access_token"));

        when(mock.fetchInformation(any(OauthType.class), eq(MockAuthCodes.hyenaAuthCode())))
                .thenReturn(oauthInformation(MemberFixture.createHyena(), "hyena_access_token"));

        when(mock.fetchInformation(any(OauthType.class), eq(MockAuthCodes.judyAuthCode())))
                .thenReturn(oauthInformation(MemberFixture.createJudy(), "judy_access_token"));

        return mock;
    }

    public OauthInformation oauthInformation(final Member member, final String mockSocialToken) {

        return OauthInformation.builder()
                .oauthId(member.getOauthId())
                .socialToken(new SocialToken(mockSocialToken))
                .oauthId(member.getOauthId())
                .memberName(member.getMemberName())
                .socialId(member.getSocialId())
                .githubUrl(member.getGithubUrl())
                .imageUrl(member.getImageUrl())
                .build();
    }
}
