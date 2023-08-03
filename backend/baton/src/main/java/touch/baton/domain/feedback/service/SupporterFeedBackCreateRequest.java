package touch.baton.domain.feedback.service;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.validator.ValidNotNull;

import java.util.List;

public record SupporterFeedBackCreateRequest(@ValidNotNull(clientErrorCode = ClientErrorCode.REVIEW_TYPE_IS_NULL)
                                             String reviewType,
                                             List<String> descriptions,
                                             @ValidNotNull(clientErrorCode = ClientErrorCode.SUPPORTER_ID_IS_NULL)
                                             Long supporterId,
                                             @ValidNotNull(clientErrorCode = ClientErrorCode.RUNNER_ID_IS_NULL)
                                             Long runnerPostId
) {
}
