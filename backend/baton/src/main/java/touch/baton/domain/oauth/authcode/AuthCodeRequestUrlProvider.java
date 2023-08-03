package touch.baton.domain.oauth.authcode;

import touch.baton.domain.oauth.OauthType;

public interface AuthCodeRequestUrlProvider {

    OauthType oauthServer();

    String getRequestUrl();
}
