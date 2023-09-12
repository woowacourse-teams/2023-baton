package touch.baton.domain.tag.controller.response;

import touch.baton.domain.tag.Tag;

public record TagSearchResponse() {

    public record TagResponse(Long id, String tagName) {
        public static TagResponse of(final Tag tag) {
            return new TagResponse(tag.getId(), tag.getTagReducedName().getValue());
        }
    }
}
