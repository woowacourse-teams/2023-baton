package touch.baton.domain.oauth.command.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.oauth.command.authcode.AuthCodeRequestUrlProviderComposite;
import touch.baton.domain.oauth.command.client.OauthInformationClientComposite;
import touch.baton.domain.oauth.command.repository.OauthMemberCommandRepository;
import touch.baton.domain.oauth.command.repository.OauthRunnerCommandRepository;
import touch.baton.domain.oauth.command.repository.OauthSupporterCommandRepository;
import touch.baton.domain.oauth.command.repository.RefreshTokenCommandRepository;
import touch.baton.fixture.domain.MemberFixture;
import touch.baton.infra.auth.jwt.JwtDecoder;
import touch.baton.infra.auth.jwt.JwtEncoder;

import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.only;

@ExtendWith(MockitoExtension.class)
class OauthCommandServiceDeleteTest {

    private OauthCommandService oauthCommandService;

    @Mock
    private AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;

    @Mock
    private OauthInformationClientComposite oauthInformationClientComposite;

    @Mock
    private OauthMemberCommandRepository oauthMemberCommandRepository;

    @Mock
    private OauthRunnerCommandRepository oauthRunnerCommandRepository;

    @Mock
    private OauthSupporterCommandRepository oauthSupporterCommandRepository;

    @Mock
    private RefreshTokenCommandRepository refreshTokenCommandRepository;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private JwtEncoder expiredJwtEncoder;

    @Mock
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        oauthCommandService = new OauthCommandService(authCodeRequestUrlProviderComposite, oauthInformationClientComposite, oauthMemberCommandRepository, oauthRunnerCommandRepository, oauthSupporterCommandRepository, refreshTokenCommandRepository, jwtEncoder, jwtDecoder);
    }

    @DisplayName("Member 로 RefreshToken 을 삭제할 수 있다.")
    @Test
    void success_logout() {
        // given
        final Member ethan = MemberFixture.createEthan();

        // when
        oauthCommandService.logout(ethan);

        // then
        verify(refreshTokenCommandRepository, only()).deleteByMember(ethan);
    }
}
