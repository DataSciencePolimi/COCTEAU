package Application.Domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * Administrator is the class that maps the database Entity Administrator.
 * An Administrator is a user that manages the {@link Application.Domain.Scenario}(s).
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 */

@Entity
public class Administrator extends Credentials {
	
	/**
	 * The unique identifier of the Administrator.
	 */
	@Id
	@NotNull
	private String idAdministrator;
	
	/**
	 * The boolean variable representing whether the Administrator is one of the root ones.
	 */
	@NotNull
	private boolean root;
	
	/**
	 * The list of {@link Application.Domain.Scenario}(s) managed by the Administrator.
	 */
	@OneToMany(mappedBy = "creator")
	private Collection<Scenario> scenarios;
	
	/**
	 * Creates a new Administrator.
	 */
	public Administrator() {
		
	}
	
	/**
	 * Creates a new Administrator.
	 * 
	 * @param	idAdministrator	The unique identifier of the Administrator.
	 */
	public Administrator(String idAdministrator) {
		this.idAdministrator = idAdministrator;
	}

	/**
	 * Returns the unique identifier of the Administrator.
	 * 
	 * @return	The unique identifier of the Administrator.
	 */
	public String getIdAdministrator() {
		return idAdministrator;
	}

	/**
	 * Sets the unique identifier of the Administrator.
	 * 
	 * @param	idAdministrator	The unique identifier of the Administrator.
	 */
	public void setIdAdministrator(String idAdministrator) {
		this.idAdministrator = idAdministrator;
	}

	/**
	 * Returns the list of {@link Application.Domain.Scenario}(s) managed by the Administrator.
	 * 
	 * @return	The list of {@link Application.Domain.Scenario}(s) managed by the Administrator.
	 */
	public Collection<Scenario> getScenarios() {
		return scenarios;
	}

	/**
	 * Sets the list of {@link Application.Domain.Scenario}(s) managed by the Administrator.
	 * 
	 * @param scenarios	The list of {@link Application.Domain.Scenario}(s) managed by the Administrator.
	 */
	public void setScenarios(Collection<Scenario> scenarios) {
		this.scenarios = scenarios;
	}

	/**
	 * Returns he boolean variable representing whether the Administrator is one of the root ones.
	 * 
	 * @return	 The boolean variable representing whether the Administrator is one of the root ones.
	 */
	public boolean isRoot() {
		return root;
	}

	/**
	 * Sets the boolean variable representing whether the Administrator is one of the root ones.
	 * 
	 * @param	root	 The boolean variable representing whether the Administrator is one of the root ones.
	 */
	public void setRoot(boolean root) {
		this.root = root;
	}

}
