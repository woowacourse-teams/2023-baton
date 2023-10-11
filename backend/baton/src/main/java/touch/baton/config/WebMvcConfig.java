package touch.baton.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpMethod.*;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origin}")
    private List<String> origins;

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        final String[] allowedOrigins = origins.toArray(String[]::new);

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowCredentials(true)
                .allowedMethods(GET.name(), POST.name(), PUT.name(), PATCH.name(), DELETE.name(), OPTIONS.name())
                .exposedHeaders(LOCATION, AUTHORIZATION)
                .maxAge(3600);
    }
}
