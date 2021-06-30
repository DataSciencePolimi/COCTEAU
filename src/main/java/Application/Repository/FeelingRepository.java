package Application.Repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Feeling;

/**
 * An interface to manage the data access to the {@link Application.Domain.Feeling} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface FeelingRepository extends JpaRepository<Feeling, Integer>{

	/**
	 * Returns the {@link Application.Domain.Feeling} with the provided unique identifiers.
	 * 
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @return	If found, the {@link Application.Domain.Feeling} with the provided unique identifiers, null otherwise.
	 */
	@Query(value = "SELECT * FROM feeling WHERE scenario = :scenarioId AND cookie = :user_id", nativeQuery = true)
	Optional<Feeling> findByScenarioAndUser(@Param("scenarioId") int scenarioId, @Param("user_id") String user_id);
	
	/**
	 * Returns the list of {@link Application.Domain.Feeling} with the provided unique identifier.
	 * 
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @return	The list of the {@link Application.Domain.Feeling} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM feeling WHERE cookie = :user_id", nativeQuery = true)
	Collection<Feeling> findByUser(@Param("user_id") String user_id);
	
	/**
	 * Returns the {@link Application.Domain.Feeling} with the provided unique identifier.
	 * 
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @return	The list of the {@link Application.Domain.Feeling} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM feeling WHERE scenario = :scenarioId", nativeQuery = true)
	Collection<Feeling> findByScenario(@Param("scenarioId") int scenarioId);
}
