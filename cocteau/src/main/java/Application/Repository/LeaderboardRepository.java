package Application.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Leaderboard;
import Application.Domain.Match;

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
	@Query(value = "SELECT u.cookie, u.username, points, u.profile_picture AS 'picture' FROM leaderboard AS l JOIN user AS u ON l.cookie = u.cookie ORDER BY points DESC, l.cookie ASC", nativeQuery = true)
	List<Leaderboard> findOrderByPoints();
	
	@Query(value = "SELECT u.cookie, u.username, m.points, u.profile_picture AS 'picture' FROM (SELECT player AS 'cookie', SUM(points) AS 'points' FROM ((matchmaking AS mw JOIN user AS uw ON mw.player = uw.cookie) JOIN vision AS vw ON mw.vision_challenger = vw.id_vision) JOIN scenario AS sw on sw.id_scenario = vw.scenario WHERE sw.lang = :lang AND uw.cookie != uw.username GROUP BY player ORDER BY points DESC LIMIT 7) AS m JOIN user AS u ON m.cookie = u.cookie", nativeQuery = true)
	ArrayList<Leaderboard> findByUserGrouped(@Param("lang") String lang);
	
	@Query(value = "SELECT u.cookie, u.username, m.points, u.profile_picture AS 'picture' FROM (SELECT player AS 'cookie', SUM(points) AS 'points' FROM ((matchmaking AS mw JOIN user AS uw ON mw.player = uw.cookie) JOIN vision AS vw ON mw.vision_challenger = vw.id_vision) JOIN scenario AS sw on sw.id_scenario = vw.scenario WHERE sw.lang = :lang AND uw.cookie != uw.username AND played_date >= :from AND played_date <= :to GROUP BY player ORDER BY points DESC LIMIT 7) AS m JOIN user AS u ON m.cookie = u.cookie", nativeQuery = true)
	ArrayList<Leaderboard> findByUserGroupedFromTo(@Param("from") Date from, @Param("to") Date to, @Param("lang") String lang);
	
}