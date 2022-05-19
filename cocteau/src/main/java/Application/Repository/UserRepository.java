package Application.Repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.User;

/**
 * An interface to manage the data access to the {@link Application.Domain.User} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface UserRepository extends JpaRepository<User, Integer>{

	/**
	 * Returns the count of the {@link Application.Domain.User}(s) with the provided unique identifiers.
	 * 
	 * @param cookieValue	The unique identifiers of the {@link Application.Domain.User}.
	 * @return	The count of the {@link Application.Domain.User}(s) with the provided unique identifiers.
	 */
	@Query(value = "SELECT COUNT(*) FROM user WHERE cookie = :cookieValue", nativeQuery = true)
	int findAllWithCookie(@Param("cookieValue") String cookieValue);
	
	/**
	 * Returns the {@link Application.Domain.User} with the provided unique identifier.
	 * 
	 * @param cookieValue	The unique identifiers of the {@link Application.Domain.User}.
	 * @return If found, the {@link Application.Domain.User} with the provided unique identifier, null otherwise.
	 */
	@Query(value = "SELECT * FROM user WHERE cookie = :cookieValue", nativeQuery = true)
	Optional<User> findByCookie(@Param("cookieValue") String cookieValue);

	/**
	 * Returns the {@link Application.Domain.User} with the provided username.
	 * 
	 * @param username	The username of the {@link Application.Domain.User}.
	 * @return If found, the {@link Application.Domain.User} with the provided username, null otherwise.
	 */
	@Query(value = "SELECT * FROM user WHERE username = :username", nativeQuery = true)
	Optional<User> findByUsername(@Param("username") String username);
	
	/**
	 * Returns the last entry number of the {@link Application.Domain.User}(s).
	 * 
	 * @return If found, the last entry number of the {@link Application.Domain.User}(s), null otherwise.
	 */
	@Query(value = "SELECT MAX(entry_number) FROM user", nativeQuery = true)
	Optional<Integer> findMaxEntry();
	
	/**
	 * Returns the {@link Application.Domain.User} with the provided entry number.
	 * 
	 * @param userEntry	The entry number of the {@link Application.Domain.User}.
	 * @return	The {@link Application.Domain.User} with the provided entry number.
	 */
	@Query(value = "SELECT * FROM user WHERE entry_number = :userEntry", nativeQuery = true)
	Optional<User> findByUserEntryNumber(@Param("userEntry") int userEntry);
	
	@Query(value = "SELECT * FROM user AS u JOIN (SELECT player, COUNT(*) AS 'count' FROM ((matchmaking AS mm JOIN user as uu ON mm.player = uu.cookie) JOIN vision AS vv ON mm.vision_challenger = vv.id_vision) JOIN scenario AS ss ON vv.scenario = ss.id_scenario WHERE ss.lang = :lang AND uu.cookie != uu.username GROUP BY player ORDER BY count DESC LIMIT 1) AS m ON u.cookie = m.player", nativeQuery = true)
	User findByMostMatchesPlayed(@Param("lang") String lang);
	
	@Query(value = "SELECT * FROM user AS u JOIN (SELECT player, COUNT(*) AS 'count' FROM ((matchmaking AS mm JOIN user as uu ON mm.player = uu.cookie) JOIN vision AS vv ON mm.vision_challenger = vv.id_vision) JOIN scenario AS ss ON vv.scenario = ss.id_scenario WHERE ss.lang = :lang AND uu.cookie != uu.username AND played_date >= :from AND played_date <= :to GROUP BY player ORDER BY count DESC LIMIT 1) AS m ON u.cookie = m.player", nativeQuery = true)
	User findByMostMatchesPlayedFromTo(@Param("from") Date from, @Param("to") Date to, @Param("lang") String lang);
	
	@Query(value = "SELECT * FROM user AS u JOIN (SELECT player, COUNT(*) AS 'count' FROM ((matchmaking AS mm JOIN user as uu ON mm.player = uu.cookie) JOIN vision AS vv ON mm.vision_challenger = vv.id_vision) JOIN scenario AS ss ON vv.scenario = ss.id_scenario WHERE ss.lang = :lang AND uu.cookie != uu.username AND points = 10 GROUP BY player ORDER BY count DESC LIMIT 1) AS m ON u.cookie = m.player", nativeQuery = true)
	User findByMostCorrectMatchesPlayed(@Param("lang") String lang);
	
	@Query(value = "SELECT * FROM user AS u JOIN (SELECT player, COUNT(*) AS 'count' FROM ((matchmaking AS mm JOIN user as uu ON mm.player = uu.cookie) JOIN vision AS vv ON mm.vision_challenger = vv.id_vision) JOIN scenario AS ss ON vv.scenario = ss.id_scenario WHERE ss.lang = :lang AND uu.cookie != uu.username AND points = 10 AND played_date >= :from AND played_date <= :to GROUP BY player ORDER BY count DESC LIMIT 1) AS m ON u.cookie = m.player", nativeQuery = true)
	User findByMostCorrectMatchesPlayedFromTo(@Param("from") Date from, @Param("to") Date to, @Param("lang") String lang);
	
	@Query(value = "SELECT * FROM user AS u JOIN (SELECT vv.cookie, COUNT(*) AS 'count' FROM (vision AS vv JOIN user as uu ON vv.cookie = uu.cookie) JOIN scenario AS ss ON vv.scenario = ss.id_scenario WHERE ss.lang = :lang AND uu.cookie != uu.username GROUP BY vv.cookie ORDER BY count DESC LIMIT 1) AS v ON u.cookie = v.cookie", nativeQuery = true)
	User findByMostVisionsShared(@Param("lang") String lang);
	
	@Query(value = "SELECT * FROM user AS u JOIN (SELECT vv.cookie, COUNT(*) AS 'count' FROM (vision AS vv JOIN user as uu ON vv.cookie = uu.cookie) JOIN scenario AS ss ON vv.scenario = ss.id_scenario WHERE ss.lang = :lang AND uu.cookie != uu.username AND share_date >= :from AND share_date <= :to GROUP BY vv.cookie ORDER BY count DESC LIMIT 1) AS v ON u.cookie = v.cookie", nativeQuery = true)
	User findByMostVisionsSharedFromTo(@Param("from") Date from, @Param("to") Date to, @Param("lang") String lang);
}
