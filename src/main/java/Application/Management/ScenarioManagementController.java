package Application.Management;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import Application.Domain.Administrator;
import Application.Domain.Narrative;
import Application.Domain.Question;
import Application.Domain.Quiz;
import Application.Domain.ReferralCode;
import Application.Domain.ReferralCodeScenario;
import Application.Domain.Scenario;
import Application.Exception.NoQuizFoundException;
import Application.Exception.PictureUploadException;
import Application.Repository.AdministratorRepository;
import Application.Repository.NarrativeRepository;
import Application.Repository.QuestionRepository;
import Application.Repository.QuizRepository;
import Application.Repository.ReferralCodeRepository;
import Application.Repository.ReferralCodeScenarioRepository;
import Application.Repository.ScenarioRepository;
import Application.Repository.VisionRepository;

/**
 * Controller class for managing {@link Application.Domain.Scenario}s.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes({"scenarioId"})
@Controller
public class ScenarioManagementController {
	
	/**
	 * Private instance of {@link Application.Repository.ScenarioRepository}.
	 */
	private final ScenarioRepository scenarioRepository;
	
	/**
	 * Private instance of {@link Application.Repository.ScenarioRepository}.
	 */
	private final NarrativeRepository narrativeRepository;
	
	/**
	 * Private instance of {@link Application.Repository.QuizRepository}.
	 */
	private final QuizRepository quizRepository;
	
	/**
	 * Private instance of {@link Application.Repository.QuestionRepository}.
	 */
	private final QuestionRepository questionRepository;
	
	/**
	 * Private instance of {@link Application.Repository.ReferralCodeRepository}.
	 */
	private final ReferralCodeRepository referralCodeRepository;
	
	/**
	 * Private instance of {@link Application.Repository.ReferralCodeScenarioRepository}.
	 */
	private final ReferralCodeScenarioRepository referralCodeScenarioRepository;
	
	/**
	 * Private instance of {@link Application.Repository.AdministratorRepository}.
	 */
	private final AdministratorRepository administratorRepository;
	
	/**
	 * Private instance of {@link Application.Repository.VisionRepository}.
	 */
	private final VisionRepository visionRepository;
	
	/**
	 * Constructor for the {@link Application.Management.ScenarioManagementController} class.
	 * 
	 * @param scenarioRepository instance of {@link Application.Repository.ScenarioRepository}.
	 * @param narrativeRepository instance of {@link Application.Repository.NarrativeRepository}.
	 * @param referralCodeScenarioRepository instance of {@link Application.Repository.ReferralCodeScenarioRepository}.
	 * @param quizRepository instance of {@link Application.Repository.QuizRepository}.
	 * @param questionRepository instance of {@link Application.Repository.QuestionRepository}.
	 * @param referralCodeRepository instance of {@link Application.Repository.ReferralCodeRepository}.
	 * @param administratorRepository instance of {@link Application.Repository.AdministratorRepository}.
	 * @param visionRepository instance of {@link Application.Repository.VisionRepository}.
	 */
	@Autowired
	public ScenarioManagementController(ScenarioRepository scenarioRepository, NarrativeRepository narrativeRepository, ReferralCodeScenarioRepository referralCodeScenarioRepository,
			QuizRepository quizRepository, QuestionRepository questionRepository, ReferralCodeRepository referralCodeRepository, AdministratorRepository administratorRepository,
			VisionRepository visionRepository) {
		this.scenarioRepository = scenarioRepository;
		this.narrativeRepository = narrativeRepository;
		this.quizRepository = quizRepository;
		this.questionRepository = questionRepository;
		this.referralCodeRepository = referralCodeRepository;
		this.referralCodeScenarioRepository = referralCodeScenarioRepository;
		this.administratorRepository = administratorRepository;
		this.visionRepository = visionRepository;
	}
	
	/**
	 * Returns the page for creating a new {@link Application.Domain.Scenario}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @return "scenario-management" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/scenario-management", method = {RequestMethod.GET, RequestMethod.POST})
	public String getScenarioManagementPage(Model model) {
		
		List<Narrative> narratives = narrativeRepository.findAll();
		
		model.addAttribute("narratives", narratives);
		model.addAttribute("coupableScenarios", scenarioRepository.findAllWithoutScenarioGroup(narratives.get(0).getName(), narratives.get(0).getLang()));
		model.addAttribute("referral_codes", referralCodeRepository.findAll());
		
		return "scenario-management";
	}
	
	/**
	 * Implements the creation of a new {@link Application.Domain.Scenario}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param description String, description of the new {@link Application.Domain.Scenario}.
	 * @param idNarrative Integer, identifier of the {@link Application.Domain.Narrative} to which the new {@link Application.Domain.Scenario} belongs.
	 * @param title String, title of the new {@link Application.Domain.Scenario}.
	 * @param img MultipartFile, cover picture of the new {@link Application.Domain.Scenario}.
	 * @param scenarioGroup Integer, identifier of the group to which the new {@link Application.Domain.Scenario} belongs (used to solve localizations.
	 * @param firstDimName String, name of the first dimension of the new {@link Application.Domain.Scenario}.
	 * @param firstDimDesc String, description of the first dimension of the new {@link Application.Domain.Scenario}.
	 * @param secondDimName String, name of the first dimension of the new {@link Application.Domain.Scenario}.
	 * @param secondDimDesc String, description of the first dimension of the new {@link Application.Domain.Scenario}.
	 * @param imgCredits String, URL to the author of the picture used as cover image for a {@link Application.Domain.Scenario}.
	 * @param referral_code String, {@link Application.Domain.ReferralCode#value} of the {@link Application.Domain.ReferralCode} to use for the new {@link Application.Domain.Scenario}.
	 * @param user_id String, cookie of the creator of the new {@link Application.Domain.Scenario}.
	 * @return "redirect:/scenario-panel" string redirecting to the specified Thymeleaf template to parse and return to the client.
	 * @throws PictureUploadException  PictureUploadException, exception in uploading the cover image for the new {@link Application.Domain.Scenario}.
	 */
	@RequestMapping(value = "/create-scenario", method = RequestMethod.POST)
	public String createScenario(Model model, @ModelAttribute("scenarioDescription") String description, @RequestParam("narrative") int idNarrative,
			@RequestParam("scenarioName") String title, @RequestParam("img") MultipartFile img, @RequestParam(name = "coupleScenario", required = false, defaultValue = "0") int scenarioGroup,
			@RequestParam("scenario-first-dim-name") String firstDimName, @RequestParam("scenario-first-dim-desc") String firstDimDesc, @RequestParam("scenario-second-dim-name") String secondDimName,
			@RequestParam("scenario-second-dim-desc") String secondDimDesc, @RequestParam("imgCredits") String imgCredits,
			@RequestParam("referral_code") int referral_code, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id) throws PictureUploadException {
		
		Optional<Administrator> tempAdmin = administratorRepository.findByCookie(user_id);
		if(!tempAdmin.isPresent()) {
			return "redirect:/error";
		}
		
		Scenario newScenario = new Scenario();
		
		Narrative narrative = narrativeRepository.findById(idNarrative).get();
		newScenario.setNarrative(narrative);
		newScenario.setDescription(description);
		newScenario.setTitle(title);
		newScenario.setFirstDimName(firstDimName);
		newScenario.setFirstDimDesc(firstDimDesc);
		newScenario.setSecondDimName(secondDimName);
		newScenario.setSecondDimDesc(secondDimDesc);
		newScenario.setCreator(new Administrator(user_id));
		newScenario.setImgCredits(imgCredits);
		
		if(scenarioGroup == 0) {
			scenarioGroup = scenarioRepository.findLastScenarioGroup() + 1;
			
			Quiz newQuiz = new Quiz("temp");
			
			quizRepository.save(newQuiz);
			
			newScenario.setQuiz(quizRepository.findLastQuiz().get());
		} else {
			newScenario.setQuiz(scenarioRepository.findByScenarioGroup(scenarioGroup).get());
		}
		
		newScenario.setScenarioGroup(scenarioGroup);
		
		String lang = narrative.getLang();
		newScenario.setLang(lang);
		
		if(lang.equals("en")) {
			newScenario.setQuestionVisionFirst("What's your attitude with respect to the described scenario?");
		} else if(lang.equals("it")) {
			newScenario.setQuestionVisionFirst("Quale e' la tua posizione rispetto allo scenario descritto?");
		}
		
		if (!img.isEmpty()) {
			try {
				byte[] bytes = img.getBytes();
				
				newScenario.setImgPath(bytes);
			} catch (Exception e) {
				throw new PictureUploadException();
			}
		}

		scenarioRepository.save(newScenario);
		
		int scenarioId = scenarioRepository.findLastScenario().getIdScenario();
		
		model.addAttribute("scenarioId", scenarioId);
		
		ReferralCodeScenario rcs = new ReferralCodeScenario();
		
		rcs.setReferralCode(new ReferralCode(referral_code));
		rcs.setScenario(new Scenario(scenarioId));
		rcs.setReleased(false);
		
		referralCodeScenarioRepository.save(rcs);
		
		return "redirect:/scenario-panel";
	}
	
	/**
	 * Implements the creation of a new {@link Application.Domain.Narrative}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param name String, name of the new {@link Application.Domain.Narrative}.
	 * @param description String, description of the new {@link Application.Domain.Narrative}.
	 * @param lang String, language code for the new {@link Application.Domain.Narrative}.
	 * @return "fragments/narrative-management-fragment :: narrative" string, specifying the Thymeleaf fragment to parse and return to the client.
	 */
	@RequestMapping(value = "/create-narrative", method = RequestMethod.POST)
	public String createNarrative(Model model, @RequestParam("narrativeName") String name, @RequestParam("narrativeDescription") String description, @RequestParam("narrativeLang") String lang) {
		
		Narrative newNarrative = new Narrative();
		
		newNarrative.setName(name);
		newNarrative.setDescription(description);
		newNarrative.setLang(lang);
		
		narrativeRepository.save(newNarrative);
		
		model.addAttribute("narratives", narrativeRepository.findAll());
		
		return "fragments/narrative-management-fragment :: narrative";
	}
	
	/**
	 * Implements the creation of a new {@link Application.Domain.ReferralCode}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param name String, name to identify the new {@link Application.Domain.ReferralCode}. A numeric identifier is automatically assigned on creation.
	 * @return "fragments/referral-code-fragment.html :: referral_code" string, specifying the Thymeleaf fragment to parse and return to the client.
	 */
	@RequestMapping(value = "/create-referral-code", method = RequestMethod.POST)
	public String createReferralCode(Model model, @RequestParam("referralName") String name) {
		
		ReferralCode rfc = new ReferralCode();
		String referral_code = null;
		
		do {
			referral_code = RandomStringUtils.randomAlphanumeric(15);
		} while(referralCodeRepository.findByReferral(referral_code).isPresent());
		
		rfc.setName(name);
		rfc.setValue(referral_code);
		
		referralCodeRepository.save(rfc);
		
		model.addAttribute("referral_codes", referralCodeRepository.findAll());
		
		return "fragments/referral-code-fragment.html :: referral_code";
	}
	
	/**
	 * Returns the {@link Application.Domain.Scenario}s from a given {@link Application.Domain.Narrative}, in a certain language, for which exists an alternative localization.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param narrativeName String, name of the {@link Application.Domain.Narrative}.
	 * @param narrativeLang String, language code of the {@link Application.Domain.Narrative}.
	 * @return "fragments/coupable-scenarios-fragment :: coupable" string, specifying the Thymeleaf fragment to parse and return to the client.
	 */
	@RequestMapping(value = "/retrieve-coupable-scenarios", method = RequestMethod.GET)
	public String getCoupableScenarios(Model model, @RequestParam("narrativeName") String narrativeName, @RequestParam("narrativeLang") String narrativeLang) {

		model.addAttribute("coupableScenarios", scenarioRepository.findAllWithoutScenarioGroup(narrativeName, narrativeLang));
		
		return "fragments/coupable-scenarios-fragment :: coupable";
	}
	
	/**
	 * Returns the page for viewing all of the {@link Application.Domain.Scenario}s created by an {@link Application.Domain.Administrator}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param user_id String, identifier of an {@link Application.Domain.Administrator}.
	 * @return "scenario-panel" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/scenario-panel", method = {RequestMethod.GET, RequestMethod.POST})
	public String getScenarioPanel(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id) {

		List<Scenario> scenarios = (List<Scenario>) scenarioRepository.findByCreator(user_id);
		
		Optional<Administrator> tempAdmin = administratorRepository.findByCookie(user_id);
		
		if(!tempAdmin.isPresent()) {
			return "redirect:/error";
		}
		
		model.addAttribute("isRoot", tempAdmin.get().isRoot());
		model.addAttribute("scenarios", scenarios);
		return "scenario-panel";
	}
	
	/**
	 * Returns the page to edit an existing {@link Application.Domain.Scenario}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param idScenario Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @return "scenario-edit" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/scenario-edit", method = RequestMethod.POST)
	public String editScenario(Model model, @RequestParam("selectedScenario")int idScenario) {
		Optional<Scenario> tempScenario = scenarioRepository.findById(idScenario);
		if(!tempScenario.isPresent()) {
			return "redirect:/scenario-panel";
		}
		
		Scenario s = tempScenario.get();
		if(s.isPublished()) {
			return "redirect:scenario-panel";
		}
		model.addAttribute("scenario", tempScenario.get());
		return "scenario-edit";
	}
	
	/**
	 * Updates the status of a {@link Application.Domain.Scenario} to 'Published'.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param idScenario Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @return "redirect:/scenario-panel" string redirecting to the specified Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/publishScenario", method = RequestMethod.POST)
	public String publishScenario(Model model, @RequestParam("selectedScenario")int idScenario) {
		Optional<Scenario> tempScenario = scenarioRepository.findById(idScenario);
		if(!tempScenario.isPresent()) {
			return "redirect:/scenario-panel";
		}
		
		Scenario s = tempScenario.get();
		if(s.isPublished()) {
			return "redirect:/scenario-panel";
		}
		
		if(visionRepository.findByScenario(idScenario).size() < 5) {
			return "redirect:/vision-management";
		}
		
		s.setPublished(true);
		for (ReferralCodeScenario rfcScenario : s.getAssociatedReferralCode()){
			if (rfcScenario.getScenario().getIdScenario() == idScenario) {
				rfcScenario.setReleased(true);
			}
		}
		scenarioRepository.save(s);
		return "redirect:/scenario-panel";
	}
	
	/**
	 * Updates the status of a {@link Application.Domain.Scenario} to 'Hidden'.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param idScenario Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @return "redirect:/scenario-panel" string redirecting to the specified Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/hideScenario", method = RequestMethod.POST)
	public String hideScenario(Model model, @RequestParam("selectedScenario")int idScenario) {
		Optional<Scenario> tempScenario = scenarioRepository.findById(idScenario);
		if(!tempScenario.isPresent()) {
			return "redirect:/scenario-panel";
		}
		
		Scenario s = tempScenario.get();
		if(!s.isPublished()) {
			return "redirect:/scenario-panel";
		}
		
		s.setPublished(false);
		scenarioRepository.save(s);
		return "redirect:/scenario-panel";
	}
	
	/**
	 * Updates the status of a {@link Application.Domain.Scenario} to 'Deleted'. 
	 * This operation does <strong> not </strong> deletes any data related to a {@link Application.Domain.Scenario}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param idScenario Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @return "redirect:/scenario-panel" string redirecting to the specified Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/deleteScenario", method = RequestMethod.POST)
	public String deleteScenario(Model model, @RequestParam("selectedScenario")int idScenario) {
		Optional<Scenario> tempScenario = scenarioRepository.findById(idScenario);
		if(!tempScenario.isPresent()) {
			return "redirect:/scenario-panel";
		}
		Scenario s = tempScenario.get();
		s.setDeleted(true);
		scenarioRepository.save(s);
		return "redirect:/scenario-panel";
	}
	
	/**
	 * Updates the details of a {@link Application.Domain.Scenario}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param editedScenario {@link Application.Domain.Scenario} which details have been changed.
	 * @param bindingResult see {@link org.springframework.validation.BindingResult}.
	 * @param s1 String, 1st {@link Application.Domain.Question} for the new {@link Application.Domain.Quiz}.
	 * @param s2 String, 2nd {@link Application.Domain.Question} for the new {@link Application.Domain.Quiz}.
	 * @param s3 String, 3rd {@link Application.Domain.Question} for the new {@link Application.Domain.Quiz}.
	 * @param s4 String, 4th {@link Application.Domain.Question} for the new {@link Application.Domain.Quiz}.
	 * @param s5 String, 5th {@link Application.Domain.Question} for the new {@link Application.Domain.Quiz}.
	 * @return "redirect:/scenario-panel" string redirecting to the specified Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/updateScenario", method = RequestMethod.POST)
	public String updateScenario(Model model, @ModelAttribute Scenario editedScenario, BindingResult bindingResult,
			@RequestParam(name = "statement0", defaultValue="") String s1, @RequestParam(name = "statement1", defaultValue="") String s2,
			@RequestParam(name = "statement2", defaultValue="") String s3, @RequestParam(name = "statement3", defaultValue="") String s4,
			@RequestParam(name = "statement4", defaultValue="") String s5) {
		if(bindingResult.hasErrors()) {
			return "redirect:/scenario-panel";
		}
		
		Scenario oldScenario = scenarioRepository.findById(editedScenario.getIdScenario()).get();
		editedScenario.setQuestionVisionFirst(oldScenario.getQuestionVisionFirst());
		/*
		 * Recupero dal db campi che comunque non sono editabili per un dato scenario
		 * */
		editedScenario.setImgPath(oldScenario.getImgPath());
		editedScenario.setNarrative(oldScenario.getNarrative());
		editedScenario.setScenarioGroup(oldScenario.getScenarioGroup());
		editedScenario.setLang(oldScenario.getLang());
		editedScenario.setQuiz(oldScenario.getQuiz());
		editedScenario.setPublished(oldScenario.isPublished());
		editedScenario.setCreator(oldScenario.getCreator());
		
		for(Question q : editedScenario.getQuiz().getQuestions()) {
			if(q.getLanguage().equals(editedScenario.getLang())){
				switch(q.getQuestionNumber()) {
				case 1:
					q.setQuestionText(s1);
					break;
				case 2:
					q.setQuestionText(s2);
					break;
				case 3:
					q.setQuestionText(s3);
					break;
				case 4:
					q.setQuestionText(s4);
					break;
				case 5:
					q.setQuestionText(s5);
					break;
				}
			}
		}
		
		scenarioRepository.save(editedScenario);
		
		return "redirect:/scenario-panel";
	}
	
	/**
	 * Returns the details of a {@link Application.Domain.Scenario} given its identifier.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param idScenario Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @return JSON formatted string containing the details of the specified {@link Application.Domain.Scenario}.
	 */
	@ResponseBody
	@RequestMapping(value = "/getScenarioDetails", method = RequestMethod.GET)
	public String getScenarioDetails(Model model, @RequestParam("idScenario")int idScenario) {
		Optional<Scenario> tempScenario = scenarioRepository.findById(idScenario);
		
		if(!tempScenario.isPresent()) {
			return "";
		}
		Scenario scenario = tempScenario.get();
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
	
	/** 
	 * Returns the {@link Application.Domain.ReferralCode} given the identifier of a {@link Application.Domain.Scenario}.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param idScenario Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @return JSON formatted string containing the details about the {@link Application.Domain.ReferralCode} of the specified {@link Application.Domain.Scenario}.
	 */
	@ResponseBody
	@RequestMapping(value = "/getScenarioRefCode", method = RequestMethod.GET)
	public String getScenarioRefCode(Model model, @RequestParam("idScenario")int idScenario) {
		Optional<Scenario> tempScenario = scenarioRepository.findById(idScenario);
		
		if(!tempScenario.isPresent()) {
			return "";
		}
		Scenario scenario = tempScenario.get();
		
		Collection<ReferralCodeScenario> rfc = scenario.getAssociatedReferralCode();
		
		JSONArray response = new JSONArray();
		JSONObject jsonRefC = new JSONObject();
		
		for (ReferralCodeScenario rf : rfc){
			
			ReferralCode rfcode = rf.getReferralCode();
			
			if(rfcode.getValue().equals("universal")) {
				continue;
			}
			
			String url = "www.trigger-game.eu/?ref=" + rfcode.getValue();
			jsonRefC.put("url", url);
			response.put(jsonRefC);
			
			jsonRefC = new JSONObject();
		}
		
		return response.toString();
	}

}
