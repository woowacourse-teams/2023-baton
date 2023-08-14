package touch.baton.domain.common.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(List<T> data,
                              PageInfo pageInfo
) {

    public static <T> PageResponse<T> from(final Page<T> page) {
        return new PageResponse<>(page.getContent(), PageInfo.from(page));
    }

    public record PageInfo(boolean isFirst,
                           boolean isLast,
                           boolean hasNext,
                           int totalPages,
                           long totalElements,
                           int currentPage,
                           int currentSize
    ) {

        private static final int START_PAGE_NUMBER = 1;

        public static <T> PageInfo from(final Page<T> page) {
            return new PageInfo(
                    page.isFirst(),
                    page.isLast(),
                    page.hasNext(),
                    page.getTotalPages(),
                    page.getTotalElements(),
                    page.getNumber() + START_PAGE_NUMBER,
                    page.getSize()
            );
        }
    }
}
