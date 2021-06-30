package Application.Controller;

import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Domain.Feeling;
import Application.Domain.Match;
import Application.Domain.Scenario;
import Application.Domain.User;
import Application.Repository.FeelingRepository;
import Application.Repository.MatchRepository;
import Application.Repository.ScenarioRepository;
import Application.Repository.UserAchievementRepository;
import Application.Repository.UserRepository;
import Application.Utils.AchievementUtils;
import Application.Utils.CodeManagement;

/**
 * Controller class for managing the page in which the {@link Application.Domain.User}(s) express their {@link Application.Domain.Feeling}(s).
 * The page contains the model with all the feeling among which the {@link Application.Domain.User}(s) can choose.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes("scenarioId")
@Controller
public class FeelingController {

	/**
	 * Private instance of {@link Application.Repository.FeelingRepository}.
	 */
	private final FeelingRepository feelingRepository;
	
	/**
	 * Private instance of {@link Application.Repository.UserRepository}.
	 */
	private final UserRepository userRepository;
	
	/**
	 * Private instance of {@link Application.Repository.ScenarioRepository}.
	 */
	private final ScenarioRepository scenarioRepository;
	
	/**
	 * Private instance of {@link Application.Repository.MatchRepository}.
	 */
	private final MatchRepository matchRepository;
	
	/**
	 * Private instance of {@link Application.Repository.UserAchievementRepository}.
	 */
	private final UserAchievementRepository userAchievementRepository;
	
	/**
	 * Constructor for the {@link Application.Controller.FeelingController} class.
	 * 
	 * @param feelingRepository	Instance of {@link Application.Repository.FeelingRepository}.
	 * @param userRepository	Instance of {@link Application.Repository.UserRepository}.
	 * @param scenarioRepository	Instance of {@link Application.Repository.ScenarioRepository}.
	 * @param matchRepository	Instance of {@link Application.Repository.MatchRepository}.
	 * @param userAchievementRepository	Instance of {@link Application.Repository.UserAchievementRepository}.
	 */
	@Autowired
	public FeelingController(FeelingRepository feelingRepository, UserRepository userRepository, ScenarioRepository scenarioRepository,
			MatchRepository matchRepository, UserAchievementRepository userAchievementRepository) {
		this.feelingRepository = feelingRepository;
		this.userRepository = userRepository;
		this.scenarioRepository = scenarioRepository;
		this.matchRepository = matchRepository;
		this.userAchievementRepository = userAchievementRepository;
	}
	
	/**
	 * Returns the page containing the model through which the {@link Application.Domain.User}(s) can express their {@link Application.Domain.Feeling}(s).
	 * {@link Application.Domain.User}(s) share their {@link Application.Domain.Feeling}(s) twice.
	 * Once when the {@link Application.Domain.User} first join the Scenario. The second time when they have played a specific amount of {@link Application.Domain.Match}(es).
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param referral	The referral code of the {@link Application.Domain.User}.
	 * @param lang	The language chosen by the {@link Application.Domain.User}.
	 * @param request	Instance of {@link javax.servlet.http.HttpServletRequest} class.
	 * @return	"feelings" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/feelings", method = {RequestMethod.GET, RequestMethod.POST})
	public String getFeelings(Model model, @RequestParam(value = "scenarioId", required = false, defaultValue = "0") Integer scenarioId,  
			@CookieValue(value = "user_id", defaultValue = "NULL") String user_id, @SessionAttribute(value = "referral", required = false) String referral,
			@RequestParam(name="lang", defaultValue="en") String lang, HttpServletRequest request) {
		
		if(user_id.equals("NULL")) {
			return "redirect:/error";
		}
		
		if(scenarioId == 0) {
			scenarioId = (int) model.asMap().get("scenarioId");
		}
		
		Optional<User> tempUser = userRepository.findByCookie(user_id);
		if(!tempUser.isPresent()) {
			return "redirect:/error";
		}
		
		Optional<Feeling> feels = feelingRepository.findByScenarioAndUser(scenarioId, user_id);
		
		if(scenarioId == 0 || (feels.isPresent() && feels.get().getVoteAfter() != 0 && feels.get().getVoteBefore() != 0)) {
			return "redirect:/error";
		}
		
		boolean firstTimeFeelings = true;
		User user = userRepository.findByCookie(user_id).get();
		
		Collection<Match> playedMatches = matchRepository.findByUserIdAllPlayed(user_id, scenarioId);
		
		if(playedMatches.size() > 4) {
			firstTimeFeelings = false;
			
			if(CodeManagement.getValueAt(user.getAccessCode(), "FeelingsII") == 1) {
				user.setAccessCode(CodeManagement.updateCode(user.getAccessCode(), "FeelingsII", '0'));
			}
			
			model.addAttribute("firstTime", CodeManagement.getValueAt(user.getAccessCode(), "Feelings"));
			model.addAttribute("secondTime", CodeManagement.getValueAt(user.getAccessCode(), "FeelingsII"));
			
			user.setAccessCode(CodeManagement.updateCode(user.getAccessCode(), "FeelingsII", '2'));
			userRepository.save(user);
		} else {
			model.addAttribute("firstTime", CodeManagement.getValueAt(user.getAccessCode(), "Feelings"));
			model.addAttribute("secondTime", CodeManagement.getValueAt(user.getAccessCode(), "FeelingsII"));
			
			user.setAccessCode(CodeManagement.updateCode(user.getAccessCode(), "Feelings", '1'));
			userRepository.save(user);
		}
		
		model.addAttribute("referral", referral);
		model.addAttribute("firstTimeFeelings", firstTimeFeelings);
		model.addAttribute("scenarioId", scenarioId);
		model.addAttribute("lang", lang);
		
		return "feelings";
	}
	
	/**
	 * Saves the {@link Application.Domain.Feeling}(s) expressed by the {@link Application.Domain.User}(s).
	 * 
	 * @param model	Spring {@link org.springframework.ui.Model} object.
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param vote	The numerical representation of the {@link Application.Domain.Feeling} shared by the {@link Application.Domain.User}.
	 * @param opinion	The opinion about the {@link Application.Domain.Feeling} shared by the {@link Application.Domain.User}.
	 * @return	A string indicating the name of the Thymeleaf template to parse and return to the client.
	 * Possible returned strings (depending on whether the {@link Application.Domain.User} expressed their {@link Application.Domain.Feeling}(s) once or twice) are:
	 * <ul>
	 * <li> "redirect:/feed" </li>
	 * <li> "redirect:/error" </li>
	 * <li> "redirect:/vision" </li>
	 * </ul>
	 */
	@RequestMapping(value = "/saveFeelings", method = RequestMethod.POST)
	public String submitFeels(Model model, @ModelAttribute("scenarioId") Integer scenarioId, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id,
			@RequestParam("feelingsVote") int vote, @RequestParam("opinion") String opinion) {
		
		if(user_id.equals("NULL") || vote < 1 || vote > 24) {
			return "redirect:/error";
		}
		
		if(scenarioId == 0) {
			scenarioId = (int) model.asMap().get("scenarioId");
		}
		
		boolean firstTimeFeelings = !feelingRepository.findByScenarioAndUser(scenarioId, user_id).isPresent();
		
		if(firstTimeFeelings) {
			
			Optional<User> tempUser = userRepository.findByCookie(user_id);
			User user = null;
			if(tempUser.isPresent()) {
				user = tempUser.get();
			} else {
				return "redirect:/error";
			}
			
			Optional<Scenario> tempScenario = scenarioRepository.findById(scenarioId);
			Scenario scenario = null;
			if(tempScenario.isPresent()) {
				scenario = tempScenario.get();
			} else {
				return "redirect:/error";
			}
			
			Feeling newFeelings = new Feeling();
			newFeelings.setUser(user);
			newFeelings.setScenario(scenario);
			newFeelings.setVoteBefore(vote);
			newFeelings.setOpinionBefore(opinion);
			
			feelingRepository.save(newFeelings);
			
			if(!userAchievementRepository.findByCookieAndAchievement(user_id,2).isPresent()) {
				AchievementUtils.createAchievement(user_id, userAchievementRepository, 2);
			}
			
			return "redirect:/vision";
			
		} else {
			
			Optional<Feeling> tempFeelings = feelingRepository.findByScenarioAndUser(scenarioId, user_id);
			Feeling existingFeelings = null;
			
			if(tempFeelings.isPresent()) {
				existingFeelings = tempFeelings.get();
			} else {
				return "redirect:/error";
			}
			existingFeelings.setVoteAfter(vote);
			existingFeelings.setOpinionAfter(opinion);
			
			feelingRepository.save(existingFeelings);
			
			return "redirect:/feed";
		}
	}
}
