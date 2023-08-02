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
    COMPANY_IS_NULL(HttpStatus.BAD_REQUEST, "OM001", "사용자의 회사 정보를 입력해주세요."),
    OAUTH_REQUEST_URL_PROVIDER_IS_WRONG(HttpStatus.BAD_REQUEST, "OA001", "redirect 할 url 이 조회되지 않는 잘못된 소셜 타입입니다."),
    OAUTH_INFORMATION_CLIENT_IS_WRONG(HttpStatus.BAD_REQUEST, "OA002", " 소셜 계정 정보를 조회할 수 없는 잘못된 소셜 타입입니다."),
    OAUTH_AUTHORIZATION_VALUE_IS_NULL(HttpStatus.BAD_REQUEST, "OA003", "Authorization 값을 입력해주세요."),
    OAUTH_AUTHORIZATION_BEARER_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "OA004", "Authorization 값을 Bearer 타입으로 입력해주세요."),
    OAUTH_EMAIL_IS_WRONG(HttpStatus.BAD_REQUEST, "OA005", "조회되지 않는 잘못된 사용자의 이메일입니다."),
    JWT_SIGNATURE_IS_WRONG(HttpStatus.BAD_REQUEST, "JW001", "시그니처가 다른 잘못된 JWT 입니다.");

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
