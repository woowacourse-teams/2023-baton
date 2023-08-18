package touch.baton.domain.common.response;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MINUTES;

public record ServerErrorResponse(String message, LocalDateTime createdAt) {

    public static ServerErrorResponse from(final Exception e) {
        return new ServerErrorResponse(e.getMessage(), LocalDateTime.now().truncatedTo(MINUTES));
    }

    public static ServerErrorResponse unExpected() {
        return new ServerErrorResponse("예상치 못한 에러입니다. 관리자에게 문의해주세요.", LocalDateTime.now().truncatedTo(MINUTES));
    }
}
