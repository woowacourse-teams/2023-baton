package touch.baton.domain.runnerpost.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class RunnerPostCreateRequest {

    private static final int CONTENTS_MAX_LENGTH = 1000;
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm";

    private String title;
    private List<String> tags;
    private String pullRequestUrl;
    private String deadline;
    private String contents;

    public RunnerPostCreateRequest(final String title,
                                   final List<String> tags,
                                   final String pullRequestUrl,
                                   final String deadline,
                                   final String contents
    ) {
        validate(title, tags, pullRequestUrl, deadline, contents);
        this.title = title;
        this.tags = tags;
        this.pullRequestUrl = pullRequestUrl;
        this.deadline = deadline;
        this.contents = contents;
    }

    private static void validate(final String title,
                          final List<String> tags,
                          final String pullRequestUrl,
                          final String deadline,
                          final String contents
    ) {
        validateNotNull(title, tags, pullRequestUrl, deadline, contents);
        validateContentsLength(contents);
        validateDeadLine(deadline);
    }

    private static void validateNotNull(final String title,
                                 final List<String> tags,
                                 final String pullRequestUrl,
                                 final String deadline,
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
        if (contents.length() > CONTENTS_MAX_LENGTH) {
            throw new IllegalArgumentException("RP005");
        }
    }

    private static void validateDeadLine(final String deadline) {
        LocalDateTime formattedDeadline = formatDeadline(deadline);
        validateDeadlineNotPassed(formattedDeadline);
    }

    private static LocalDateTime formatDeadline(final String deadline) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        try {
            return LocalDateTime.parse(deadline, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("RP00????");
        }
    }

    private static void validateDeadlineNotPassed(final LocalDateTime deadline) {
        if (deadline.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("RP006");
        }
    }
}
