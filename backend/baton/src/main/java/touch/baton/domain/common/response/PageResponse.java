package touch.baton.domain.common.response;

import touch.baton.domain.common.request.PageParams;

import java.util.List;

public record PageResponse<T extends IdExtractable>(List<T> data, PageInfo pageInfo) {

    public static <T extends IdExtractable> PageResponse<T> of(final List<T> responses, final PageParams pageParams) {
        final int limit = pageParams.limit();
        if (isLastPage(responses, limit)) {
            return new PageResponse<>(responses, PageInfo.last());
        }
        final List<T> limitResponses = responses.subList(0, pageParams.getLimitForQuery());
        return new PageResponse<>(limitResponses, PageInfo.normal(getLastElementId(limitResponses)));
    }

    public record PageInfo(boolean isLast, Long nextCursor) {

        public static PageInfo last() {
            return new PageInfo(true, null);
        }

        public static PageInfo normal(final Long nextCursor) {
            return new PageInfo(false, nextCursor);
        }
    }

    private static <T> boolean isLastPage(final List<T> responses, final int limit) {
        return responses.size() <= limit;
    }

    private static <T extends IdExtractable> Long getLastElementId(final List<T> responses) {
        final int lastIndex = responses.size() - 1;
        return responses.get(lastIndex).extractId();
    }
}
