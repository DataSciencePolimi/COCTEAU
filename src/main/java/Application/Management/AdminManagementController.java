package Application.Management;

import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
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

import Application.Domain.Administrator;
import Application.Domain.Credentials;
import Application.Repository.AdministratorRepository;
import Application.Repository.UserRepository;

/**
 * Controller class for managing {@link Application.Domain.Administrator} accounts.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Controller
public class AdminManagementController {

	/**
	 * Private instance of {@link Application.Repository.AdministratorRepository}.
	 */
	private final AdministratorRepository administratorRepository;
	
	/**
	 * Private instance of {@link Application.Repository.UserRepository}.
	 */
	private final UserRepository userRepository;
	
	/**
	 * {@link org.springframework.context.ApplicationContext} for the SpringBoot application.
	 */
	private ApplicationContext context;
	
	/**
	 * Constructor for the {@link Application.Management.AdminManagementController} class.
	 * 
	 * @param administratorRepository instance of {@link Application.Repository.AdministratorRepository}.
	 * @param context instance of {@link Application.Repository.UserRepository}.
	 * @param userRepository instance of {@link org.springframework.context.ApplicationContext}.
	 */
	@Autowired
	public AdminManagementController(AdministratorRepository administratorRepository, ApplicationContext context, UserRepository userRepository) {
		this.administratorRepository = administratorRepository;
		this.userRepository = userRepository;
		this.context = context;
	}
	
	/**
	 * Loads the page for managing {@link Application.Domain.Administrator} account. 
	 * Prepares the creation of a new admin account by creating an instance of the {@link Application.Domain.Credentials} class 
	 * and adding it to the {@link org.springframework.ui.Model}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @return "admin-management" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/create-admin", method = RequestMethod.GET)
	public String getLogin(Model model) {
		
		Credentials newCredentials = new Credentials();
		model.addAttribute("newAdmin", newCredentials);
		
		return "admin-management";
	}
	
	/**
	 * Implements the creation of a {@link Application.Domain.Administrator} account.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param newPasswordTextForm String with the password to use for the {@link Application.Domain.Administrator} account.
	 * @param confirmNewPasswordTextForm String with the password to use for the {@link Application.Domain.Administrator} account. This is mainly to double-check on the server-side
	 * that the passwords match.
	 * @param username String with the username to use for the {@link Application.Domain.Administrator} account.
	 * @param cookie String, cookie of the creator of the {@link Application.Domain.Administrator} account.
	 * @param lang String, code for the language to use.
	 * @return array of two elements {String, String}. The first element is the status of the operation, the second element is the name of the Thymeleaf template.
	 * Possible returned pages are:
	 * <ul>
	 * <li> "scenario-panel" </li>
	 * <li> "error" </li>
	 * </ul>
	 */
	@ResponseBody
	@RequestMapping(value = "/createAdminProfile", method = RequestMethod.POST)
	private ArrayList<String[]> createAdminProfile(Model model, @RequestParam("createNewPasswordTextForm") String newPasswordTextForm,  @RequestParam("createConfirmNewPasswordTextForm") String confirmNewPasswordTextForm,
			@RequestParam("createUsernameTextForm") String username, @CookieValue(value = "user_id", defaultValue="NULL") String cookie, @CookieValue(value = "localeInfo", defaultValue="en") String lang) {
		
		if(!"NULL".equals(cookie)) {
			Optional<Administrator> adminTmp = administratorRepository.findByCookie(cookie);
			SCryptPasswordEncoder encoder = context.getBean(SCryptPasswordEncoder.class);
			
			if(adminTmp.isPresent() && adminTmp.get().isRoot()) {
			
				ArrayList<String[]> errors = new ArrayList<String[]>();
				
				if(username.equals("")) {
					if(lang.equals("it")) {
						errors.add(new String[]{"username-error-creation", "Campo Vuoto"});
					} else {
						errors.add(new String[]{"username-error-creation", "Empty Field"});
					}
				} else if(administratorRepository.findByUsername(username).isPresent() || userRepository.findByUsername(username).isPresent()) {
					if(lang.equals("it")) {
						errors.add(new String[]{"username-error-creation", "Username Non Disponibile"});
					} else {
						errors.add(new String[]{"username-error-creation", "Username Already Taken"});
					}
				}
			
				if(newPasswordTextForm.equals("")) {
					if(lang.equals("it")) {
						errors.add(new String[]{"new-password-error-creation", "Campo Vuoto"});
					} else {
						errors.add(new String[]{"new-password-error-creation", "Empty Field"});
					}
				}
				
				if(confirmNewPasswordTextForm.equals("")) {
					if(lang.equals("it")) {
						errors.add(new String[]{"confirm-password-error-creation", "Campo Vuoto"});
					} else {
						errors.add(new String[]{"confirm-password-error-creation", "Empty Field"});
					}
				}
				
				if(!newPasswordTextForm.equals("") && !confirmNewPasswordTextForm.equals("") && !newPasswordTextForm.equals(confirmNewPasswordTextForm)) {
					if(lang.equals("it")) {
						errors.add(new String[]{"confirm-password-error-creation", "Mancata Corrispondenza delle Password"});
					} else {
						errors.add(new String[]{"confirm-password-error-creation", "Password Mismatch"});
					}
				}
				
				if(errors.size() > 0) {
					return errors;
				}
				
				Administrator newAdmin = new Administrator();
				newAdmin.setUsername(username);
				newAdmin.setPassword(encoder.encode(newPasswordTextForm));
				newAdmin.setIdAdministrator(createAdminId());
				newAdmin.setRoot(false);
				newAdmin.setCookieConsent(true);
				
				administratorRepository.save(newAdmin);
			} else {
				ArrayList<String[]> errors = new ArrayList<String[]>();
				
				errors.add(new String[]{"error", "/error"});
				
				return errors;
			}
		} else {
			ArrayList<String[]> errors = new ArrayList<String[]>();
			
			errors.add(new String[]{"error", "/error"});
			
			return errors;
		}
		
		ArrayList<String[]> success = new ArrayList<String[]>();
		
		success.add(new String[]{"success", "/scenario-panel"});
		
		return success;
	}
	
	/**
	 * Private helper method used to create a new {@link Application.Domain.Administrator} identifier.
	 * @return newId the new {@link Application.Domain.Administrator} identifier.
	 */
	private String createAdminId() {
		String newId = "";
		
		do {
			newId = RandomStringUtils.randomAlphanumeric(19);
		} while(administratorRepository.findAllWithCookie(newId) == 1);
		
		return newId;
	}
	
	/**
	 * Performs the logout action by invalidating the cookie for an {@link Application.Domain.Administrator}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param user_id cookie of the creator of the new {@link Application.Domain.Scenario} to perform the logout for.
	 * @param response see {@link javax.servlet.http.HttpServletResponse}.
	 * @return "redirect:/" string redirecting to the specified Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/admin-logout", method = RequestMethod.POST)
	private String adminLogout(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, HttpServletResponse response) {
		
		Cookie cookie = new Cookie("user_id", user_id);
		cookie.setMaxAge(0);
		
		response.addCookie(cookie);
		
		return "redirect:/";
	}
}
