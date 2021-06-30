package Application.Domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * Keyword is the class that maps the database Entity Keyword.
 * A Keyword is a relevant word assigned to a {@link Application.Domain.Vision} by a {@link Application.Domain.User}
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
public class Keyword {
	
	/**
	 * The unique identifier of the Keyword.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idKeyword;
	
	/**
	 * The text of the Keyword.
	 */
	@NotNull
	private String keyword;
	
	/**
	 * The list of {@link Application.Domain.Vision}(s) to which the Keyword is associated.
	 */
	@OneToMany(mappedBy = "keyword")
	private Collection<VisionKeyword> visionCollection;

	/**
	 * Creates a new Keyword.
	 */
	public Keyword() {
		
	}
	
	/**
	 * Creates a new Keyword.
	 * 
	 * @param keyword	The text of the Keyword.
	 */
	public Keyword(@NotNull String keyword) {
		this.keyword = keyword;
	}
	
	/**
	 * Creates a new Keyword.
	 * 
	 * @param idKeyword	The unique identifier of the Keyword.
	 * @param keyword	The text of the Keyword.
	 * @param visionCollection	The list of {@link Application.Domain.Vision}(s) to which the Keyword is associated.
	 */
	public Keyword(@NotNull int idKeyword, @NotNull String keyword, Collection<VisionKeyword> visionCollection) {
		this.idKeyword = idKeyword;
		this.keyword = keyword;
		this.visionCollection = visionCollection;
	}

	/**
	 * Returns the unique identifier of the Keyword.
	 * 
	 * @return	The unique identifier of the Keyword.
	 */
	public int getIdKeyword() {
		return idKeyword;
	}

	/**
	 * Sets the unique identifier of the Keyword.
	 * 
	 * @param idKeyword	The unique identifier of the Keyword.
	 */
	public void setIdKeyword(int idKeyword) {
		this.idKeyword = idKeyword;
	}

	/**
	 * Returns the text of the Keyword.
	 * 
	 * @return	The text of the Keyword.
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * Sets the text of the Keyword.
	 * 
	 * @param keyword	The text of the Keyword.
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * Returns the list of {@link Application.Domain.Vision}(s) to which the Keyword is associated.
	 * 
	 * @return	The list of {@link Application.Domain.Vision}(s) to which the Keyword is associated.
	 */
	public Collection<VisionKeyword> getVisionCollection() {
		return visionCollection;
	}

	/**
	 * Sets the list of {@link Application.Domain.Vision}(s) to which the Keyword is associated.
	 * 
	 * @param visionCollection	The list of {@link Application.Domain.Vision}(s) to which the Keyword is associated.
	 */
	public void setVisionCollection(Collection<VisionKeyword> visionCollection) {
		this.visionCollection = visionCollection;
	}
}
