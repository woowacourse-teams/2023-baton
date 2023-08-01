package touch.baton.config;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.annotation.AliasFor;
import touch.baton.domain.oauth.controller.AuthRunnerPrincipalArgumentResolver;
import touch.baton.domain.oauth.controller.AuthSupporterPrincipalArgumentResolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@WebMvcTest(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
        ArgumentResolverConfig.class,
        AuthRunnerPrincipalArgumentResolver.class,
        AuthSupporterPrincipalArgumentResolver.class
}))
public @interface MockMvcTest {

    @AliasFor(annotation = WebMvcTest.class, attribute = "value")
    Class<?>[] value() default {};
}
