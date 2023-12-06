package nlu.edu.vn.ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Minh Huy
 *
 * Configuration class for Cross-Origin Resource Sharing (CORS).
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configures CORS mappings for the application.
     *
     * @param registry The CorsRegistry to configure CORS settings.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
