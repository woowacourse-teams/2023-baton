package touch.baton.domain.runnerpost.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public record RunnerPostUpdateRequest(
        String title,
        List<String> tags,
        String pullRequestUrl,
        LocalDateTime deadline,
        String contents
) {

    private static void validate(final String title,
                          final List<String> tags,
                          final String pullRequestUrl,
                          final LocalDateTime deadline,
                          final String contents
    ) {
        validateNotNull(title, tags, pullRequestUrl, deadline, contents);
        validateContentsLength(contents);
        validateDeadLine(deadline);
    }

    private static void validateNotNull(final String title,
                                 final List<String> tags,
                                 final String pullRequestUrl,
                                 final LocalDateTime deadline,
                                 final String contents
    ) {
        if (Objects.isNull(title)) {
            throw new IllegalArgumentException("RP001");
        }

        if (Objects.isNull(tags)) {
            throw new IllegalArgumentException("RP00???");
        }

        if (Objects.isNull(pullRequestUrl)) {
            throw new IllegalArgumentException("RP002");
        }

        if (Objects.isNull(deadline)) {
            throw new IllegalArgumentException("RP003");
        }

        if (Objects.isNull(contents)) {
            throw new IllegalArgumentException("RP004");
        }
    }

    private static void validateContentsLength(final String contents) {
        if (contents.length() > 1000) {
            throw new IllegalArgumentException("RP005");
        }
    }

    private static void validateDeadLine(final LocalDateTime deadline) {
        validateDeadlineNotPassed(deadline);
    }

    private static void validateDeadlineNotPassed(final LocalDateTime deadline) {
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("RP006");
        }
    }
}
