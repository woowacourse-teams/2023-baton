package touch.baton.domain.common.response;

import touch.baton.domain.runnerpost.query.service.dto.PageParams;

import java.util.List;

public record PageResponse<T>(List<T> data, PageInfo pageInfo) {

    private static final int NEXT_INDEX = 1;

    public static <T> PageResponse<T> of(final List<T> responses, final PageParams pageParams) {
        final int limit = pageParams.limit();
        if (isLastPage(responses, limit)) {
            return new PageResponse<>(responses, PageInfo.Last());
        }
        return new PageResponse<>(responses.subList(0, limit + NEXT_INDEX), PageInfo.Normal());
    }

    public record PageInfo(boolean isLast) {

        public static PageInfo Last() {
            return new PageInfo(true);
        }

        public static PageInfo Normal() {
            return new PageInfo(false);
        }
    }

    private static <T> boolean isLastPage(final List<T> responses, final int limit) {
        return responses.size() <= limit;
    }
}
