package touch.baton.domain.runnerpost.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record RunnerPostCreateRequest(String title,
                                      List<String> tags,
                                      String pullRequestUrl,
                                      @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
                                      LocalDateTime deadline,
                                      String contents
) {
}
