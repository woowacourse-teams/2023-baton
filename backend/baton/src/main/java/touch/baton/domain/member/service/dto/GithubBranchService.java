package touch.baton.domain.member.service.dto;

public interface GithubBranchService {

    void createBranch(final String repoName, final String newBranchName);
}
