package Application.Controller;

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

import Application.Domain.ReferralCode;
import Application.Domain.User;
import Application.Exception.JarAccessException;
import Application.Repository.ReferralCodeRepository;
import Application.Repository.UserRepository;
import Application.Utils.CookieManagement;
import Application.Utils.LanguageFilesUtils;

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

@Controller
public class IndexController {

	/**
	 * Private instance of {@link Application.Repository.UserRepository}.
	 */
	private final UserRepository userRepository;
	
	/**
	 * Private instance of {@link Application.Repository.ReferralCodeRepository}.
	 */
	private final ReferralCodeRepository referralCodeRepository;
	
	/**
	 * Private instance of {@link Application.Utils.LanguageFilesUtils}.
	 */
	private final LanguageFilesUtils languageFilesUtils;
	
	/**
	 * {@link org.springframework.context.ApplicationContext} for the SpringBoot application.
	 */
	private ApplicationContext context;
	
	/**
	 * Constructor for the {@link Application.Controller.IndexController} class.
	 * 
	 * @param userRepository	Instance of {@link Application.Repository.UserRepository}.
	 * @param referralCodeRepository	Instance of {@link Application.Repository.ReferralCodeRepository}.
	 * @param languageFilesUtils	Instance of {@link Application.Utils.LanguageFilesUtils}.
	 * @param context	Instance of {@link org.springframework.context.ApplicationContext} for the SpringBoot application.
	 */
	@Autowired
	public IndexController(UserRepository userRepository, ReferralCodeRepository referralCodeRepository, 
			LanguageFilesUtils languageFilesUtils, ApplicationContext context) {
		this.userRepository = userRepository;
		this.referralCodeRepository = referralCodeRepository;
		this.languageFilesUtils = languageFilesUtils;
		this.context = context;
	}
	
	/**
	 * Returns the main page of the platform.
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @param referral	The referral code of the {@link Application.Domain.User}.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param response	Instance of {@link javax.servlet.http.HttpServletRequest}.
	 * @return	"index"	string indicating the name of the Thymeleaf template to parse and return to the client.
	 * @throws JarAccessException	JarAccessException, exception thrown by {@link Application.Utils.LanguageFilesUtils} when trying to access files of the deployed and running jar.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getIndexPage(Model model, @RequestParam(value = "ref", required = false) String referral,
			@CookieValue(value = "user_id", defaultValue="NULL") String user_id, HttpServletResponse response) throws JarAccessException {
		
		Optional<ReferralCode> tempReferral = null;
		ReferralCode found = referralCodeRepository.findByReferral("universal").get();
		
		// Get list of languages files for dropdown
		model.addAttribute("langs", languageFilesUtils.getLanguages());
		
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
				
				if (existingUser.getReferralCode() != null && !found.getValue().equals(existingUser.getReferralCode().getValue())) {
					existingUser.setReferralCode(found);
					userRepository.save(existingUser);
				}				
				
				Cookie cookie = new Cookie("user_id", existingUser.getCookie());
				cookie.setMaxAge(60 * 60 * 24 * 365); //1 Year
				cookie.setPath("/"); //accesible everywhere
				response.addCookie(cookie);
			}
		}
		
		model.addAttribute("referral", referral);
		return "index";
	}
	
	/**
	 * Saves the cookies consent of the {@link Application.Domain.User}.
	 * 
	 * @param consent	The boolean values representing whether the {@link Application.Domain.User} accepted the consent of the cookies or not.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param response	Instance of {@link javax.servlet.http.HttpServletRequest}.
	 * @param referral	The referral code of the {@link Application.Domain.User}.
	 * @return "true" or "false" depending on whether or not the {@link Application.Domain.User} consented to the cookies.
	 */
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
	
	/**
	 * Returns the fragment with the buttons to explore the platform.
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @return	"fragments/join-buttons-fragment :: join-buttons" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/getJoinButtons", method = RequestMethod.GET)
	public String getJoinButtons(Model model) {
		return "fragments/join-buttons-fragment :: join-buttons";
	}
	
}
