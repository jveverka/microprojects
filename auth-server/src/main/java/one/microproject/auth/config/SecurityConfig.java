package one.microproject.auth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        LOGGER.info("httpSecurity setup");
        return httpSecurity
                .addFilterBefore(new SecurityFilter(), BasicAuthenticationFilter.class)
                .securityMatcher("/api/v1/user/**")
                .build();
    }

}


