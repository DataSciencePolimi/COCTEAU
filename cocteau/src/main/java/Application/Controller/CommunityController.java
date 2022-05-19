package Application.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import Application.Domain.Leaderboard;
import Application.Domain.Match;
import Application.Domain.User;
import Application.Domain.Vision;
import Application.Repository.AchievementRepository;
import Application.Repository.LeaderboardRepository;
import Application.Repository.UserAchievementRepository;
import Application.Repository.UserRepository;
import Application.Repository.VisionRepository;
import Application.Utils.AchievementUtils;
import Application.Utils.ImageFinder;

@SessionAttributes({"logged", "lang"})
@Controller
public class CommunityController {
	
	private final UserAchievementRepository userAchievementRepository;
	private final AchievementRepository achievementRepository;
	private final LeaderboardRepository leaderboardRepository;
	private final VisionRepository visionRepository;
	private final UserRepository userRepository;

	@Autowired
	public CommunityController(UserAchievementRepository userAchievementRepository, AchievementRepository achievementRepository,
			LeaderboardRepository leaderboardRepository, VisionRepository visionRepository, UserRepository userRepository) {
		this.userAchievementRepository = userAchievementRepository;
		this.achievementRepository = achievementRepository;
		this.leaderboardRepository = leaderboardRepository;
		this.visionRepository = visionRepository;
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/community", method = RequestMethod.GET)
	public String getCommunity(Model model, @CookieValue(value = "user_id", defaultValue="NULL") String user_id,
			@RequestParam(value = "lang", required = false, defaultValue="no-lang") String lang) {
		
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
		
		if(model.asMap().get("logged") != null) {
			model.addAttribute("logged", (boolean) model.asMap().get("logged"));
		} else {
			model.addAttribute("logged", false);
		}
		
		LocalDate now = LocalDate.now();
		TemporalField fieldISOW = WeekFields.of(Locale.ITALY).dayOfWeek();
		
		Date fromW = java.sql.Date.valueOf(now.with(fieldISOW, 1));
		Date toW = java.sql.Date.valueOf(now.with(fieldISOW, 7));
		Date fromM = java.sql.Date.valueOf(now.withDayOfMonth(1));
		Date toM = java.sql.Date.valueOf(now.withDayOfMonth(now.lengthOfMonth()));
		
		// MOST PLAYED VISIONS
		Collection<Vision> mostPlayedAll = visionRepository.findMostPlayed(lang);
		Collection<Vision> mostPlayedMonth = visionRepository.findMostPlayedFromTo(fromM, toM, lang);
		Collection<Vision> mostPlayedWeek = visionRepository.findMostPlayedFromTo(fromW, toW, lang);
		
		model.addAttribute("mostWeekly", mostPlayedWeek.size() > 0);
		model.addAttribute("mostMonthly", mostPlayedMonth.size() > 0);
		model.addAttribute("mostPlayedAll", adjustVisions(new ArrayList<Vision>(mostPlayedAll)));
		model.addAttribute("mostPlayedMonth", adjustVisions(new ArrayList<Vision>(mostPlayedMonth)));
		model.addAttribute("mostPlayedWeek", adjustVisions(new ArrayList<Vision>(mostPlayedWeek)));
		
		// LEADERBOARD
		ArrayList<Leaderboard> leaderboardAll = leaderboardRepository.findByUserGrouped(lang);
		ArrayList<Leaderboard> leaderboardMonth = leaderboardRepository.findByUserGroupedFromTo(fromM, toM, lang);
		ArrayList<Leaderboard> leaderboardWeek = leaderboardRepository.findByUserGroupedFromTo(fromW, toW, lang);
		
		model.addAttribute("leadWeekly", leaderboardWeek.size() > 0);
		model.addAttribute("leadMonthly", leaderboardMonth.size() > 0);
		model.addAttribute("leaderboardAll", adjustLeaderboard(leaderboardAll));
		model.addAttribute("leaderboardMonth", adjustLeaderboard(leaderboardMonth));
		model.addAttribute("leaderboardWeek", adjustLeaderboard(leaderboardWeek));
		
		// BEST USERS
		// Weekly
		ArrayList<Leaderboard> lWBestTmp = adjustLeaderboard(leaderboardWeek);
		Leaderboard lWBest = lWBestTmp.size() > 0? lWBestTmp.get(0) : null;
		
		User uWBestTmp = userRepository.findByMostMatchesPlayedFromTo(fromW, toW, lang);
		User uWBest1 = uWBestTmp != null? adjustUser(uWBestTmp) : null;
		
		uWBestTmp = userRepository.findByMostCorrectMatchesPlayedFromTo(fromW, toW, lang);
		User uWBest2 = uWBestTmp != null? addCorrectMatches(adjustUser(uWBestTmp)) : null;
		
		uWBestTmp = userRepository.findByMostVisionsSharedFromTo(fromW, toW, lang);
		User uWBest3 = uWBestTmp != null? adjustUser(uWBestTmp) : null;
		
		model.addAttribute("weekly", new ArrayList<Object>(Arrays.asList(lWBest, uWBest1, uWBest2, uWBest3)));
		model.addAttribute("bestWeekly", lWBest != null || uWBest1 != null || uWBest2 != null || uWBest3 != null);
		
		//Monthly
		ArrayList<Leaderboard> lMBestTmp = adjustLeaderboard(leaderboardMonth);
		Leaderboard lMBest = lMBestTmp.size() > 0? lMBestTmp.get(0) : null;
		
		User uMBestTmp = userRepository.findByMostMatchesPlayedFromTo(fromM, toM, lang);
		User uMBest1 = uMBestTmp != null? adjustUser(uMBestTmp) : null;
		
		uMBestTmp = userRepository.findByMostCorrectMatchesPlayedFromTo(fromM, toM, lang);
		User uMBest2 = uMBestTmp != null? addCorrectMatches(adjustUser(uMBestTmp)) : null;
		
		uMBestTmp = userRepository.findByMostVisionsSharedFromTo(fromM, toM, lang);
		User uMBest3 = uMBestTmp != null? adjustUser(uMBestTmp) : null;
		
		model.addAttribute("monthly", new ArrayList<Object>(Arrays.asList(lMBest, uMBest1, uMBest2, uMBest3)));
		model.addAttribute("bestMonthly", lMBest != null || uMBest1 != null || uMBest2 != null || uMBest3 != null);
		
		ArrayList<Leaderboard> lABestTmp = adjustLeaderboard(leaderboardAll);
		Leaderboard lABest = lABestTmp.size() > 0? lABestTmp.get(0) : null;
		
		User uABestTmp = userRepository.findByMostMatchesPlayed(lang);
		User uABest1 = uABestTmp != null? adjustUser(uABestTmp) : null;
		
		uABestTmp = userRepository.findByMostCorrectMatchesPlayed(lang);
		User uABest2 = uABestTmp != null? addCorrectMatches(adjustUser(uABestTmp)) : null;
		
		uABestTmp = userRepository.findByMostVisionsShared(lang);
		User uABest3 = uABestTmp != null? adjustUser(uABestTmp) : null;
		
		model.addAttribute("alltimes", new ArrayList<Object>(Arrays.asList(lABest, uABest1, uABest2, uABest3)));
		model.addAttribute("bestAll", lABest != null || uABest1 != null || uABest2 != null || uABest3 != null);
		
		model.addAttribute("achievementAchieved", "");
		
		if(!userAchievementRepository.findByCookieAndAchievement(user_id, 2).isPresent() && (boolean) model.asMap().get("logged")) {
			AchievementUtils.createAchievement(user_id, userAchievementRepository, 2);
			model.addAttribute("achievementAchieved", "You achieved '" + achievementRepository.findById(2).get().getTitle() + "'");
		}
		
		return "community";
	}
	
	private ArrayList<Leaderboard> adjustLeaderboard(ArrayList<Leaderboard> leaderboard) {
		
		for(Leaderboard l : leaderboard) {
			if(l.getPicture() != null) {
				try {
					ImageFinder.findUserPicture(l);
				} catch (Exception e) {
					l.setImg("/assets/default-profile-picture.jpg");
				}
			} else {
				l.setImg("/assets/default-profile-picture.jpg");
			}
		}
		
		return leaderboard;
	}
	
	private ArrayList<Vision> adjustVisions(ArrayList<Vision> visions) {
		for(Vision v : visions) {
			try {
				ImageFinder.findImage(v);
			} catch(IOException e) {
				v.setImg("/assets/default-vision-picture.png");
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
	
	private User adjustUser(User user) {
		
		if(user.getProfilePicture() != null) {
			try {
				ImageFinder.findUserPicture(user);
			} catch (Exception e) {
				user.setImg("/assets/default-profile-picture.jpg");
			}
		} else {
			user.setImg("/assets/default-profile-picture.jpg");
		}
		
		return user;
	}
	
	private User addCorrectMatches(User user) {
		
		int count = 0;
		
		for(Match m : user.getMatchPlayerCollection()) {
			if(m.getPoints() == 10) {
				count++;
			}
		}
		
		user.setCorrectMatches(count);
		
		return user;
	}
	
}
