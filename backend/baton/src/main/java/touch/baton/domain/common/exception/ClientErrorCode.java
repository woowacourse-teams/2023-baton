package touch.baton.domain.common.exception;

import org.springframework.http.HttpStatus;

public enum ClientErrorCode {

    TITLE_IS_NULL(HttpStatus.BAD_REQUEST, "RP001", "제목을 입력해주세요."),
    PULL_REQUEST_URL_IS_NULL(HttpStatus.BAD_REQUEST, "RP002", "PR 주소를 입력해주세요."),
    DEADLINE_IS_NULL(HttpStatus.BAD_REQUEST, "RP003", "마감일을 입력해주세요."),
    IMPLEMENTED_CONTENTS_ARE_NULL(HttpStatus.BAD_REQUEST, "RP004", "구현 내용을 입력해주세요."),
    CONTENTS_OVERFLOW(HttpStatus.BAD_REQUEST, "RP005", "내용은 1000자 까지 입력해주세요."),
    PAST_DEADLINE(HttpStatus.BAD_REQUEST, "RP006", "마감일은 오늘보다 과거일 수 없습니다."),
    RUNNER_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "RP007", "존재하지 않는 게시물입니다."),
    TAGS_ARE_NULL(HttpStatus.BAD_REQUEST, "RP008", "태그 목록을 빈 값이라도 입력해주세요."),
    ASSIGN_SUPPORTER_ID_IS_NULL(HttpStatus.BAD_REQUEST, "RP009", "선택한 서포터의 식별자를 입력해주세요."),
    APPLICANT_MESSAGE_IS_OVERFLOW(HttpStatus.BAD_REQUEST, "RP010", "서포터 지원 메시지는 500자 까지 입력해주세요."),
    PULL_REQUEST_URL_IS_NOT_URL(HttpStatus.BAD_REQUEST, "RP011", "올바른 PR 주소를 입력해주세요."),
    CURIOUS_CONTENTS_ARE_NULL(HttpStatus.BAD_REQUEST, "RP012", "궁금한 내용을 입력해주세요."),
    POSTSCRIPT_CONTENTS_ARE_NULL(HttpStatus.BAD_REQUEST, "RP013", "참고 사항을 입력해주세요."),

    REVIEW_TYPE_IS_NULL(HttpStatus.BAD_REQUEST, "FB001", "만족도를 입력해주세요."),
    SUPPORTER_ID_IS_NULL(HttpStatus.BAD_REQUEST, "FB002", "서포터 식별자를 입력해주세요."),
    RUNNER_ID_IS_NULL(HttpStatus.BAD_REQUEST, "FB003", "러너 식별자를 입력해주세요."),

    NAME_IS_NULL(HttpStatus.BAD_REQUEST, "MB001", "사용자의 이름을 입력해주세요."),
    COMPANY_IS_NULL(HttpStatus.BAD_REQUEST, "MB002", "사용자의 회사 정보를 입력해주세요."),
    SUPPORTER_TECHNICAL_TAGS_ARE_NULL(HttpStatus.BAD_REQUEST, "MB003", "서포터 기술 태그 목록을 빈 값이라도 입력해주세요."),
    RUNNER_TECHNICAL_TAGS_ARE_NULL(HttpStatus.BAD_REQUEST, "MB004", "러너 기술 태그 목록을 빈 값이라도 입력해주세요."),
    RUNNER_INTRODUCTION_IS_NULL(HttpStatus.BAD_REQUEST, "MB005", "러너의 정보를 입력해주세요."),
    SUPPORTER_INTRODUCTION_IS_NULL(HttpStatus.BAD_REQUEST, "MB006", "서포터의 정보를 입력해주세요."),

    OAUTH_REQUEST_URL_PROVIDER_IS_WRONG(HttpStatus.UNAUTHORIZED, "OA001", "redirect 할 url 이 조회되지 않는 잘못된 소셜 타입입니다."),
    OAUTH_INFORMATION_CLIENT_IS_WRONG(HttpStatus.UNAUTHORIZED, "OA002", " 소셜 계정 정보를 조회할 수 없는 잘못된 소셜 타입입니다."),
    OAUTH_AUTHORIZATION_VALUE_IS_NULL(HttpStatus.UNAUTHORIZED, "OA003", "Authorization 값을 입력해주세요."),
    OAUTH_AUTHORIZATION_BEARER_TYPE_NOT_FOUND(HttpStatus.UNAUTHORIZED, "OA004", "Authorization 값을 Bearer 타입으로 입력해주세요."),

    JWT_SIGNATURE_IS_WRONG(HttpStatus.UNAUTHORIZED, "JW001", "시그니처가 다른 잘못된 JWT 입니다."),
    JWT_FORM_IS_WRONG(HttpStatus.UNAUTHORIZED, "JW002", "잘못 생성된 JWT 로 디코딩 할 수 없습니다."),
    JWT_CLAIM_IS_WRONG(HttpStatus.UNAUTHORIZED, "JW003", "JWT 에 기대한 정보를 모두 포함하고 있지 않습니다."),
    JWT_CLAIM_SOCIAL_ID_IS_WRONG(HttpStatus.UNAUTHORIZED, "JW004", "사용자의 잘못된 소셜 아이디(SocialId) 정보를 가진 JWT 입니다."),
    JWT_CLAIM_IS_ALREADY_EXPIRED(HttpStatus.UNAUTHORIZED, "JW005", "기간이 만료된 JWT 입니다."),
    JWT_CLAIM_IS_NOT_EXPIRED(HttpStatus.UNAUTHORIZED, "JW006", "만료되지 않은 JWT로 새로운 JWT를 발급 받을 수 없습니다."),
    REFRESH_TOKEN_IS_NOT_FOUND(HttpStatus.UNAUTHORIZED, "JW007", "해당 사용자의 Refresh Token이 존재하지 않습니다."),
    ACCESS_TOKEN_AND_REFRESH_TOKEN_HAVE_DIFFERENT_OWNER(HttpStatus.UNAUTHORIZED, "JW008", "Access Token 과 Refresh Token 의 주인이 다릅니다."),
    REFRESH_TOKEN_IS_ALREADY_EXPIRED(HttpStatus.UNAUTHORIZED, "JW009", "기간이 만료된 Refresh Token 입니다."),

    DUPLICATED_BRANCH_NAME(HttpStatus.BAD_REQUEST, "BR001", "이미 존재하는 이름의 브랜치입니다."),
    REPO_NAME_IS_NULL(HttpStatus.BAD_REQUEST, "BR002", "레포지토리 이름을 입력해주세요."),
    REPO_NOT_FOUND(HttpStatus.NOT_FOUND, "BR003", "레포지토리를 찾을 수 없습니다.");

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
