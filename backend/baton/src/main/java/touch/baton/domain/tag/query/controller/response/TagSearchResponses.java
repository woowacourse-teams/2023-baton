package touch.baton.domain.tag.query.controller.response;

import touch.baton.domain.tag.command.Tag;

import java.util.List;

public record TagSearchResponses() {

    public record Detail(List<TagSearchResponse.TagResponse> data) {

        public static Detail from(final List<Tag> tags) {
            final List<TagSearchResponse.TagResponse> response = tags.stream()
                    .map(TagSearchResponse.TagResponse::from)
                    .toList();

            return new Detail(response);
        }
    }
}
