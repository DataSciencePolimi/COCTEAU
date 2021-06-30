package Application.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Feeling is the class that maps the database Entity Feeling.
 * The Feeling represent the sentiment expressed by the {@link Application.Domain.User}.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
public class Feeling {

	/**
	 * The unique identifier of the Feeling.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idFeeling;
	
	/**
	 * The {@link Application.Domain.User} who expressed their Feeling.
	 */
	@NotNull
	@ManyToOne
	@JoinColumn(name = "cookie")
	private User user;
	
	/**
	 * The {@link Application.Domain.Scenario} for which the {@link Application.Domain.User} expressed their Feeling.
	 */
	@NotNull
	@ManyToOne
	@JoinColumn(name = "scenario")
	private Scenario scenario;
	
	/**
	 * The number representing the Feeling expressed by the {@link Application.Domain.User} before playing within the {@link Application.Domain.Scenario}.
	 */
	private int voteBefore;
	
	/**
	 * The number representing the Feeling expressed by the {@link Application.Domain.User} after playing within the {@link Application.Domain.Scenario}.
	 */
	private int voteAfter;
	
	/**
	 * The additional comment provided by the {@link Application.Domain.User} before playing within the {@link Application.Domain.Scenario}.
	 */
	private String opinionBefore;
	
	/**
	 * The additional comment provided by the {@link Application.Domain.User} after playing within the {@link Application.Domain.Scenario}.
	 */
	private String opinionAfter;

	/**
	 * Creates a new Feeling.
	 */
	public Feeling() {
		
	}
	/**
	 * The unique identifier of the Feeling.
	 * 
	 * @return	The unique identifier of the Feeling.
	 */
	public int getIdFeeling() {
		return idFeeling;
	}

	/**
	 * The unique identifier of the Feeling.
	 * 
	 * @param idFeeling	The unique identifier of the Feeling.
	 */
	public void setIdFeeling(int idFeeling) {
		this.idFeeling = idFeeling;
	}

	/**
	 * Returns the {@link Application.Domain.User} who expressed their Feeling.
	 * 
	 * @return	The {@link Application.Domain.User} who expressed their Feeling.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the {@link Application.Domain.User} who expressed their Feeling.
	 * 
	 * @param user	The {@link Application.Domain.User} who expressed their Feeling.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Returns the {@link Application.Domain.Scenario} for which a {@link Application.Domain.User} expressed their Feeling.
	 * 
	 * @return the {@link Application.Domain.Scenario} for which a {@link Application.Domain.User} expressed their Feeling.
	 */
	public Scenario getScenario() {
		return scenario;
	}

	/**
	 * Sets the {@link Application.Domain.Scenario} for which the {@link Application.Domain.User} expressed their Feeling.
	 * 
	 * @param scenario	The {@link Application.Domain.Scenario} for which the {@link Application.Domain.User} expressed their Feeling.
	 */
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	/**
	 * Returns the number representing the Feeling expressed by the {@link Application.Domain.User} before playing within the {@link Application.Domain.Scenario}.
	 * 
	 * @return	The number representing the Feeling expressed by the {@link Application.Domain.User} before playing within the {@link Application.Domain.Scenario}.
	 */
	public int getVoteBefore() {
		return voteBefore;
	}

	/**
	 * Sets the number representing the Feeling expressed by the {@link Application.Domain.User} before playing within the {@link Application.Domain.Scenario}.
	 * 
	 * @param voteBefore	The number representing the Feeling expressed by the {@link Application.Domain.User} before playing within the {@link Application.Domain.Scenario}.
	 */
	public void setVoteBefore(int voteBefore) {
		this.voteBefore = voteBefore;
	}

	/**
	 * Returns the number representing the Feeling expressed by the {@link Application.Domain.User} after playing within the {@link Application.Domain.Scenario}.
	 * 
	 * @return	The number representing the Feeling expressed by the {@link Application.Domain.User} after playing within the {@link Application.Domain.Scenario}.
	 */
	public int getVoteAfter() {
		return voteAfter;
	}

	/**
	 * Sets the number representing the Feeling expressed by the {@link Application.Domain.User} after playing within the {@link Application.Domain.Scenario}.
	 * 
	 * @param voteAfter	The number representing the Feeling expressed by the {@link Application.Domain.User} after playing within the {@link Application.Domain.Scenario}.
	 */
	public void setVoteAfter(int voteAfter) {
		this.voteAfter = voteAfter;
	}

	/**
	 * Returns the additional comment provided by the {@link Application.Domain.User} before playing within the {@link Application.Domain.Scenario}.
	 * 
	 * @return	The additional comment provided by the {@link Application.Domain.User} before playing within the {@link Application.Domain.Scenario}.
	 */
	public String getOpinionBefore() {
		return opinionBefore;
	}

	/**
	 * Sets the additional comment provided by the {@link Application.Domain.User} before playing within the {@link Application.Domain.Scenario}.
	 * 
	 * @param opinionBefore	The additional comment provided by the {@link Application.Domain.User} before playing within the {@link Application.Domain.Scenario}.
	 */
	public void setOpinionBefore(String opinionBefore) {
		this.opinionBefore = opinionBefore;
	}

	/**
	 * Returns the additional comment provided by the {@link Application.Domain.User} after playing within the {@link Application.Domain.Scenario}.
	 * 
	 * @return	The additional comment provided by the {@link Application.Domain.User} after playing within the {@link Application.Domain.Scenario}.
	 */
	public String getOpinionAfter() {
		return opinionAfter;
	}

	/**
	 * Sets the additional comment provided by the {@link Application.Domain.User} after playing within the {@link Application.Domain.Scenario}.
	 * 
	 * @param opinionAfter	The additional comment provided by the {@link Application.Domain.User} after playing within the {@link Application.Domain.Scenario}.
	 */
	public void setOpinionAfter(String opinionAfter) {
		this.opinionAfter = opinionAfter;
	}
}
