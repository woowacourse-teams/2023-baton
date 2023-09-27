package touch.baton.domain.member.command.service;

public interface GithubBranchManageable {

    void createBranch(final String repoName, final String newBranchName);
}
