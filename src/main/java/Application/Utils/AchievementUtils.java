package Application.Utils;

import Application.Domain.Achievement;
import Application.Domain.User;
import Application.Domain.UserAchievement;
import Application.Repository.UserAchievementRepository;

/**
 * Utility class used to create new {@link Application.Domain.Achievement}s.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public class AchievementUtils {

	/**
	 * Assigns an {@link Application.Domain.Achievement} to a {@link Application.Domain.User}.
	 * 
	 * @param user_id String containing the {@link Application.Domain.User} identifier to whom to assign the {@link Application.Domain.Achievement}.
	 * @param userAchievementRepository instance of {@link Application.Repository.UserAchievementRepository}.
	 * @param achievementNumber Integer, number of the {@link Application.Domain.Achievement} to assign.
	 */
	public static void createAchievement(String user_id, UserAchievementRepository userAchievementRepository, int achievementNumber) {
		UserAchievement newAchievement = new UserAchievement();
		
		newAchievement.setAchievement(new Achievement(achievementNumber));
		newAchievement.setAchiever(new User(user_id));
		
		userAchievementRepository.save(newAchievement);
		
		if(userAchievementRepository.findByCookie(user_id).size() >= 10 && !userAchievementRepository.findByCookieAndAchievement(user_id,8).isPresent()) {
			UserAchievement finalAchievement = new UserAchievement();
			
			finalAchievement.setAchievement(new Achievement(8));
			finalAchievement.setAchiever(new User(user_id));
			
			userAchievementRepository.save(finalAchievement);
		}
	}
}
