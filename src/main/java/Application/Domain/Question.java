package Application.Domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * Question is the class that maps the database Entity Question.
 * A Question is part of a {@link Application.Domain.Quiz} and it is made of both the text and the answers.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
public class Question {
	
	/**
	 * The unique identifier of the Question.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idQuestion;
	
	/**
	 * The text of the Question.
	 */
	@NotNull
	private String questionText;
	
	/**
	 * The first answer to the Question.
	 */
	@NotNull
	private String answerOne;
	
	/**
	 * The second answer to the Question.
	 */
	@NotNull
	private String answerTwo;
	
	/**
	 * The third answer to the Question.
	 */
	@NotNull
	private String answerThree;
	
	/**
	 * The fourth answer to the Question.
	 */
	@NotNull
	private String answerFour;
	
	/**
	 * The fifth answer to the Question.
	 */
	@NotNull
	private String answerFive;
	
	/**
	 * The (optional) additional text provided for the Question.
	 */
	@NotNull
	private String commentQuestionText;
	
	/**
	 * Whether the Question is inside the {@link Application.Domain.Quiz}.
	 */
	@NotNull
	private boolean showQuestion;
	
	/**
	 * Whether the (optional) additional text provided for the Question is visible or not.
	 */
	@NotNull
	private boolean showCommentQuestion;
	
	/**
	 * The number of the Question 
	 */
	@NotNull
	private int questionNumber;
	
	/**
	 * The language of the Question 
	 */
	@NotNull
	private String language;
	
	/**
	 * The {@link Application.Domain.Quiz} the Question is part of.
	 */
	@ManyToOne
	@JoinColumn(name = "id_quiz")
	private Quiz quiz;
	
	/**
	 * The list of {@link Application.Domain.Answer}(s) provided by the {@link Application.Domain.User}(s) to the Question.
	 */
	@OneToMany(mappedBy = "question")
	private Collection<Answer> answers;
	
	/**
	 * Creates a new Question.
	 */
	public Question() {
		
	}
	
	/**
	 * Creates a new Question.
	 * 
	 * @param idQuestion	The unique identifier of the Question.
	 */
	public Question(@NotNull int idQuestion) {
		this.idQuestion = idQuestion;
	}

	/**
	 * Returns the unique identifier of the Question.
	 * 
	 * @return	The unique identifier of the Question.
	 */
	public int getIdQuestion() {
		return idQuestion;
	}

	/**
	 * Sets the unique identifier of the Question.
	 * 
	 * @param idQuestion	The unique identifier of the Question.
	 */
	public void setIdQuestion(int idQuestion) {
		this.idQuestion = idQuestion;
	}

	/**
	 * Returns the text of the Question.
	 * 
	 * @return	The text of the Question.
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * Sets the text of the Question.
	 * 
	 * @param questionText	The text of the Question.
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	/**
	 * Returns the first answer to the Question.
	 * 
	 * @return	The first answer to the Question.
	 */
	public String getAnswerOne() {
		return answerOne;
	}

	/**
	 * Sets the first answer to the Question.
	 * 
	 * @param answerOne	The first answer to the Question.
	 */
	public void setAnswerOne(String answerOne) {
		this.answerOne = answerOne;
	}

	/**
	 * Returns the second answer to the Question.
	 * 
	 * @return	The second answer to the Question.
	 */
	public String getAnswerTwo() {
		return answerTwo;
	}
	
	/**
	 * Sets the second answer to the Question.
	 * 
	 * @param answerTwo	The second answer to the Question.
	 */
	public void setAnswerTwo(String answerTwo) {
		this.answerTwo = answerTwo;
	}

	/**
	 * Returns the third answer to the Question.
	 * 
	 * @return	The third answer to the Question.
	 */
	public String getAnswerThree() {
		return answerThree;
	}

	/**
	 * Sets the third answer to the Question.
	 * 
	 * @param answerThree	The third answer to the Question.
	 */
	public void setAnswerThree(String answerThree) {
		this.answerThree = answerThree;
	}

