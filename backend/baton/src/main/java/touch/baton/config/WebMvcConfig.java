package touch.baton.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origin}")
    private String allowedOrigin;

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigin)
                .allowCredentials(false)
                .allowedMethods(GET.name(), POST.name(), PUT.name(), PATCH.name(), DELETE.name(), OPTIONS.name())
                .exposedHeaders(LOCATION)
                .maxAge(3600);
    }
}
