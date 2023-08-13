package touch.baton.domain.runnerpost.vo;

public enum ReviewStatus {

    NOT_STARTED,
    IN_PROGRESS,
    DONE;

    public boolean isNotStarted() {
        return this == NOT_STARTED;
    }
}
