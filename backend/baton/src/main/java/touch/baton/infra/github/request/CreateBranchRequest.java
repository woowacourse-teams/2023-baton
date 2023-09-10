package touch.baton.infra.github.request;

public record CreateBranchRequest(String ref, String sha) {
}
