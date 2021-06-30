package Application.Controller;

import java.util.Collection;
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

import Application.Domain.Answer;
import Application.Domain.Question;
import Application.Domain.Quiz;
import Application.Domain.Scenario;
import Application.Domain.User;
import Application.Repository.AnswerRepository;
import Application.Repository.QuestionRepository;
import Application.Repository.QuizRepository;
import Application.Repository.ScenarioRepository;
import Application.Repository.UserAchievementRepository;
import Application.Repository.UserRepository;
import Application.Utils.AchievementUtils;
import Application.Utils.CodeManagement;

/**
 * Controller class for viewing, answering and recording the answers to {@link Application.Domain.Quiz}zes.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes("scenarioId")
@Controller
public class QuizController {
	
	/**
	 * Private instance of {@link Application.Repository.QuizRepository}.
	 */
	private final QuizRepository quizRepository;
	
	/**
	 * Private instance of {@link Application.Repository.AnswerRepository}.
	 */
	private final AnswerRepository answerRepository;
	
	/**
	 * Private instance of {@link Application.Repository.QuestionRepository}.
	 */
	private final QuestionRepository questionRepository;
	
	/**
	 * Private instance of {@link Application.Repository.ScenarioRepository}.
	 */
	private final ScenarioRepository scenarioRepository;
	
	/**
	 * Private instance of {@link Application.Repository.UserRepository}.
	 */
	private final UserRepository userRepository;
	
	/**
	 * Private instance of {@link Application.Repository.UserAchievementRepository}.
	 */
	private final UserAchievementRepository userAchievementRepository;
	
	/**
	 * Constructor for the {@link Application.Controller.QuizController} class.
	 * 
	 * @param quizRepository instance of {@link Application.Repository.QuizRepository}.
	 * @param answerRepository instance of {@link Application.Repository.AnswerRepository}.
	 * @param questionRepository instance of {@link Application.Repository.QuestionRepository}.
	 * @param scenarioRepository instance of {@link Application.Repository.ScenarioRepository}.
	 * @param userRepository instance of {@link Application.Repository.UserRepository}.
	 * @param userAchievementRepository instance of {@link Application.Repository.UserAchievementRepository}.
	 */
	@Autowired
	public QuizController(QuizRepository quizRepository, AnswerRepository answerRepository, QuestionRepository questionRepository,
			ScenarioRepository scenarioRepository, UserRepository userRepository, UserAchievementRepository userAchievementRepository){
		this.quizRepository = quizRepository;
		this.answerRepository = answerRepository;
		this.questionRepository = questionRepository;
		this.scenarioRepository = scenarioRepository;
		this.userRepository = userRepository;
		this.userAchievementRepository = userAchievementRepository;
	}
	
	/**
	 * Returns the Questionnaire page.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param user_id cookie of the {@link Application.Domain.User}.
	 * @param scenarioId Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @param lang String, language code from locale cookie.
	 * @param language String, language code passed as query-string parameter.
	 * @return "quiz" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/quiz", method = {RequestMethod.POST, RequestMethod.GET})
	private String getQuiz(Model model, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id, 
			@RequestParam(name = "scenarioId", required = false, defaultValue = "0") int scenarioId,
			@RequestParam(name = "lang", defaultValue="en") String lang, @RequestParam(value = "lang", required = false) String language) {
		
		if(language != null && (language.equals("it") || language.equals("en"))) {
			lang = language;
		}
		
		if(scenarioId == 0) {
			scenarioId = (int) model.asMap().get("scenarioId");
		}
		
		Collection<Quiz> quizCollection = quizRepository.findByIdScenario(scenarioId);
		
		Quiz quiz = quizCollection.iterator().next();
		Collection<Question> questions = questionRepository.findUnansweredQuestions(quiz.getIdQuiz(), user_id, lang);
		
		Collection<Question> allQuestions = questionRepository.findAllByQuiz(quiz.getIdQuiz(), lang);
		
		if(user_id == "NULL" || quizCollection.size() < 1) {
			return "redirect:/error";
		}
		
		if(questions.size() < 1) {
			return "redirect:/scenario";
		}
		
		float progress = (float) (1.0 - (((float)questions.size()-1.0) / (float)allQuestions.size()));
		User user = userRepository.findByCookie(user_id).get();
		
		Scenario s = scenarioRepository.findById(scenarioId).get();
		
		model.addAttribute("scenarioTitle", s.getTitle());
		model.addAttribute("scenarioDescription", s.getDescription());
		model.addAttribute("scenarioId", scenarioId);
		model.addAttribute("question", questions.iterator().next());
		model.addAttribute("toBeShown", questions.size() - 1);
		model.addAttribute("progress", (int)(100 * progress));
		model.addAttribute("firstTime", CodeManagement.getValueAt(user.getAccessCode(), "Quiz"));
		
		user.setAccessCode(CodeManagement.updateCode(user.getAccessCode(), "Quiz", '1'));
		userRepository.save(user);
		
		return "quiz";
	}
	
	/**
	 * Returns the fragment with the current {@link Application.Domain.Question} to be answered.
	 * This function is called multiple times to load the different {@link Application.Domain.Question}s
	 * regardless of where the user left off.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param user_id cookie of the {@link Application.Domain.User}.
	 * @param question_id Integer indicating the identifier of a {@link Application.Domain.Question} for which to record an {@link Application.Domain.Answer}.
	 * @param answer_number Integer indicating the identifier of the {@link Application.Domain.Answer} given.
	 * @param comment String, additional comment a {@link Application.Domain.User} can leave under a question.
	 * @param scenarioId Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @param quiz_id Integer indicating the identifier of a {@link Application.Domain.Quiz}.
	 * @param lang String, language code from locale cookie.
	 * @return "fragments/quiz-fragment :: question" string, specifying the Thymeleaf fragment to parse and return to the client.
	 */
	@RequestMapping(value = "/answerQuestion", method = RequestMethod.POST)
	private String answerQuestion(Model model, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id, @ModelAttribute("question") int question_id,
			@ModelAttribute("answer") int answer_number, @ModelAttribute("comment") String comment, @ModelAttribute("scenarioId") int scenarioId, @ModelAttribute("quiz_id") int quiz_id,
			@CookieValue(value = "localeInfo", defaultValue="en") String lang) {
		
		if(user_id == "NULL") {
			return "redirect:/error";
		}
		
		User u = null;
		Optional<User> tempU = userRepository.findByCookie(user_id);
		
		if(tempU.isPresent()) {
			u = tempU.get();
		} else {
			return "redirect:/error";
		}
		
		Question q = null;
		Optional<Question> tempQ = questionRepository.findById(question_id);
		
		if(tempQ.isPresent()) {
			q = tempQ.get();
		} else {
			return "redirect:/error";
		}
		
		Answer answer = new Answer(comment, answer_number, q, u);
		
		answerRepository.saveAndFlush(answer);
		
		Collection<Question> unansweredQuestions = questionRepository.findUnansweredQuestions(quiz_id, user_id, lang);
		
		Collection<Question> allQuestions = questionRepository.findAllByQuiz(quiz_id, lang);
		
		if(unansweredQuestions.size() == 0) {
			model.addAttribute("scenarioId", scenarioId);
			model.addAttribute("lang", lang);
			
			if(!userAchievementRepository.findByCookieAndAchievement(user_id,1).isPresent()) {
				AchievementUtils.createAchievement(user_id, userAchievementRepository, 1);
			}
			
			return "redirect:/feelings";
		}
		
		float progress = (float) (1.0 - (((float)unansweredQuestions.size()-1.0) / (float)allQuestions.size()));
		model.addAttribute("question", unansweredQuestions.iterator().next());
		model.addAttribute("scenarioId", scenarioId);
		model.addAttribute("toBeShown", unansweredQuestions.size() - 1);
		model.addAttribute("progress", (int)(100 * progress));
		
		return "fragments/quiz-fragment :: question";
	}
	
}
