package Application.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes({"logged", "lang"})
@Controller
public class ErrorController {

	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String getAbout(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, @RequestParam(value = "lang", required = false, defaultValue="no-lang") String lang) {
		
		if(lang.equals("no-lang")) {
			if(model.asMap().get("lang") == null) {
				lang = "en";
				model.addAttribute("lang", lang);
			} else {
				lang = model.asMap().get("lang").toString();
			}
		} else {
			model.addAttribute("lang", lang);
		}
		
		if(model.asMap().get("logged") != null) {
			model.addAttribute("logged", (boolean) model.asMap().get("logged"));
		} else {
			model.addAttribute("logged", false);
		}
		
		model.addAttribute("achievementAchieved", "");
		
		return "error";
	}
	
}
