package touch.baton.domain.runnerpost.service.dto;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.validator.ValidNotNull;
import touch.baton.domain.runnerpost.exception.validator.ValidFuture;
import touch.baton.domain.runnerpost.exception.validator.ValidMaxLength;
import touch.baton.domain.runnerpost.exception.validator.ValidNotUrl;

import java.time.LocalDateTime;
import java.util.List;

public record RunnerPostCreateRequest(@ValidNotNull(clientErrorCode = ClientErrorCode.TITLE_IS_NULL)
                                      String title,
                                      @ValidNotNull(clientErrorCode = ClientErrorCode.TAGS_ARE_NULL)
                                      List<String> tags,
                                      @ValidNotNull(clientErrorCode = ClientErrorCode.PULL_REQUEST_URL_IS_NULL)
                                      @ValidNotUrl(clientErrorCode = ClientErrorCode.PULL_REQUEST_URL_IS_NOT_URL)
                                      String pullRequestUrl,
                                      @ValidNotNull(clientErrorCode = ClientErrorCode.DEADLINE_IS_NULL)
                                      @ValidFuture(clientErrorCode = ClientErrorCode.PAST_DEADLINE)
                                      LocalDateTime deadline,
                                      @ValidNotNull(clientErrorCode = ClientErrorCode.CONTENTS_ARE_NULL)
                                      @ValidMaxLength(clientErrorCode = ClientErrorCode.CONTENTS_OVERFLOW, max = 1000)
                                      String contents
) {
}
