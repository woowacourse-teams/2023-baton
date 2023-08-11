package touch.baton.domain.runner.service;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.validator.ValidNotNull;

public record RunnerProfileRequest(
        @ValidNotNull(clientErrorCode = ClientErrorCode.RUNNER_PROFILE_NAME_IS_NULL)
        String name,
        String company,
        @ValidNotNull(clientErrorCode = ClientErrorCode.RUNNER_PROFILE_INTRODUCTION_IS_NULL)
        String introduction,
        String[] technicalTags
) {
}
