package Application.Domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * QueryWord is the class that maps the database Entity QueryWord.
 * A QueryWord is a word for which a {@link Application.Domain.User} searched for while creating the Picture of a {@link Application.Domain.Vision}.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
public class QueryWord {

	/**
	 * The unique identifier of the QueryWord.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idQueryWord;
	
	/**
	 * The text of the QueryWord.
	 */
	@NotNull
	private String queryWord;
	
	/**
	 * The list of {@link Application.Domain.Vision}(s) to which the QueryWord is associated.
	 */
	@OneToMany(mappedBy = "queryWord")
	private Collection<VisionQueryWord> visionQueryCollection;
	
	/**
	 * Creates a new QueryWord.
	 */
	public QueryWord() {
		
	}
	
	/**
	 * Creates a new QueryWord.
	 * 
	 * @param queryWord	The text of the QueryWord.
	 */
	public QueryWord(String queryWord) {
		this.queryWord = queryWord;
	}

	/**
	 * Returns the unique identifier of the QueryWord.
	 * 
	 * @return	The unique identifier of the QueryWord.
	 */
	public int getIdQueryWord() {
		return idQueryWord;
	}

	/**
	 * Sets the unique identifier of the QueryWord.
	 * 
	 * @param idQueryWord	The unique identifier of the QueryWord.
	 */
	public void setIdQueryWord(int idQueryWord) {
		this.idQueryWord = idQueryWord;
	}

	/**
	 * Returns the text of the QueryWord.
	 * 
	 * @return	The text of the QueryWord.
	 */
	public String getQueryWord() {
		return queryWord;
	}

	/**
	 * Sets the text of the QueryWord.
	 * 
	 * @param queryWord	The text of the QueryWord.
	 */
	public void setQueryWord(String queryWord) {
		this.queryWord = queryWord;
	}

	/**
	 * Returns the list of {@link Application.Domain.Vision}(s) to which the QueryWord is associated.
	 * 
	 * @return	The list of {@link Application.Domain.Vision}(s) to which the QueryWord is associated.
	 */
	public Collection<VisionQueryWord> getVisionQueryCollection() {
		return visionQueryCollection;
	}

	/**
	 * Sets the list of {@link Application.Domain.Vision}(s) to which the QueryWord is associated.
	 * 
	 * @param visionQueryCollection	The list of {@link Application.Domain.Vision}(s) to which the QueryWord is associated.
	 */
	public void setVisionQueryCollection(Collection<VisionQueryWord> visionQueryCollection) {
		this.visionQueryCollection = visionQueryCollection;
	}
}
