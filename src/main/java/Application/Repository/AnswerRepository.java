package Application.Repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Answer;

/**
 * An interface to manage the data access to the {@link Application.Domain.Answer} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface AnswerRepository extends JpaRepository<Answer, Integer>{
	
	/**
	 * Returns the list of {@link Application.Domain.Answer}(s) to the {@link Application.Domain.Question}(s) of a {@link Application.Domain.Quiz} with the provided unique identifier.
	 * 
	 * @param id_quiz	The unique identifier of the {@link Application.Domain.Quiz}.
	 * @return	The list of {@link Application.Domain.Answer}(s) to the {@link Application.Domain.Question}(s) of a {@link Application.Domain.Quiz} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM answer as a JOIN question as q ON a.id_question = q.id_question WHERE q.id_quiz = :id_quiz", nativeQuery = true)
	Collection<Answer> findAllQuizAnswers(@Param("id_quiz") int id_quiz);

}
