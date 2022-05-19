package Application.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Configures the security aspects of the platform.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	/**
	 * Configures the headers and the filters.
	 * 
	 * @param http	An instance of the HttpSecurity class.
	 */
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
//		http.requiresChannel().anyRequest().requiresSecure();
		http.csrf().disable();
		http.headers().contentSecurityPolicy("frame-ancestors 'self' https://trigger.eui.eu");
//		http.headers().frameOptions().sameOrigin();
	}
	
}
