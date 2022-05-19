package Application.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import Application.Domain.Achievement;
import Application.Domain.Leaderboard;
import Application.Domain.User;
import Application.Domain.Vision;
import Application.Exception.PictureUploadException;
import Application.Repository.AchievementRepository;
import Application.Repository.LeaderboardRepository;
import Application.Repository.UserAchievementRepository;
import Application.Repository.UserRepository;
import Application.Repository.VisionRepository;
import Application.Utils.AchievementUtils;
import Application.Utils.ImageFinder;

@SessionAttributes({"logged", "lang"})
@Controller
public class ProfileController {
	
	private final UserRepository userRepository;
	private final LeaderboardRepository leaderboardRepository;
	private final VisionRepository visionRepository;
	private final AchievementRepository achievementRepository;
	private final UserAchievementRepository userAchievementRepository;

	@Autowired
	public ProfileController(UserRepository userRepository, LeaderboardRepository leaderboardRepository, UserAchievementRepository userAchievementRepository,
			VisionRepository visionRepository, AchievementRepository achievementRepository) {
		this.userRepository = userRepository;
		this.leaderboardRepository = leaderboardRepository;
		this.visionRepository = visionRepository;
		this.achievementRepository = achievementRepository;
		this.userAchievementRepository = userAchievementRepository;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String getProfile(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, @RequestParam(value = "entry-number", defaultValue = "-1") int entryNumber,
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
		
		if(user_id.equals("NULL")) {
			return "redirect:/error";
		}
		
		Optional<User> userTmp = null;
		
		model.addAttribute("achievementAchieved", "");
		
		if(entryNumber == -1) { // PROFILO PERSONALE
			model.addAttribute("personal", true);
			
			userTmp = userRepository.findByCookie(user_id);
		} else { // PROFILO ALTRUI
			model.addAttribute("personal", false);
			
			userTmp = userRepository.findByUserEntryNumber(entryNumber);
			
			if(!userAchievementRepository.findByCookieAndAchievement(user_id, 6).isPresent() && (boolean) model.asMap().get("logged")) {
				AchievementUtils.createAchievement(user_id, userAchievementRepository, 6);
				model.addAttribute("achievementAchieved", "You achieved '" + achievementRepository.findById(6).get().getTitle() + "'");
			}
		}
		
		if(userTmp.isPresent()) {
			User user = userTmp.get();
			
			if(user.getProfilePicture() != null) {
				try {
					ImageFinder.findUserPicture(user);
				} catch (Exception e) {
					user.setImg("/assets/default-profile-picture.jpg");
				}
			} else {
				user.setImg("/assets/default-profile-picture.jpg");
			}
			
			Collection<Vision> recentVisions = visionRepository.findRecentVisions(user.getCookie(), 6);
			
			for(Vision v : recentVisions) {
				try {
					ImageFinder.findImage(v);
				} catch(IOException e) {
					v.setImg("/assets/default-vision-picture.jpg");
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
//					model.addAttribute("playedMatches", matchRepository.findByUserId(user.getCookie()).size());
				}
			}
			
			model.addAttribute("user", user);
			
			Collection<Achievement> achievedAchievements = achievementRepository.findAchievedAchievement(user.getCookie());
			ArrayList<ArrayList<Achievement>> achievements = new ArrayList<ArrayList<Achievement>>();
			ArrayList<Achievement> tmpArray = new ArrayList<Achievement>();
			int count = 0;
			
			for(Achievement a : achievedAchievements) {
				
				a.setAchieved(true);
				tmpArray.add(a);
				count++;
				
				if(count % 3 == 0) {
					achievements.add(tmpArray);
					tmpArray = new ArrayList<Achievement>();
				}
			}
			
			Collection<Achievement> notAchievedAchievements = achievementRepository.findNotAchievedAchievement(user.getCookie());
			
			for(Achievement a : notAchievedAchievements) {
				
				a.setAchieved(false);
				tmpArray.add(a);
				count++;
				
				if(count % 3 == 0) {
					achievements.add(tmpArray);
					tmpArray = new ArrayList<Achievement>();
				}
			}
			
			if(count % 3 != 0) {
				achievements.add(tmpArray);
			}
			
			model.addAttribute("achievements", achievements);
		} else {
			
			return "redirect:/error";
		}
		
		return "profile";
	}
	
	@RequestMapping(value = "/updateProfilePicture", method = RequestMethod.POST)
	private String updateProfilePicture(Model model, @RequestParam("newProfilePicture") MultipartFile img, @CookieValue(value = "user_id", defaultValue="NULL") String cookie) throws PictureUploadException {

		Optional<User> userTmp = userRepository.findByCookie(cookie);
		
		if(userTmp.isPresent()) {
			User user = userTmp.get();
			
			if (!img.isEmpty()) {
				try {
					byte[] bytes = img.getBytes();
					
					user.setProfilePicture(bytes);
					
					userRepository.save(user);
				} catch (Exception e) {
					throw new PictureUploadException();
				}
			}
		}
		
		return "redirect:/profile";
	}
	
	@RequestMapping(value = "/logout-step", method = RequestMethod.POST)
	private String logout(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id,	HttpServletResponse response) {
		
		model.addAttribute("logged", false);
		
		Cookie cookie = new Cookie("user_id", user_id);
		cookie.setMaxAge(0);
		
		response.addCookie(cookie);
		
		return "redirect:/";
	}
	
}
