package touch.baton.tobe.domain.tag.query.controller.response;

import touch.baton.tobe.domain.tag.command.Tag;

public record TagSearchResponse() {

    public record TagResponse(Long id, String tagName) {
        public static TagResponse from(final Tag tag) {
            return new TagResponse(tag.getId(), tag.getTagName().getValue());
        }
    }
}
