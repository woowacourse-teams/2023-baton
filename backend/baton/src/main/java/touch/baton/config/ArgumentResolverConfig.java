package touch.baton.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import touch.baton.domain.oauth.controller.resolver.AuthMemberPrincipalArgumentResolver;
import touch.baton.domain.oauth.controller.resolver.AuthRunnerPrincipalArgumentResolver;
import touch.baton.domain.oauth.controller.resolver.AuthSupporterPrincipalArgumentResolver;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class ArgumentResolverConfig extends WebMvcConfig {

    private final AuthRunnerPrincipalArgumentResolver authRunnerPrincipalArgumentResolver;
    private final AuthSupporterPrincipalArgumentResolver authSupporterPrincipalArgumentResolver;
    private final AuthMemberPrincipalArgumentResolver authMemberPrincipalArgumentResolver;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authRunnerPrincipalArgumentResolver);
        resolvers.add(authSupporterPrincipalArgumentResolver);
        resolvers.add(authMemberPrincipalArgumentResolver);
    }
}
