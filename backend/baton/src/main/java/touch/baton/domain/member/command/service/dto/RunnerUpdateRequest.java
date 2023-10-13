package touch.baton.domain.member.command.service.dto;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.validator.ValidNotNull;

import java.util.List;

public record RunnerUpdateRequest(@ValidNotNull(clientErrorCode = ClientErrorCode.NAME_IS_NULL)
                                  String name,
                                  @ValidNotNull(clientErrorCode = ClientErrorCode.COMPANY_IS_NULL)
                                  String company,
                                  @ValidNotNull(clientErrorCode = ClientErrorCode.RUNNER_INTRODUCTION_IS_NULL)
                                  String introduction,
                                  @ValidNotNull(clientErrorCode = ClientErrorCode.RUNNER_TECHNICAL_TAGS_ARE_NULL)
                                  List<String> technicalTags
) {
}
