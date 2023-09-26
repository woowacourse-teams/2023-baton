package touch.baton.tobe.domain.runnerpost.command.vo;

import static java.util.Locale.ENGLISH;

public enum ReviewStatus {

    NOT_STARTED,
    IN_PROGRESS,
    DONE,
    OVERDUE;

    public static ReviewStatus from(final String name) {
        return ReviewStatus.valueOf(name.toUpperCase(ENGLISH));
    }

    public boolean isSame(final ReviewStatus reviewStatus) {
        return this == reviewStatus;
    }

    public boolean isSameAsNotStarted() {
        return this == NOT_STARTED;
    }

    public boolean isNotSameAsNotStarted() {
        return this != NOT_STARTED;
    }

    public boolean isOverdue() {
        return this == OVERDUE;
    }

    public boolean isNotStarted() {
        return this == NOT_STARTED;
    }
}
