package Application;

import java.io.File;
import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import Application.Exception.GetCanonicalPathException;

@ServletComponentScan
@SpringBootApplication
@EnableAutoConfiguration
public class Application {
	
	public static String IMAGE_DIR;
	public static String IMAGE_SCEN_DIR;
	public static String LOG_DIR;
	public static String PROF_DIR;
	public static String ACH_DIR;
	
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(Application.class);
    }

	public static void main(String[] args) throws GetCanonicalPathException {
		
		try {
			IMAGE_DIR = new File(".").getCanonicalPath() + "/images/";
			IMAGE_SCEN_DIR = new File(".").getCanonicalPath() + "/imagesScenario/";
			LOG_DIR = new File(".").getCanonicalPath() + "/loggedExceptions/";
			PROF_DIR = new File(".").getCanonicalPath() + "/profilePictures/";
			ACH_DIR = new File(".").getCanonicalPath() + "/imagesAchievement/";
		} catch (IOException e) {
			throw new GetCanonicalPathException();
		}
		
		SpringApplication.run(Application.class, args);
	}

	
	@Bean
	public SCryptPasswordEncoder sCryptPasswordEncoder() {
		return new SCryptPasswordEncoder();
	}
}
