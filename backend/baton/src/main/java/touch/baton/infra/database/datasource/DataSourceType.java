package touch.baton.infra.database.datasource;

import static touch.baton.infra.database.datasource.DataSourceType.Name.REPLICA_NAME;
import static touch.baton.infra.database.datasource.DataSourceType.Name.SOURCE_NAME;

public enum DataSourceType {

    SOURCE(SOURCE_NAME),
    REPLICA(REPLICA_NAME);

    private final String name;

    DataSourceType(final String name) {
        this.name = name;
    }

    public static class Name {

        public static final String ROUTING_NAME = "ROUTING";
        public static final String SOURCE_NAME = "SOURCE";
        public static final String REPLICA_NAME = "REPLICA";
    }

}
