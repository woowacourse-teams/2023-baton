package touch.baton.tobe.domain.member.command.service;

public interface GithubBranchManageable {

    void createBranch(final String repoName, final String newBranchName);
}
