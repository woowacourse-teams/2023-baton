package touch.baton.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import touch.baton.infra.auth.oauth.github.http.GithubHttpInterface;

import static org.springframework.web.reactive.function.client.support.WebClientAdapter.forClient;

@Configuration
public class OauthHttpInterfaceConfig {

    @Bean
    public GithubHttpInterface githubHttpClient() {
        return createHttpClient(GithubHttpInterface.class);
    }

    private <T> T createHttpClient(final Class<T> clazz) {
        return HttpServiceProxyFactory
                .builder(forClient(WebClient.create()))
                .build()
                .createClient(clazz);
    }
}
