package touch.baton.domain.common.response;

import touch.baton.domain.runnerpost.command.RunnerPost;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T>(List<T> data,
                              PageInfo pageInfo
) {

    public static <T> PageResponse<T> of(final List<RunnerPost> runnerPosts,
                                         final Function<RunnerPost, T> mapper,
                                         final int limit) {
        final List<T> data = runnerPosts.stream()
                .map(mapper)
                .toList();
        final PageInfo pageInfo = PageInfo.from(limit, data.size());
        if (pageInfo.isLast) {
            return new PageResponse<>(data, pageInfo);
        }
        return new PageResponse<>(data.subList(0, limit), pageInfo);
    }

    public record PageInfo(boolean isLast) {

        public static PageInfo from(final int limit, final int dataSize) {
            if (dataSize > limit) {
                return new PageInfo(false);
            }
            return new PageInfo(true);
        }
    }
}
