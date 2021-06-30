package Application.Domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * ReferralCode is the class that maps the database Entity ReferralCode.
 * A ReferralCode is associated with a {@link Application.Domain.Scenario} to allow only the {@link Application.Domain.User}(s) with the ReferralCode to access it. 
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
public class ReferralCode {

	/**
	 * The unique identifier of the ReferralCode.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idReferralCode;
	
	/**
	 * The value of the ReferralCode.
	 */
	@NotNull
	private String value;
	
	/**
	 * The name of the ReferralCode.
	 */
	private String name;
	
	/**
	 * The list of the {@link Application.Domain.User}(s) who entered the ReferralCode.
	 */
	@OneToMany(mappedBy = "referralCode")
	private Collection<User> referredUsers;
	
	/**
	 * The list of the {@link Application.Domain.ReferralCodeScenario}(s) associated with the ReferralCode.
	 */
	@OneToMany(mappedBy = "referralCode")
	private Collection<ReferralCodeScenario> associatedScenarios;
	
	/**
	 * Creates a new ReferralCode.
	 */
	public ReferralCode() {
		
	}
	
	/**
	 * Creates a new ReferralCode.
	 * 
	 * @param idReferralCode	The unique identifier of the ReferralCode.
	 */
	public ReferralCode(int idReferralCode) {
		this.idReferralCode = idReferralCode;
	}

	/**
	 * Creates a new ReferralCode.
	 * 
	 * @param value	The value of the ReferralCode.
	 */
	public ReferralCode(String value) {
		this.value = value;
	}
	
	/**
	 * Returns the unique identifier of the ReferralCode.
	 * 
	 * @return	The unique identifier of the ReferralCode.
	 */
	public int getIdReferralCode() {
		return idReferralCode;
	}

	/**
	 * Sets	the unique identifier of the ReferralCode.
	 * 
	 * @param idReferralCode	The unique identifier of the ReferralCode.
	 */
	public void setIdReferralCode(int idReferralCode) {
		this.idReferralCode = idReferralCode;
	}

	/**
	 * Returns the value of the ReferralCode.
	 * 
	 * @return	The value of the ReferralCode.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the ReferralCode.
	 * 
	 * @param value	The value of the ReferralCode.
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Returns the name of the ReferralCode.
	 * 
	 * @return	The name of the ReferralCode.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the ReferralCode.
	 * 
	 * @param name	The name of the ReferralCode.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the list of the {@link Application.Domain.User}(s) who entered the ReferralCode.
	 * 
	 * @return	The list of the {@link Application.Domain.User}(s) who entered the ReferralCode.
	 */
	public Collection<User> getReferredUsers() {
		return referredUsers;
	}

	/**
	 * Sets the list of the {@link Application.Domain.User}(s) who entered the ReferralCode.
	 * 
	 * @param referredUsers	The list of the {@link Application.Domain.User}(s) who entered the ReferralCode.
	 */
	public void setReferredUsers(Collection<User> referredUsers) {
		this.referredUsers = referredUsers;
	}

	/**
	 * Returns the list of the {@link Application.Domain.ReferralCodeScenario}(s) associated with the ReferralCode.
	 * 
	 * @return	The list of the {@link Application.Domain.ReferralCodeScenario}(s) associated with the ReferralCode.
	 */
	public Collection<ReferralCodeScenario> getAssociatedScenarios() {
		return associatedScenarios;
	}

	/**
	 * Sets the list of the {@link Application.Domain.ReferralCodeScenario}(s) associated with the ReferralCode.
	 * 
	 * @param associatedScenarios	The list of the {@link Application.Domain.ReferralCodeScenario}(s) associated with the ReferralCode.
	 */
	public void setAssociatedScenarios(Collection<ReferralCodeScenario> associatedScenarios) {
		this.associatedScenarios = associatedScenarios;
	}
}
