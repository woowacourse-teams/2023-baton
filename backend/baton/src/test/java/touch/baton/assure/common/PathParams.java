package touch.baton.assure.common;

import java.util.Map;

public class PathParams {

    private final Map<String, Object> values;

    public PathParams(final Map<String, Object> values) {
        this.values = values;
    }

    public Map<String, Object> getValues() {
        return values;
    }
}
