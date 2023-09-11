package touch.baton.infra.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import touch.baton.infra.github.request.CreateBranchRequest;
import touch.baton.infra.github.response.ReadBranchInfoResponse;

@Service
public class GithubApiService {

    private static final String GITHUB_API_URL = "https://api.github.com/repos/baton-mission";
    private static final String CREATE_BRANCH_API_POSTFIX = "/git/refs";
    private static final String CREATE_BRANCH_API_POSTFIX = "/git/refs";
    private static final String READ_BRANCH_API_POSTFIX = "/git/refs/heads/main";

    @Value("${github.personal_access_token}")
    private String token;

    public void createBranch(final String repoName, final String newBranchName) {
        final ReadBranchInfoResponse branchInfoResponse = readMainBranch(repoName);
        final String commitSha = branchInfoResponse.object().sha();

        // 새 브랜치 생성
        final HttpHeaders httpHeaders = setBearerAuth();
        final String requestUrl = GITHUB_API_URL + repoName + CREATE_BRANCH_API_POSTFIX;
        final String ref = ""
        new CreateBranchRequest()
        String body = String.format("{\"ref\": \"refs/heads/%s\", \"sha\": \"%s\"}", newBranchName, commitSha);
        entity = new HttpEntity<>(body, headers);
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }

    private HttpHeaders setBearerAuth() {
        final HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

    private ReadBranchInfoResponse readMainBranch(final String repoName) {
        final RestTemplate restTemplate = new RestTemplate();
        final String requestUrl =  GITHUB_API_URL + repoName + READ_BRANCH_API_POSTFIX;
        final HttpHeaders httpHeaders = setBearerAuth();
        final HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        return restTemplate.exchange(requestUrl, HttpMethod.GET, entity, ReadBranchInfoResponse.class).getBody();
    }
}
