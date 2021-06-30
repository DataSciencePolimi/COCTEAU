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
import org.springframework.web.multipart.MultipartFile;

import Application.Domain.Achievement;
import Application.Domain.Feeling;
import Application.Domain.Leaderboard;
import Application.Domain.User;
import Application.Domain.Vision;
import Application.Exception.PictureUploadException;
import Application.Repository.AchievementRepository;
import Application.Repository.AdministratorRepository;
import Application.Repository.FeelingRepository;
import Application.Repository.LeaderboardRepository;
import Application.Repository.MatchRepository;
import Application.Repository.UserAchievementRepository;
import Application.Repository.UserRepository;
import Application.Repository.VisionRepository;
import Application.Utils.AchievementUtils;
import Application.Utils.CodeManagement;
import Application.Utils.ImageFinder;
import Application.Utils.PaletteManagement;

/**
 * Controller class for viewing and managing a {@link Application.Domain.User}'s profile.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Controller
public class ProfileController {

	/**
	 * Private instance of {@link Application.Repository.UserRepository}.
	 */
	private final UserRepository userRepository;
	
	/**
	 * Private instance of {@link Application.Repository.LeaderboardRepository}.
	 */
	private final LeaderboardRepository leaderboardRepository;
	
	/**
	 * Private instance of {@link Application.Repository.VisionRepository}.
	 */
	private final VisionRepository visionRepository;
	
	/**
	 * Private instance of {@link Application.Repository.MatchRepository}.
	 */
	private final MatchRepository matchRepository;
	
	/**
	 * Private instance of {@link Application.Repository.AdministratorRepository}.
	 */
	private final AdministratorRepository administratorRepository;
	
	/**
	 * Private instance of {@link Application.Repository.FeelingRepository}.
	 */
	private final FeelingRepository feelingRepository;
	
	/**
	 * Private instance of {@link org.springframework.context.ApplicationContext}.
	 */
	private ApplicationContext context;
	
	/**
	 * Private instance of {@link Application.Repository.AchievementRepository}.
	 */
	private final AchievementRepository achievementRepository;
	
	/**
	 * Private instance of {@link Application.Repository.UserAchievementRepository}.
	 */
	private final UserAchievementRepository userAchievementRepository;
	
	
	/**
	 * Constructor of the {@link Application.Controller.ProfileController} class.
	 * 
	 * @param userRepository instance of {@link Application.Repository.UserRepository}.
	 * @param leaderboardRepository instance of {@link Application.Repository.LeaderboardRepository}.
	 * @param userAchievementRepository instance of {@link Application.Repository.UserAchievementRepository}.
	 * @param visionRepository instance of {@link Application.Repository.VisionRepository}.
	 * @param matchRepository instance of {@link Application.Repository.MatchRepository}.
	 * @param context instance of {@link org.springframework.context.ApplicationContext}.
	 * @param administratorRepository instance of {@link Application.Repository.AdministratorRepository}.
	 * @param feelingRepository instance of {@link Application.Repository.FeelingRepository}.
	 * @param achievementRepository instance of {@link Application.Repository.AchievementRepository}.
	 */
	@Autowired
	public ProfileController(UserRepository userRepository, LeaderboardRepository leaderboardRepository,  UserAchievementRepository userAchievementRepository,
			VisionRepository visionRepository, MatchRepository matchRepository, ApplicationContext context,
			AdministratorRepository administratorRepository, FeelingRepository feelingRepository, AchievementRepository achievementRepository) {
		this.userRepository = userRepository;
		this.leaderboardRepository = leaderboardRepository;
		this.visionRepository = visionRepository;
		this.matchRepository = matchRepository;
		this.administratorRepository = administratorRepository;
		this.context = context;
		this.feelingRepository = feelingRepository;
		this.achievementRepository = achievementRepository;
		this.userAchievementRepository = userAchievementRepository;
	}
	
	/**
	 * Returns the profile page of a {@link Application.Domain.User}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param user_id cookie of the {@link Application.Domain.User}.
	 * @param userEntry Integer, specifies the entry number of a {@link Application.Domain.User} (auto-increment field).
	 * If the transmitted one is different from the current {@link Application.Domain.User} it means that that {@link Application.Domain.User} 
	 * is trying to see another {@link Application.Domain.User}'s profile. Otherwise it is the {@link Application.Domain.User}'s
	 * own profile.
	 * @param scenarioId Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @param from String, specifies from which page the request is sent. If "feed" it means that a {@link Application.Domain.User} 
	 * is trying to see another {@link Application.Domain.User}'s profile. Otherwise it is the {@link Application.Domain.User}'s
	 * own profile.<br> <strong>Redundant, to be removed.</strong>
	 * @return "profile" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/profile", method = {RequestMethod.GET, RequestMethod.POST})
	public String getProfile(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, @RequestParam("userEntry") int userEntry, 
			@RequestParam(name = "scenarioId", required = false, defaultValue = "0") int scenarioId, @RequestParam("from") String from) {
		
		if(user_id.equals("NULL")) {
			return "redirect:/error";
		}
		
		if(from.equals("feed")) {
			model.addAttribute("from", true);
		} else if (from.equals("scenario")) {
			model.addAttribute("from", false);
		}
		
		Optional<User> userTmp = null;
		int currentUserEntry = userRepository.findByCookie(user_id).get().getEntryNumber();
		
		if(currentUserEntry == userEntry) {
			
			//Visito il mio profilo
			userTmp = userRepository.findByCookie(user_id);
			model.addAttribute("personal", true);
			
		} else {
			
			//Visito un altro profilo
			userTmp = userRepository.findByUserEntryNumber(userEntry);
			model.addAttribute("personal", false);
			
			if(!userAchievementRepository.findByCookieAndAchievement(user_id,6).isPresent()) {
				AchievementUtils.createAchievement(user_id, userAchievementRepository, 6);
			}
			
			//Controllo giusto per scrupolo
			if(scenarioId != 0) {
				model.addAttribute("scenarioId", scenarioId);
			}
		}
		
		if(userTmp.isPresent()) {
			User user = userTmp.get();
			
			if(user.getUsername() == null || user.getUsername().isEmpty()) {
				user.setUsername(user.getCookie());
			}
			
			if(user.getProfilePicture() != null) {
				try {
					ImageFinder.findUserPicture(user);
				} catch (Exception e) {
					user.setImg("/appimg/Default-Profile-Picture.jpg");
				}
				model.addAttribute("defaultPicture", false);
			} else {
				user.setImg("/appimg/Default-Profile-Picture.jpg");
				model.addAttribute("defaultPicture", true);
			}
			
			SCryptPasswordEncoder encoder = context.getBean(SCryptPasswordEncoder.class);
			
			if(encoder.matches(user.getCookie(), user.getPassword())) {
				model.addAttribute("defaultPassword", true);
			} else {
				model.addAttribute("defaultPassword", false);
			}
			
			Collection<Vision> recentVisions = visionRepository.findRecentVisions(user.getCookie(), 6);
			
			for(Vision v : recentVisions) {
				try {
					ImageFinder.findImage(v);
				} catch(IOException e) {
					v.setImg("/appimg/Default-Vision-Picture.png");
				}
			}

			model.addAttribute("recentVisions", recentVisions);
			
			Collection<Leaderboard> leaderboard = leaderboardRepository.findOrderByPoints();
			
			if(leaderboard.size() > 0) {
				
				int position = 1;
				int points = 0;
				boolean found = false;
				
				for(Leaderboard l : leaderboard) {
					if(l.getCookie().equals(user.getCookie())) {
						points = l.getPoints();
						found = true;
						break;
					}
					position++;
				}
				
				if(found) {
					model.addAttribute("points", points);
					model.addAttribute("position", position);
					
					model.addAttribute("playedMatches", matchRepository.findByUserId(user.getCookie()).size());
				}
				
				model.addAttribute("found", found);
			}
			
			if(!user.isActivated() && !user.getUsername().equals(user.getCookie()) && !encoder.matches(user.getCookie(), user.getPassword())) {
				user.setActivated(true);
				model.addAttribute("justActivated", true);
				
				userRepository.save(user);
			} else {
				model.addAttribute("justActivated", false);
			}
			
			int code = CodeManagement.getValueAt(user.getAccessCode(), "Profile");
			
			if(code == 0) {
				model.addAttribute("firstTime", code);
				user.setAccessCode(CodeManagement.updateCode(user.getAccessCode(), "Profile", '1'));
				userRepository.save(user);
			} else {
				model.addAttribute("firstTime", CodeManagement.getValueAt(user.getAccessCode(), "Profile"));
			}
			
			model.addAttribute("activated", user.isActivated());
			model.addAttribute("user", user);
			
			Collection<Feeling> f = feelingRepository.findByUser(user.getCookie());
			
			model.addAttribute("profileColors", PaletteManagement.getProfileColors(f));
			
			Collection<Achievement> achievedAchievements = achievementRepository.findAchievedAchievement(user_id);
			
			for(Achievement ach : achievedAchievements) {
				try {
					ImageFinder.findImageAchievement(ach);
				} catch(IOException e) {
					ach.setImg("/appimg/Default-Achievement-Picture.png");
				}
			}
			
			Collection<Achievement> notAchievedAchievements = achievementRepository.findNotAchievedAchievement(user_id);
			
			for(Achievement nach : notAchievedAchievements) {
				try {
					ImageFinder.findImageAchievement(nach);
				} catch(IOException e) {
					nach.setImg("/appimg/Default-Achievement-Picture.png");
				}
			}
			
			model.addAttribute("achievedAchievements", achievedAchievements);
			model.addAttribute("notAchievedAchievements", notAchievedAchievements);
		} else {
			
			return "redirect:/error";
		}
		
		return "profile";
	}
	
	/**
	 * Implements the creation of a {@link Application.Domain.User} account.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param newPasswordTextForm String, {@link Application.Domain.Credentials#password} for the account.
	 * @param confirmNewPasswordTextForm String, confirmation of the {@link Application.Domain.Credentials#password} for the account.
	 * @param username String, {@link Application.Domain.Credentials#username} for the account.
	 * @param cookie cookie of the {@link Application.Domain.User}.
	 * @param lang String, language code from locale cookie.
	 * @return ArrayList of String Array containing, if any, errors.
	 */
	@ResponseBody
	@RequestMapping(value = "/createProfile", method = RequestMethod.POST)
	private ArrayList<String[]> createProfile(Model model, @RequestParam("createNewPasswordTextForm") String newPasswordTextForm,  @RequestParam("createConfirmNewPasswordTextForm") String confirmNewPasswordTextForm,
			@RequestParam("createUsernameTextForm") String username, @CookieValue(value = "user_id", defaultValue="NULL") String cookie, @CookieValue(value = "localeInfo", defaultValue="en") String lang) {
	
		Optional<User> userTmp = userRepository.findByCookie(cookie);
		SCryptPasswordEncoder encoder = context.getBean(SCryptPasswordEncoder.class);
		
		if(userTmp.isPresent()) {
		
			User user = userTmp.get();
		
			ArrayList<String[]> errors = new ArrayList<String[]>();
			
			if(username.equals("")) {
				if(lang.equals("it")) {
					errors.add(new String[]{"username-error-creation", "Campo Vuoto"});
				} else {
					errors.add(new String[]{"username-error-creation", "Empty Field"});
				}
			} else if(userRepository.findByUsername(username).isPresent() || administratorRepository.findByUsername(username).isPresent()) {
				if(user.getUsername().equals(username)) {
					if(lang.equals("it")) {
						errors.add(new String[]{"username-error-creation", "Scegli un Nuovo Username"});
					} else {
						errors.add(new String[]{"username-error-creation", "Insert a New Username"});
					}
				} else {
					if(lang.equals("it")) {
						errors.add(new String[]{"username-error-creation", "Username Non Disponibile"});
					} else {
						errors.add(new String[]{"username-error-creation", "Username Already Taken"});
					}
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
			} else if(!newPasswordTextForm.equals("") && encoder.matches(newPasswordTextForm, user.getPassword())){
				if(lang.equals("it")) {
					errors.add(new String[]{"confirm-password-error-creation", "Scegli una password nuova"});
				} else {
					errors.add(new String[]{"confirm-password-error-creation", "Pick a new Password"});
				}
			}
			
			if(errors.size() > 0) {
				return errors;
			}
			
			user.setUsername(username);
			user.setPassword(encoder.encode(newPasswordTextForm));
			
			userRepository.save(user);
		}
		
		return new ArrayList<String[]>();
	}
	
	/**
	 * Updates the {@link Application.Domain.Credentials#username} of an account.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param username String, the new {@link Application.Domain.Credentials#username} to be used.
	 * @param cookie cookie of the {@link Application.Domain.User}.
	 * @param lang String, language code from locale cookie.
	 * @return ArrayList of String Array containing, if any, errors.
	 */
	@ResponseBody
	@RequestMapping(value = "/updateUsername", method = RequestMethod.POST)
	private ArrayList<String[]> updateUsername(Model model, @RequestParam("usernameTextForm") String username, @CookieValue(value = "user_id", defaultValue="NULL") String cookie, @CookieValue(value = "localeInfo", defaultValue="en") String lang) {
		
		Optional<User> userTmp = userRepository.findByCookie(cookie);
		
		if(userTmp.isPresent()) {
			User user = userTmp.get();
			
			ArrayList<String[]> errors = new ArrayList<String[]>();
			
			if(username.equals("")) {
				if(lang.equals("it")) {
					errors.add(new String[]{"username-error", "Campo Vuoto"});
				} else {
					errors.add(new String[]{"username-error", "Empty Field"});
				}
			} else if(userRepository.findByUsername(username).isPresent() || administratorRepository.findByUsername(username).isPresent()) {
				if(user.getUsername().equals(username)) {
					if(lang.equals("it")) {
						errors.add(new String[]{"username-error", "Scegli un Nuovo Username"});
					} else {
						errors.add(new String[]{"username-error", "Insert a New Username"});
					}
				} else {
					if(lang.equals("it")) {
						errors.add(new String[]{"username-error", "Username Non Disponibile"});
					} else {
						errors.add(new String[]{"username-error", "Username Already Taken"});
					}
				}
			}
			
			if(errors.size() > 0) {
				return errors;
			}
			
			user.setUsername(username);
			
			userRepository.save(user);
		}
		
		return new ArrayList<String[]>();
	}
	
	/**
	 * Updates the {@link Application.Domain.Credentials#password} of an account.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param newPasswordTextForm String, new {@link Application.Domain.Credentials#password} for the account.
	 * @param oldPasswordTextForm String, old {@link Application.Domain.Credentials#password} for the account.
	 * @param cookie cookie of the {@link Application.Domain.User}.
	 * @param confirmNewPasswordTextForm String, confirmation of the new {@link Application.Domain.Credentials#password} for the account.
	 * @param lang String, language code from locale cookie.
	 * @return ArrayList of String Array containing, if any, errors.
	 */
	@ResponseBody
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	private ArrayList<String[]> updatePassword(Model model, @RequestParam("newPasswordTextForm") String newPasswordTextForm, @RequestParam(name = "oldPasswordTextForm", required = false, defaultValue = "") String oldPasswordTextForm,
			@CookieValue(value = "user_id", defaultValue="NULL") String cookie, @RequestParam("confirmNewPasswordTextForm") String confirmNewPasswordTextForm, @CookieValue(value = "localeInfo", defaultValue="en") String lang) {
		
		Optional<User> userTmp = userRepository.findByCookie(cookie);
		SCryptPasswordEncoder encoder = context.getBean(SCryptPasswordEncoder.class);
		
		if(userTmp.isPresent()) {
		
			User user = userTmp.get();
		
			ArrayList<String[]> errors = new ArrayList<String[]>();
		
			if(newPasswordTextForm.equals("")) {
				if(lang.equals("it")) {
					errors.add(new String[]{"new-password-error", "Campo Vuoto"});
				} else {
					errors.add(new String[]{"new-password-error", "Empty Field"});
				}
			}
			
			if(confirmNewPasswordTextForm.equals("")) {
				if(lang.equals("it")) {
					errors.add(new String[]{"confirm-password-error", "Campo Vuoto"});
				} else {
					errors.add(new String[]{"confirm-password-error", "Empty Field"});
				}
			}
			
			if(oldPasswordTextForm.equals("") && !encoder.matches(user.getCookie(), user.getPassword())) {
				if(lang.equals("it")) {
					errors.add(new String[]{"old-password-error", "Campo Vuoto"});
				} else {
					errors.add(new String[]{"old-password-error", "Empty Field"});
				}
			}
			
			if(!newPasswordTextForm.equals("") && !confirmNewPasswordTextForm.equals("") && !newPasswordTextForm.equals(confirmNewPasswordTextForm)) {
				if(lang.equals("it")) {
					errors.add(new String[]{"confirm-password-error", "Mancata Corrispondenza delle Password"});
				} else {
					errors.add(new String[]{"confirm-password-error", "Password Mismatch"});
				}
			} else if(!newPasswordTextForm.equals("") && encoder.matches(newPasswordTextForm, user.getPassword())){
				if(lang.equals("it")) {
					errors.add(new String[]{"confirm-password-error", "Scegli una password nuova"});
				} else {
					errors.add(new String[]{"confirm-password-error", "Pick a new Password"});
				}
			}
			
			if(!oldPasswordTextForm.equals("") && !encoder.matches(oldPasswordTextForm, user.getPassword())) {
				if(lang.equals("it")) {
					errors.add(new String[]{"old-password-error", "Mancata Corrispondenza delle Password"});
				} else {
					errors.add(new String[]{"old-password-error", "Password Mismatch"});
				}
			}
			
			if(errors.size() > 0) {
				return errors;
			}
		
			user.setPassword(encoder.encode(newPasswordTextForm));
			
			userRepository.save(user);
		}
		
		return new ArrayList<String[]>();
	}
	
	/**
	 * Updates the {@link Application.Domain.Credentials#password} of an account.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param img MultipartFile, new {@link Application.Domain.User} profile picture.
	 * @param cookie cookie of the {@link Application.Domain.User}.
	 * @return "fragments/profile-fragments :: picture" string, specifying the Thymeleaf fragment to parse and return to the client.
	 * @throws PictureUploadException PictureUploadException, exception in uploading the profile picture for the new {@link Application.Domain.User} account.
	 */
	@RequestMapping(value = "/updateProfilePicture", method = RequestMethod.POST)
	private String updateProfilePicture(Model model, @RequestParam("newProfilePicture") MultipartFile img, @CookieValue(value = "user_id", defaultValue="NULL") String cookie) throws PictureUploadException {

		Optional<User> userTmp = userRepository.findByCookie(cookie);
		
		if(userTmp.isPresent()) {
			User user = userTmp.get();
			
			if (!img.isEmpty()) {
				try {
					byte[] bytes = img.getBytes();
					
					user.setProfilePicture(bytes);
				} catch (Exception e) {
					throw new PictureUploadException();
				}
				
				userRepository.save(user);
				model.addAttribute("defaultPicture", false);
			} else {
				model.addAttribute("defaultPicture", true);
			}
		}
		
		User userUp = userRepository.findByCookie(cookie).get();
		try {
			ImageFinder.findUserPicture(userUp);
		} catch (Exception e) {
			userUp.setImg("/appimg/Default-Profile-Picture.jpg");
		}
		
		model.addAttribute("user", userUp);
		model.addAttribute("personal", true);
		
		return "fragments/profile-fragments :: picture";
	}
	
	/**
	 * Performs the logout action by invalidating the cookie for a {@link Application.Domain.User}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param user_id cookie of the {@link Application.Domain.User}.
	 * @param response see {@link javax.servlet.http.HttpServletResponse}.
	 * @return "redirect:/" string redirecting to the specified Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	private String logout(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id,	HttpServletResponse response) {
		
		Cookie cookie = new Cookie("user_id", user_id);
		cookie.setMaxAge(0);
		
		response.addCookie(cookie);
		
		return "redirect:/";
	}
	
}
