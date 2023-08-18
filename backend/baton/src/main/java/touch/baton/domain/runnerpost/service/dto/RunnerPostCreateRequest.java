package touch.baton.domain.runnerpost.service.dto;

import touch.baton.domain.common.exception.validator.ValidNotNull;
import touch.baton.domain.runnerpost.exception.validator.ValidFuture;
import touch.baton.domain.runnerpost.exception.validator.ValidMaxLength;
import touch.baton.domain.runnerpost.exception.validator.ValidNotUrl;

import java.time.LocalDateTime;
import java.util.List;

import static touch.baton.domain.common.exception.ClientErrorCode.CONTENTS_ARE_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.CONTENTS_OVERFLOW;
import static touch.baton.domain.common.exception.ClientErrorCode.DEADLINE_IS_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.PAST_DEADLINE;
import static touch.baton.domain.common.exception.ClientErrorCode.PULL_REQUEST_URL_IS_NOT_URL;
import static touch.baton.domain.common.exception.ClientErrorCode.PULL_REQUEST_URL_IS_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.TAGS_ARE_NULL;
import static touch.baton.domain.common.exception.ClientErrorCode.TITLE_IS_NULL;

public record RunnerPostCreateRequest(@ValidNotNull(clientErrorCode = TITLE_IS_NULL)
                                      String title,
                                      @ValidNotNull(clientErrorCode = TAGS_ARE_NULL)
                                      List<String> tags,
                                      @ValidNotNull(clientErrorCode = PULL_REQUEST_URL_IS_NULL)
                                      @ValidNotUrl(clientErrorCode = PULL_REQUEST_URL_IS_NOT_URL)
                                      String pullRequestUrl,
                                      @ValidNotNull(clientErrorCode = DEADLINE_IS_NULL)
                                      @ValidFuture(clientErrorCode = PAST_DEADLINE)
                                      LocalDateTime deadline,
                                      @ValidNotNull(clientErrorCode = CONTENTS_ARE_NULL)
                                      @ValidMaxLength(clientErrorCode = CONTENTS_OVERFLOW, max = 1000)
                                      String contents
) {
}
