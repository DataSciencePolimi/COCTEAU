package Application.Controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Domain.Answer;
import Application.Domain.Keyword;
import Application.Domain.Match;
import Application.Domain.Narrative;
import Application.Domain.QueryWord;
import Application.Domain.Question;
import Application.Domain.Quiz;
import Application.Domain.Scenario;
import Application.Domain.User;
import Application.Domain.Vision;
import Application.Domain.VisionKeyword;
import Application.Domain.VisionQueryWord;
import Application.Exception.PictureCreationException;
import Application.Exception.TranslateException;
import Application.Exception.UnsplashConnectionException;
import Application.Repository.AchievementRepository;
import Application.Repository.AnswerRepository;
import Application.Repository.KeywordRepository;
import Application.Repository.MatchRepository;
import Application.Repository.NarrativeRepository;
import Application.Repository.QueryWordRepository;
import Application.Repository.QuestionRepository;
import Application.Repository.QuizRepository;
import Application.Repository.ReferralCodeRepository;
import Application.Repository.ScenarioRepository;
import Application.Repository.UserAchievementRepository;
import Application.Repository.UserRepository;
import Application.Repository.VisionKeywordRepository;
import Application.Repository.VisionQueryWordRepository;
import Application.Repository.VisionRepository;
import Application.Utils.AchievementUtils;
import Application.Utils.CookieManagement;
import Application.Utils.ImageFinder;
import Application.Utils.ImageProcessor;
import Application.Utils.UnsplashConnector;

@SessionAttributes({"logged", "justAchievedAchievement", "points", "lang"})
@Controller
public class ScenarioController {
	
	private final QuizRepository quizRepository;
	private final AnswerRepository answerRepository;
	private final QuestionRepository questionRepository;
	private final ScenarioRepository scenarioRepository;
	private final UserRepository userRepository;
	private final MatchRepository matchRepository;
	private final VisionRepository visionRepository;
	private final NarrativeRepository narrativeRepository;
	private final UnsplashConnector unsplashConnector;
	private final ImageProcessor imageProcessor;
	private final KeywordRepository keywordRepository;
	private final VisionKeywordRepository visionKeywordRepository;
	private final QueryWordRepository queryWordRepository;
	private final VisionQueryWordRepository visionQueryWordRepository;
	private final UserAchievementRepository userAchievementRepository;
	private final AchievementRepository achievementRepository;
	private final ReferralCodeRepository referralCodeRepository;
	private ApplicationContext context;
	
	@Autowired
	public ScenarioController(QuizRepository quizRepository, AnswerRepository answerRepository, QuestionRepository questionRepository, UserAchievementRepository userAchievementRepository,
			ScenarioRepository scenarioRepository, UserRepository userRepository, ImageProcessor imageProcessor, QueryWordRepository queryWordRepository, AchievementRepository achievementRepository,
			MatchRepository matchRepository, VisionRepository visionRepository, NarrativeRepository narrativeRepository, UnsplashConnector unsplashConnector, ReferralCodeRepository referralCodeRepository,
			KeywordRepository keywordRepository, VisionKeywordRepository visionKeywordRepository, VisionQueryWordRepository visionQueryWordRepository, ApplicationContext context){
		this.quizRepository = quizRepository;
		this.answerRepository = answerRepository;
		this.questionRepository = questionRepository;
		this.scenarioRepository = scenarioRepository;
		this.userRepository = userRepository;
		this.matchRepository = matchRepository;
		this.visionRepository = visionRepository;
		this.narrativeRepository = narrativeRepository;
		this.unsplashConnector = unsplashConnector;
		this.imageProcessor = imageProcessor;
		this.keywordRepository = keywordRepository;
		this.visionKeywordRepository = visionKeywordRepository;
		this.queryWordRepository = queryWordRepository;
		this.visionQueryWordRepository = visionQueryWordRepository;
		this.userAchievementRepository = userAchievementRepository;
		this.achievementRepository = achievementRepository;
		this.referralCodeRepository = referralCodeRepository;
		this.context = context;
	}

