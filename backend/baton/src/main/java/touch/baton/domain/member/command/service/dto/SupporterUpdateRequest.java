package touch.baton.domain.member.command.service.dto;

import touch.baton.domain.common.exception.validator.ValidNotNull;

import java.util.List;

import static touch.baton.domain.common.exception.ClientErrorCode.*;

public record SupporterUpdateRequest(@ValidNotNull(clientErrorCode = NAME_IS_NULL)
                                     String name,
                                     @ValidNotNull(clientErrorCode = COMPANY_IS_NULL)
                                     String company,
                                     @ValidNotNull(clientErrorCode = SUPPORTER_INTRODUCTION_IS_NULL)
                                     String introduction,
                                     @ValidNotNull(clientErrorCode = SUPPORTER_TECHNICAL_TAGS_ARE_NULL)
                                     List<String> technicalTags
) {
}
