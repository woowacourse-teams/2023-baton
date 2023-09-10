package touch.baton.infra.github.response;

public record ReadLastCommitInfoResponse(String sha, String type, String url) {
}
