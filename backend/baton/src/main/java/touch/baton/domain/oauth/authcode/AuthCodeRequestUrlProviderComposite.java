package touch.baton.domain.oauth.authcode;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.oauth.OauthType;
import touch.baton.domain.oauth.exception.OauthRequestException;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Profile("!test")
@Component
public class AuthCodeRequestUrlProviderComposite {

    private final Map<OauthType, AuthCodeRequestUrlProvider> authCodeProviders;

    public AuthCodeRequestUrlProviderComposite(final Set<AuthCodeRequestUrlProvider> authCodeProviders) {
        this.authCodeProviders = authCodeProviders.stream()
                .collect(Collectors.toMap(
                        AuthCodeRequestUrlProvider::oauthServer, identity()
                ));
    }

    public String findRequestUrl(final OauthType oauthType) {
        return findAuthCodeProvider(oauthType).getRequestUrl();
    }

    private AuthCodeRequestUrlProvider findAuthCodeProvider(final OauthType oauthType) {
        return Optional.ofNullable(authCodeProviders.get(oauthType))
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.OAUTH_REQUEST_URL_PROVIDER_IS_WRONG));
    }
}
