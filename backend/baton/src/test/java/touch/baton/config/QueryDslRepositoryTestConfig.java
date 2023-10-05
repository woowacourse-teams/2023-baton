package touch.baton.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import touch.baton.domain.runnerpost.query.repository.RunnerPostPageRepository;

@TestConfiguration
public class QueryDslRepositoryTestConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    public RunnerPostPageRepository runnerPostPageRepository() {
        return new RunnerPostPageRepository(jpaQueryFactory());
    }
}
