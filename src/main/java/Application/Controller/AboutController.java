package Application.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller class for managing the page containing the information about the platform and the useful contacts.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Controller
public class AboutController {

	/**
	 * Returns the page containing the information about the platform and the contacts of the developers of the application.
	 * 
	 * @return	"about" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String getAbout() {
		return "about";
	}
}
