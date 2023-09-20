package touch.baton.infra.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;
import touch.baton.domain.member.service.dto.GithubBranchManageable;
import touch.baton.infra.exception.InfraException;
import touch.baton.infra.github.request.CreateBranchRequest;
import touch.baton.infra.github.response.ReadBranchInfoResponse;

@Profile("!test")
@Component
public class GithubBranchManager implements GithubBranchManageable {

    private static final String GITHUB_API_URL = "https://api.github.com/repos/baton-mission/";
    private static final String CREATE_BRANCH_API_POSTFIX = "/git/refs";
    private static final String NEW_BRANCH_HEAD_PREFIX = "refs/heads/";
    private static final String READ_BRANCH_API_POSTFIX = "/git/refs/heads/main";
    private static final String DUPLICATED_BRANCH_ERROR_MESSAGE = "Reference already exists";

    private final String token;

    public GithubBranchManager(@Value("${github.personal_access_token}") final String token) {
        this.token = token;
    }

    @Override
    public void createBranch(final String repoName, final String newBranchName) {
        final RestTemplate restTemplate = new RestTemplate();
        final ReadBranchInfoResponse branchInfoResponse = readMainBranch(repoName);

        final HttpHeaders httpHeaders = setBearerAuth();
        final String requestUrl = GITHUB_API_URL + repoName + CREATE_BRANCH_API_POSTFIX;
        final String ref = NEW_BRANCH_HEAD_PREFIX + newBranchName;
        final String sha = branchInfoResponse.object().sha();
        final CreateBranchRequest createBranchRequest = new CreateBranchRequest(ref, sha);
        final HttpEntity<CreateBranchRequest> entity = new HttpEntity<>(createBranchRequest, httpHeaders);
        try {
            restTemplate.exchange(requestUrl, HttpMethod.POST, entity, String.class);
        } catch (HttpStatusCodeException e) {
            handleCreateBranch(e.getResponseBodyAsString());
            throw new InfraException("브랜치 추가가 올바르게 이루어지지 않았습니다. github 응답: " + e.getResponseBodyAsString());
        }
    }

    private HttpHeaders setBearerAuth() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    private void handleCreateBranch(final String errorBodyMessage) {
        if (errorBodyMessage.contains(DUPLICATED_BRANCH_ERROR_MESSAGE)) {
            throw new ClientRequestException(ClientErrorCode.DUPLICATED_BRANCH_NAME);
        }
    }

    private ReadBranchInfoResponse readMainBranch(final String repoName) {
        final RestTemplate restTemplate = new RestTemplate();
        final String requestUrl = GITHUB_API_URL + repoName + READ_BRANCH_API_POSTFIX;
        final HttpHeaders httpHeaders = setBearerAuth();
        final HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        try {
            final ResponseEntity<ReadBranchInfoResponse> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, ReadBranchInfoResponse.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            handleReadBranch(e.getStatusCode());
            throw new InfraException("레포지토리 브랜치 조회가 올바르게 이루어지지 않았습니다. github 응답: " + e.getResponseBodyAsString());
        }
    }

    private void handleReadBranch(final HttpStatusCode statusCode) {
        if (statusCode == HttpStatus.NOT_FOUND) {
            throw new ClientRequestException(ClientErrorCode.REPO_NOT_FOUND);
        }
    }
}
