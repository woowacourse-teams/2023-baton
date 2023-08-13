package touch.baton.domain.runnerpost.vo;

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

    public boolean isNotSameAsNotStarted() {
        return this != NOT_STARTED;
    }
}
