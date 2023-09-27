package touch.baton.domain.runnerpost.command.service.dto;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.validator.ValidNotNull;

public record RunnerPostUpdateRequest() {

    public record SelectSupporter(@ValidNotNull(clientErrorCode = ClientErrorCode.ASSIGN_SUPPORTER_ID_IS_NULL)
                                  Long supporterId
    ) {
    }
}
