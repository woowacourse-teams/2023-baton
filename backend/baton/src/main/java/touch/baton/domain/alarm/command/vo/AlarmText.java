package touch.baton.domain.alarm.command.vo;

public enum AlarmText {

    TITLE_RUNNER_POST_APPLICANT("서포터의 제안이 왔습니다."),
    TITLE_RUNNER_POST_ASSIGN_SUPPORTER("코드 리뷰 매칭이 완료되었습니다."),
    TITLE_RUNNER_POST_REVIEW_STATUS_DONE("코드 리뷰 상태가 완료로 변경되었습니다."),

    MESSAGE_REFERENCED_BY_RUNNER_POST("관련 게시글 - %s");

    private final String text;

    AlarmText(final String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
