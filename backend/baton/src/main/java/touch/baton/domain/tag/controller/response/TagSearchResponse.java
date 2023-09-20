package touch.baton.domain.tag.controller.response;

import touch.baton.domain.tag.Tag;

public record TagSearchResponse() {

    public record TagResponse(Long id, String tagName) {
        public static TagResponse from(final Tag tag) {
            return new TagResponse(tag.getId(), tag.getTagName().getValue());
        }
    }
}
