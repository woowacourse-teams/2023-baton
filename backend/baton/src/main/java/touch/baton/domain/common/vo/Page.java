package touch.baton.domain.common.vo;

import java.util.List;

public record Page<T>(List<T> contents, long total) {
}
