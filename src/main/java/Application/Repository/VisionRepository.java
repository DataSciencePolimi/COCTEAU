package Application.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Vision;

/**
 * An interface to manage the data access to the {@link Application.Domain.Vision} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface VisionRepository extends JpaRepository<Vision, Integer>{

	/**
	 * Returns the list of {@link Application.Domain.Vision}(s) associated with the {@link Application.Domain.Scenario} with the provided unique identifier.
	 * 
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @return	The list of {@link Application.Domain.Vision}(s) associated with the {@link Application.Domain.Scenario} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM vision WHERE scenario = :scenarioId", nativeQuery = true)
	List<Vision> findByScenario(@Param("scenarioId") int scenarioId);
	
	/**
	 * Returns a limited list of {@link Application.Domain.Vision}(s) associated with the {@link Application.Domain.Scenario} with the provided unique identifier.
	 * 
	 * @param limit	The amount of {@link Application.Domain.Vision}(s) to collect.
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @return	a limited list of {@link Application.Domain.Vision}(s) associated with the {@link Application.Domain.Scenario} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM vision WHERE scenario = :scenarioId ORDER BY id_vision DESC LIMIT :limit", nativeQuery = true)
	List<Vision> findByScenarioLimit(@Param("limit") int limit, @Param("scenarioId") int scenarioId);
	
	/**
	 * Returns the last {@link Application.Domain.Vision} created by the {@link Application.Domain.Scenario} with the provided unique identifier.
	 * 
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @return	The last {@link Application.Domain.Vision} created by the {@link Application.Domain.Scenario} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM vision WHERE cookie = :user_id ORDER BY id_vision DESC LIMIT 1", nativeQuery = true)
	Optional<Vision> findByLastUser(@Param("user_id")String user_id);

	/**
	 * Returns the list of {@link Application.Domain.Vision}(s) with the provided picture identifier.
	 * 
	 * @param pictureId	The unique identifier of the picture of the {@link Application.Domain.Vision}.
	 * @return	The list of {@link Application.Domain.Vision}(s) with the provided picture identifier.
	 */
	@Query(value = "SELECT * FROM vision WHERE picture = :pictureId", nativeQuery = true)
	Optional<Collection<Vision>> findByPictureId(@Param("pictureId") int pictureId);
	
	/**
	 * Returns a {@link Application.Domain.Vision} to prepare a {@link Application.Domain.Match} for the {@link Application.Domain.User} with the provided unique identifier.
	 * 
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param pictureId	The unique identifier of the picture of the {@link Application.Domain.Vision}.
	 * @return	a {@link Application.Domain.Vision} to prepare a {@link Application.Domain.Match} for the {@link Application.Domain.User} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM vision tV WHERE tV.cookie != :user_id AND tV.picture = :pictureId AND tV.id_vision NOT IN(SELECT M.vision_challenger FROM matchmaking AS M, vision AS V WHERE M.vision_challenger = V.id_vision AND M.vision_challenger = tV.id_vision)", nativeQuery = true)
	Optional<Collection<Vision>> findMatch(@Param("user_id") String user_id, @Param("pictureId") int pictureId);
	
	/**
	 * Returns a {@link Application.Domain.Vision} to prepare a {@link Application.Domain.Match} for the {@link Application.Domain.User} with the provided unique identifier within the provided {@link Application.Domain.Scenario}.
	 * 
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param pictureId	The unique identifier of the picture of the {@link Application.Domain.Vision}.
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @return A {@link Application.Domain.Vision} to prepare a {@link Application.Domain.Match} for the {@link Application.Domain.User} with the provided unique identifier within the provided {@link Application.Domain.Scenario}.
	 */
	@Query(value = "SELECT tV.* FROM vision tV JOIN scenario s ON tV.scenario = s.id_scenario WHERE s.id_scenario = :scenarioId AND tV.cookie != :user_id AND tV.picture = :pictureId AND tV.id_vision NOT IN(SELECT M.vision_challenger FROM matchmaking AS M, vision AS V WHERE M.vision_challenger = V.id_vision AND M.vision_challenger = tV.id_vision)", nativeQuery = true)
	Optional<Collection<Vision>> findMatchSameScenario(@Param("user_id") String user_id, @Param("pictureId") int pictureId, @Param("scenarioId") int scenarioId);

	/**
	 * Returns a {@link Application.Domain.Vision} to prepare a new unique {@link Application.Domain.Match} for the {@link Application.Domain.User} with the provided unique identifier within the provided {@link Application.Domain.Scenario}. 
	 * 
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @return A {@link Application.Domain.Vision} to prepare a new unique {@link Application.Domain.Match} for the {@link Application.Domain.User} with the provided unique identifier within the provided {@link Application.Domain.Scenario}. 
	 */
	@Query(value = "SELECT * FROM vision WHERE scenario = :scenarioId and cookie != :user_id AND id_vision NOT IN (SELECT vision_challenger FROM matchmaking WHERE player = :user_id) ORDER BY RAND() LIMIT 1", nativeQuery = true)
	Optional<Vision> findByScenarioNotOwnedAndPlayed(@Param("scenarioId") int scenarioId, @Param("user_id") String user_id);

	/**
	 * Returns the list of the {@link Application.Domain.Vision}(s) not owned by the {@link Application.Domain.User} with the provided unique identifier within the provided {@link Application.Domain.Scenario}.
	 * 
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @return The list of the {@link Application.Domain.Vision}(s) not owned by the {@link Application.Domain.User} with the provided unique identifier within the provided {@link Application.Domain.Scenario}.
	 */
	@Query(value = "SELECT * FROM vision WHERE cookie != :user_id AND scenario = :scenarioId", nativeQuery = true)
	Collection<Vision> findByNotIdUser(@Param("user_id")String user_id, @Param("scenarioId") int scenarioId);
	
	/**
	 * Returns the list of the {@link Application.Domain.Vision}(s) of the {@link Application.Domain.User} with the provided unique identifier within the provided {@link Application.Domain.Scenario}.
	 * 
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @return The list of the {@link Application.Domain.Vision}(s) of the {@link Application.Domain.User} with the provided unique identifier within the provided {@link Application.Domain.Scenario}.
	 */
	@Query(value = "SELECT * FROM vision WHERE cookie = :user_id AND scenario = :scenarioId", nativeQuery = true)
	Collection<Vision> findByIdUser(@Param("user_id")String user_id, @Param("scenarioId") int scenarioId);

	/**
	 * Returns the list of the latest {@link Application.Domain.Vision}(s) of the {@link Application.Domain.User} with the provided unique identifier.
	 * 
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param limit	The amount of {@link Application.Domain.Vision}(s) to collect.
	 * @return	The list of the latest {@link Application.Domain.Vision}(s) for the {@link Application.Domain.User} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM vision WHERE cookie = :user_id ORDER BY id_vision DESC LIMIT :limit", nativeQuery = true)
	Collection<Vision> findRecentVisions(@Param("user_id") String user_id, @Param("limit") int limit);
}
