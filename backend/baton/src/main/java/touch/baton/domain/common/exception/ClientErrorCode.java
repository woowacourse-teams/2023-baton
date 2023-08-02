package touch.baton.domain.common.exception;

import org.springframework.http.HttpStatus;

public enum ClientErrorCode {

    TITLE_IS_NULL(HttpStatus.BAD_REQUEST, "RP001", "제목을 입력해주세요."),
    PULL_REQUEST_URL_IS_NULL(HttpStatus.BAD_REQUEST, "RP002", "PR 주소를 입력해주세요."),
    DEADLINE_IS_NULL(HttpStatus.BAD_REQUEST, "RP003", "마감일을 입력해주세요."),
    CONTENTS_ARE_NULL(HttpStatus.BAD_REQUEST, "RP004", "내용을 입력해주세요."),
    CONTENTS_OVERFLOW(HttpStatus.BAD_REQUEST, "RP005", "내용은 1000자 까지 입력해주세요."),
    PAST_DEADLINE(HttpStatus.BAD_REQUEST, "RP006", "마감일은 오늘보다 과거일 수 없습니다."),
    CONTENTS_NOT_FOUND(HttpStatus.NOT_FOUND, "RP007", "존재하지 않는 게시물입니다."),
    TAGS_ARE_NULL(HttpStatus.BAD_REQUEST, "RP008", "태그 목록을 빈 값이라도 입력해주세요."),
    REVIEW_TYPE_IS_NULL(HttpStatus.BAD_REQUEST, "FB001", "만족도를 입력해주세요."),
    SUPPORTER_ID_IS_NULL(HttpStatus.BAD_REQUEST, "FB002", "서포터 식별자를 입력해주세요."),
    RUNNER_ID_IS_NULL(HttpStatus.BAD_REQUEST, "FB003", "러너 식별자를 입력해주세요."),
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;

    ClientErrorCode(final HttpStatus httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.errorCode = code;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
