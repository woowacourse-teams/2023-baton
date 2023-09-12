package touch.baton.infra.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.common.exception.ClientRequestException;
import touch.baton.domain.member.service.dto.GithubBranchService;
import touch.baton.infra.exception.InfraException;
import touch.baton.infra.github.request.CreateBranchRequest;
import touch.baton.infra.github.response.ReadBranchInfoResponse;

import java.util.Objects;

@Profile("!test")
@Service
public class GithubBranchManager implements GithubBranchService {

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
        final ResponseEntity<String> response = restTemplate.exchange(requestUrl, HttpMethod.POST, entity, String.class);
        validateCreateResponseMessage(response);
    }

    private HttpHeaders setBearerAuth() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    private void validateCreateResponseMessage(final ResponseEntity<String> response) {
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return;
        }
        if (Objects.requireNonNull(response.getBody()).contains(DUPLICATED_BRANCH_ERROR_MESSAGE)) {
            throw new ClientRequestException(ClientErrorCode.DUPLICATED_BRANCH_NAME);
        }
        throw new InfraException("브랜치 추가가 올바르게 이루어지지 않았습니다. github 응답: " + response.getBody());
    }

    private ReadBranchInfoResponse readMainBranch(final String repoName) {
        final RestTemplate restTemplate = new RestTemplate();
        final String requestUrl = GITHUB_API_URL + repoName + READ_BRANCH_API_POSTFIX;
        final HttpHeaders httpHeaders = setBearerAuth();
        final HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        final ResponseEntity<ReadBranchInfoResponse> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, ReadBranchInfoResponse.class);
        validateReadBranchInfoResponse(response);
        return response.getBody();
    }

    private void validateReadBranchInfoResponse(final ResponseEntity<ReadBranchInfoResponse> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            return;
        }
        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new ClientRequestException(ClientErrorCode.REPO_NOT_FOUND);
        }
        throw new InfraException("GITHUB API 내부 오류입니다. api 변경 사항이 있는지 github api docs를 확인해보세요.");
    }
}
