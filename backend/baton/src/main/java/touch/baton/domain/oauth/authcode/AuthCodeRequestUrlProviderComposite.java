package touch.baton.domain.oauth.authcode;

import org.springframework.stereotype.Component;
import touch.baton.domain.oauth.OauthType;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

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
                .orElseThrow(() -> new IllegalStateException("예외 만들어주쇼."));
    }
}
