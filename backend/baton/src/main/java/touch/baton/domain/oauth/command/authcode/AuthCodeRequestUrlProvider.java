package touch.baton.domain.oauth.command.authcode;

import touch.baton.domain.oauth.command.OauthType;

public interface AuthCodeRequestUrlProvider {

    OauthType oauthServer();

    String getRequestUrl();
}
