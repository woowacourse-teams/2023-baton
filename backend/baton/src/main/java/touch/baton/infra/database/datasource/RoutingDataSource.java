package touch.baton.infra.database.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;

public class RoutingDataSource extends AbstractRoutingDataSource {

    public static RoutingDataSource createDefaultSetting(final Map<Object, Object> dataSources) {
        final RoutingDataSource routingDataSource = new RoutingDataSource();
        routingDataSource.setDefaultTargetDataSource(dataSources.get(DataSourceType.SOURCE));
        routingDataSource.setTargetDataSources(dataSources);
        return routingDataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        final boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (readOnly) {
            return DataSourceType.REPLICA;
        }

        return DataSourceType.SOURCE;
    }
}
