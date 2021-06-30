package Application.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Answer is the class that maps the database Entity Answer.
 * An Answer is the representation of the answer provided by the {@link Application.Domain.User} to a {@link Application.Domain.Question} of a {@link Application.Domain.Quiz}.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
public class Answer {
	
	/**
	 * The unique identifier of the Answer.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idAnswer;
	
	/**
	 * The additional comment provided by the {@link Application.Domain.User}.
	 */
	private String comment;
	
	/**
	 * The number of the answer chosen by the {@link Application.Domain.User}.
	 */
	private int answerNumber;
	
	/**
	 * The {@link Application.Domain.Question} the {@link Application.Domain.User} is answering to.
	 */
	@ManyToOne
	@JoinColumn(name = "id_question")
	private Question question;
	
	/**
	 * The {@link Application.Domain.User} providing the Answer to the {@link Application.Domain.Question}.
	 */
	@NotNull
	@ManyToOne
	@JoinColumn(name = "cookie")
	private User user;
	
	/**
	 * Creates a new Answer.
	 */
	public Answer() {
		
	}
	
	/**
	 * Creates a new Answer.
	 * 
	 * @param comment	The additional comment provided by the {@link Application.Domain.User}.
	 * @param answerNumber	The number of the answer chosen by the {@link Application.Domain.User}.
	 * @param question	The {@link Application.Domain.Question} the {@link Application.Domain.User} is answering to.
	 * @param user	The {@link Application.Domain.User} providing the Answer to the {@link Application.Domain.Question}.
	 */
	public Answer(String comment, int answerNumber, Question question, User user) {
		this.comment = comment;
		this.answerNumber = answerNumber;
		this.question = question;
		this.user = user;
	}

	/**
	 * Returns the unique identifier of the Answer.
	 * 
	 * @return	The unique identifier of the Answer.
	 */
	public int getIdAnswer() {
		return idAnswer;
	}

	/**
	 * Sets the unique identifier of the Answer.
	 * 
	 * @param idAnswer	The unique identifier of the Answer.
	 */
	public void setIdAnswer(int idAnswer) {
		this.idAnswer = idAnswer;
	}

	/**
	 * Returns the additional comment provided by the {@link Application.Domain.User}.
	 * 
	 * @return	The additional comment provided by the {@link Application.Domain.User}.
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the additional comment provided by the {@link Application.Domain.User}.
	 * 
	 * @param comment	The additional comment provided by the {@link Application.Domain.User}.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * Returns the number of the answer chosen by the {@link Application.Domain.User}.
	 * 
	 * @return	The number of the answer chosen by the {@link Application.Domain.User}.
	 */
	public int getAnswerNumber() {
		return answerNumber;
	}

	/**
	 * Sets the number of the answer chosen by the {@link Application.Domain.User}.
	 * 
	 * @param answerNumber	The number of the answer chosen by the {@link Application.Domain.User}.
	 */
	public void setAnswerNumber(int answerNumber) {
		this.answerNumber = answerNumber;
	}

	/**
	 * Returns the {@link Application.Domain.Question} the {@link Application.Domain.User} is answering to.
	 * 
	 * @return	The {@link Application.Domain.Question} the {@link Application.Domain.User} is answering to.
	 */
	public Question getQuestion() {
		return question;
	}

	/**
	 * Sets the {@link Application.Domain.Question} the {@link Application.Domain.User} is answering to.
	 * 
	 * @param question	The {@link Application.Domain.Question} the {@link Application.Domain.User} is answering to.
	 */
	public void setQuestion(Question question) {
		this.question = question;
	}

	/**
	 * Returns the {@link Application.Domain.User} providing the Answer to the {@link Application.Domain.Question}.
	 * 
	 * @return	The {@link Application.Domain.User} providing the Answer to the {@link Application.Domain.Question}.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the {@link Application.Domain.User} providing the Answer to the {@link Application.Domain.Question}.
	 * 
	 * @param user	The {@link Application.Domain.User} providing the Answer to the {@link Application.Domain.Question}.
	 */
	public void setUser(User user) {
		this.user = user;
	}

}
