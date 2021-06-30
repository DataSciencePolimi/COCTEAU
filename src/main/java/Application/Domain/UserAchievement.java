package Application.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * UserAchievement is the class that maps the database Entity UserAchievement.
 * A UserAchievement maps the {@link Application.Domain.User} with the {@link Application.Domain.Achievement}(s) they achieved.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
@Table(name = "user_achievement")
public class UserAchievement {
	
	/**
	 * The unique identifier of the UserAchievement.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id_user_achievement;
	
	/**
	 * The {@link Application.Domain.User} who achieved the Achievement.
	 */
	@ManyToOne
	@JoinColumn(name = "cookie")
	private User achiever;
	
	/**
	 * The {@link Application.Domain.Achievement} achieved by the {@link Application.Domain.User}.
	 */
	@ManyToOne
	@JoinColumn(name = "achievement")
	private Achievement achievement;
	
	/**
	 * Creates a new UserAchievement.
	 */
	public UserAchievement() {
		
	}

	/**
	 * Returns the unique identifier of the UserAchievement.
	 * 
	 * @return	The unique identifier of the UserAchievement.
	 */
	public int getIdUserAchievement() {
		return id_user_achievement;
	}

	/**
	 * Sets the unique identifier of the UserAchievement.
	 * 
	 * @param id_user_achievement	The unique identifier of the UserAchievement.
	 */
	public void setIdUserAchievement(int id_user_achievement) {
		this.id_user_achievement = id_user_achievement;
	}

	/**
	 * Returns the {@link Application.Domain.User} who achieved the Achievement.
	 * 
	 * @return	The {@link Application.Domain.User} who achieved the Achievement.
	 */
	public User getAchiever() {
		return achiever;
	}

	/**
	 * Sets the {@link Application.Domain.User} who achieved the Achievement.
	 * 
	 * @param achiever	The {@link Application.Domain.User} who achieved the Achievement.
	 */
	public void setAchiever(User achiever) {
		this.achiever = achiever;
	}

	/**
	 * Returns the {@link Application.Domain.Achievement} achieved by the {@link Application.Domain.User}.
	 * 
	 * @return	The {@link Application.Domain.Achievement} achieved by the {@link Application.Domain.User}.
	 */
	public Achievement getAchievement() {
		return achievement;
	}

	/**
	 * Sets the {@link Application.Domain.Achievement} achieved by the {@link Application.Domain.User}.
	 * 
	 * @param achievement	The {@link Application.Domain.Achievement} achieved by the {@link Application.Domain.User}.
	 */
	public void setAchievement(Achievement achievement) {
		this.achievement = achievement;
	}

}
