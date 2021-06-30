package Application.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller class for managing the page that displays the {@link Application.Exception}.
 * It manages the content of the page depending on the detected error.
 * It also manages the page displayed when an unauthorized access is detected.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Controller
public class ErrorController {

	/**
	 * Returns the "error" string indicating the name of the Thymeleaf template to parse and return to the client.
	 * 
	 * @return	"error" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String getError() {
		return "error";
	}
	
	/**
	 * Returns the "access-denied" string indicating the name of the Thymeleaf template to parse and return to the client.
	 * 
	 * @return	"access-denied" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/access-denied", method = RequestMethod.GET)
	public String getAccessDenied() {
		return "access-denied";
	}
	
}
