package Application.Handler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import Application.Application;
import Application.Exception.GetCanonicalPathException;
import Application.Exception.JarAccessException;
import Application.Exception.PictureCreationException;
import Application.Exception.PictureUploadException;
import Application.Exception.TranslateException;
import Application.Exception.UnsplashConnectionException;

/**
 * The ExceptionLogger Class logs Exceptions when they take place.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@ControllerAdvice
public class ExceptionLogger extends ResponseEntityExceptionHandler {

	/**
	 * Logs the time, the kind of Exception, the line and the class that caused it for each {@link Application.Domain.User}.
	 * Whenever a new Exception takes place, it is logged alongside all the Exceptions caused by the same {@link Application.Domain.User}.
	 * 
	 * @param model	An instance of the Model class.
	 * @param ex	The caught Exception
	 * @param servletRequest	an instance of the ServletRequest class.
	 * @return	The "error" String if any operation fails.
	 */
	
	@ExceptionHandler(Exception.class)
	public final String handleAllExceptions(Model model, Exception ex, ServletRequest servletRequest) {
		try {
			Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
			
			String cookieValue = null;
			
			for (Cookie ck : cookies) {
				if(ck.getName().equals("user_id")) {
					cookieValue = ck.getValue();
				}
			}
			
			Path path = Paths.get(Application.LOG_DIR);

			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");  
			LocalDateTime now = LocalDateTime.now();
			
	    	BufferedWriter out = new BufferedWriter( 
	                   new FileWriter(new File(Application.LOG_DIR, cookieValue + ".txt"), true));
            
	    	out.write("Message: \"" + ex.getMessage() + "\" Date: " + dtf.format(now) + "\n");
            
            for(StackTraceElement ste : ex.getStackTrace()) {
            	if(ste.getClassName().contains("Application.")) {
            		out.write("Line: " + ste.getLineNumber() + " - Class: " + ste.getClassName().substring(ste.getClassName().lastIndexOf('.') + 1) + " - Method: " + ste.getMethodName() + "\n");
            	}
        	}
            
            out.write(" - - - - - \n");
            
            out.close();
            
            String errorText = "";
            
            if(ex instanceof GetCanonicalPathException) {
            	errorText = "GetCanonicalPathException";
            } else if(ex instanceof SQLException) {
            	errorText = "SQLException";
            } else if(ex instanceof PictureCreationException) {
            	errorText = "PictureCreationException";
            } else if(ex instanceof JarAccessException) {
            	errorText = "JarAccessException";
            } else if(ex instanceof PictureUploadException) {
            	errorText = "PictureUploadException";
            } else if(ex instanceof IOException) {
            	errorText = "IOException";
            } else if(ex instanceof FileNotFoundException) {
            	errorText = "FileNotFoundException";
            } else if(ex instanceof IllegalArgumentException) {
            	errorText = "IllegalArgumentException";
            } else if(ex instanceof TranslateException) {
            	errorText = "TranslateException";
            } else if(ex instanceof UnsplashConnectionException) {
            	errorText = "UnsplashConnectionException";
            }
            
            System.out.println(errorText);
            ex.printStackTrace();
            
            model.addAttribute("errorType", errorText);
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		return "error";
    }
	
}
