package Application.Repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Achievement;

/**
 * An interface to manage the data access to the {@link Application.Domain.Achievement} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface AchievementRepository extends JpaRepository<Achievement, Integer> {

	/**
	 * Returns the {@link Application.Domain.Achievement}(s) achieved by the {@link Application.Domain.User} given their unique identifier.
	 * 
	 * @param cookie	The unique identifier of the {@link Application.Domain.User}.
	 * @return	The list of {@link Application.Domain.Achievement}(s) achieved by the {@link Application.Domain.User} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM achievement as ac JOIN user_achievement as uac ON ac.id_achievement = uac.achievement WHERE cookie = :cookie", nativeQuery = true)
	Collection<Achievement> findAchievedAchievement(@Param("cookie") String cookie);
	
	/**
	 * Returns the {@link Application.Domain.Achievement}(s) yet to be achieved by the {@link Application.Domain.User} given their unique identifier.
	 * 
	 * @param cookie	The unique identifier of the {@link Application.Domain.User}.
	 * @return	The list of {@link Application.Domain.Achievement}(s) yet to be achieved by the {@link Application.Domain.User} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM achievement WHERE id_achievement NOT IN (SELECT ac.id_achievement FROM achievement as ac JOIN user_achievement as uac ON ac.id_achievement = uac.achievement WHERE cookie = :cookie)", nativeQuery = true)
	Collection<Achievement> findNotAchievedAchievement(@Param("cookie") String cookie);
}
