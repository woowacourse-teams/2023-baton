package touch.baton.infra.github.response;

public record ReadBranchInfoResponse(String ref, String nodeId, String url, ReadLastCommitInfoResponse object) {
}
