package Application.Controller;

import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import Application.Domain.Administrator;
import Application.Domain.Credentials;
import Application.Domain.User;
import Application.Repository.AdministratorRepository;
import Application.Repository.UserRepository;
import Application.Utils.CookieManagement;

/**
 * Controller class for managing the login of the {@link Application.Domain.User}(s) of the platform.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Controller
public class LoginController {

	/**
	 * Private instance of {@link Application.Repository.UserRepository}.
	 */
	private final UserRepository userRepository;
	
	/**
	 * Private instance of {@link Application.Repository.AdministratorRepository}.
	 */
	private final AdministratorRepository administratorRepository;
	
	/**
	 * {@link org.springframework.context.ApplicationContext} for the SpringBoot application.
	 */
	private ApplicationContext context;
	
	/**
	 * Constructor for the {@link Application.Controller.LoginController} class.
	 * 
	 * @param userRepository	Instance of {@link Application.Repository.UserRepository}.
	 * @param context	{@link org.springframework.context.ApplicationContext} for the SpringBoot application.
	 * @param administratorRepository	Instance of {@link Application.Repository.AdministratorRepository}.
	 */
	@Autowired
	public LoginController(UserRepository userRepository, ApplicationContext context, AdministratorRepository administratorRepository) {
		this.userRepository = userRepository;
		this.administratorRepository = administratorRepository;
		this.context = context;
	}
	
	/**
	 * Returns the empty credentials to be filled by the {@link Application.Domain.User} to login.
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @return	"login" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getLogin(Model model) {
		
		Credentials newCredentials = new Credentials();
		model.addAttribute("newUser", newCredentials);
		
		return "login";
	}
	
	/**
	 * Perform the login for either a {@link Application.Domain.User} or an {@link Application.Domain.Administrator}.
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @param response	Instance of {@link javax.servlet.http.HttpServletRequest}.
	 * @param credentials	The credentials provided by the {@link Application.Domain.User}.
	 * @param bindingResult	Instance of {@link org.springframework.validation.BindingResult}.
	 * @return "redirect:/scenario" or "redirect:/scenario-panel" string indicating the name of the Thymeleaf template to parse and return to the client, depending on whether a {@link Application.Domain.User} or an {@link Application.Domain.Administrator} logged in.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String doLogin(Model model, HttpServletResponse response, @ModelAttribute("newUser") Credentials credentials, BindingResult bindingResult){
		
		SCryptPasswordEncoder encoder = context.getBean(SCryptPasswordEncoder.class);
		
		Optional<User> tempUser = userRepository.findByUsername(credentials.getUsername());
		Optional<Administrator> tempAdmin = administratorRepository.findByUsername(credentials.getUsername());
		
		if(credentials.getUsername().length() > 45 || credentials.getPassword().length() > 150){
			bindingResult.rejectValue("password", "login.error");
		}
		else if(!tempUser.isPresent() && !tempAdmin.isPresent()) {
			bindingResult.rejectValue("password", "login.error");
		} else {
			if(tempUser.isPresent()) {
				if(!encoder.matches(credentials.getPassword(), tempUser.get().getPassword())) {
					bindingResult.rejectValue("password", "login.error");
				} else if(!tempUser.get().isActivated()) {
					bindingResult.rejectValue("password", "login.error");
				}
			} else if (tempAdmin.isPresent()) {
				if(!encoder.matches(credentials.getPassword(), tempAdmin.get().getPassword())) {
					bindingResult.rejectValue("password", "login.error");
				}
			}
		}
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("newUser", credentials);
			return "login";
		}
		
		if(tempUser.isPresent()) {
			response.addCookie(CookieManagement.createCookie("cookieconsent_status", tempUser.get().isCookieConsent()? "allow" : "deny"));
			response.addCookie(CookieManagement.createCookie("user_id", tempUser.get().getCookie()));
			return "redirect:/scenario";
		}
		
		if(tempAdmin.isPresent()) {
			response.addCookie(CookieManagement.createCookie("cookieconsent_status", tempAdmin.get().isCookieConsent()? "allow" : "deny"));
			response.addCookie(CookieManagement.createCookie("user_id", tempAdmin.get().getIdAdministrator()));
			return "redirect:/scenario-panel";
		}
		
		return "redirect:/error";
	}
	
}
