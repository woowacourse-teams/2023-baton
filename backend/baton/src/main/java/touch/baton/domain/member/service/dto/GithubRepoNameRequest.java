package touch.baton.domain.member.service.dto;

import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.validator.ValidNotNull;

public record GithubRepoNameRequest(@ValidNotNull(clientErrorCode = ClientErrorCode.REPO_NAME_IS_NULL) String repoName) {
}