	@RequestMapping(value = "/scenario", method = RequestMethod.GET)
	public String getAbout(Model model, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id, @RequestParam(name = "limit", required = false, defaultValue = "-1") int limit,
			@RequestParam(name = "idScenario", required = false, defaultValue = "0") int idScenario, @RequestParam(value = "lang", required = false, defaultValue="no-lang") String lang,
			@CookieValue(value = "cookieconsent_status", defaultValue = "deny") String cookie_consent, HttpServletResponse response) {
		
		if(lang.equals("no-lang")) {
			if(model.asMap().get("lang") == null) {
				lang = "en";
				model.addAttribute("lang", lang);
			} else {
				lang = model.asMap().get("lang").toString();
			}
		} else {
			model.addAttribute("lang", lang);
		}
		
		User user = null;
		
		if((user_id.equals("NULL") || !userRepository.findByCookie(user_id).isPresent()) && !cookie_consent.equals("allow")) {
			return "redirect:/";
		} else if((user_id.equals("NULL") || !userRepository.findByCookie(user_id).isPresent()) && cookie_consent.equals("allow")) {
			SCryptPasswordEncoder encoder = context.getBean(SCryptPasswordEncoder.class);
			Cookie cookie = CookieManagement.createNewCookie(this.userRepository);
			
			Optional<Integer> currentEntry = userRepository.findMaxEntry();
			User new_user = null;
			
			if(currentEntry.isPresent()) {
				new_user = new User(cookie.getValue(), encoder.encode(cookie.getValue()), currentEntry.get() + 1);
			} else {
				new_user = new User(cookie.getValue(), encoder.encode(cookie.getValue()), 1);
			}
			
			new_user.setCookieConsent(true);
			new_user.setReferralCode(referralCodeRepository.findByReferral("universal").get());
			
			userRepository.save(new_user);
			
			response.addCookie(cookie);
			
			user = new_user;
			user_id = cookie.getValue();
		}
		
		if(model.asMap().get("logged") != null) {
			model.addAttribute("logged", (boolean) model.asMap().get("logged"));
		} else {
			model.addAttribute("logged", false);
		}
		
		if(idScenario == 0) {
			return "redirect:/";
		} else if(!scenarioRepository.findById(idScenario).get().getLang().equals(lang)) {
			Scenario rightScenario = scenarioRepository.findByScenarioGroupAndLanguage(scenarioRepository.findById(idScenario).get().getScenarioGroup(), lang);
			
			if(rightScenario != null) {
				idScenario = rightScenario.getIdScenario();
			} else {
				return "redirect:/";
			}
		}
		
		// DATA FOR QUIZ
		Collection<Quiz> quizCollection = quizRepository.findByIdScenario(idScenario);
		Quiz quiz = quizCollection.iterator().next();
		Collection<Question> questions = questionRepository.findUnansweredQuestions(quiz.getIdQuiz(), user_id, lang);
		
		model.addAttribute("questions", questions);
		
		Scenario currentScenario = scenarioRepository.findById(idScenario).get();
		
		currentScenario.setInfoTitle(currentScenario.getTitle() + "  <i type='button' class='fa-solid fa-circle-info text-cyan' data-bs-toggle='modal' data-bs-target='#scenario-info' ></i>");
		
		model.addAttribute("currentScenario", currentScenario);
		
		// DATA FOR VISIONS
		limit += 9;
		
		ArrayList<Vision> visionList = adjustVisions(new ArrayList<Vision>(visionRepository.findByScenarioLimit(limit, idScenario)), user_id);
		
		model.addAttribute("limit", limit);
		model.addAttribute("visions", visionList);
		model.addAttribute("loadMore", visionRepository.findByScenario(idScenario).size() > visionRepository.findByScenarioLimit(limit, idScenario).size());
		
		// DATA FOR VISION FILTERING FRONT END
		List<Integer> topVisionListId = visionRepository.findByMatchesPlayed(idScenario, 9);
		ArrayList<Vision> topVisionList = new ArrayList<Vision>();
		
		for(int v : topVisionListId) {
			topVisionList.add(visionRepository.findById(v).get());
		}
		
		topVisionList = adjustVisions(topVisionList, user_id);
		
		model.addAttribute("topVisions", topVisionList);
		
		ArrayList<Vision> playableVisions = new ArrayList<Vision>();
		ArrayList<Vision> tmpVisions = new ArrayList<Vision>();
		int countPlayableVisions = 0;
		
		for(Vision v : topVisionList) {
			if(v.isShowPlayButton() && !tmpVisions.contains(v)) {
				tmpVisions.add(v);
				countPlayableVisions++;
			}
		}
		
		for(Vision v : visionList) {
			if(v.isShowPlayButton() && !tmpVisions.contains(v)) {
				tmpVisions.add(v);
				countPlayableVisions++;
			}
		}
		
		if(countPlayableVisions < 9) {
			
			for(Vision v: visionRepository.findByScenarioNotOwnedAndPlayedMultiple(idScenario, user_id)) {
				if(countPlayableVisions >= 9) {
					break;
				}
				
				if(!visionList.contains(v) && !topVisionList.contains(v)) {
					playableVisions.add(v);
					countPlayableVisions++;
				}
			}
		}
		
		model.addAttribute("playableVisions", adjustVisions(playableVisions, user_id));
		
		// DATA FOR MATCH
		Optional<Vision> matchVision = visionRepository.findByScenarioNotOwnedAndPlayed(idScenario, user_id);
		
		if(!matchVision.isPresent()) {
			model.addAttribute("playable", false);
		} else {
			model.addAttribute("playable", true);
			model.addAttribute("challengeVision", adjustVision(matchVision.get(), user_id));
		}
		
		// DATA FOR SCENARIOS BOTTOM PAGE
		if(user == null) {
			user = userRepository.findByCookie(user_id).get();
		}
		Narrative narrative = narrativeRepository.findByIdScenario(idScenario);
		
		Collection<Scenario> otherScenarios = scenarioRepository.findByLanguageUserAndNarrative(lang, user_id, "universal", narrative.getIdNarrative());
		
		if(user.getReferralCode() != null) {
			otherScenarios = scenarioRepository.findByLanguageUserAndNarrative(lang, user_id, userRepository.findByCookie(user_id).get().getReferralCode().getValue(), narrative.getIdNarrative());
		}
		
		for(Scenario s : otherScenarios) {			
			try {
				ImageFinder.findImageScenario(s);
			} catch(IOException e) {
				s.setImg("/assets/default-vision-picture.jpg");
			}
		}
		
		model.addAttribute("otherScenarios", otherScenarios);
		model.addAttribute("narrative", narrative);
		model.addAttribute("achievementAchieved", "");
		model.addAttribute("pointsEarned", "");
		
		if(model.asMap().get("points") != null && (int) model.asMap().get("points") != -1) {
			switch((int) model.asMap().get("points")) {
				case 0:
					model.addAttribute("pointsEarned", "Unlucky! You earned 0 points.");
					break;
				case 5:
					model.addAttribute("pointsEarned", "Close! You earned 5 points.");
					break;
				case 10:
					model.addAttribute("pointsEarned", "Correct! You earned 10 points.");
					break;
			}
			model.addAttribute("points", -1);
		}
		
		if(!userAchievementRepository.findByCookieAndAchievement(user_id, 11).isPresent() && (boolean) model.asMap().get("logged")) {
			AchievementUtils.createAchievement(user_id, userAchievementRepository, 11);
			model.addAttribute("achievementAchieved", "You achieved '" + achievementRepository.findById(11).get().getTitle() + "'");
		}
		
		if(model.asMap().get("justAchievedAchievement") != null && (int) model.asMap().get("justAchievedAchievement") != 0) {
			model.addAttribute("achievementAchieved", "You achieved '" + achievementRepository.findById((int) model.asMap().get("justAchievedAchievement")).get().getTitle() + "'");
			model.addAttribute("justAchievedAchievement", 0);
		}
		
		return "scenario";
	}
	
