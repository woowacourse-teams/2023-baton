package touch.baton.domain.supporter.service.dto;

import touch.baton.domain.common.exception.validator.ValidNotNull;

import java.util.List;

import static touch.baton.domain.common.exception.ClientErrorCode.*;

public record SupporterUpdateRequest(@ValidNotNull(clientErrorCode = NAME_IS_NULL)
                                     String name,
                                     @ValidNotNull(clientErrorCode = COMPANY_IS_NULL)
                                     String company,
                                     String introduction,
                                     @ValidNotNull(clientErrorCode = SUPPORTER_TECHNICAL_TAGS_ARE_NULL)
                                     List<String> technicalTags) {
}
