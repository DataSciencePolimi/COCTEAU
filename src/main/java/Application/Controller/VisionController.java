package Application.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

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

import Application.Domain.Keyword;
import Application.Domain.QueryWord;
import Application.Domain.Scenario;
import Application.Domain.User;
import Application.Domain.Vision;
import Application.Domain.VisionKeyword;
import Application.Domain.VisionQueryWord;
import Application.Exception.PictureCreationException;
import Application.Exception.TranslateException;
import Application.Exception.UnsplashConnectionException;
import Application.Repository.KeywordRepository;
import Application.Repository.MatchRepository;
import Application.Repository.QueryWordRepository;
import Application.Repository.ScenarioRepository;
import Application.Repository.UserAchievementRepository;
import Application.Repository.UserRepository;
import Application.Repository.VisionKeywordRepository;
import Application.Repository.VisionQueryWordRepository;
import Application.Repository.VisionRepository;
import Application.Utils.AchievementUtils;
import Application.Utils.CodeManagement;
import Application.Utils.ImageProcessor;
import Application.Utils.UnsplashConnector;

/**
 * Controller class for managing {@link Application.Domain.Vision}s.
 * When a {@link Application.Domain.User} creates a new {@link Application.Domain.Vision} some actions are 
 * done differently with respect to what and {@link Application.Domain.Administrator} does
 * (see {@link Application.Management.VisionManagementController}).
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@SessionAttributes({"limit", "scenarioId"})
@Controller
public class VisionController {

	/**
	 * Private instance of {@link Application.Repository.VisionRepository}.
	 */
	private final VisionRepository visionRepository;
	
	/**
	 * Private instance of {@link Application.Repository.UserRepository}.
	 */
	private final UserRepository userRepository;
	
	/**
	 * Private instance of {@link Application.Repository.KeywordRepository}.
	 */
	private final KeywordRepository keywordRepository;
	
	/**
	 * Private instance of {@link Application.Repository.VisionKeywordRepository}.
	 */
	private final VisionKeywordRepository visionKeywordRepository;
	
	/**
	 * Private instance of {@link Application.Repository.ScenarioRepository}.
	 */
	private final ScenarioRepository scenarioRepository;
	
	/**
	 * Private instance of {@link Application.Repository.QueryWordRepository}.
	 */
	private final QueryWordRepository queryWordRepository;
	
	/**
	 * Private instance of {@link Application.Repository.VisionQueryWordRepository}.
	 */
	private final VisionQueryWordRepository visionQueryWordRepository;
	
	/**
	 * Private instance of {@link Application.Utils.ImageProcessor}.
	 */
	private final ImageProcessor imageProcessor;
	
	/**
	 * Private instance of {@link Application.Utils.UnsplashConnector}.
	 */
	private final UnsplashConnector unsplashConnector;
	
	/**
	 * Private instance of {@link Application.Repository.MatchRepository}.
	 */
	private final MatchRepository matchRepository;
	
	/**
	 * Private instance of {@link Application.Repository.UserAchievementRepository}.
	 */
	private final UserAchievementRepository userAchievementRepository;
	
	/**
	 * Constructor for the {@link Application.Controller.VisionController} class.
	 * 
	 * @param visionRepository instance of {@link Application.Repository.VisionRepository}.
	 * @param userRepository instance of {@link Application.Repository.UserRepository}.
	 * @param keywordRepository instance of {@link Application.Repository.KeywordRepository}.
	 * @param visionKeywordRepository instance of {@link Application.Repository.VisionKeywordRepository}.
	 * @param scenarioRepository instance of {@link Application.Repository.ScenarioRepository}.
	 * @param queryWordRepository instance of {@link Application.Repository.QueryWordRepository}.
	 * @param imageProcessor instance of {@link Application.Utils.ImageProcessor}.
	 * @param unsplashConnector instance of {@link Application.Utils.UnsplashConnector}.
	 * @param visionQueryWordRepository instance of {@link Application.Repository.VisionQueryWordRepository}.
	 * @param matchRepository instance of {@link Application.Repository.MatchRepository}.
	 * @param userAchievementRepository instance of {@link Application.Repository.UserAchievementRepository}.
	 */
	@Autowired
	public VisionController(VisionRepository visionRepository, UserRepository userRepository, KeywordRepository keywordRepository,
			VisionKeywordRepository visionKeywordRepository, ScenarioRepository scenarioRepository, QueryWordRepository queryWordRepository,
			ImageProcessor imageProcessor, UnsplashConnector unsplashConnector, VisionQueryWordRepository visionQueryWordRepository,
			MatchRepository matchRepository, UserAchievementRepository userAchievementRepository) {
		this.visionRepository = visionRepository;
		this.userRepository = userRepository;
		this.keywordRepository = keywordRepository;
		this.visionKeywordRepository = visionKeywordRepository;
		this.scenarioRepository = scenarioRepository;
		this.imageProcessor = imageProcessor;
		this.unsplashConnector = unsplashConnector;
		this.visionQueryWordRepository = visionQueryWordRepository;
		this.queryWordRepository = queryWordRepository;
		this.matchRepository = matchRepository;
		this.userAchievementRepository = userAchievementRepository;
	}
	
	/**
	 * Returns the page for creating new {@link Application.Domain.Vision}s from the {@link Application.Domain.User} side.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param scenarioId Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @param user_id cookie of the {@link Application.Domain.User} creating of the new {@link Application.Domain.Vision}.
	 * @param lang String, language code from locale cookie.
	 * @param language String, language code passed as query-string parameter.
	 * @return "vision" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/vision", method = {RequestMethod.POST, RequestMethod.GET})
	public String getVision(Model model, @RequestParam(name = "scenarioId", required = false, defaultValue = "0") int scenarioId, 
			@CookieValue(value = "user_id", defaultValue="NULL") String user_id, @CookieValue(value = "localeInfo", defaultValue="en") String lang,
			@RequestParam(value = "lang", required = false) String language) {
		
		if(language != null && (language.equals("it") || language.equals("en"))) {
			lang = language;
		}
		
		if(scenarioId == 0) {
			scenarioId = (int) model.asMap().get("scenarioId");
		}
		
		Scenario scenario = null;
		Optional<Scenario> tempScenario = scenarioRepository.findById(scenarioId);
		
		if(tempScenario.isPresent()) {
			scenario = tempScenario.get();
		}
 		
		Vision newVision = new Vision(scenario);
		
		User user = userRepository.findByCookie(user_id).get();
		
		model.addAttribute("played", matchRepository.findByUserIdAllPlayed(user_id, scenarioId).size() >= 5? true : false);
		model.addAttribute("vision", newVision);
		model.addAttribute("scenarioId", scenarioId);
		model.addAttribute("scenarioDesc", scenario.getDescription());
		model.addAttribute("firstTime", CodeManagement.getValueAt(user.getAccessCode(), "Vision"));
		model.addAttribute("lang", lang);
		model.addAttribute("firstDimName", scenario.getFirstDimName());
		model.addAttribute("firstDimDesc", scenario.getFirstDimDesc());
		model.addAttribute("secondDimName", scenario.getSecondDimName());
		model.addAttribute("secondDimDesc", scenario.getSecondDimDesc());
		
		user.setAccessCode(CodeManagement.updateCode(user.getAccessCode(), "Vision", '1'));
		userRepository.save(user);

		return "vision";
	}
	
	/**
	 * Implements the creation and persisting of a new {@link Application.Domain.Vision} from the {@link Application.Domain.User} side.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param request see {@link javax.servlet.http.HttpServletRequest}.
	 * @param scenarioId Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @param vision Spring {@link org.springframework.web.bind.annotation.ModelAttribute} containing information about the new {@link Application.Domain.Vision}.
	 * @param layout Integer representing the chosen layout for the {@link Application.Domain.Vision}. It is used to build the {@link Application.Domain.Vision} server-side.
	 * @param picturesListStr String containing the URLs of the pictures to merge for the new {@link Application.Domain.Vision}. URLs are ";" separated.
	 * @param result see {@link org.springframework.validation.BindingResult}.
	 * @param user_id cookie of the {@link Application.Domain.User} creating of the new {@link Application.Domain.Vision}.
	 * @param pickQueryWords List of the search terms used on Unsplash that are also part of the {@link Application.Domain.Keyword}s.
	 * @param searchQueryWords List of the search terms used on Unsplash.
	 * @return 
	 * <ul>
	 * <li>"redirect:/match" if the {@link Application.Domain.User} has played less than 5 {@link Application.Domain.Match}es</li>
	 * <li>"redirect:/feed" otherwise</li>
	 * </ul> string redirecting to the specified Thymeleaf template to parse and return to the client.
	 * @throws PictureCreationException PictureCreationException, exception in creating photo collage for the new {@link Application.Domain.Vision}.
	 * @throws UnsplashConnectionException UnsplashConnectionException, exception in connecting to Unsplash's APIs.
	 */
	@RequestMapping(value = "/saveVision", method = RequestMethod.POST)
	public String saveVision(Model model, HttpServletRequest request, @RequestParam("scenarioId") int scenarioId, 
			@ModelAttribute("vision") Vision vision, @RequestParam("layoutToUse") int layout,
			@RequestParam("picturesList") String picturesListStr, BindingResult result, 
			@CookieValue(value = "user_id", defaultValue="NULL") String user_id,
			@RequestParam("pickQueryWords") String pickQueryWords, @RequestParam("searchQueryWords") String searchQueryWords) throws PictureCreationException, UnsplashConnectionException {
		
		if(user_id == "NULL" || !imageProcessor.checkPictureListStr(layout, picturesListStr)) {
			return "redirect:/error";
		}
		
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
		
		visionRepository.save(vision);
		
		Optional<Vision> tmpVision = visionRepository.findByLastUser(user_id);
		
		if(tmpVision.isPresent()) {
			vision = tmpVision.get();
		}
		
		for(String assignedKw : vision.getKeywordStringCollection()) {
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
		
		boolean firstTime = true;
		
		model.addAttribute("scenarioId", scenarioId);
		model.addAttribute("firstTime", firstTime);
		
		if(!userAchievementRepository.findByCookieAndAchievement(user_id,3).isPresent()) {
			AchievementUtils.createAchievement(user_id, userAchievementRepository, 3);
		}
		
		if(matchRepository.findByUserIdAllPlayed(user_id, scenarioId).size() >= 5) {
			return "redirect:/feed";
		}
		
		return "redirect:/match";
	}
	
	/**
	 * Relays the request to {@link Application.Controller.VisionController#unsplashConnector}
	 * for requesting the next batch of photos from Unsplash.
	 * 
	 * @param keyword String used as search term on Unplash.
	 * @param pageNumber Integer used to keep track of pagination of Unsplash results.
	 * @param lang String, language code.
	 * @return JSON formatted String containing the response from Unsplash's APIs.
	 * @throws TranslateException TranslateException, exception in translating search terms to English.
	 * @throws UnsplashConnectionException UnsplashConnectionException, exception in connecting to Unsplash's APIs.
	 */
	@ResponseBody
	@RequestMapping(value = "/getUnsplashPics", method = RequestMethod.GET)
	public String searchOnUnsplash(@RequestParam("keyword") String keyword, @RequestParam("pageNum") int pageNumber,
			@RequestParam("lang") String lang) throws TranslateException, UnsplashConnectionException {
		String response = "";
		response = unsplashConnector.getSearchResults(keyword, pageNumber);
		return response;
	}
}
