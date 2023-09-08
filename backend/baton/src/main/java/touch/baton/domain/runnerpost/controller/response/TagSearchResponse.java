package touch.baton.domain.runnerpost.controller.response;

public record TagSearchResponse() {

    public record TagResponse(Long id, String tagName) {
        public static TagResponse of(final Long id, final String name) {
            return new TagResponse(id, name);
        }
    }
}
