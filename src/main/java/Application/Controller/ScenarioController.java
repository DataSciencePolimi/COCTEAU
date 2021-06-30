package Application.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Domain.Feeling;
import Application.Domain.Match;
import Application.Domain.Narrative;
import Application.Domain.Question;
import Application.Domain.Quiz;
import Application.Domain.Scenario;
import Application.Domain.User;
import Application.Domain.Vision;
import Application.Repository.FeelingRepository;
import Application.Repository.MatchRepository;
import Application.Repository.NarrativeRepository;
import Application.Repository.QuestionRepository;
import Application.Repository.QuizRepository;
import Application.Repository.ScenarioRepository;
import Application.Repository.UserRepository;
import Application.Repository.VisionRepository;
import Application.Utils.CodeManagement;
import Application.Utils.ImageFinder;

/**
 * Controller class for viewing {@link Application.Domain.Scenario}s and their data.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes({"limit", "scenarioId"})
@Controller
public class ScenarioController {
	
	/**
	 * Private instance of {@link Application.Repository.ScenarioRepository}.
	 */
	private final ScenarioRepository scenarioRepository;
	
	/**
	 * Private instance of {@link Application.Repository.QuizRepository}.
	 */
	private final QuizRepository quizRepository;
	
	/**
	 * Private instance of {@link Application.Repository.QuestionRepository}.
	 */
	private final QuestionRepository questionRepository;
	
	/**
	 * Private instance of {@link Application.Repository.FeelingRepository}.
	 */
	private final FeelingRepository feelingRepository;
	
	/**
	 * Private instance of {@link Application.Repository.UserRepository}.
	 */
	private final UserRepository userRepository;
	
	/**
	 * Private instance of {@link Application.Repository.MatchRepository}.
	 */
	private final MatchRepository matchRepository;
	
	/**
	 * Private instance of {@link Application.Repository.VisionRepository}.
	 */
	private final VisionRepository visionRepository;
	
	/**
	 * Private instance of {@link Application.Repository.NarrativeRepository}.
	 */
	private final NarrativeRepository narrativeRepository;
	
	
	/**
	 * Constructor for the {@link Application.Controller.ScenarioController} class.
	 * 
	 * @param scenarioRepository instance of {@link Application.Repository.ScenarioRepository}.
	 * @param userRepository instance of {@link Application.Repository.UserRepository}.
	 * @param quizRepository instance of {@link Application.Repository.QuizRepository}.
	 * @param questionRepository instance of {@link Application.Repository.QuestionRepository}.
	 * @param feelingRepository instance of {@link Application.Repository.FeelingRepository}.
	 * @param matchRepository instance of {@link Application.Repository.MatchRepository}.
	 * @param visionRepository instance of {@link Application.Repository.VisionRepository}.
	 * @param narrativeRepository instance of {@link Application.Repository.NarrativeRepository}.
	 */
	@Autowired
	public ScenarioController(ScenarioRepository scenarioRepository, UserRepository userRepository,
			QuizRepository quizRepository, QuestionRepository questionRepository, FeelingRepository feelingRepository, MatchRepository matchRepository,
			VisionRepository visionRepository, NarrativeRepository narrativeRepository) {
		this.scenarioRepository = scenarioRepository;
		this.quizRepository = quizRepository;
		this.questionRepository = questionRepository;
		this.feelingRepository = feelingRepository;
		this.userRepository = userRepository;
		this.matchRepository = matchRepository;
		this.visionRepository = visionRepository;
		this.narrativeRepository = narrativeRepository;
	}
	
	/**
	 * Returns the page for viewing and picking {@link Application.Domain.Scenario}s.<br>
	 * When a {@link Application.Domain.Scenario} is selected, depending on the progress on the platform, 
	 * a {@link Application.Domain.User} may be redirected to different pages (first vision creation, feelings, match or feed).<br>
	 * It also provides access to a {@link Application.Domain.User}'s profile page.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param response see {@link javax.servlet.http.HttpServletResponse}.
	 * @param user_id cookie of the {@link Application.Domain.User}.
	 * @param cookieConsentStatus String, "accept" or "deny", represent whether or not a {@link Application.Domain.User} has expressed consent for cookies.
	 * @param lang String, language code from locale cookie.
	 * @param referral String, invite code related to some {@link Application.Domain.Scenario}s.
	 * @param language String, language code passed as query-string parameter.
	 * @return "scenario" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/scenario", method = {RequestMethod.GET, RequestMethod.POST})
	public String getScenario(Model model, HttpServletResponse response, @CookieValue(value = "user_id", defaultValue="NULL") String user_id,
			@CookieValue(value = "cookieconsent_status", defaultValue="NULL") String cookieConsentStatus, @CookieValue(value="localeInfo", defaultValue="en") String lang,
			@RequestParam(value = "referral", required = false) String referral, @RequestParam(value = "lang", required = false) String language){

		if(language != null && (language.equals("it") || language.equals("en"))) {
			lang = language;
		}
		
		if(user_id.equals("NULL")) {
			return "redirect:/error";
		}
		
		Optional<User> tempUser = userRepository.findByCookie(user_id);
		
		User existingUser = null;
		
		if (tempUser.isPresent()) {
			existingUser = tempUser.get();
		} else {
			return "redirect:/error";
		}
		
		if(user_id.equals("NULL")) {
			user_id = existingUser.getCookie();
		}
		
		Collection<Scenario> scenarios = scenarioRepository.findByLanguageAndUser(lang, user_id, existingUser.getReferralCode().getValue());
		Collection<Narrative> narratives = narrativeRepository.findByLanguage(lang);
		Narrative firstNarrative = narratives.iterator().next();
		Collection<Scenario> firstNarrScenarios = new ArrayList<Scenario>();

		for(Scenario s : scenarios) {
			if(firstNarrative.getScenarios().contains(s)) {
				
				if(!s.getImgCredits().equals("")) {
					if(s.getImgCredits().contains("unsplash")) {
						s.setAuthorName(s.getImgCredits().split("@")[1]);
					}
				}
				
				try {
					ImageFinder.findImageScenario(s);
				} catch(IOException e) {
					s.setImg("/appimg/Default-Scenario-Picture.png");
				}
				
				Collection<Quiz> quizCollection = quizRepository.findByIdScenario(s.getIdScenario());
				
				if(quizCollection.size() < 1) {
					s.setHasCompletedQuiz(true);
				} else {
					Quiz quiz = quizCollection.iterator().next();	
					
					Collection<Question> questions = questionRepository.findUnansweredQuestions(quiz.getIdQuiz(), user_id, lang);
					
					if(questions.size() < 1) {
						s.setHasCompletedQuiz(true);
					} else {
						s.setHasCompletedQuiz(false);
					}
				}
				
				Optional<Feeling> feel = feelingRepository.findByScenarioAndUser(s.getIdScenario(), user_id);
				
				if(feel.isPresent()) {
					s.setExpressedFeelings(true);
				} else {
					s.setExpressedFeelings(false);
				}
				
				Collection<Match> playedMatches = matchRepository.findByUserIdAllPlayed(user_id, s.getIdScenario());
				
				s.setSecondFeelingsGiven(true);
				
				if(playedMatches.size() >= 5) {
					s.setHasPlayedEnoughMatches(true);
					
					Optional<Feeling> feels = feelingRepository.findByScenarioAndUser(s.getIdScenario(), user_id);
					
					if(feels.isPresent() && feels.get().getVoteAfter() == 0) {
						s.setSecondFeelingsGiven(false);
					}
				} else {
					s.setHasPlayedEnoughMatches(false);
				}
				
				Collection<Vision> createdVisions = visionRepository.findByIdUser(user_id, s.getIdScenario());
				
				if(createdVisions.size() > 0) {
					s.setHasCreatedAVision(true);
				} else {
					s.setHasCreatedAVision(false);
				}
				
				firstNarrScenarios.add(s);
			}
		}
		
		User user = userRepository.findByCookie(user_id).get();
		
		model.addAttribute("scenarios", firstNarrScenarios);
		model.addAttribute("narratives", narratives);
		model.addAttribute("scenarioId", 0);
		model.addAttribute("limit", 0);
		model.addAttribute("firstTime" , CodeManagement.getValueAt(user.getAccessCode(), "Scenario"));
		model.addAttribute("user", user);
		model.addAttribute("lang", lang);
		
		user.setAccessCode(CodeManagement.updateCode(user.getAccessCode(), "Scenario", '1'));
		userRepository.save(user);
		
		return "scenario";
	}
	
	/**
	 * Loads a fragment with the {@link Application.Domain.Scenario}s belonging to the requested {@link Application.Domain.Narrative}.
	 * This method is called when switching {@link Application.Domain.Narrative}s in the tabbed view in the {@link Application.Domain.Scenario}s page.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param id_narr Integer indicating the identifier of a {@link Application.Domain.Narrative}.
	 * @param user_id cookie of the {@link Application.Domain.User}.
	 * @param cookieConsentStatus  String, "accept" or "deny", represent whether or not a {@link Application.Domain.User} has expressed consent for cookies.
	 * @param lang String, language code from locale cookie.
	 * @return "fragments/scenario-fragment-group :: content" string, specifying the Thymeleaf fragment to parse and return to the client.
	 */
	@RequestMapping(value = "/getNarrativeScenarios", method = RequestMethod.GET)
	public String getNarrativeScenarios(Model model, @RequestParam("narrative") int id_narr, @CookieValue(value = "user_id", defaultValue="NULL") String user_id,
			@CookieValue(value = "cookieconsent_status", defaultValue="NULL") String cookieConsentStatus, @CookieValue(value="localeInfo", defaultValue="en") String lang){
		
		if(user_id.equals("NULL")) {
			// TO-DO: FIX
			return "redirect:/error";
		}
		
		Optional<User> tempUser = userRepository.findByCookie(user_id);
		
		User existingUser = null;
		
		if (tempUser.isPresent()) {
			existingUser = tempUser.get();
		} else {
			// TO-DO: FIX
			return "redirect:/error";
		}

		Collection<Scenario> scenarios = scenarioRepository.findByLanguageAndUser(lang, user_id, existingUser.getReferralCode().getValue());
		Optional<Narrative> narrative = narrativeRepository.findById(id_narr);
		Collection<Scenario> narrScenarios = new ArrayList<Scenario>();
		
		if(narrative.isPresent()) {
			
			for(Scenario s : scenarios) {
				if(narrative.get().getScenarios().contains(s)) {
					
					if(!s.getImgCredits().equals("")) {
						if(s.getImgCredits().contains("unsplash")) {
							s.setAuthorName(s.getImgCredits().split("@")[1]);
						}
					}
					
					try {
						ImageFinder.findImageScenario(s);
					} catch(IOException e) {
						s.setImg("/appimg/Default-Scenario-Picture.png");
					}
					
					Collection<Quiz> quizCollection = quizRepository.findByIdScenario(s.getIdScenario());
					
					if(quizCollection.size() < 1) {
						s.setHasCompletedQuiz(true);
					} else {
						Quiz quiz = quizCollection.iterator().next();	
						
						Collection<Question> questions = questionRepository.findUnansweredQuestions(quiz.getIdQuiz(), user_id, lang);
						
						if(questions.size() < 1) {
							s.setHasCompletedQuiz(true);
						} else {
							s.setHasCompletedQuiz(false);
						}
					}
					
					Optional<Feeling> feel = feelingRepository.findByScenarioAndUser(s.getIdScenario(), user_id);
					
					if(feel.isPresent()) {
						s.setExpressedFeelings(true);
					} else {
						s.setExpressedFeelings(false);
					}
					
					Collection<Match> playedMatches = matchRepository.findByUserIdAllPlayed(user_id, s.getIdScenario());
					
					s.setSecondFeelingsGiven(true);
					
					if(playedMatches.size() >= 5) {
						s.setHasPlayedEnoughMatches(true);
						
						Optional<Feeling> feels = feelingRepository.findByScenarioAndUser(s.getIdScenario(), user_id);
						
						if(feels.isPresent() && feels.get().getVoteAfter() == 0) {
							s.setSecondFeelingsGiven(false);
						}
					} else {
						s.setHasPlayedEnoughMatches(false);
					}
					
					Collection<Vision> createdVisions = visionRepository.findByIdUser(user_id, s.getIdScenario());
					
					if(createdVisions.size() > 0) {
						s.setHasCreatedAVision(true);
					} else {
						s.setHasCreatedAVision(false);
					}
					
					narrScenarios.add(s);
				}
			}
			
			Collection<Narrative> narratives = narrativeRepository.findByLanguage(lang);
			
			model.addAttribute("narratives", narratives);
			model.addAttribute("scenarios", narrScenarios);
			model.addAttribute("lang", lang);
		}
		
		return "fragments/scenario-fragment-group :: content";
	}
	
}
