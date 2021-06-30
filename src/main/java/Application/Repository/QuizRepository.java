package Application.Repository;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Quiz;

/**
 * An interface to manage the data access to the {@link Application.Domain.Quiz} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
	
	/**
	 * Returns the {@link Application.Domain.Quiz} associated with the {@link Application.Domain.Scenario} provided their unique identifier.
	 * 
	 * @param scenarioId	The unique identifier of the {@link Application.Domain.Scenario}.
	 * @return	The {@link Application.Domain.Quiz} associated with the {@link Application.Domain.Scenario} provided their unique identifier.
	 */
	@Query(value = "SELECT * FROM quiz WHERE id_quiz IN (SELECT quiz FROM scenario WHERE id_scenario = :scenarioId AND deleted = 0)", nativeQuery = true)
	Collection<Quiz> findByIdScenario(@Param("scenarioId") int scenarioId);
	
	/**
	 * Returns the last {@link Application.Domain.Quiz} created by an {@link Application.Domain.Administrator}.
	 * 
	 * @return	The last {@link Application.Domain.Quiz} created by an {@link Application.Domain.Administrator}.
	 */
	@Query(value = "SELECT * FROM quiz ORDER BY id_quiz DESC LIMIT 1", nativeQuery = true)
	Optional<Quiz> findLastQuiz();
	
}
