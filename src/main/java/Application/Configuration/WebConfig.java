package Application.Configuration;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import Application.Application;

/**
 * A configuration class for the web aspects of the platform.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Configuration
public class WebConfig implements WebMvcConfigurer {

	/**
	 * Setup the configurations for the folders for the pictures and the logs.
	 * 
	 * @param registry	An instance of the ResourceHandlerRegistry class.
	 */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
          	.addResourceHandler("/webjars/**")
          	.addResourceLocations("/webjars/");
        registry
        	.addResourceHandler("/images/**")
        	.addResourceLocations("file:" + Application.IMAGE_DIR);
        registry
	        .addResourceHandler("/imagesScenario/**")
	    	.addResourceLocations("file:" + Application.IMAGE_SCEN_DIR);
        registry
	    	.addResourceHandler("/loggedExceptions/**")
	    	.addResourceLocations("file:" + Application.LOG_DIR);
        registry
	    	.addResourceHandler("/profilePictures/**")
	    	.addResourceLocations("file:" + Application.PROF_DIR);
        registry
	    	.addResourceHandler("/imagesAchievement/**")
	    	.addResourceLocations("file:" + Application.ACH_DIR);
    }
    
    /**
     * Returns an instance of a ServletWebServerFactory.
     * 
     * @return An instance of the ServletWebServerFactory.
     */
    @Bean
    public ServletWebServerFactory getTomcatServletWebServerFactory() {
        final TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                final SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                final SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        serverFactory.addAdditionalTomcatConnectors(getHttpConnector());
        return serverFactory;
    }
    
    /**
     * Setup the https connection for the platform.
     * 
     * @return An instance of the Connector class.
     */
    private Connector getHttpConnector() {
        final Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(80);
        connector.setSecure(false);
        connector.setRedirectPort(443);
        return connector;
    }
}