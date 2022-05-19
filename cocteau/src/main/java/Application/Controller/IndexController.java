package Application.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Domain.Narrative;
import Application.Domain.ReferralCode;
import Application.Domain.Scenario;
import Application.Domain.User;
import Application.Exception.JarAccessException;
import Application.Repository.NarrativeRepository;
import Application.Repository.ReferralCodeRepository;
import Application.Repository.ScenarioRepository;
import Application.Repository.UserRepository;
import Application.Utils.CookieManagement;
import Application.Utils.ImageFinder;

/**
 * Controller class for managing the home page of the platform.
 * The main page is made of different components that are shown as the {@link Application.Domain.User}(s) interact with them.
 * The main page also manages the cookies and the authorizations to join the platform.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes({"logged", "lang"})
@Controller
public class IndexController {

	private final UserRepository userRepository;
	private final ReferralCodeRepository referralCodeRepository;
	private final NarrativeRepository narrativeRepository;
	private final ScenarioRepository scenarioRepository;
	private ApplicationContext context;

	@Autowired
	public IndexController(UserRepository userRepository, ReferralCodeRepository referralCodeRepository, ScenarioRepository scenarioRepository,
			NarrativeRepository narrativeRepository, ApplicationContext context) {
		this.userRepository = userRepository;
		this.referralCodeRepository = referralCodeRepository;
		this.narrativeRepository = narrativeRepository;
		this.scenarioRepository = scenarioRepository;
		this.context = context;
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getIndexPage(Model model, @RequestParam(value = "ref", required = false, defaultValue = "universal") String referral, @RequestParam(value = "lang", required = false, defaultValue="no-lang") String lang, @CookieValue(value = "user_id", defaultValue="NULL") String user_id,
			@CookieValue(value = "cookieconsent_status", required = false) String cookieconsent_status, HttpServletResponse response) throws JarAccessException {
		
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
		
		Optional<ReferralCode> tempReferral = null;
		ReferralCode found = referralCodeRepository.findByReferral(referral).get();
		
		if(cookieconsent_status != null && cookieconsent_status.equals("deny")) {
			Cookie status_cookie = new Cookie("cookieconsent_status", "deny");
			status_cookie.setMaxAge(0);
			
			response.addCookie(status_cookie);
		}
		
		if (referral == null) {
			referral = "universal";
			if (!user_id.equals("NULL")) {
				Optional<User> u = userRepository.findByCookie(user_id);
				if(u.isPresent()) {
					referral = u.get().getReferralCode().getValue();
				}
			}
			model.addAttribute("referral", referral);
			return "index";
		} else {
			tempReferral = referralCodeRepository.findByReferral(referral);
			if(!tempReferral.isPresent()) {
				return "index";
			} else {
				found = tempReferral.get();
			}
		}
		
		if (found != null) {
			Optional<User> tempUser = userRepository.findByCookie(user_id);
			User existingUser = null;
			if (tempUser.isPresent()) {
				existingUser = tempUser.get();
				
//				if (existingUser.getReferralCode() != null && !found.getValue().equals(existingUser.getReferralCode().getValue())) {
//					existingUser.setReferralCode(found);
//					userRepository.save(existingUser);
//				}		
				
				Cookie cookie = new Cookie("user_id", existingUser.getCookie());
				cookie.setMaxAge(60 * 60 * 24 * 365); //1 Year
				cookie.setPath("/"); //accesible everywhere
				response.addCookie(cookie);
			} else {
				Cookie cookie = new Cookie("user_id", user_id);
				cookie.setMaxAge(0);
				
				response.addCookie(cookie);
				
				Cookie consent_cookie = new Cookie("cookieconsent_status", "");
				consent_cookie.setMaxAge(0);
				
				response.addCookie(consent_cookie);
			}
		}
		
		Collection<Scenario> scenarios = scenarioRepository.findByLanguageAndReferral(lang, referral);
		
		if(!user_id.equals("NULL")) {
			scenarios = scenarioRepository.findByLanguageAndUser(lang, user_id, referral);
			
			if(userRepository.findByCookie(user_id).isPresent()) {
				scenarios = scenarioRepository.findByLanguageAndUser(lang, user_id, userRepository.findByCookie(user_id).get().getReferralCode().getValue());
			}
		}
		
		ArrayList<Narrative> narratives = new ArrayList<Narrative>();
		
		for(Scenario s : scenarios) {
			if(!s.getImgCredits().equals("")) {
				if(s.getImgCredits().contains("unsplash")) {
					s.setAuthorName(s.getImgCredits().split("@")[1]);
				}
			}
			
			try {
				ImageFinder.findImageScenario(s);
			} catch(IOException e) {
				s.setImg("/appimg/Default-Scenario-Picture.png");
			}
		}
		
		for(Scenario s : scenarios) {
			if(!narratives.contains(s.getNarrative())) {
				narratives.add(s.getNarrative());
			}
		}
		
		model.addAttribute("narratives", narratives);
		model.addAttribute("scenarios", scenarios);
		model.addAttribute("achievementAchieved", "");
		model.addAttribute("referral", referral);
		
		return "index";
	}
	
	@ResponseBody
	@RequestMapping(value = "/saveUpdateCookieConsent", method = RequestMethod.POST)
	public String saveUpdateCookie(@RequestParam("consent") boolean consent, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, HttpServletResponse response,
			@RequestParam(name = "referral", required = false, defaultValue="universal") String referral) {
		
		if(!referralCodeRepository.findByReferral(referral).isPresent()) {
			return "redirect:/error";
		}
		
		SCryptPasswordEncoder encoder = context.getBean(SCryptPasswordEncoder.class);
		
		if (user_id.equals("NULL")) {
			if(consent) {
				Cookie cookie = CookieManagement.createNewCookie(this.userRepository);
				
				Optional<Integer> currentEntry = userRepository.findMaxEntry();
				User new_user = null;
				
				if(currentEntry.isPresent()) {
					new_user = new User(cookie.getValue(), encoder.encode(cookie.getValue()), currentEntry.get() + 1);
				} else {
					new_user = new User(cookie.getValue(), encoder.encode(cookie.getValue()), 1);
				}
				
				new_user.setCookieConsent(consent);
				new_user.setReferralCode(referralCodeRepository.findByReferral(referral).get());
				
				userRepository.save(new_user);
				
				response.addCookie(cookie);
				return "true";
			} else {
				return "false";
			}
		} else {
			// The user exists but their preferences have been updated, update the DB and invalidate the cookie client side
			Optional<User> tempUser = userRepository.findByCookie(user_id);
			User existingUser = null;
			if (tempUser.isPresent()) {
				existingUser = tempUser.get();
			} else {
				return "false";
			}
			if (!consent) {
				existingUser.setCookieConsent(consent);
				
				userRepository.save(existingUser);
				Cookie cookie = new Cookie("user_id", user_id);
				cookie.setMaxAge(0);
				
				response.addCookie(cookie);
				
				return "false";
			} else {
				// You can't change the referral code once set
				return "true";
			}
		}
	}
	
}
