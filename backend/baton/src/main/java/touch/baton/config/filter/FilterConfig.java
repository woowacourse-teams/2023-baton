package touch.baton.config.filter;

import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class FilterConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<Filter> getFilterRegistrationBean() {
        final FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>(new MDCLoggingFilter());
        registrationBean.setOrder(Integer.MIN_VALUE);
        registrationBean.setUrlPatterns(List.of("/api/**"));
        return registrationBean;
    }
}
