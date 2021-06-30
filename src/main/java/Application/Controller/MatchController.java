package Application.Controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Domain.Feeling;
import Application.Domain.Leaderboard;
import Application.Domain.Match;
import Application.Domain.Scenario;
import Application.Domain.User;
import Application.Domain.Vision;
import Application.Repository.FeelingRepository;
import Application.Repository.LeaderboardRepository;
import Application.Repository.MatchRepository;
import Application.Repository.ScenarioRepository;
import Application.Repository.UserAchievementRepository;
import Application.Repository.UserRepository;
import Application.Repository.VisionRepository;
import Application.Utils.AchievementUtils;
import Application.Utils.CodeManagement;
import Application.Utils.ImageFinder;

/**
 * Controller class for managing the Quick {@link Application.Domain.Match}(es).
 * A {@link Application.Domain.User} can play a {@link Application.Domain.Match} from the Feed page or the first time they play within a {@link Application.Domain.Scenario}.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes({"scenarioId", "goToInDepth"})
@Controller
public class MatchController {

	/**
	 * Private instance of {@link Application.Repository.MatchRepository}.
	 */
	private final MatchRepository matchRepository;
	
	/**
	 * Private instance of {@link Application.Repository.VisionRepository}.
	 */
	private final VisionRepository visionRepository;
	
	/**
	 * Private instance of {@link Application.Repository.LeaderboardRepository}.
	 */
	private final LeaderboardRepository leaderboardRepository;
	
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
	 * Constructor for the {@link Application.Controller.MatchController} class.
	 * 
	 * @param userRepository	Instance of {@link Application.Repository.UserRepository}.
	 * @param matchRepository	Instance of {@link Application.Repository.MatchRepository}.
	 * @param visionRepository	Instance of {@link Application.Repository.VisionRepository}.
	 * @param leaderboardRepository	Instance of {@link Application.Repository.LeaderboardRepository}.
	 * @param scenarioRepository	Instance of {@link Application.Repository.ScenarioRepository}.
	 * @param feelingRepository	Instance of {@link Application.Repository.FeelingRepository}.
	 * @param userAchievementRepository	Instance of {@link Application.Repository.UserAchievementRepository}.
	 */
	@Autowired
	public MatchController(UserRepository userRepository, MatchRepository matchRepository, VisionRepository visionRepository, 
									LeaderboardRepository leaderboardRepository, ScenarioRepository scenarioRepository, FeelingRepository feelingRepository,
									UserAchievementRepository userAchievementRepository) {
		this.userRepository = userRepository;
		this.matchRepository = matchRepository;
		this.visionRepository = visionRepository;
		this.leaderboardRepository = leaderboardRepository;
		this.scenarioRepository = scenarioRepository;
		this.feelingRepository = feelingRepository;
		this.userAchievementRepository = userAchievementRepository;
	}
	
	/**
	 * Returns the page with the Quick {@link Application.Domain.Match} ready to be played by the {@link Application.Domain.User}.
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @param goToInDepth	Whether the {@link Application.Domain.User} decided to play the In-depth version of the {@link Application.Domain.Match}.
	 * @param selectedVisionId	The unique identifier of the chosen {@link Application.Domain.Vision}.
	 * @param req	Instance of {@link javax.servlet.http.HttpServletRequest} class.
	 * @return "match" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/match", method = {RequestMethod.GET, RequestMethod.POST})
	public String getMatch(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, @RequestParam(name = "scenarioId", required = false, defaultValue = "0") int scenarioId,
			@RequestParam(value="goToInDepth", required = false, defaultValue="false") boolean goToInDepth, @RequestParam(value="vision", required = false, defaultValue="0") int selectedVisionId, HttpServletRequest req){
		
		if(user_id.equals("NULL")) {
			return "redirect:/error";
		}
		
		if(scenarioId == 0) {
			scenarioId = (int) model.asMap().get("scenarioId");
		}
		
		Match newMatch = new Match();
		Optional<Vision> tmpVision;
		Vision vision = new Vision();
		Optional<Scenario> tmpScenario = scenarioRepository.findById(scenarioId);
		Scenario scenario = new Scenario();
		
		if (goToInDepth) {
			tmpVision = visionRepository.findById(selectedVisionId);
		} else {
			tmpVision = visionRepository.findByScenarioNotOwnedAndPlayed(scenarioId, user_id);
		}
		
		Collection<Match> playedMatches = matchRepository.findByUserIdAllPlayed(user_id, scenarioId);
		
		if(tmpVision.isPresent()) {
			vision = tmpVision.get();
		} else {
			if(playedMatches.size() >= 5) {
				return "redirect:/feed";
			} else {
				return "redirect:/scenario";
			}
		}
		
		if(tmpScenario.isPresent()) {
			scenario = tmpScenario.get();
		} else {
			return "redirect:error";
		}
		
		if(playedMatches.size() >= 5) {
			
			model.addAttribute("played", true);
		}
		
		if(playedMatches.size() >= 4) {
			Optional<Feeling> feels = feelingRepository.findByScenarioAndUser(scenarioId, user_id);
			
			if(feels.isPresent() && feels.get().getVoteAfter() == 0) {
				model.addAttribute("feelingsAvailability", true);
			} else {
				model.addAttribute("feelingsAvailability", false);
			}
			
			model.addAttribute("playedEnd", true);
		} else {
			model.addAttribute("playedEnd", false);
			model.addAttribute("feelingsAvailability", false);
		}
		
		try {
			ImageFinder.findImage(vision);
		} catch(IOException e) {
			vision.setImg("/appimg/Default-Vision-Picture.png");
		}
		
		newMatch = new Match(0, userRepository.findByCookie(user_id).get(), vision, 0, 0, 0);
		
		User user = userRepository.findByCookie(user_id).get();
		
		model.addAttribute("scenarioDescription", scenarioRepository.findById(scenarioId).get().getDescription());
		model.addAttribute("new_match", newMatch);
		model.addAttribute("scenarioId", scenarioId);
		model.addAttribute("goToInDepth", goToInDepth);
		model.addAttribute("firstTime", CodeManagement.getValueAt(user.getAccessCode(), "Match"));
		model.addAttribute("firstDimName", scenario.getFirstDimName());
		model.addAttribute("firstDimDesc", scenario.getFirstDimDesc());
		model.addAttribute("secondDimName", scenario.getSecondDimName());
		model.addAttribute("secondDimDesc", scenario.getSecondDimDesc());
		
		user.setAccessCode(CodeManagement.updateCode(user.getAccessCode(), "Match", '1'));
		userRepository.save(user);
		
		return "match";
	}
	
	/**
	 * Navigate the {@link Application.Domain.User} to the correct page depending on the button pressed.
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param buttonPressed	The button pressed by the {@link Application.Domain.User} to identify which page they should be redirected to.
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @param goToInDepth	Whether the {@link Application.Domain.User} want to play the In-depth version of the {@link Application.Domain.Match}.
	 * @param feelingsAvailability	Whether the {@link Application.Domain.User} should express their {@link Application.Domain.Feeling}.
	 * @return A string indicating the name of the Thymeleaf template to parse and return to the client.
	 * Possible return strings:
	 * <ul>
	 * <li> "redirect:/feelings" </li>
	 * <li> "redirect:/error" </li>
	 * <li> "redirect:/match" </li>
	 * <li> "redirect:/in-depth" </li>
	 * </ul>
	 */
	@RequestMapping(value = "/changePage", method = RequestMethod.GET)
	public String changePage(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, @ModelAttribute("button-pressed") String buttonPressed, @ModelAttribute("scenarioId") int scenarioId,
			@RequestParam(value="goToInDepth", required=false, defaultValue="false") boolean goToInDepth, @RequestParam("feelingsAvailability") boolean feelingsAvailability) {
		
		if(user_id.equals("NULL")) {
			return "redirect:/error";
		}
		
		if(!userAchievementRepository.findByCookieAndAchievement(user_id,4).isPresent()) {
			AchievementUtils.createAchievement(user_id, userAchievementRepository, 4);
		}
		
		if(!userAchievementRepository.findByCookieAndAchievement(user_id,9).isPresent() && matchRepository.findByUserIdAllQuickMatchPlayed(user_id).size() >= 10) {
			AchievementUtils.createAchievement(user_id, userAchievementRepository, 9);
		}
		
		if(feelingsAvailability && !buttonPressed.equals("in-depth")) {
			return "redirect:/feelings";
		}
		
		if(buttonPressed.equals("again")) {
			return "redirect:/match";
		} else if(buttonPressed.equals("in-depth")) {
			model.addAttribute("goToInDepth", goToInDepth);
			return "redirect:/in-depth";
		}
		
		return "redirect:/feed";
	}
	
	/**
	 * Saves the guesses of the {@link Application.Domain.Match} played by the {@link Application.Domain.User}.
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param newMatch	The instance of {@link Application.Domain.Match} being played.
	 */
	@ResponseBody
	@RequestMapping(value = "/saveGuesses", method = RequestMethod.POST)
	public void saveMatchGuesses(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, 
			@ModelAttribute("new_match") Match newMatch) {
		
		newMatch.setPlayer(userRepository.findByCookie(user_id).get());
		
		int points = 0;
		double firstDelta = Math.abs(newMatch.getFirstDimGuess() - newMatch.getVisionChallenger().getFirstDim());
		double secondDelta = Math.abs(newMatch.getSecondDimGuess() - newMatch.getVisionChallenger().getSecondDim());
		
		if(firstDelta == 0.0) {
			points += 10;
		} else if (firstDelta == 1.0) {
			points += 5;
		} else {
			points += 0;
		}
		
		if(secondDelta == 0.0) {
			points += 10;
		} else if (secondDelta == 1.0) {
			points += 5;
		} else {
			points += 0;
		}
		
		newMatch.setPoints(points);
		matchRepository.save(newMatch);
	}
	
	/**
	 * Compute and returns the updated position and score of the {@link Application.Domain.User} when a {@link Application.Domain.Match} is played.
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param points	The scores achieved by the {@link Application.Domain.User} in the current {@link Application.Domain.Match}.
	 * @return	The information about the updates of the Leaderboard.
	 */
	@ResponseBody
	@RequestMapping(value = "/updated-position", method = RequestMethod.GET)
	public String getUpdatedPosition(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, @RequestParam("points") int points) {
		
		List<Leaderboard> completeLeaderboard = leaderboardRepository.findOrderByPoints();
		
		int initialPosition = 0;
		int updatedPosition = 0;
		int i = 0;
		
		for(Leaderboard l : completeLeaderboard) {
			if(l.getCookie().equals(user_id)) {
				initialPosition = i;
				break;
			}
			i++;
		}
		
		Leaderboard updateL = completeLeaderboard.get(initialPosition);
		updateL.setPoints(updateL.getPoints() + points);
		completeLeaderboard.set(initialPosition, updateL);
		
		Comparator<Leaderboard> leaderboardComparatorPoints = (Leaderboard l1, Leaderboard l2) -> ((Integer)l1.getPoints()).compareTo(l2.getPoints());
		Comparator<Leaderboard> leaderboardComparatorCookie = (Leaderboard l1, Leaderboard l2) -> ((String)l1.getCookie().toLowerCase()).compareTo(l2.getCookie().toLowerCase());
		Comparator<Leaderboard> leaderboardComparator = leaderboardComparatorPoints.reversed().thenComparing(leaderboardComparatorCookie);
		Collections.sort(completeLeaderboard, leaderboardComparator);
		
		int totPoints = 0;
		int j = 0;
		
		for(Leaderboard l : completeLeaderboard) {
			if(l.getCookie().equals(user_id)) {
				updatedPosition = j;
				totPoints = l.getPoints();
				break;
			}
			j++;
		}
		
		JSONObject leaderboardInfo = new JSONObject();
		leaderboardInfo.put("deltaPos", updatedPosition - initialPosition);
		leaderboardInfo.put("position", updatedPosition + 1);
		leaderboardInfo.put("totPoints", totPoints);
		
		return leaderboardInfo.toString();
	}
	
}