	/**
	 * Returns the fourth answer to the Question.
	 * 
	 * @return	The fourth answer to the Question.
	 */
	public String getAnswerFour() {
		return answerFour;
	}

	/**
	 * Sets the fourth answer to the Question.
	 * 
	 * @param answerFour	The fourth answer to the Question.
	 */
	public void setAnswerFour(String answerFour) {
		this.answerFour = answerFour;
	}
	
	/**
	 * Returns the fifth answer to the Question.
	 * 
	 * @return	The fifth answer to the Question.
	 */
	public String getAnswerFive() {
		return answerFive;
	}

	/**
	 * Sets the fifth answer to the Question.
	 * 
	 * @param answerFive	The fifth answer to the Question.
	 */
	public void setAnswerFive(String answerFive) {
		this.answerFive = answerFive;
	}

	/**
	 * Returns the (optional) additional text provided for the Question.
	 * 
	 * @return	The (optional) additional text provided for the Question.
	 */
	public String getCommentQuestionText() {
		return commentQuestionText;
	}

	/**
	 * Sets the (optional) additional text provided for the Question.
	 * 
	 * @param commentQuestionText	The (optional) additional text provided for the Question.
	 */
	public void setCommentQuestionText(String commentQuestionText) {
		this.commentQuestionText = commentQuestionText;
	}

	/**
	 * Returns whether the Question is inside the {@link Application.Domain.Quiz}.
	 * 
	 * @return	Whether the Question is inside the {@link Application.Domain.Quiz}.
	 */
	public boolean isShowQuestion() {
		return showQuestion;
	}

	/**
	 * Sets whether the Question is inside the {@link Application.Domain.Quiz}.
	 * 
	 * @param showQuestion	Whether the Question is inside the {@link Application.Domain.Quiz}.
	 */
	public void setShowQuestion(boolean showQuestion) {
		this.showQuestion = showQuestion;
	}

	/**
	 * Returns whether the (optional) additional text provided for the Question is visible or not.
	 * 
	 * @return	 Whether the (optional) additional text provided for the Question is visible or not.
	 */
	public boolean isShowCommentQuestion() {
		return showCommentQuestion;
	}

	/**
	 * Sets whether the (optional) additional text provided for the Question is visible or not.
	 * 
	 * @param showCommentQuestion	Whether the (optional) additional text provided for the Question is visible or not.
	 */
	public void setShowCommentQuestion(boolean showCommentQuestion) {
		this.showCommentQuestion = showCommentQuestion;
	}

	/**
	 * Returns the {@link Application.Domain.Quiz} the Question is part of.
	 * 
	 * @return	The {@link Application.Domain.Quiz} the Question is part of.
	 */
	public Quiz getQuiz() {
		return quiz;
	}

	/**
	 * Sets the {@link Application.Domain.Quiz} the Question is part of.
	 * 
	 * @param quiz	The {@link Application.Domain.Quiz} the Question is part of.
	 */
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	/**
	 * Returns the list of {@link Application.Domain.Answer}(s) provided by the {@link Application.Domain.User}(s) to the Question.
	 * 
	 * @return	The list of {@link Application.Domain.Answer}(s) provided by the {@link Application.Domain.User}(s) to the Question.
	 */
	public Collection<Answer> getAnswers() {
		return answers;
	}

	/**
	 * Sets the list of {@link Application.Domain.Answer}(s) provided by the {@link Application.Domain.User}(s) to the Question.
	 * 
	 * @param answers	The list of {@link Application.Domain.Answer}(s) provided by the {@link Application.Domain.User}(s) to the Question.
	 */
	public void setAnswers(Collection<Answer> answers) {
		this.answers = answers;
	}

	/**
	 * Returns the number of the Question 
	 * 
	 * @return	 The number of the Question 
	 */
	public int getQuestionNumber() {
		return questionNumber;
	}

	/**
	 * Sets the number of the Question 
	 * 
	 * @param questionNumber	The number of the Question 
	 */
	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}

	/**
	 * Returns the language of the Question 
	 * 
	 * @return	The language of the Question 
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Sets the language of the Question
	 * 
	 * @param language	The language of the Question 
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
}
