package Application.Repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.Question;

/**
 * An interface to manage the data access to the {@link Application.Domain.Question} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface QuestionRepository extends JpaRepository<Question, Integer> {

	/**
	 * Returns the list of the {@link Application.Domain.Question}(s) unanswered by the {@link Application.Domain.User} with the provided unique identifier.
	 * 
	 * @param quiz_id	The unique identifier of the {@link Application.Domain.Quiz}.
	 * @param user_id	The unique identifier of the {@link Application.Domain.User}.
	 * @param lang	The language of the {@link Application.Domain.Question}(s) of the {@link Application.Domain.Quiz}.
	 * @return	The list of the {@link Application.Domain.Question}(s) unanswered by the {@link Application.Domain.User} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM question WHERE id_quiz = :quiz_id AND language = :lang AND question_number NOT IN (SELECT question_number FROM question as q JOIN answer as a ON q.id_question = a.id_question WHERE id_quiz = :quiz_id AND cookie = :user_id)", nativeQuery = true)
	Collection<Question> findUnansweredQuestions(@Param("quiz_id") int quiz_id, @Param("user_id") String user_id, @Param("lang") String lang);
	
	/**
	 * Returns the list of the {@link Application.Domain.Question}(s) of the {@link Application.Domain.User} with the provided unique identifier.
	 * 
	 * @param quiz_id	The unique identifier of the {@link Application.Domain.Quiz}.
	 * @param lang	The language of the {@link Application.Domain.Question}(s) of the {@link Application.Domain.Quiz}.
	 * @return	The list of the {@link Application.Domain.Question}(s) of the {@link Application.Domain.User} with the provided unique identifier.
	 */
	@Query(value = "SELECT * FROM quiz as Q1 JOIN question as Q2 ON Q1.id_quiz = Q2.id_quiz WHERE Q1.id_quiz = :quiz_id and Q2.language = :lang", nativeQuery = true)
	Collection<Question> findAllByQuiz(@Param("quiz_id") int quiz_id, @Param("lang") String lang);
}
