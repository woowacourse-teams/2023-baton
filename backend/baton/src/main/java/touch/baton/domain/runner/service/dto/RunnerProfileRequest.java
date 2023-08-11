package touch.baton.domain.runner.service.dto;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.validator.ValidNotNull;

import java.util.List;

public record RunnerProfileRequest(@ValidNotNull(clientErrorCode = ClientErrorCode.NAME_IS_NULL)
                                   String name,
                                   @ValidNotNull(clientErrorCode = ClientErrorCode.COMPANY_IS_NULL)
                                   String company,
                                   String introduction,
                                   @ValidNotNull(clientErrorCode = ClientErrorCode.RUNNER_TECHNICAL_TAGS_ARE_NULL)
                                   List<String> technicalTags
) {
}
