package Application.Domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * Quiz is the class that maps the database Entity Quiz.
 * A Quiz is a set of questions asked to a {@link Application.Domain.User} to improve their understanding of the {@link Application.Domain.Scenario}
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
public class Quiz {
	
	/**
	 * The unique identifier of the Quiz.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idQuiz;
	
	/**
	 * The name of the Quiz.
	 */
	@NotNull
	private String quizName;
	
	/**
	 * The {@link Application.Domain.Scenario}(s) the Quiz is assigned to.
	 */
	@OneToMany(mappedBy = "quiz")
	private Collection<Scenario> scenarios;
	
	/**
	 * The {@link Application.Domain.Question}(s) of the Quiz.
	 */
	@OneToMany(mappedBy = "quiz")
	private Collection<Question> questions;
	
	/**
	 * Creates a new Quiz.
	 */
	public Quiz() {
		
	}

	/**
	 * Creates a new Quiz.
	 * 
	 * @param quizName	The name of the Quiz.
	 */
	public Quiz(String quizName) {
		this.quizName = quizName;
	}
	
	/**
	 * Returns the unique identifier of the Quiz.
	 * 
	 * @return	The unique identifier of the Quiz.
	 */
	public int getIdQuiz() {
		return idQuiz;
	}

	/**
	 * Sets the unique identifier of the Quiz.
	 * 
	 * @param idQuiz	The unique identifier of the Quiz.
	 */
	public void setIdQuiz(int idQuiz) {
		this.idQuiz = idQuiz;
	}

	/**
	 * Returns the name of the Quiz.
	 * 
	 * @return	The name of the Quiz.
	 */
	public String getQuizName() {
		return quizName;
	}

	/**
	 * Sets the name of the Quiz.
	 * 
	 * @param quizName	The name of the Quiz.
	 */
	public void setQuizName(String quizName) {
		this.quizName = quizName;
	}

	/**
	 * Returns the {@link Application.Domain.Scenario}(s) the Quiz is assigned to.
	 * 
	 * @return	The {@link Application.Domain.Scenario}(s) the Quiz is assigned to.
	 */
	public Collection<Scenario> getScenario() {
		return scenarios;
	}

	/**
	 * Sets the {@link Application.Domain.Scenario}(s) the Quiz is assigned to.
	 * 
	 * @param scenarios	The {@link Application.Domain.Scenario}(s) the Quiz is assigned to.
	 */
	public void setScenario(Collection<Scenario> scenarios) {
		this.scenarios = scenarios;
	}

	/**
	 * Returns the {@link Application.Domain.Question}(s) of the Quiz.
	 * 
	 * @return	The {@link Application.Domain.Question}(s) of the Quiz.
	 */
	public Collection<Question> getQuestions() {
		return questions;
	}
	
	/**
	 * Sets the {@link Application.Domain.Question}(s) of the Quiz.
	 * 
	 * @param questions	The {@link Application.Domain.Question}(s) of the Quiz.
	 */
	public void setQuestions(Collection<Question> questions) {
		this.questions = questions;
	}
}
