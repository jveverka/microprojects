package one.microproject.configserver.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(SecurityConfig.class);

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		LOG.info("Applying custom security configuration ...");
		http.authorizeRequests()
			.anyRequest().authenticated()
			.and().httpBasic()
			.and().csrf().disable();
	}

}
