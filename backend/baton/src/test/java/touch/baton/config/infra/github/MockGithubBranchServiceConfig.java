package touch.baton.config.infra.github;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import touch.baton.infra.github.GithubBranchManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@TestConfiguration
public abstract class MockGithubBranchServiceConfig {

    @Bean
    public GithubBranchManager githubBranchService() {
        final GithubBranchManager mock = Mockito.mock(GithubBranchManager.class);
        doNothing().when(mock).createBranch(any(String.class), any(String.class));

        return mock;
    }
}
