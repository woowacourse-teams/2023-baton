package touch.baton.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import touch.baton.domain.oauth.controller.AuthRunnerPrincipalArgumentResolver;
import touch.baton.domain.oauth.controller.AuthSupporterPrincipalArgumentResolver;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ArgumentResolverConfig extends WebMvcConfig {

    private final AuthRunnerPrincipalArgumentResolver authRunnerPrincipalArgumentResolver;
    private final AuthSupporterPrincipalArgumentResolver authSupporterPrincipalArgumentResolver;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authRunnerPrincipalArgumentResolver);
        resolvers.add(authSupporterPrincipalArgumentResolver);
    }
}
