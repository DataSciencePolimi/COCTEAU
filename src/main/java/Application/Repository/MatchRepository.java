package Application.Repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Match;

/**
 * An interface to manage the data access to the {@link Application.Domain.Match} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface MatchRepository extends JpaRepository<Match, Integer>{

	/**
	 * Returns the last {@link Application.Domain.Match} played by the {@link Application.Domain.User} provided their unique identifier.
	 * 
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @return	If found, the last {@link Application.Domain.Match} played by the {@link Application.Domain.User} provided their unique identifier, null otherwise.
	 */
	@Query(value = "SELECT * FROM matchmaking WHERE player = :user_id ORDER BY id_match DESC LIMIT 1", nativeQuery = true)
	Optional<Match> findByUserLast(@Param("user_id") String user_id);
	
	/**
	 * Returns the list of {@link Application.Domain.Match}(es) played by the {@link Application.Domain.User} in a {@link Application.Domain.Scenario} provided their unique identifiers.
	 * 
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @return	the list of {@link Application.Domain.Match}(es) played by the {@link Application.Domain.User} in a {@link Application.Domain.Scenario} provided their unique identifiers.
	 */
	@Query(value = "SELECT * FROM matchmaking as m JOIN vision as v ON m.vision_challenger = v.id_vision WHERE m.player = :user_id AND v.scenario = :scenarioId", nativeQuery = true)
	Collection<Match> findByUserIdAllPlayed(@Param("user_id") String user_id, @Param("scenarioId") int scenarioId);

	/**
	 * Returns the list of Quick {@link Application.Domain.Match}(es) played by the {@link Application.Domain.User} provided their unique identifier.
	 * 
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @return	The list of Quick {@link Application.Domain.Match}(es) played by the {@link Application.Domain.User} provided their unique identifier.
	 */
	@Query(value = "SELECT * FROM matchmaking as m JOIN vision as v ON m.vision_challenger = v.id_vision WHERE m.player = :user_id", nativeQuery = true)
	Collection<Match> findByUserIdAllQuickMatchPlayed(@Param("user_id") String user_id);
	
	/**
	 * Returns the list of In-depth {@link Application.Domain.Match}(es) played by the {@link Application.Domain.User} provided their unique identifier.
	 * 
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @return	The list of In-depth {@link Application.Domain.Match}(es) played by the {@link Application.Domain.User} provided their unique identifier.
	 */
	@Query(value = "SELECT * FROM matchmaking WHERE player = :user_id AND thoughts IS NOT NULL", nativeQuery = true)
	Collection<Match> findByUserIdAllInDepthMatchPlayed(@Param("user_id") String user_id);

	/**
	 * Returns the list of {@link Application.Domain.Match}(es) played by the {@link Application.Domain.User} provided their unique identifier.
	 * 
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @return	The list of {@link Application.Domain.Match}(es) played by the {@link Application.Domain.User} provided their unique identifier.
	 */
	@Query(value = "SELECT * FROM matchmaking WHERE player = :user_id", nativeQuery = true)
	Collection<Match> findByUserId(@Param("user_id") String user_id);
}