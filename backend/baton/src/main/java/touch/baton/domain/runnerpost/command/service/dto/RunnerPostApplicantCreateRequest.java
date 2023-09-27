package touch.baton.domain.runnerpost.command.service.dto;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.runnerpost.command.exception.validator.ValidMaxLength;

public record RunnerPostApplicantCreateRequest(@ValidMaxLength(clientErrorCode = ClientErrorCode.APPLICANT_MESSAGE_IS_OVERFLOW, max = 500)
                                               String message
) {
}
