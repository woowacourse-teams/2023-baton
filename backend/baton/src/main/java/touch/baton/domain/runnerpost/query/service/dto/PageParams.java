package touch.baton.domain.runnerpost.query.service.dto;

import touch.baton.domain.common.exception.validator.ValidNotNull;

import static touch.baton.domain.common.exception.ClientErrorCode.INVALID_QUERY_STRING_FORMAT;

public record PageParams(Long cursor, @ValidNotNull(clientErrorCode = INVALID_QUERY_STRING_FORMAT) Integer limit) {
}
