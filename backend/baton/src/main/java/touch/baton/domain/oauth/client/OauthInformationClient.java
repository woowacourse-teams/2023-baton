package touch.baton.domain.oauth.client;

import touch.baton.domain.oauth.OauthInformation;
import touch.baton.domain.oauth.OauthType;

public interface OauthInformationClient {

    OauthType oauthType();

    OauthInformation fetchInformation(final String authCode);
}
