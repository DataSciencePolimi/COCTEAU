package Application.Management;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import Application.Domain.Administrator;
import Application.Domain.Keyword;
import Application.Domain.Scenario;
import Application.Domain.User;
import Application.Domain.Vision;
import Application.Domain.VisionKeyword;
import Application.Exception.PictureCreationException;
import Application.Exception.TranslateException;
import Application.Exception.UnsplashConnectionException;
import Application.Repository.AdministratorRepository;
import Application.Repository.KeywordRepository;
import Application.Repository.ScenarioRepository;
import Application.Repository.UserRepository;
import Application.Repository.VisionKeywordRepository;
import Application.Repository.VisionRepository;
import Application.Utils.ImageProcessor;
import Application.Utils.UnsplashConnector;

/**
 * Controller class for managing {@link Application.Domain.Vision}s.
 * When a new {@link Application.Domain.Scenario} gets created it is empty and a user can not play.
 * This controller implements the logic for adding new initial {@link Application.Domain.Vision}s
 * from the perspective of the creator of the {@link Application.Domain.Scenario}.<br>
 * An {@link Application.Domain.Administrator} account is needed.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Controller
public class VisionManagementController {
	
	/**
	 * String containing the cookie of the {@link Application.Domain.User} to which initial {@link Application.Domain.Vision}s are assigned to.
	 */
	@Value("${fakeUserCookie}")
	private String fakeUserCookie;
	
	/**
	 * Private instance of {@link Application.Repository.VisionRepository}.
	 */
	private final VisionRepository visionRepository;
	
	/**
	 * Private instance of {@link Application.Repository.AdministratorRepository}.
	 */
	private final AdministratorRepository administratorRepository;
	
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
	 * Private instance of {@link Application.Utils.ImageProcessor}.
	 */
	private final ImageProcessor imageProcessor;
	
	/**
	 * Private instance of {@link Application.Utils.UnsplashConnector}.
	 */
	private final UnsplashConnector unsplashConnector;
	
	/**
	 * Private instance of {@link Application.Repository.UserRepository}.
	 */
	private final UserRepository userRepository;
	
	/**
	 * Constructor for the {@link Application.Management.VisionManagementController} class.
	 * 
	 * @param visionRepository instance of {@link Application.Repository.VisionRepository}.
	 * @param administratorRepository instance of {@link Application.Repository.AdministratorRepository}.
	 * @param keywordRepository instance of {@link Application.Repository.KeywordRepository}.
	 * @param visionKeywordRepository instance of {@link Application.Repository.VisionKeywordRepository}.
	 * @param scenarioRepository instance of {@link Application.Repository.ScenarioRepository}.
	 * @param imageProcessor instance of {@link Application.Utils.ImageProcessor}.
	 * @param unsplashConnector instance of {@link Application.Utils.UnsplashConnector}.
	 * @param userRepository instance of {@link Application.Repository.UserRepository}.
	 */
	@Autowired
	public VisionManagementController(VisionRepository visionRepository, AdministratorRepository administratorRepository, KeywordRepository keywordRepository,
			VisionKeywordRepository visionKeywordRepository, ScenarioRepository scenarioRepository, ImageProcessor imageProcessor, 
			UnsplashConnector unsplashConnector, UserRepository userRepository) {
		this.visionRepository = visionRepository;
		this.administratorRepository = administratorRepository;
		this.keywordRepository = keywordRepository;
		this.visionKeywordRepository = visionKeywordRepository;
		this.scenarioRepository = scenarioRepository;
		this.imageProcessor = imageProcessor;
		this.unsplashConnector = unsplashConnector;
		this.userRepository = userRepository;
	}
	
	/**
	 * Returns the page for creating new {@link Application.Domain.Vision}s from the {@link Application.Domain.Administrator} side.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param scenarioId Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @param user_id cookie of the {@link Application.Domain.Administrator} creating of the new {@link Application.Domain.Vision}.
	 * @param lang String, language code.
	 * @param request see {@link javax.servlet.http.HttpServletRequest}.
	 * @return "vision-management" string indicating the name of the Thymeleaf template to parse and return to the client.
	 */
	@RequestMapping(value = "/vision-management", method = {RequestMethod.POST, RequestMethod.GET})
	public String getVisionManagement(Model model, @RequestParam(name = "selectedScenario", required = false, defaultValue = "0") int scenarioId, 
			@CookieValue(value = "user_id", defaultValue="NULL") String user_id, @CookieValue(value = "localeInfo", defaultValue="en") String lang,
			HttpServletRequest request) {
		
		if(user_id.equals("NULL")) {
			return "redirect:/error";
		}
		
		Optional<Administrator> tempAdmin = administratorRepository.findByCookie(user_id);
		
		if(!tempAdmin.isPresent()) {
			return "redirect:/error";
		}
		
		if(scenarioId == 0) {
			//scenarioId = (int) model.asMap().get("selectedScenario");
			Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);
			scenarioId = (int) flashMap.get("selectedScenario");
		}
		
		Scenario scenario = null;
		Optional<Scenario> tempScenario = scenarioRepository.findById(scenarioId);
		
		if(tempScenario.isPresent()) {
			scenario = tempScenario.get();
		}
 		
		Vision newVision = new Vision(scenario);
		
		model.addAttribute("vision", newVision);
		model.addAttribute("scenarioId", scenarioId);
		model.addAttribute("lang", lang);
		model.addAttribute("firstDimName", scenario.getFirstDimName());
		model.addAttribute("firstDimDesc", scenario.getFirstDimDesc());
		model.addAttribute("secondDimName", scenario.getSecondDimName());
		model.addAttribute("secondDimDesc", scenario.getSecondDimDesc());
		return "vision-management";
	}
	
	/**
	 * Implements the creation and persisting of a new {@link Application.Domain.Vision} from the {@link Application.Domain.Administrator} side.
	 * 
	 * @param model Spring {@link org.springframework.ui.Model} object.
	 * @param request see {@link javax.servlet.http.HttpServletRequest}.
	 * @param scenarioId Integer indicating the identifier of a {@link Application.Domain.Scenario}.
	 * @param vision Spring {@link org.springframework.web.bind.annotation.ModelAttribute} containing information about the new {@link Application.Domain.Vision}.
	 * @param layout Integer representing the chosen layout for the {@link Application.Domain.Vision}. It is used to build the {@link Application.Domain.Vision} server-side.
	 * @param picturesListStr String containing the URLs of the pictures to merge for the new {@link Application.Domain.Vision}. URLs are ";" separated.
	 * @param result see {@link org.springframework.validation.BindingResult}.
	 * @param user_id cookie of the {@link Application.Domain.Administrator} creating of the new {@link Application.Domain.Vision}.
	 * @param redirectAttributes see {@link org.springframework.web.servlet.mvc.support.RedirectAttributes}. Used to pass 'scenarioId' via a Redirect.
	 * @return "redirect:/scenario-panel" string redirecting to the specified Thymeleaf template to parse and return to the client.
	 * @throws PictureCreationException PictureCreationException, exception in creating photo collage for the new {@link Application.Domain.Vision}.
	 * @throws UnsplashConnectionException UnsplashConnectionException, exception in connecting to Unsplash's APIs.
	 */
	@RequestMapping(value = "/save-vision-management", method = RequestMethod.POST)
	public String saveVision(Model model, HttpServletRequest request, @RequestParam("scenarioId") int scenarioId, 
			@ModelAttribute("vision") Vision vision, @RequestParam("layoutToUse") int layout,
			@RequestParam("picturesList") String picturesListStr, BindingResult result, 
			@CookieValue(value = "user_id", defaultValue="NULL") String user_id,
			RedirectAttributes redirectAttributes) throws PictureCreationException, UnsplashConnectionException {
		
		if(user_id == "NULL" || !imageProcessor.checkPictureListStr(layout, picturesListStr)) {
			return "redirect:/error";
		}
		
		String[] imagesStr = picturesListStr.split(Pattern.quote(";"));
		List<String> imagesPaths = new ArrayList<String>();
		List<Integer> xPositions = new ArrayList<Integer>();
		List<Integer> yPositions = new ArrayList<Integer>();
		List<Float> captureSizes = new ArrayList<Float>();
		
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
		
		User fakeUser = userRepository.findByCookie(fakeUserCookie).get();
		vision.setUser(fakeUser);
		
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
		
		model.addAttribute("selectedScenario", scenarioId);
		
		if(visionRepository.findByScenario(scenarioId).size() < 5) {
			redirectAttributes.addFlashAttribute("selectedScenario", scenarioId);
			return "redirect:/vision-management";
		}
		
		return "redirect:/scenario-panel";
	}
	
	/**
	 * Relays the request to {@link Application.Management.VisionManagementController#unsplashConnector}
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
	@RequestMapping(value = "/get-unsplash-pics-management", method = RequestMethod.GET)
	public String searchOnUnsplash(@RequestParam("keyword") String keyword, @RequestParam("pageNum") int pageNumber,
			@RequestParam("lang") String lang) throws TranslateException, UnsplashConnectionException {
		return unsplashConnector.getSearchResults(keyword, pageNumber);
	}
}
