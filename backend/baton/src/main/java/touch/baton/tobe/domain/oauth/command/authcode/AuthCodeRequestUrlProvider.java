package touch.baton.tobe.domain.oauth.command.authcode;

import touch.baton.tobe.domain.oauth.command.OauthType;

public interface AuthCodeRequestUrlProvider {

    OauthType oauthServer();

    String getRequestUrl();
}
