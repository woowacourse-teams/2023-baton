package touch.baton.domain.common.request;

import touch.baton.domain.common.exception.validator.ValidNotNull;

import static touch.baton.domain.common.exception.ClientErrorCode.INVALID_QUERY_STRING_FORMAT;

public record PageParams(Long cursor, @ValidNotNull(clientErrorCode = INVALID_QUERY_STRING_FORMAT) Integer limit) {

    private static final int ADDITIONAL_QUERY_DATA_COUNT = 1;

    public Integer getLimitForQuery() {
        return limit + ADDITIONAL_QUERY_DATA_COUNT;
    }
}
