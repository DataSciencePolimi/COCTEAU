package Application.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Component;

/**
 * Utility class used to read the property file of the application.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Component
public class ReadPropertyFiles {
	
	/**
	 * Loads the requested property file.
	 * 
	 * @param fileName String specifying the name of the property file to load.
	 * @return {@link java.util.Properties} object corresponding to the requested property file.
	 * @throws IOException see {@link java.io.IOException}.
	 * @throws FileNotFoundException see {@link java.io.FileNotFoundException}.
	 */
	public Properties readPropFile(String fileName) throws IOException, FileNotFoundException {
		Properties prop = null;
		InputStream fis = null;
		try {
			prop = new Properties();
			fis = getFile(fileName);
			prop.load(fis);
		} finally {
			fis.close();
		}
		return prop;
	}
	
	/**
	 * Private utility function to access a given file.
	 * 
	 * @param fileName String specifying the name of the file to load.
	 * @return {@link java.io.InputStream} to access the requested file.
	 * @throws FileNotFoundException see {@link java.io.FileNotFoundException}.
	 * @throws IllegalArgumentException see {@link java.lang.IllegalArgumentException}.
	 */
	private InputStream getFile (String fileName) throws FileNotFoundException, IllegalArgumentException {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream resource = classLoader.getResourceAsStream(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file is not found!");
        } else {
        	return resource;
        }
	}
}
