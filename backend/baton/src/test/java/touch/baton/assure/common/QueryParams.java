package touch.baton.assure.common;

import java.util.Map;

public class QueryParams {

    private final Map<String, Object> values;

    public QueryParams(final Map<String, Object> values) {
        this.values = values;
    }

    public Map<String, Object> getValues() {
        return values;
    }
}
