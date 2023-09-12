package touch.baton.domain.tag.controller.response;

import java.util.List;

public record TagSearchResponses() {

    public record Detail(List<TagSearchResponse.TagResponse> data) {
        public static Detail from(final List<TagSearchResponse.TagResponse> data) {
            return new Detail(data);
        }
    }
}
