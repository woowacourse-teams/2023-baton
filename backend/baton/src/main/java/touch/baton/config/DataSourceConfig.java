package touch.baton.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import touch.baton.infra.database.datasource.DataSourceType;
import touch.baton.infra.database.datasource.RoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

import static touch.baton.infra.database.datasource.DataSourceType.Name.REPLICA_NAME;
import static touch.baton.infra.database.datasource.DataSourceType.Name.ROUTING_NAME;
import static touch.baton.infra.database.datasource.DataSourceType.Name.SOURCE_NAME;

@Profile("deploy")
@Configuration
public class DataSourceConfig {

    @Qualifier(SOURCE_NAME)
    @ConfigurationProperties(prefix = "spring.datasource.source")
    @Bean
    public DataSource sourceDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Qualifier(REPLICA_NAME)
    @ConfigurationProperties(prefix = "spring.datasource.replica")
    @Bean
    public DataSource replicaDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Qualifier(ROUTING_NAME)
    @Bean
    public DataSource routingDataSource(@Qualifier(SOURCE_NAME) final DataSource sourceDataSource,
                                        @Qualifier(REPLICA_NAME) final DataSource replicaDataSource
    ) {
        return RoutingDataSource.createDefaultSetting(
                Map.of(DataSourceType.SOURCE, sourceDataSource,
                        DataSourceType.REPLICA, replicaDataSource)
        );
    }

    @Bean
    @Primary
    public DataSource dataSource(@Qualifier(ROUTING_NAME) final DataSource replicationRoutingDataSource) {
        return new LazyConnectionDataSourceProxy(replicationRoutingDataSource);
    }
}
