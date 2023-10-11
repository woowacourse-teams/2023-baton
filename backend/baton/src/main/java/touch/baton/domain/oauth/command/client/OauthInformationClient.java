package touch.baton.domain.oauth.command.client;

import touch.baton.domain.oauth.command.OauthInformation;
import touch.baton.domain.oauth.command.OauthType;

public interface OauthInformationClient {

    OauthType oauthType();

    OauthInformation fetchInformation(final String authCode);
}
