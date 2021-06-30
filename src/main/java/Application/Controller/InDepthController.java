package Application.Controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Domain.Match;
import Application.Domain.Scenario;
import Application.Domain.User;
import Application.Repository.MatchRepository;
import Application.Repository.ScenarioRepository;
import Application.Repository.UserAchievementRepository;
import Application.Repository.UserRepository;
import Application.Utils.AchievementUtils;
import Application.Utils.CodeManagement;
import Application.Utils.ImageFinder;

/**
 * Controller class for managing the In-depth {@link Application.Domain.Match}(es).
 * When a {@link Application.Domain.User} played a {@link Application.Domain.Match}, they can choose whether to play the In-depth version of such {@link Application.Domain.Match}. 
 * In the In-depth {@link Application.Domain.Match} the {@link Application.Domain.User} is asked for their opinion on the {@link Application.Domain.Vision} of the challenger.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes({"scenarioId", "goToInDepth"})
@Controller
public class InDepthController {

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
	 * Private instance of {@link Application.Repository.UserAchievementRepository}.
	 */
	private final UserAchievementRepository userAchievementRepository;

	/**
	 * Constructor for the {@link Application.Controller.InDepthController} class.
	 * 
	 * @param userRepository	Instance of {@link Application.Repository.UserRepository}.
	 * @param matchRepository	Instance of {@link Application.Repository.MatchRepository}.
	 * @param scenarioRepository	Instance of {@link Application.Repository.ScenarioRepository}.
	 * @param userAchievementRepository	Instance of {@link Application.Repository.UserAchievementRepository}.
	 */
	@Autowired
	public InDepthController(UserRepository userRepository, MatchRepository matchRepository, ScenarioRepository scenarioRepository,
			UserAchievementRepository userAchievementRepository) {
		this.matchRepository = matchRepository;
		this.userRepository = userRepository;
		this.scenarioRepository = scenarioRepository;
		this.userAchievementRepository = userAchievementRepository;
	}
	
	/**
	 * Returns the page containing the {@link Application.Domain.Vision} of the challenger, with all its details.
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @return	"indepth" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/in-depth", method = {RequestMethod.GET, RequestMethod.POST})
	public String getMatch(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id){
		
		if(user_id.equals("NULL")) {
			return "redirect:/error";
		}
		
		int scenarioId = (int) model.asMap().get("scenarioId");
		
		Optional<Match> tmpMatch = matchRepository.findByUserLast(user_id);
		Optional<Scenario> tmpScenario = scenarioRepository.findById(scenarioId);
		
		if(!tmpMatch.isPresent()) {
			return "redirect:/error";
		}
		
		if(!tmpScenario.isPresent()) {
			return "redirect:/error";
		}
		
		Match match = tmpMatch.get();
		Scenario scenario = tmpScenario.get();
		
		try {
			ImageFinder.findImage(match.getVisionChallenger());
		} catch(IOException e) {
			match.getVisionChallenger().setImg("/appimg/Default-Vision-Picture.png");
		}
		
		User user = userRepository.findByCookie(user_id).get();
		
		model.addAttribute("played", matchRepository.findByUserIdAllPlayed(user_id, (int) model.asMap().get("scenarioId")).size() > 5? true : false);
		model.addAttribute("match", match);
		model.addAttribute("goToInDepth", model.getAttribute("goToInDepth"));
		model.addAttribute("scenarioDescription", scenarioRepository.findById(scenarioId).get().getDescription());
		model.addAttribute("firstDimName", scenario.getFirstDimName());
		model.addAttribute("secondDimName", scenario.getSecondDimName());
		model.addAttribute("firstTime", CodeManagement.getValueAt(user.getAccessCode(), "Indepth"));
		if(CodeManagement.getValueAt(user.getAccessCode(), "Indepth") == 0) {
			user.setAccessCode(CodeManagement.updateCode(user.getAccessCode(), "Indepth", '1'));
			userRepository.save(user);
		}
		
		return "indepth";
	}
	
	/**
	 * Saves the thoughts of the {@link Application.Domain.User} who played the In-depth {@link Application.Domain.Match}.
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param match	The Quick {@link Application.Domain.Match} played by the {@link Application.Domain.User}.
	 * @param buttonPressed	The input received by the front-end when the "Submit" button is pressed.
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @return	A string indicating the name of the Thymeleaf template to parse and return to the client.
	 * Possible return strings:
	 * <ul>
	 * <li> "redirect:/feelings" </li>
	 * <li> "redirect:/error" </li>
	 * <li> "redirect:/match" </li>
	 * </ul>
	 */
	@RequestMapping(value = "/saveThoughts", method = RequestMethod.POST)
	public String saveInDepthMatch(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, @ModelAttribute("match") Match match,
			@ModelAttribute("button-pressed-indepth") String buttonPressed, @ModelAttribute("scenarioId") int scenarioId){
		
		if(user_id.equals("NULL")) {
			return "redirect:/error";
		}
		
		match.setPlayer(userRepository.findByCookie(user_id).get());
		
		if(buttonPressed.equals("check")) {
			matchRepository.save(match);
		}
		
		if(!userAchievementRepository.findByCookieAndAchievement(user_id,5).isPresent()) {
			AchievementUtils.createAchievement(user_id, userAchievementRepository, 5);
		}
		
		if(!userAchievementRepository.findByCookieAndAchievement(user_id,7).isPresent() && matchRepository.findByUserIdAllInDepthMatchPlayed(user_id).size() >= 3) {
			AchievementUtils.createAchievement(user_id, userAchievementRepository, 7);
		}
		
		Collection<Match> playedMatches = matchRepository.findByUserIdAllPlayed(user_id, scenarioId);
				
		if(playedMatches.size() == 5) {
			return "redirect:/feelings";
		} else {
			return "redirect:/match";
		}
	}
	
}
