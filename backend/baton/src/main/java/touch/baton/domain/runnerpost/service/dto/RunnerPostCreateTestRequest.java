package touch.baton.domain.runnerpost.service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record RunnerPostCreateTestRequest(String title,
                                          List<String> tags,
                                          String pullRequestUrl,
                                          LocalDateTime deadline,
                                          String contents,
                                          Long supporterId
) {
}
