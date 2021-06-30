package Application.Repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.UserAchievement;

/**
 * An interface to manage the data access to the {@link Application.Domain.UserAchievement} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface UserAchievementRepository extends JpaRepository<UserAchievement, Integer>{
	
	/**
	 * Returns the {@link Application.Domain.UserAchievement} with the provided identifiers.
	 * 
	 * @param cookieValue	The unique identifier of the {@link Application.Domain.User}.
	 * @param achievement	The number associated with the {@link Application.Domain.Achievement}.
	 * @return	The {@link Application.Domain.UserAchievement} with the provided identifiers.
	 */
	@Query(value = "SELECT * FROM user_achievement WHERE cookie = :cookieValue AND achievement = :achievement", nativeQuery = true)
	Optional<UserAchievement> findByCookieAndAchievement(@Param("cookieValue") String cookieValue, @Param("achievement") int achievement);

	/**
	 * Returns the list of {@link Application.Domain.UserAchievement} associated with the {@link Application.Domain.User} with the provided unique identifier. 
	 * 
	 * @param cookieValue	The unique identifier of the {@link Application.Domain.User}.
	 * @return	The list of {@link Application.Domain.UserAchievement} associated with the {@link Application.Domain.User} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM user_achievement WHERE cookie = :cookieValue", nativeQuery = true)
	Collection<UserAchievement> findByCookie(@Param("cookieValue") String cookieValue);
	
}