	@RequestMapping(value = "/submit-quiz", method = RequestMethod.POST)
	public String submitQuiz(@RequestParam(name = "idScenario", required = false, defaultValue = "0") int idScenario, @RequestParam("answer") ArrayList<String> answers, Model model,
			@RequestParam("comment") ArrayList<String> comments, @RequestParam(value = "lang", required = false, defaultValue="en") String lang, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id) {
		
		Collection<Quiz> quizCollection = quizRepository.findByIdScenario(idScenario);
		Quiz quiz = quizCollection.iterator().next();
		Collection<Question> questions = questionRepository.findUnansweredQuestions(quiz.getIdQuiz(), user_id, lang);
		int i = 0;
		
		for(Question q : questions) {
			if(!answers.get(i).equals("")) {
				Answer a = new Answer(comments.get(i), Integer.parseInt(answers.get(i)), q, new User(user_id));
				
				answerRepository.save(a);
			}
			i++;
		}
		
		Collection<Question> unansweredQuestions = questionRepository.findUnansweredQuestions(quiz.getIdQuiz(), user_id, lang);
		
		if(unansweredQuestions.size() == 0) {
			if(!userAchievementRepository.findByCookieAndAchievement(user_id,1).isPresent() && model.asMap().containsKey("logged") && (boolean) model.asMap().get("logged")) {
				AchievementUtils.createAchievement(user_id, userAchievementRepository, 1);
				model.addAttribute("justAchievedAchievement", 1);
			}
		}
		
		return "redirect:/scenario?idScenario=" + idScenario;
	}
	
