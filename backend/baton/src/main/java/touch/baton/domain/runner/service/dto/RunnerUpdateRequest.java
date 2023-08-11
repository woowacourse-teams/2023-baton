package touch.baton.domain.runner.service.dto;

import touch.baton.domain.common.exception.validator.ValidNotNull;

import java.util.List;

import static touch.baton.domain.common.exception.ClientErrorCode.COMPANY_IS_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.NAME_IS_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.RUNNER_TECHNICAL_TAGS_ARE_NULL;

public record RunnerUpdateRequest(@ValidNotNull(clientErrorCode = NAME_IS_NULL)
                                  String name,
                                  @ValidNotNull(clientErrorCode = COMPANY_IS_NULL)
                                  String company,
                                  String introduction,
                                  @ValidNotNull(clientErrorCode = RUNNER_TECHNICAL_TAGS_ARE_NULL)
                                  List<String> technicalTags) {
}
