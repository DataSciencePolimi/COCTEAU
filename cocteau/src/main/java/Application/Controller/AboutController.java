package Application.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Repository.AchievementRepository;
import Application.Repository.UserAchievementRepository;
import Application.Utils.AchievementUtils;

/**
 * Controller class for managing the page containing the information about the platform and the useful contacts.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes({"logged", "lang"})
@Controller
public class AboutController {

	private final UserAchievementRepository userAchievementRepository;
	private final AchievementRepository achievementRepository;
	
	@Autowired
	public AboutController(UserAchievementRepository userAchievementRepository, AchievementRepository achievementRepository) {
		this.userAchievementRepository = userAchievementRepository;
		this.achievementRepository = achievementRepository;
	}

	/**
	 * Returns the page containing the information about the platform and the contacts of the developers of the application.
	 * 
	 * @return	"about" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String getAbout(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id,
			@RequestParam(value = "lang", required = false, defaultValue="no-lang") String lang) {
		
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
		
		if(!userAchievementRepository.findByCookieAndAchievement(user_id, 5).isPresent() && (boolean) model.asMap().get("logged")) {
			AchievementUtils.createAchievement(user_id, userAchievementRepository, 5);
			model.addAttribute("achievementAchieved", "You achieved '" + achievementRepository.findById(5).get().getTitle() + "'");
		}
		
		return "about";
	}
}
