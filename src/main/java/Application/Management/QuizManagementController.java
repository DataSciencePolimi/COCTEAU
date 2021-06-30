package Application.Management;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Domain.Question;
import Application.Domain.Quiz;
import Application.Domain.Scenario;
import Application.Repository.QuestionRepository;
import Application.Repository.QuizRepository;
import Application.Repository.ScenarioRepository;

/**
 * Controller class for managing {@link Application.Domain.Quiz} for new {@link Application.Domain.Scenario}s.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes({"scenarioId"})
@Controller
public class QuizManagementController {
	
	/**
	 * Private instance of {@link Application.Repository.QuizRepository}.
	 */
	private final QuizRepository quizRepository;
	
	/**
	 * Private instance of {@link Application.Repository.QuestionRepository}.
	 */
	private final QuestionRepository questionRepository;
	
	/**
	 * Private instance of {@link Application.Repository.ScenarioRepository}.
	 */
	private final ScenarioRepository scenarioRepository;
	
	/**
	 * Constructor for the {@link Application.Management.QuizManagementController} class.
	 * 
	 * @param quizRepository instance of {@link Application.Repository.QuizRepository}.
	 * @param questionRepository instance of {@link Application.Repository.QuestionRepository}.
	 * @param scenarioRepository instance of {@link Application.Repository.ScenarioRepository}.
	 */
	@Autowired
	public QuizManagementController(QuizRepository quizRepository, QuestionRepository questionRepository,
			ScenarioRepository scenarioRepository) {
		this.quizRepository = quizRepository;
		this.questionRepository = questionRepository;
		this.scenarioRepository = scenarioRepository;
	}
	
	/**
	 * Returns the page for managing a {@link Application.Domain.Quiz}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param scenarioId Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @return "quiz-management" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/quiz-management", method = {RequestMethod.GET, RequestMethod.POST})
	public String getQuizManagementController(Model model, @RequestParam(value = "selectedScenario", required = false, defaultValue = "0") Integer scenarioId) {
		
		if(scenarioId == 0) {
			scenarioId = (int) model.asMap().get("scenarioId");
		}
		
		model.addAttribute("scenarioId", scenarioId);
		
		return "quiz-management";
	}
	
	/**
	 * Implements the creation of a new {@link Application.Domain.Quiz} for the specified {@link Application.Domain.Scenario}.
	 * 
	 * @param question1 String, 1st {@link Application.Domain.Question} for the new {@link Application.Domain.Quiz}.
	 * @param question2 String, 2nd {@link Application.Domain.Question} for the new {@link Application.Domain.Quiz}.
	 * @param question3 String, 3rd {@link Application.Domain.Question} for the new {@link Application.Domain.Quiz}.
	 * @param question4 String, 4th {@link Application.Domain.Question} for the new {@link Application.Domain.Quiz}.
	 * @param question5 String, 5th {@link Application.Domain.Question} for the new {@link Application.Domain.Quiz}.
	 * @param scenarioId Integer indicating the identifier of the {@link Application.Domain.Scenario} to create the new {@link Application.Domain.Quiz} for.
	 * @return "redirect:/scenario-panel" string redirecting to the specified Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/submit-questions", method = {RequestMethod.GET, RequestMethod.POST})
	public String submitQuestions(@RequestParam("question1") String question1, @RequestParam("question2") String question2, @RequestParam("question3") String question3,
			@RequestParam(name = "question4", required = false, defaultValue="") String question4, @RequestParam(name = "question5", required = false, defaultValue="") String question5, @RequestParam("scenarioId") Integer scenarioId) {
		
		Collection<Quiz> newQuiz = quizRepository.findByIdScenario(scenarioId);
		Scenario scenario = scenarioRepository.findById(scenarioId).get();
		ArrayList<String> questions = new ArrayList<>(Arrays.asList(question1, question2, question3));
		
		if(!question4.equals("")) {
			questions.add(question4);
		}
		
		if(!question5.equals("")) {
			questions.add(question5);
		}
		
		int count = 1;
		for(String questionText : questions) {
			Question q = new Question();
			
			q.setQuestionText(questionText);
			q.setLanguage(scenario.getLang());
			switch(scenario.getLang()) {
				case "it":
						q.setAnswerOne("In Forte Disaccordo");
						q.setAnswerTwo("In Disaccordo");
						q.setAnswerThree("Imparziale");
						q.setAnswerFour("D'Accordo");
						q.setAnswerFive("Completamente d'Accordo");
						q.setCommentQuestionText("Hai qualche opinione a riguardo?");
					break;
				case "en":
						q.setAnswerOne("Strongly Disagree");
						q.setAnswerTwo("Disagree");
						q.setAnswerThree("Impartial");
						q.setAnswerFour("Agree");
						q.setAnswerFive("Strongly Agree");
						q.setCommentQuestionText("What's your opinion?");
					break;
			}
			
			q.setShowCommentQuestion(true);
			q.setShowQuestion(true);
			q.setQuestionNumber(count);
			q.setQuiz(newQuiz.iterator().next());
			
			count++;
			
			questionRepository.save(q);
		}
		
		return "redirect:/scenario-panel";
	}
	
}
