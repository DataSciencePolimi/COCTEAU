package Application.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * ReferralCodeScenario is the class that maps the database Entity ReferralCodeScenario.
 * A ReferralCodeScenario is the mapping class between {@link Application.Domain.ReferralCode} and {@link Application.Domain.Scenario}.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
public class ReferralCodeScenario {
	
	/**
	 * The unique identifier of the ReferralCodeScenario.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idReferralCodeScenario;
	
	/**
	 * The {@link Application.Domain.ReferralCode} associated with the ReferralCodeScenario.
	 */
	@ManyToOne
	@JoinColumn(name = "referral_code")
	private ReferralCode referralCode;
	
	/**
	 * The {@link Application.Domain.Scenario} associated with the ReferralCodeScenario.
	 */
	@ManyToOne
	@JoinColumn(name = "scenario")
	private Scenario scenario;
	
	/**
	 * Whether the {@link Application.Domain.Scenario} associated with the {@link Application.Domain.ReferralCode} is visible to the {@link Application.Domain.User}(s).
	 */
	@NotNull
	private boolean released;
	
	/**
	 * Creates a new ReferralCodeScenario.
	 */
	public ReferralCodeScenario() {
		
	}

	/**
	 * Returns the unique identifier of the ReferralCodeScenario.
	 * 
	 * @return	The unique identifier of the ReferralCodeScenario.
	 */
	public int getIdReferralCodeScenario() {
		return idReferralCodeScenario;
	}

	/**
	 * Sets the unique identifier of the ReferralCodeScenario.
	 * 
	 * @param idReferralCodeScenario	The unique identifier of the ReferralCodeScenario.
	 */
	public void setIdReferralCodeScenario(int idReferralCodeScenario) {
		this.idReferralCodeScenario = idReferralCodeScenario;
	}

	/**
	 * Returns the {@link Application.Domain.ReferralCode} associated with the ReferralCodeScenario.
	 * 
	 * @return	The {@link Application.Domain.ReferralCode} associated with the ReferralCodeScenario.
	 */
	public ReferralCode getReferralCode() {
		return referralCode;
	}

	/**
	 * Sets the {@link Application.Domain.ReferralCode} associated with the ReferralCodeScenario.
	 * 
	 * @param referralCode	The {@link Application.Domain.ReferralCode} associated with the ReferralCodeScenario.
	 */
	public void setReferralCode(ReferralCode referralCode) {
		this.referralCode = referralCode;
	}

	/**
	 * Returns the {@link Application.Domain.Scenario} associated with the ReferralCodeScenario.
	 * 
	 * @return	The {@link Application.Domain.Scenario} associated with the ReferralCodeScenario.
	 */
	public Scenario getScenario() {
		return scenario;
	}

	/**
	 * Sets the {@link Application.Domain.Scenario} associated with the ReferralCodeScenario.
	 * 
	 * @param scenario	The {@link Application.Domain.Scenario} associated with the ReferralCodeScenario.
	 */
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	/**
	 * Returns whether the {@link Application.Domain.Scenario} associated with the {@link Application.Domain.ReferralCode} is visible to the {@link Application.Domain.User}(s).
	 * 
	 * @return	Whether the {@link Application.Domain.Scenario} associated with the {@link Application.Domain.ReferralCode} is visible to the {@link Application.Domain.User}(s).
	 */
	public boolean isReleased() {
		return released;
	}

	/**
	 * Sets whether the {@link Application.Domain.Scenario} associated with the {@link Application.Domain.ReferralCode} is visible to the {@link Application.Domain.User}(s).
	 * 
	 * @param released	Whether the {@link Application.Domain.Scenario} associated with the {@link Application.Domain.ReferralCode} is visible to the {@link Application.Domain.User}(s).
	 */
	public void setReleased(boolean released) {
		this.released = released;
	}
	
}
