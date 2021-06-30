package Application.Controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Domain.Answer;
import Application.Domain.Feeling;
import Application.Domain.Match;
import Application.Domain.Quiz;
import Application.Repository.AnswerRepository;
import Application.Repository.FeelingRepository;
import Application.Repository.MatchRepository;
import Application.Repository.QuizRepository;

/**
 * Controller class for retrieving and formatting statistics data.
 * In the current implementation Google Charts is used to provide some basic graphs.<br>
 * <strong>Please note that this page is not shown on the platform as it needs to be reworked.</strong>
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes("scenarioId")
@Controller
public class StatisticsController {
	
	/**
	 * Private instance of {@link Application.Repository.QuizRepository}.
	 */
	private final QuizRepository quizRepository;
	
	/**
	 * Private instance of {@link Application.Repository.MatchRepository}.
	 */
	private final MatchRepository matchRepository;
	
	/**
	 * Private instance of {@link Application.Repository.AnswerRepository}.
	 */
	private final AnswerRepository answerRepository;
	
	/**
	 * Private instance of {@link Application.Repository.FeelingRepository}.
	 */
	private final FeelingRepository feelingsRepository;

	/**
	 * Constructor for the {@link Application.Controller.StatisticsController} class.
	 * 
	 * @param matchRepository instance of {@link Application.Repository.MatchRepository}.
	 * @param quizRepository instance of {@link Application.Repository.QuizRepository}.
	 * @param answerRepository instance of {@link Application.Repository.AnswerRepository}.
	 * @param feelingsRepository instance of {@link Application.Repository.FeelingRepository}.
	 */
	@Autowired
	public StatisticsController(MatchRepository matchRepository, QuizRepository quizRepository, AnswerRepository answerRepository, FeelingRepository feelingsRepository) {
		this.matchRepository = matchRepository;
		this.quizRepository = quizRepository;
		this.answerRepository = answerRepository;
		this.feelingsRepository = feelingsRepository;
	}
	
	/**
	 * Returns the page for viewing the statistics.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param user_id cookie of the {@link Application.Domain.User} trying to view their statistics on COCTEAU.
	 * @param scenarioId Integer, indicates the identifier of a {@link Application.Domain.Scenario}.
	 * @return "statistics" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/statistics", method = {RequestMethod.POST, RequestMethod.GET})
	private String getReward(Model model, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id, @RequestParam(name = "scenarioId", required = false, defaultValue = "0") int scenarioId) {
		
		if(scenarioId == 0) {
			scenarioId = (int) model.asMap().get("scenarioId");
		}
		
		Collection<Match> playedMatches = matchRepository.findByUserIdAllPlayed(user_id, scenarioId);
		
		if(playedMatches.size() < 3) {
			return "/access-denied";
		}
		
		model.addAttribute("scenarioId", scenarioId);
		
		return "statistics";
	}
	
	/**
	 * Return the statistics data formatted to be displayed via Google Chart.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param user_id cookie of the {@link Application.Domain.User} trying to view their statistics on COCTEAU.
	 * @param scenarioId Integer, indicates the identifier of a {@link Application.Domain.Scenario}.
	 * @return JSON formatted string with the data about a {@link Application.Domain.User}'s statistics.
	 */
	@ResponseBody
	@RequestMapping(value = "/statisticsStat", method = RequestMethod.GET)
	private String getRewardStatistics(Model model, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id, @RequestParam(name = "scenarioId", required = false, defaultValue = "0") int scenarioId) {
		
		if(scenarioId == 0) {
			scenarioId = (int) model.asMap().get("scenarioId");
		}
		
		Collection<Quiz> quizCollection = quizRepository.findByIdScenario(scenarioId);
		
		Quiz quiz = quizCollection.iterator().next();
		
		Collection<Answer> answerCollection = answerRepository.findAllQuizAnswers(quiz.getIdQuiz());
		
		ArrayList<Integer> averageAnswers = new ArrayList<>();
		ArrayList<String> cookies = new ArrayList<>();
		ArrayList<Integer> frequency = new ArrayList<>();
		
		for(Answer a : answerCollection) {
			
			if(cookies.contains(a.getUser().getCookie())) {
				int pos = cookies.indexOf(a.getUser().getCookie());
				
				averageAnswers.set(pos, averageAnswers.get(pos) + a.getAnswerNumber());
				frequency.set(pos, frequency.get(pos) + 1);
			} else {
				cookies.add(a.getUser().getCookie());
				averageAnswers.add(a.getAnswerNumber());
				frequency.add(1);
			}
		}
		
		ArrayList<Integer> x = new ArrayList<>();
		ArrayList<Integer> y = new ArrayList<>();
		int e = 0;
		
		for(String c : cookies) {
			int avgAnswer = Math.round(averageAnswers.get(cookies.indexOf(c)) / frequency.get(cookies.indexOf(c)));
			
			if(x.contains(avgAnswer)) {
				y.set(x.indexOf(avgAnswer), y.get(x.indexOf(avgAnswer)) + 1);
			} else {
				x.add(avgAnswer);
				y.add(1);
			}
			
			if(c.equals(user_id)) {
				e = avgAnswer;
			}
		}
		
		ArrayList<String> marker = new ArrayList<>();
		
		for(Integer val : x) {
			if(e == val) {
				marker.add("rgba(51, 204, 51, 1)");
			} else {
				marker.add("rgba(31, 119, 180, 1)");
			}
		}
		
		Collection<Feeling> scenarioFeelings = feelingsRepository.findByScenario(scenarioId);
		Optional<Feeling> userFeelingsTmp = feelingsRepository.findByScenarioAndUser(scenarioId, user_id);
		
		if(userFeelingsTmp.isPresent()) {
			model.addAttribute("user_bef_feel", userFeelingsTmp.get().getVoteBefore());
			model.addAttribute("user_aft_feel", userFeelingsTmp.get().getVoteAfter());
		}
		
		int totFeelingBefore = 0;
		int numberFeelingBefore = 0;
		
		for(Feeling f : scenarioFeelings) {
			if(f.getVoteBefore() != 0) {
				totFeelingBefore += f.getVoteBefore();
				numberFeelingBefore++;
			}
		}
		
		int totFeelingAfter = 0;
		int numberFeelingAfter = 0;
		
		for(Feeling f : scenarioFeelings) {
			if(f.getVoteAfter() != 0) {
				totFeelingAfter += f.getVoteAfter();
				numberFeelingAfter++;
			}
		}
		
		model.addAttribute("comm_bef_feel", Math.round(totFeelingBefore/numberFeelingBefore));
		model.addAttribute("comm_aft_feel", Math.round(totFeelingAfter/numberFeelingAfter));
		
		
		JSONObject data = new JSONObject();
		
		data.put("x", x.toArray());
		data.put("y", y.toArray());
		data.put("marker", marker.toArray());
		data.put("user_bef_feel", userFeelingsTmp.get().getVoteBefore());
		data.put("user_aft_feel", userFeelingsTmp.get().getVoteAfter());
		data.put("comm_bef_feel", Math.round(totFeelingBefore/numberFeelingBefore));
		data.put("comm_aft_feel", Math.round(totFeelingAfter/numberFeelingAfter));
		
		return data.toString();
	}
}
