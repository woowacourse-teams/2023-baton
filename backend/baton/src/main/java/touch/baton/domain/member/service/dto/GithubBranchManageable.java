package touch.baton.domain.member.service.dto;

public interface GithubBranchManageable {

    void createBranch(final String repoName, final String newBranchName);
}
