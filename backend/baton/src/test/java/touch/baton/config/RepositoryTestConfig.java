package touch.baton.config;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import(JpaConfig.class)
@DataJpaTest
public abstract class RepositoryTestConfig {
}
