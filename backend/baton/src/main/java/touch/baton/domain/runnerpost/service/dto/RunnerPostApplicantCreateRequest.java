package touch.baton.domain.runnerpost.service.dto;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.runnerpost.exception.validator.ValidMaxLength;

public record RunnerPostApplicantCreateRequest(@ValidMaxLength(clientErrorCode = ClientErrorCode.APPLICANT_MESSAGE_IS_OVERFLOW, max = 500)
                                               String message
) {
}
