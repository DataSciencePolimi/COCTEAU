package Application.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Leaderboard;

/**
 * An interface to manage the data access to the {@link Application.Domain.Leaderboard} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Integer>{
	
	/**
	 * Returns the {@link Application.Domain.Leaderboard} entry of a {@link Application.Domain.User} with the provided unique identifier.
	 * 
	 * @param cookieValue	The unique identifier of a {@link Application.Domain.User}.
	 * @return	the {@link Application.Domain.Leaderboard} entry of a {@link Application.Domain.User}
	 */
	@Query(value = "SELECT * FROM leaderboard WHERE cookie = :cookieValue", nativeQuery = true)
	Optional<Leaderboard> findByCookie(@Param("cookieValue") String cookieValue);
	
	/**
	 * Returns the position of the {@link Application.Domain.User} in the {@link Application.Domain.Leaderboard} with the provided score.
	 * 
	 * @param points	The total amount of points scored by the {@link Application.Domain.User}.
	 * @return	the position of the {@link Application.Domain.User} in the {@link Application.Domain.Leaderboard}.
	 */
	@Query(value = "SELECT COUNT(*) as position FROM leaderboard WHERE points > :userScore", nativeQuery = true)
	int findLeaderboardPosition(@Param("userScore") int points);
	
	/**
	 * Returns the list of {@link Application.Domain.Leaderboard} entries ordered by the scores. 
	 * 
	 * @return	the {@link Application.Domain.Leaderboard} ordered by the scores.
	 */
	@Query(value = "SELECT * FROM leaderboard ORDER BY points DESC, cookie ASC", nativeQuery = true)
	List<Leaderboard> findOrderByPoints();
	
}