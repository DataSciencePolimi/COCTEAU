package Application.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Domain.Feeling;
import Application.Domain.Match;
import Application.Domain.User;
import Application.Domain.Vision;
import Application.Repository.FeelingRepository;
import Application.Repository.MatchRepository;
import Application.Repository.ScenarioRepository;
import Application.Repository.UserAchievementRepository;
import Application.Repository.UserRepository;
import Application.Repository.VisionRepository;
import Application.Utils.AchievementUtils;
import Application.Utils.CodeManagement;
import Application.Utils.ImageFinder;

/**
 * Controller class for managing the page with all the {@link Application.Domain.Vision}(s).
 * The feed page contains the most recent {@link Application.Domain.Vision}(s) shared on the platform by the {@link Application.Domain.User}(s).
 * From there, it is also possible to visit the profile of other {@link Application.Domain.User}(s) and start a new {@link Application.Domain.Match}.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes({"limit", "scenarioId"})
@Controller
public class FeedController {

	/**
	 * Private instance of {@link Application.Repository.VisionRepository}.
	 */
	private final VisionRepository visionRepository;
	
	/**
	 * Private instance of {@link Application.Repository.MatchRepository}.
	 */
	private final MatchRepository matchRepository;
	
	/**
	 * Private instance of {@link Application.Repository.UserRepository}.
	 */
	private final UserRepository userRepository;
	
	/**
	 * Private instance of {@link Application.Repository.ScenarioRepository}.
	 */
	private final ScenarioRepository scenarioRepository;
	
	/**
	 * Private instance of {@link Application.Repository.FeelingRepository}.
	 */
	private final FeelingRepository feelingRepository;
	
	/**
	 * Private instance of {@link Application.Repository.UserAchievementRepository}.
	 */
	private final UserAchievementRepository userAchievementRepository;
	
	/**
	 * Constructor for the {@link Application.Controller.FeedController} class.
	 * 
	 * @param visionRepository	Instance of {@link Application.Repository.VisionRepository}.
	 * @param matchRepository	Instance of {@link Application.Repository.MatchRepository}.
	 * @param userRepository	Instance of {@link Application.Repository.UserRepository}.
	 * @param scenarioRepository	Instance of {@link Application.Repository.ScenarioRepository}.
	 * @param feelingRepository	Instance of {@link Application.Repository.FeelingRepository}.
	 * @param userAchievementRepository	Instance of {@link Application.Repository.UserAchievementRepository}.
	 */
	@Autowired
	public FeedController(VisionRepository visionRepository, MatchRepository matchRepository, UserRepository userRepository, ScenarioRepository scenarioRepository,
			FeelingRepository feelingRepository, UserAchievementRepository userAchievementRepository) {
		this.visionRepository = visionRepository;
		this.matchRepository = matchRepository;
		this.userRepository = userRepository;
		this.scenarioRepository = scenarioRepository;
		this.feelingRepository = feelingRepository;
		this.userAchievementRepository = userAchievementRepository;
	}
	
	/**
	 * Returns the page containing all the {@link Application.Domain.Vision}(s) shared by the {@link Application.Domain.User}(s) within the {@link Application.Domain.Scenario}.
	 * Only a fixed amount of {@link Application.Domain.Vision}(s) are loaded by default.
	 * The page is only accessible after all the other activities have been carried out.
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param limit	The fixed amount of {@link Application.Domain.Vision}(s) loaded.
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @return	"feed" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/feed", method = {RequestMethod.GET, RequestMethod.POST})
	public String getFeed(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, @RequestParam(name = "limit", required = false, defaultValue="0") int limit, 
			@RequestParam(name = "scenarioId", required = false, defaultValue="0") int scenarioId){
			
		if(user_id.equals("NULL")) {
			return "redirect:/error";
		}
		
		if(scenarioId == 0) {
			scenarioId = (int) model.asMap().get("scenarioId");
		}
		
		Optional<Feeling> secondFeelingNeeded = feelingRepository.findByScenarioAndUser(scenarioId, user_id);
		
		if(!secondFeelingNeeded.isPresent() || (secondFeelingNeeded.isPresent() && secondFeelingNeeded.get().getVoteAfter() == 0)) {
			return "redirect:/error";
		}
		
		Collection<Match> playedMatches = matchRepository.findByUserIdAllPlayed(user_id, scenarioId);
		
		if(playedMatches.size() > 2) {
			model.addAttribute("allowStatistics", true);
		} else {
			model.addAttribute("allowStatistics", false);
		}
		
		if(visionRepository.findByNotIdUser(user_id, scenarioId).size() > 0) {
			model.addAttribute("game", true);
		} else {
			model.addAttribute("game", false);
		}
		
		model.addAttribute("limit", 11);
		
		List<Vision> visionList = visionRepository.findByScenarioLimit(11, scenarioId);
		
		for(Vision v : visionList) {
			try {
				ImageFinder.findImage(v);
			} catch(IOException e) {
				v.setImg("/appimg/Default-Vision-Picture.png");
			}
			
			boolean found = false;
			
			for(Match m : v.getMatchOpponentCollection()) {
				if(m.getPlayer().getCookie().equals(user_id)) {
					found = true;
				}
			}
			
			if(v.getUser().getCookie().equals(user_id) || found) {
				v.setShowInDepth(false);
			} else {
				v.setShowInDepth(true);
			}
			
			User visionUser = v.getUser();
			
			if(visionUser.getProfilePicture() != null) {
				try {
					ImageFinder.findUserPicture(visionUser);
				} catch (Exception e) {
					visionUser.setImg("/appimg/Default-Profile-Picture.jpg");
				}
			} else {
				visionUser.setImg("/appimg/Default-Profile-Picture.jpg");
			}
			
			v.setUser(visionUser);
		}
		
		if(!userAchievementRepository.findByCookieAndAchievement(user_id,10).isPresent() && feelingRepository.findByUser(user_id).size() >= 3) {
			AchievementUtils.createAchievement(user_id, userAchievementRepository, 10);
		}
		
		if(!userAchievementRepository.findByCookieAndAchievement(user_id,11).isPresent()) {
			AchievementUtils.createAchievement(user_id, userAchievementRepository, 11);
		}
		
		User user = userRepository.findByCookie(user_id).get();
		model.addAttribute("survey", false);
		model.addAttribute("surveybtn", true);
		
		model.addAttribute("firstTime", CodeManagement.getValueAt(user.getAccessCode(), "Feed"));
		model.addAttribute("visions", visionList);
		model.addAttribute("missing", visionRepository.findByScenario(scenarioId).size() - 11);
		model.addAttribute("firstLoad", true);
		model.addAttribute("scenarioId", scenarioId);
		model.addAttribute("user_id", user_id);
		model.addAttribute("lang", scenarioRepository.findById(scenarioId).get().getLang());
		
		user.setAccessCode(CodeManagement.updateCode(user.getAccessCode(), "Feed", '1'));
		userRepository.save(user);
		
		return "feed";
	}
	
	/**
	 * Returns the part of the page containing the {@link Application.Domain.Vision}(s) with an increased limit.
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param limit	The fixed amount of {@link Application.Domain.Vision}(s) loaded.
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @return	"fragments/feed-fragment :: content" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/feed-fragment", method = {RequestMethod.GET, RequestMethod.POST})
	public String getFeedFragment(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, @ModelAttribute("limit") int limit, @ModelAttribute("scenarioId") int scenarioId) {
		
		if(user_id.equals("NULL")) {
			return "redirect:/error";
		}
		
		model.addAttribute("limit", limit + 12);
		
		ArrayList<Vision> total = new ArrayList<Vision>(visionRepository.findByScenario(scenarioId));
		ArrayList<Vision> all = new ArrayList<Vision>(visionRepository.findByScenarioLimit(limit + 12, scenarioId));
		ArrayList<Vision> shown = new ArrayList<Vision>(visionRepository.findByScenarioLimit(limit, scenarioId));
		ArrayList<Vision> toBeShown = new ArrayList<Vision>();
		
		for(Vision v : all) {
			if(!shown.contains(v)) {
				toBeShown.add(v);
			}
		}
		
		for(Vision v : toBeShown) {
			try {
				ImageFinder.findImage(v);
			} catch(IOException e) {
				v.setImg("/appimg/Default-Vision-Picture.png");
			}
			
			boolean found = false;
			
			for(Match m : v.getMatchOpponentCollection()) {
				if(m.getPlayer().getCookie().equals(user_id)) {
					found = true;
				}
			}
			
			if(v.getUser().getCookie().equals(user_id) || found) {
				v.setShowInDepth(false);
			} else {
				v.setShowInDepth(true);
			}
			
			User visionUser = v.getUser();
			
			if(visionUser.getProfilePicture() != null) {
				try {
					ImageFinder.findUserPicture(visionUser);
				} catch (Exception e) {
					visionUser.setImg("/appimg/Default-Profile-Picture.jpg");
				}
			} else {
				visionUser.setImg("/appimg/Default-Profile-Picture.jpg");
			}
			
			v.setUser(visionUser);
		}
		
		model.addAttribute("user_id", user_id);
		model.addAttribute("visions", toBeShown);
		model.addAttribute("missing", total.size() - all.size());
		model.addAttribute("firstLoad", false);
		
		return "fragments/feed-fragment :: content";
	}
	
}
