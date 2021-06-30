package Application.Domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * Narrative is the class that maps the database Entity Narrative.
 * The Narrative defines the Topic of some {@link Application.Domain.Scenario}(s).
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
public class Narrative {

	/**
	 * The unique identifier of the Narrative.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idNarrative;
	
	/**
	 * The name of the Narrative.
	 */
	@NotNull
	private String name;
	
	/**
	 * The description of the Narrative.
	 */
	@NotNull
	private String description;
	
	/**
	 * The language of the Narrative.
	 */
	@NotNull
	private String lang;
	
	/**
	 * The list of {@link Application.Domain.Scenario}(s) of a Topic.
	 */
	@OneToMany(mappedBy = "narrative")
	private Collection<Scenario> scenarios;
	
	/**
	 * Creates a new Narrative.
	 */
	public Narrative() {
		
	}
	
	/**
	 * Creates a new Narrative.
	 * 
	 * @param idNarrative	The unique identifier of the Narrative.
	 */
	public Narrative(int idNarrative) {
		this.idNarrative = idNarrative;
	}

	/**
	 * Returns the unique identifier of the Narrative.
	 * 
	 * @return	The unique identifier of the Narrative.
	 */
	public int getIdNarrative() {
		return idNarrative;
	}

	/**
	 * Sets the unique identifier of the Narrative.
	 * 
	 * @param idNarrative	The unique identifier of the Narrative.
	 */
	public void setIdNarrative(int idNarrative) {
		this.idNarrative = idNarrative;
	}

	/**
	 * Returns the name of the Narrative.
	 * 
	 * @return	The name of the Narrative.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the Narrative.
	 * 
	 * @param name	The name of the Narrative.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the description of the Narrative.
	 * 
	 * @return	The description of the Narrative.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the Narrative.
	 * 
	 * @param description	The description of the Narrative.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the list of {@link Application.Domain.Scenario}(s) of a Topic.
	 * 
	 * @return	The list of {@link Application.Domain.Scenario}(s) of a Topic.
	 */
	public Collection<Scenario> getScenarios() {
		return scenarios;
	}

	/**
	 * Sets the list of {@link Application.Domain.Scenario}(s) of a Topic.
	 * 
	 * @param scenarios	The list of {@link Application.Domain.Scenario}(s) of a Topic.
	 */
	public void setScenarios(Collection<Scenario> scenarios) {
		this.scenarios = scenarios;
	}
	
	/**
	 * Returns the language of the Narrative.
	 * 
	 * @return	The language of the Narrative.
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * Sets the language of the Narrative.
	 * 
	 * @param lang	The language of the Narrative.
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

}
