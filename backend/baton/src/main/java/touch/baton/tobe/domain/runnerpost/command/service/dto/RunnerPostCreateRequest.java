package touch.baton.tobe.domain.runnerpost.command.service.dto;

import touch.baton.domain.common.exception.validator.ValidNotNull;
import touch.baton.tobe.domain.runnerpost.command.exception.validator.ValidFuture;
import touch.baton.tobe.domain.runnerpost.command.exception.validator.ValidMaxLength;
import touch.baton.tobe.domain.runnerpost.command.exception.validator.ValidNotUrl;

import java.time.LocalDateTime;
import java.util.List;

import static touch.baton.domain.common.exception.ClientErrorCode.*;

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
                                      @ValidNotNull(clientErrorCode = IMPLEMENTED_CONTENTS_ARE_NULL)
                                      @ValidMaxLength(clientErrorCode = CONTENTS_OVERFLOW, max = 1000)
                                      String implementedContents,
                                      @ValidNotNull(clientErrorCode = CURIOUS_CONTENTS_ARE_NULL)
                                      @ValidMaxLength(clientErrorCode = CONTENTS_OVERFLOW, max = 1000)
                                      String curiousContents,
                                      @ValidNotNull(clientErrorCode = POSTSCRIPT_CONTENTS_ARE_NULL)
                                      @ValidMaxLength(clientErrorCode = CONTENTS_OVERFLOW, max = 1000)
                                      String postscriptContents
) {
}
