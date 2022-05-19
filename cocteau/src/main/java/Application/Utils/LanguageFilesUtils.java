package Application.Utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import Application.Exception.JarAccessException;

/**
 * Utility class used to access language files when the application is deployed and running jar.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Component
public class LanguageFilesUtils {
	
	/**
	 * Creates a mapping between the supported languages' codes and the extended language names.
	 * 
	 * @return HashMap with language code as key and the extended language name as value.
	 * @throws JarAccessException  thrown when trying to access files of the deployed and running jar.
	 */
    public HashMap<String, String> getLanguages() throws JarAccessException {
    	String targetDir = "/lang/messages";
    	List<String> pathnames = new ArrayList<String>();
        String l;
        HashMap<String, String> langs = new HashMap<String, String>();
        
        String jarPath = getClass()
    			.getProtectionDomain()
    			.getCodeSource()
    			.getLocation()
    			.getPath();
    	
    	if(jarPath.contains(".jar")) {
     		// Get jar file path and removing some garbage in the file path
        	final File jarFile = new File(jarPath.split(Pattern.quote(":"))[1].split(Pattern.quote("!"))[0]);
    		// If running from jar explore and get the language files
        	
    		JarFile jar;
			try {
				jar = new JarFile(jarFile);
				Enumeration<JarEntry> entries = jar.entries();
				while(entries.hasMoreElements()) {
					final String name = entries.nextElement().getName();
					if(name.contains(targetDir)) {
						String[] parts = name.split(Pattern.quote("/"));
						pathnames.add(parts[parts.length-1]);
					}
				}
				jar.close();
			} catch (IOException e) {
				throw new JarAccessException();
			}
    	} else {
    		// If running from ide access directly the folder
    		String userDirectory = System.getProperty("user.dir");
            File f = new File(userDirectory + "/src/main/resources/lang");
            
            String[] names = f.list();
            for(String n : names) {
            	pathnames.add(n);
            }
    	}
    	
    	for(int i=0; i<pathnames.size(); i++) {
        	// Format: messages_<lang>.properties
        	// Split on . and keep messages_<lang>
        	// Split on _ and keep <lang>
        	l = pathnames.get(i).split(Pattern.quote("."))[0].split("_")[1];
        	switch(l) {
	        	case "en":
	        		langs.put(l, "English");
	        		break;
	        	case "it":
	        		langs.put(l, "Italiano");
	        		break;
	        	default:
	        		langs.put(l, "English");
        	}
        }
        
        return langs;
    }
    
}
