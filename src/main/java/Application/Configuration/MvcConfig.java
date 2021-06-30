package Application.Configuration;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Configures the language cookies and the internationalization aspect of the web site.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	
	/**
	 * Setups the localeInfo cookie.
	 * 
	 * @return	An instance of the CookieLocaleResolver class.
	 */
	@Bean(name = "localeResolver")
    public LocaleResolver getLocaleResolver()  {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setDefaultLocale(Locale.ENGLISH);
        resolver.setCookieName("localeInfo");
        // 60 minutes 
        resolver.setCookieMaxAge(60*60);
        
        return resolver;
    }
    
	/**
	 * Manages the sources of the text for the languages.
	 * 
	 * @return	An instance of the ReloadableResourceBundleMessageSource class.
	 */
    @Bean(name = "messageSource")
    public MessageSource getMessageResource()  {
        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();

        messageResource.setBasename("classpath:lang/messages");
        messageResource.setDefaultEncoding("UTF-8");

        return messageResource;
    }
    
    /**
     * Manages the internationalization depending on the value of the "lang" parameter.
     * 
     * @return	An instance of the LocaleChangeInterceptor class.
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        
        return lci;
    }
    
    /**
     * Adds the LocaleChangeInterceptor to the application's interceptor registry.
     * 
     * @param registry	An instance of the InterceptorRegistry class.
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor()).addPathPatterns("/*");
    }

}
