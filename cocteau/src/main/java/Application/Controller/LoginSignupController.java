package Application.Controller;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Domain.Administrator;
import Application.Domain.Credentials;
import Application.Domain.User;
import Application.Repository.AdministratorRepository;
import Application.Repository.UserRepository;
import Application.Utils.CookieManagement;

@SessionAttributes({"logged", "lang"})
@Controller
public class LoginSignupController {

	private final UserRepository userRepository;
	
	private final AdministratorRepository administratorRepository;
	
	private ApplicationContext context;
	
	@Autowired
	public LoginSignupController(UserRepository userRepository, ApplicationContext context, AdministratorRepository administratorRepository) {
		this.userRepository = userRepository;
		this.administratorRepository = administratorRepository;
		this.context = context;
	}
	
	@RequestMapping(value = "/login-registration", method = RequestMethod.GET)
	public String getLoginPage(Model model, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id,
			@RequestParam(value="lang", defaultValue="no-lang") String lang, @RequestParam(value = "lang", required = false) String language) {
		
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
		
		Credentials newCredentials = new Credentials();
		model.addAttribute("newUser", newCredentials);
		
		User newRegUser = new User();
		model.addAttribute("newRegUser", newRegUser);
		
		model.addAttribute("achievementAchieved", "");

		return "login";
	}
	
	@RequestMapping(value = "/login", method = {RequestMethod.POST, RequestMethod.GET})
	public String login(Model model, HttpServletResponse response, @ModelAttribute("newUser") Credentials credentials, BindingResult bindingResult,
			@ModelAttribute("newRegUser") User newRegUser) {
		
		SCryptPasswordEncoder encoder = context.getBean(SCryptPasswordEncoder.class);
		
		Optional<User> tempUser = userRepository.findByUsername(credentials.getUsername());
		Optional<Administrator> tempAdmin = administratorRepository.findByUsername(credentials.getUsername());
		
		if(!tempUser.isPresent() && !tempAdmin.isPresent()) {
			bindingResult.rejectValue("password", "login.error");
		} else {
			if(tempUser.isPresent()) {
				if(!encoder.matches(credentials.getPassword(), tempUser.get().getPassword())) {
					bindingResult.rejectValue("password", "login.error");
				}
			} else if (tempAdmin.isPresent()) {
				if(!encoder.matches(credentials.getPassword(), tempAdmin.get().getPassword())) {
					bindingResult.rejectValue("password", "login.error");
				}
			}
		}
		
		if(bindingResult.hasErrors()) {
			newRegUser = new User();
			
			model.addAttribute("newRegUser", new User());
			model.addAttribute("newUser", credentials);
			
			return "login";
		}
		
		model.addAttribute("logged", true);
		
		if(tempUser.isPresent()) {
			response.addCookie(CookieManagement.createCookie("cookieconsent_status", tempUser.get().isCookieConsent()? "allow" : "deny"));
			response.addCookie(CookieManagement.createCookie("user_id", tempUser.get().getCookie()));
			return "redirect:/";
		}
		
		if(tempAdmin.isPresent()) {
			response.addCookie(CookieManagement.createCookie("cookieconsent_status", tempAdmin.get().isCookieConsent()? "allow" : "deny"));
			response.addCookie(CookieManagement.createCookie("user_id", tempAdmin.get().getIdAdministrator()));
			return "redirect:/scenario-panel";
		}
		
		return "redirect:/error";
	}
	
	@RequestMapping(value = "/register", method = {RequestMethod.POST, RequestMethod.GET})
	public String register(Model model, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id, @ModelAttribute("newRegUser") User newRegUser,
			@CookieValue(value = "cookieconsent_status", defaultValue = "allow") String cookieConsent, @RequestParam(value="lang", defaultValue="en") String lang,
			BindingResult bindingResult, @ModelAttribute("newUser") Credentials credentials) {
		
		Optional<User> userTmp = userRepository.findByCookie(user_id);
		SCryptPasswordEncoder encoder = context.getBean(SCryptPasswordEncoder.class);
		
		if(userTmp.isPresent() && userTmp.get().getCookie().equals(userTmp.get().getUsername())) {
		
			User user = userTmp.get();
		
			if(userRepository.findByUsername(newRegUser.getUsername()).isPresent() || administratorRepository.findByUsername(newRegUser.getUsername()).isPresent()) {
				bindingResult.rejectValue("username", "registration.username-taken");
			}
		
			if(!newRegUser.getPassword().equals("") && !newRegUser.getConfirmPassword().equals("") && !newRegUser.getPassword().equals(newRegUser.getConfirmPassword())) {
				bindingResult.rejectValue("password", "registration.password-mismatch");
			}
			
			if(bindingResult.hasErrors()) {
				credentials = new Credentials();
				
				model.addAttribute("newRegUser", newRegUser);
				model.addAttribute("newUser", new User());
				
				return "login";
			}
			
			user.setUsername(newRegUser.getUsername());
			user.setPassword(encoder.encode(newRegUser.getPassword()));
			user.setAge(newRegUser.getAge());
			user.setNationality(newRegUser.getNationality());
			user.setGender(newRegUser.getGender());
			user.setEducation(newRegUser.getEducation());
			
			userRepository.save(user);
		} else if(userTmp.isPresent() && !userTmp.get().getCookie().equals(userTmp.get().getUsername())) {
			bindingResult.rejectValue("authorized", "registration.existing");
			
			model.addAttribute("newRegUser", newRegUser);
			model.addAttribute("newUser", new User());
			
			return "login";
		} else {
			bindingResult.rejectValue("authorized", "registration.authorization");
			
			model.addAttribute("newRegUser", newRegUser);
			model.addAttribute("newUser", new User());
			
			return "login";
		}
		
		return "redirect:/";
	}
	
}