	@RequestMapping(value = "/submit-match", method = RequestMethod.POST)
	public String submitMatch(@RequestParam(name = "idScenario", required = false, defaultValue = "0") int idScenario, @CookieValue(value = "user_id", defaultValue = "NULL") String user_id,
			@RequestParam(name = "sentiment", required = false, defaultValue = "0") int sentiment, @RequestParam(name = "idVision", required = false, defaultValue = "0") int idVision,
			@RequestParam(name = "thoughts", required = false, defaultValue = "") String thoughts, @RequestParam(name = "agree", required = false, defaultValue = "") String agree, Model model) {
		
		if(sentiment > 0 && sentiment < 9) {
			Match match = new Match();
			Vision vision = visionRepository.findById(idVision).get();
			
			match.setAgree(agree);
			match.setPlayer(new User(user_id));
			match.setPlayedDate(new Date());
			match.setThoughts(thoughts);
			match.setVisionChallenger(vision);
			match.setGuessedFeeling(sentiment);
			
			// POINTS COMPUTATION
			int points = 0;
			int correctAnswer = vision.getFeelings();
			
			if(correctAnswer == sentiment) {
				points = 10;
			} else if(correctAnswer - sentiment == 1 || correctAnswer - sentiment == -1 ||
						correctAnswer == 1 && sentiment == 8 || correctAnswer == 8 && sentiment == 1) {
					points = 5;
			}
			
			match.setPoints(points);
			model.addAttribute("points", points);
			
			matchRepository.save(match);
			
			if(!userAchievementRepository.findByCookieAndAchievement(user_id, 4).isPresent() && (boolean) model.asMap().get("logged")) {
				AchievementUtils.createAchievement(user_id, userAchievementRepository, 4);
				model.addAttribute("justAchievedAchievement", 4);
			}
			
			if(!userAchievementRepository.findByCookieAndAchievement(user_id, 9).isPresent() && matchRepository.findByUserId(user_id).size() >= 10 && (boolean) model.asMap().get("logged")) {
				AchievementUtils.createAchievement(user_id, userAchievementRepository, 9);
				model.addAttribute("justAchievedAchievement", 9);
			}
		}
		
		return "redirect:/scenario?idScenario=" + idScenario;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getUnsplashPics", method = RequestMethod.GET)
	public String searchOnUnsplash(@RequestParam("keyword") String keyword, @RequestParam("pageNum") int pageNumber,
			@RequestParam("lang") String lang) throws TranslateException, UnsplashConnectionException {
		String response = "";
		response = unsplashConnector.getSearchResults(keyword, pageNumber);
		return response;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getPreselectedPics", method = RequestMethod.GET)
	public String getPreselectedPics() throws IOException {
		JSONArray cleanResults = new JSONArray();
		
		for (String f : new File(Application.Application.PRES_PICT).list()) {
			JSONObject cleanItem = new JSONObject();
			
			cleanItem.put("picID", f);
			cleanItem.put("url", "preselectedPictures/" + f);
			
			cleanResults.put(cleanItem);
		}
		
		return cleanResults.toString();
	}
	
	@RequestMapping(value = "/saveVision", method = RequestMethod.POST)
	public String saveVision(Model model, HttpServletRequest request, @RequestParam("idScenario") int idScenario, @RequestParam("layoutToUse") int layout,
			@RequestParam("picturesList") String picturesListStr, @CookieValue(value = "user_id", defaultValue="NULL") String user_id, @RequestParam(value = "feelingsVote") int feelingsVote,
			@RequestParam("pickQueryWords") String pickQueryWords, @RequestParam("searchQueryWords") String searchQueryWords, @RequestParam("keywordInsertionForm1") String kw1,
			@RequestParam("keywordInsertionForm2") String kw2, @RequestParam("keywordInsertionForm3") String kw3, @RequestParam("descriptionInsertionForm") String description) throws PictureCreationException, UnsplashConnectionException {
		
		if(user_id == "NULL" || !imageProcessor.checkPictureListStr(layout, picturesListStr)) {
			return "redirect:/error";
		}
		
		Vision vision = new Vision();
		vision.setDescription(description);
		vision.setScenario(new Scenario(idScenario));
		
		ArrayList<String> keywords = new ArrayList<String>();
		keywords.add(kw1);
		keywords.add(kw2);
		keywords.add(kw3);
		
		String[] imagesStr = picturesListStr.split(Pattern.quote(";"));
		List<String> imagesPaths = new ArrayList<String>();
		List<Integer> xPositions = new ArrayList<Integer>();
		List<Integer> yPositions = new ArrayList<Integer>();
		List<Float> captureSizes = new ArrayList<Float>();
		int picked_number = 0;
			
		for (String elem : imagesStr) {
			String[] slotInfo = elem.split(Pattern.quote(":")); // 6 elements
			imagesPaths.add(slotInfo[1].toString());
			
			xPositions.add((int) Double.parseDouble(slotInfo[2]));
			yPositions.add((int) Double.parseDouble(slotInfo[3]));
			captureSizes.add((float) Double.parseDouble(slotInfo[4]));
		}
		
		// Merge and save photos based on the layout
		byte[] pictureBytes = imageProcessor.createPhotoCollage(layout, picturesListStr);
		
		vision.setImgPath(pictureBytes);
		
		vision.setUser(userRepository.findByCookie(user_id).get());
		vision.setFeelings(feelingsVote);
		
		vision.setShareDate(new Date());
		
		visionRepository.save(vision);
		
		Optional<Vision> tmpVision = visionRepository.findByLastUser(user_id);
		
		if(tmpVision.isPresent()) {
			vision = tmpVision.get();
		}
		
		for(String assignedKw : keywords) {
			if(!assignedKw.equals("")) {
				boolean found = false;
				
				for(String databaseKw : keywordRepository.findAllString()) {
					if(assignedKw.equals(databaseKw)) {
						found = true;
						break;
					}
				}
				
				if(!found) {
					keywordRepository.save(new Keyword(assignedKw));
				}
				
				visionKeywordRepository.save(new VisionKeyword(keywordRepository.findByName(assignedKw), vision));
			}
		}
		
		pickQueryWords = pickQueryWords.substring(0, pickQueryWords.lastIndexOf(','));
		searchQueryWords = searchQueryWords.substring(0, searchQueryWords.lastIndexOf(','));
		
		String[] pickQueryArray = pickQueryWords.split(Pattern.quote(","));
		String[] searchQueryArray = searchQueryWords.split(Pattern.quote(","));
		
		ArrayList<String> pickQueryArrayList = new ArrayList<>(Arrays.asList(pickQueryArray));
		ArrayList<String> searchQueryArrayList = new ArrayList<>(Arrays.asList(searchQueryArray));
		
		Set<String> searchQuerySet = new HashSet<String>();
		Set<String> pickQuerySet = new HashSet<String>();
		
		for(String s : searchQueryArrayList) {
			searchQuerySet.add(s.toLowerCase());
		}
		
		for(String p : pickQueryArrayList) {
			pickQuerySet.add(p.toLowerCase());
		}
		
		for(String queryword : searchQuerySet) {
			if(!queryword.equals("")) {
				
				boolean found = false;
				
				for(String databaseQw : queryWordRepository.findAllString()) {
					if(queryword.toLowerCase().equals(databaseQw.toLowerCase())) {
						found = true;
						break;
					}
				}
				
				if(!found) {
					queryWordRepository.save(new QueryWord(queryword.toLowerCase()));
				}
				
				if(pickQuerySet.contains(queryword)) {
					visionQueryWordRepository.save(new VisionQueryWord(true, xPositions.get(picked_number), yPositions.get(picked_number), captureSizes.get(picked_number),
							imagesPaths.get(picked_number), queryWordRepository.findByName(queryword.toLowerCase()), vision));
					picked_number++;
				} else {
					visionQueryWordRepository.save(new VisionQueryWord(false, queryWordRepository.findByName(queryword.toLowerCase()), vision));
				}
			}
		}
		
		if(!userAchievementRepository.findByCookieAndAchievement(user_id, 3).isPresent() && (boolean) model.asMap().get("logged")) {
			AchievementUtils.createAchievement(user_id, userAchievementRepository, 3);
			model.addAttribute("justAchievedAchievement", 3);
		}
		
		return "redirect:/scenario?idScenario=" + idScenario;
	}
	
	private ArrayList<Vision> adjustVisions(ArrayList<Vision> visions, String user_id) {
		for(Vision v : visions) {
			try {
				ImageFinder.findImage(v);
			} catch(IOException e) {
				v.setImg("/assets/default-vision-picture.png");
			}
			
			boolean found = false;
			
			for(Match m : v.getMatchOpponentCollection()) {
				if(m.getPlayer().getCookie().equals(user_id)) {
					found = true;
				}
			}
			
			if(v.getUser().getCookie().equals(user_id) || found) {
				v.setShowPlayButton(false);
			} else {
				v.setShowPlayButton(true);
			}
			
			User visionUser = v.getUser();
			
			if(visionUser.getProfilePicture() != null) {
				try {
					ImageFinder.findUserPicture(visionUser);
				} catch (Exception e) {
					visionUser.setImg("/assets/default-profile-picture.jpg");
				}
			} else {
				visionUser.setImg("/assets/default-profile-picture.jpg");
			}
			
			v.setUser(visionUser);
		}
		
		return visions;
	}
	
	private Vision adjustVision(Vision v, String user_id) {
		try {
			ImageFinder.findImage(v);
		} catch(IOException e) {
			v.setImg("/assets/default-vision-picture.png");
		}
		
		boolean found = false;
		
		for(Match m : v.getMatchOpponentCollection()) {
			if(m.getPlayer().getCookie().equals(user_id)) {
				found = true;
			}
		}
		
		if(v.getUser().getCookie().equals(user_id) || found) {
			v.setShowPlayButton(false);
		} else {
			v.setShowPlayButton(true);
		}
		
		User visionUser = v.getUser();
		
		if(visionUser.getProfilePicture() != null) {
			try {
				ImageFinder.findUserPicture(visionUser);
			} catch (Exception e) {
				visionUser.setImg("/assets/default-profile-picture.jpg");
			}
		} else {
			visionUser.setImg("/assets/default-profile-picture.jpg");
		}
		
		return v;
	}
	
}
