package Application.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import Application.Domain.Administrator;
import Application.Domain.User;
import Application.Domain.User.Gender;
import Application.Repository.AdministratorRepository;
import Application.Repository.UserRepository;

/**
 * Controller class for managing {@link Application.Domain.User} information.
 * When using COCTEAU {@link Application.Domain.User}s are asked to provide some data.
 * This step is asked only once and it is mandatory (for now) for research purposes.
 * The data collected is
 * <ul>
 * <li> Nationality </li>
 * <li> Country (if EU) </li>
 * <li> Region (if Non-EU) </li>
 * <li> Age </li>
 * <li> Gender </li>
 * <li> Education </li>
 * <li> Interest in sharing their opinions </li>
 * <li> Email address </li>
 * </ul>
 * 
 * Among these, the email is the only field that is <strong>not</strong> mandatory.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Controller
public class UserInfoController {
	
	/**
	 * Private instance of {@link Application.Repository.UserRepository}.
	 */
	private final UserRepository userRepository;
	
	/**
	 * Private instance of {@link Application.Repository.AdministratorRepository}.
	 */
	private final AdministratorRepository administratorRepository;
	
	/**
	 * Constructor for the {@link Application.Management.VisionManagementController} class.
	 * 
	 * @param userRepository instance of {@link Application.Repository.UserRepository}.
	 * @param administratorRepository instance of {@link Application.Repository.AdministratorRepository}.
	 */
	@Autowired
	public UserInfoController(UserRepository userRepository, AdministratorRepository administratorRepository){
		this.userRepository = userRepository;
		this.administratorRepository = administratorRepository;
	}
	
	/**
	 * Returns the page through which {@link Application.Domain.User}s share their data.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param user_id cookie of the {@link Application.Domain.Administrator} creating of the new {@link Application.Domain.Vision}.
	 * @param lang String, language code passed as query-string parameter.
	 * @param language String, language code from locale cookie.
	 * @return "userinfo" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/userinfo", method = RequestMethod.GET)
	public String loadPage(Model model, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id,
			@RequestParam(value="lang", defaultValue="en") String lang, @RequestParam(value = "lang", required = false) String language) {
		
		if(language != null && (language.equals("it") || language.equals("en"))) {
			lang = language;
		}
		
		if(user_id == "NULL") {
			return "redirect:/error";
		}
		
		Optional<User> tempUser = userRepository.findByCookie(user_id);
		Optional<Administrator> tempAdmin = administratorRepository.findByCookie(user_id);
		
		if(tempUser.isPresent() && (tempUser.get().getAge() != 0) && !tempAdmin.isPresent()) {
			return "redirect:/scenario";
		} else if(tempAdmin.isPresent()){
			return "redirect:/login";
		}
		model.addAttribute("lang", lang);
		
		return "userinfo";
	}
	
	/**
	 * Updates a {@link Application.Domain.User} information present on the database.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param user_id cookie of the {@link Application.Domain.Administrator} creating of the new {@link Application.Domain.Vision}.
	 * @param nationality String, either "EU" or "Non-EU".
	 * @param country String, country code for European countries.
	 * @param region String, code for the continent for people outside EU.
	 * @param age Integer, age range a {@link Application.Domain.User} belongs to.
	 * <ul>
	 * <li>1: Under 20</li>
	 * <li>2: 21 - 30</li>
	 * <li>3: 31 - 40</li>
	 * <li>4: 41 - 50</li>
	 * <li>5: 51 - 60</li>
	 * <li>6: 61 - 70</li>
	 * <li>7: 71 - 80</li>
	 * <li>8: Over 80</li>
	 * <li>9: Prefer not to say</li>
	 * </ul>
	 * @param gender String, gender of a {@link Application.Domain.User}.
	 * <ul>
	 * <li>M: Male</li>
	 * <li>F: Female</li>
	 * <li>N: Prefer not to say</li>
	 * </ul>
	 * @param education Integer, the education level of a {@link Application.Domain.User}.
	 * <ul>
	 * <li>1: Less then high school</li>
	 * <li>2: High school diploma</li>
	 * <li>3: Bachelor Degree</li>
	 * <li>4: Master Degree</li>
	 * <li>5: PhD</li>
	 * <li>6: Prefer not to say</li>
	 * </ul>
	 * @param interest Integer, the degree of interest a {@link Application.Domain.User} has in sharing their opinion.
	 * <ul>
	 * <li>1: No interest</li>
	 * <li>2: Low interest</li>
	 * <li>3: Medium interest</li>
	 * <li>4: High interest</li>
	 * <li>5: Great interest</li>
	 * <li>6: Prefer not to say</li>
	 * </ul>
	 * @param mail String, the email of a {@link Application.Domain.User}. <strong>Not</strong> mandatory.
	 * @param cookieConsent String, "accept" or "deny", represent whether or not a {@link Application.Domain.User} has expressed consent for cookies.
	 * @param lang String, language code passed as query-string parameter.
	 * @return "redirect:/scenario" string redirecting to the specified Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/saveInfo", method = RequestMethod.POST)
	public String saveInfo(Model model, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id, @RequestParam("nationality") String nationality,
			@RequestParam("country") String country, @RequestParam("region") String region, @RequestParam("age") int age, @RequestParam("gender") String gender, 
			@RequestParam("education") int education, @RequestParam("interest") int interest, @RequestParam("mail") String mail, 
			@CookieValue(value = "cookieconsent_status", defaultValue = "allow") String cookieConsent, @RequestParam(value="lang", defaultValue="en") String lang) {
		
		Optional<User> db_user = userRepository.findByCookie(user_id);
		
		if(user_id == "NULL") {
			return "redirect:/error";
		}
		
		if(db_user.isPresent()) {
			User db_user_get = db_user.get();
			
			db_user_get.setNationality(nationality);
			
			if(nationality.equals("Non-Eu")) {
				db_user_get.setRegion(region);
				db_user_get.setCountry(null);
			} else if(nationality.equals("Eu")) {
				db_user_get.setCountry(country);
				db_user_get.setRegion(null);
			}
			
			db_user_get.setGender(Gender.valueOf(gender));
			db_user_get.setAge(age);
			
			if(education != 0) {
				db_user_get.setEducation(education);
			}
			
			if(interest != 0) {
				db_user_get.setInterest(interest);
			}
			
			if(!mail.equals("")) {
				db_user_get.setMail(mail);
			}
			
			userRepository.save(db_user_get);
		}
		
		return "redirect:/scenario";
	}
}
