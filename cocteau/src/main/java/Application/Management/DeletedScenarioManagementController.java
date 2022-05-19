package Application.Management;

import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Domain.Question;
import Application.Domain.Scenario;
import Application.Exception.NoQuizFoundException;
import Application.Repository.QuestionRepository;
import Application.Repository.ScenarioRepository;

/**
 * Controller class for managing {@link Application.Domain.Scenario} that have been marked as 'Deleted' and hidden.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes({"scenarioId"})
@Controller
public class DeletedScenarioManagementController {

	/**
	 * Private instance of {@link Application.Repository.ScenarioRepository}.
	 */
	private final ScenarioRepository scenarioRepository;
	
	/**
	 * Private instance of {@link Application.Repository.QuestionRepository}.
	 */
	private final QuestionRepository questionRepository;
	
	/**
	 * Constructor for the {@link Application.Management.DeletedScenarioManagementController} class.
	 * 
	 * @param scenarioRepository instance of {@link Application.Repository.ScenarioRepository}.
	 * @param questionRepository instance of {@link Application.Repository.QuestionRepository}.
	 */
	@Autowired
	public DeletedScenarioManagementController(ScenarioRepository scenarioRepository, QuestionRepository questionRepository) {
		this.scenarioRepository = scenarioRepository;
		this.questionRepository = questionRepository;
	}
	
	/**
	 * Loads the page for viewing deleted {@link Application.Domain.Scenario}. 
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @return "scenario-deleted" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value="/scenario-deleted", method=RequestMethod.POST)
	public String getDeletedScenarios(Model model) {
		List<Scenario> deletedScenarios = (List<Scenario>) scenarioRepository.findDeletedScenarios();
		
		model.addAttribute("deletedScenarios", deletedScenarios);
		return "scenario-deleted";
	}
	
	/**
	 * Returns the details about a given {@link Application.Domain.Scenario} specified via its identifier.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param idDeletedScenario Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @return JSON formatted string containing the information about the requested {@link Application.Domain.Scenario}.
	 */
	@ResponseBody
	@RequestMapping(value = "/getDeletedScenarioDetails", method = RequestMethod.GET)
	public String getDeletedScenarioDetails(Model model, @RequestParam("idScenario")int idDeletedScenario) {
		Optional<Scenario> tempDeleted = scenarioRepository.findDeletedById(idDeletedScenario);
		
		if(!tempDeleted.isPresent()) {
			return "";
		}
		
		Scenario scenario = tempDeleted.get();
		List<Question> questions = (List<Question>) questionRepository.findAllByQuiz(scenario.getQuiz().getIdQuiz(), scenario.getLang());
		
		if(questions.size() > 0) {
			JSONArray response = new JSONArray();
			JSONObject jsonQ = new JSONObject();
			for (Question q : questions){
				jsonQ.put("text", q.getQuestionText());
				jsonQ.put("a1", q.getAnswerOne());
				jsonQ.put("a2", q.getAnswerTwo());
				jsonQ.put("a3", q.getAnswerThree());
				jsonQ.put("a4", q.getAnswerFour());
				jsonQ.put("a5", q.getAnswerFive());
				response.put(jsonQ);
				
				jsonQ = new JSONObject();
			}
			
			return response.toString();
		} else {
			NoQuizFoundException noQuiz = new NoQuizFoundException();
			JSONObject response = new JSONObject();
			response.put("noQuiz", noQuiz.getMessage());
			return response.toString();
		}
		
	}
}
