package touch.baton.domain.oauth.command.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.member.command.Member;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.oauth.command.exception.OauthRequestException;
import touch.baton.domain.oauth.command.repository.RefreshTokenCommandRepository;
import touch.baton.domain.oauth.command.token.AccessToken;
import touch.baton.domain.oauth.command.token.RefreshToken;
import touch.baton.domain.oauth.command.token.Token;
import touch.baton.domain.oauth.command.token.Tokens;
import touch.baton.infra.auth.jwt.JwtEncoder;

import java.util.Map;
import java.util.UUID;

@Transactional
@Component
public class TokenFacade {

    private final RefreshTokenCommandRepository refreshTokenCommandRepository;
    private final JwtEncoder jwtEncoder;

    private Long refreshTokenExpireMinutes;

    public TokenFacade(final RefreshTokenCommandRepository refreshTokenCommandRepository, final JwtEncoder jwtEncoder, @Value("${spring.redis.refresh_token.expire_minutes}") final Long refreshTokenExpireMinutes) {
        this.refreshTokenCommandRepository = refreshTokenCommandRepository;
        this.jwtEncoder = jwtEncoder;
        this.refreshTokenExpireMinutes = refreshTokenExpireMinutes;
    }

    public Tokens reissueAccessToken(final Member member, final Token refreshToken) {
        final RefreshToken findRefreshToken = refreshTokenCommandRepository.findById(member.getSocialId().getValue())
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.REFRESH_TOKEN_IS_NOT_FOUND));
        if (findRefreshToken.isNotOwner(refreshToken)) {
            throw new OauthRequestException(ClientErrorCode.ACCESS_TOKEN_AND_REFRESH_TOKEN_HAVE_DIFFERENT_OWNER);
        }

        return createTokens(member);
    }

    public Tokens createTokens(final Member member) {
        final AccessToken accessToken = createAccessToken(member.getSocialId());
        final RefreshToken refreshToken = createRefreshToken(member);

        return new Tokens(accessToken, refreshToken);
    }

    private AccessToken createAccessToken(final SocialId socialId) {
        final String jwtToken = jwtEncoder.jwtToken(
                Map.of("socialId", socialId.getValue()));
        return new AccessToken(jwtToken);
    }

    private RefreshToken createRefreshToken(final Member member) {
        final Token token = new Token(UUID.randomUUID().toString());
        final RefreshToken refreshToken = RefreshToken.builder()
                .socialId(member.getSocialId().getValue())
                .member(member)
                .token(token)
                .timeout(refreshTokenExpireMinutes)
                .build();

        refreshTokenCommandRepository.save(refreshToken);

        return refreshToken;
    }

    public void logout(final SocialId socialId) {
        refreshTokenCommandRepository.deleteById(socialId.getValue());
    }
}
