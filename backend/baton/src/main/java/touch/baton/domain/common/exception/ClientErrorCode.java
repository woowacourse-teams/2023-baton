package touch.baton.domain.common.exception;

import org.springframework.http.HttpStatus;

public enum ClientErrorCode {

    TITLE_IS_NULL(HttpStatus.BAD_REQUEST, "RP001", "제목을 입력해주세요."),
    PULL_REQUEST_URL_IS_NULL(HttpStatus.BAD_REQUEST, "RP002", "PR 주소를 입력해주세요."),
    DEADLINE_IS_NULL(HttpStatus.BAD_REQUEST, "RP003", "마감일을 입력해주세요."),
    CONTENTS_ARE_NULL(HttpStatus.BAD_REQUEST, "RP004", "내용을 입력해주세요."),
    CONTENTS_OVERFLOW(HttpStatus.BAD_REQUEST, "RP005", "내용은 1000자 까지 입력해주세요."),
    PAST_DEADLINE(HttpStatus.BAD_REQUEST, "RP006", "마감일은 오늘보다 과거일 수 없습니다."),
    RUNNER_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "RP007", "존재하지 않는 게시물입니다."),
    TAGS_ARE_NULL(HttpStatus.BAD_REQUEST, "RP008", "태그 목록을 빈 값이라도 입력해주세요."),

    REVIEW_TYPE_IS_NULL(HttpStatus.BAD_REQUEST, "FB001", "만족도를 입력해주세요."),
    SUPPORTER_ID_IS_NULL(HttpStatus.BAD_REQUEST, "FB002", "서포터 식별자를 입력해주세요."),
    RUNNER_ID_IS_NULL(HttpStatus.BAD_REQUEST, "FB003", "러너 식별자를 입력해주세요."),

    NAME_IS_NULL(HttpStatus.BAD_REQUEST, "MB001", "사용자의 이름을 입력해주세요."),
    COMPANY_IS_NULL(HttpStatus.BAD_REQUEST, "MB002", "사용자의 회사 정보를 입력해주세요."),
    SUPPORTER_TECHNICAL_TAGS_ARE_NULL(HttpStatus.BAD_REQUEST, "MB003", "서포터 기술 태그 목록을 빈 값이라도 입력해주세요."),
    RUNNER_TECHNICAL_TAGS_ARE_NULL(HttpStatus.BAD_REQUEST, "MB004", "러너 기술 태그 목록을 빈 값이라도 입력해주세요."),

    OAUTH_REQUEST_URL_PROVIDER_IS_WRONG(HttpStatus.UNAUTHORIZED, "OA001", "redirect 할 url 이 조회되지 않는 잘못된 소셜 타입입니다."),
    OAUTH_INFORMATION_CLIENT_IS_WRONG(HttpStatus.UNAUTHORIZED, "OA002", " 소셜 계정 정보를 조회할 수 없는 잘못된 소셜 타입입니다."),
    OAUTH_AUTHORIZATION_VALUE_IS_NULL(HttpStatus.UNAUTHORIZED, "OA003", "Authorization 값을 입력해주세요."),
    OAUTH_AUTHORIZATION_BEARER_TYPE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "OA004", "Authorization 값을 Bearer 타입으로 입력해주세요."),

    JWT_SIGNATURE_IS_WRONG(HttpStatus.UNAUTHORIZED, "JW001", "시그니처가 다른 잘못된 JWT 입니다."),
    JWT_FORM_IS_WRONG(HttpStatus.UNAUTHORIZED, "JW002", "잘못 생성된 JWT 로 디코딩 할 수 없습니다."),
    JWT_CLAIM_IS_WRONG(HttpStatus.UNAUTHORIZED, "JW003", "JWT 에 기대한 정보를 모두 포함하고 있지 않습니다."),
    JWT_CLAIM_SOCIAL_ID_IS_WRONG(HttpStatus.UNAUTHORIZED, "JW004", "사용자의 잘못된 소셜 아이디(SocialId) 정보를 가진 JWT 입니다.");

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
