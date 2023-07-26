package touch.baton.domain.runnerpost.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record RunnerPostCreateRequest(String title,
                                      List<String> tags,
                                      String pullRequestUrl,
                                      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
                                      LocalDateTime deadline,
                                      String contents
) {
}
