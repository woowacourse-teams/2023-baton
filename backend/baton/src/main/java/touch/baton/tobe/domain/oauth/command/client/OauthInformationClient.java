package touch.baton.tobe.domain.oauth.command.client;

import touch.baton.tobe.domain.oauth.command.OauthInformation;
import touch.baton.tobe.domain.oauth.command.OauthType;

public interface OauthInformationClient {

    OauthType oauthType();

    OauthInformation fetchInformation(final String authCode);
}
