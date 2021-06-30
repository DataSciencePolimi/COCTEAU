package Application.Domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * VisionKeyword is the class that maps the database Entity VisionKeyword.
 * A VisionKeyword maps a {@link Application.Domain.Vision} with a {@link Application.Domain.Keyword}.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
@Table(name = "vision_keyword")
public class VisionKeyword {

	/**
	 * The unique identifier of the VisionKeyword.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idVisionKeyword;
	
	/**
	 * The {@link Application.Domain.Keyword} associated with the VisionKeyword.
	 */
	@ManyToOne
	@JoinColumn(name = "id_keyword")
	private Keyword keyword;
	
	/**
	 * The {@link Application.Domain.Vision} associated with the VisionKeyword.
	 */
	@ManyToOne
	@JoinColumn(name = "id_vision")
	private Vision vision;
	
	/**
	 * Creates a new VisionKeyword.
	 */
	public VisionKeyword() {
		
	}
	
	/**
	 * Creates a new VisionKeyword.
	 * 
	 * @param keyword	The {@link Application.Domain.Keyword} associated with the VisionKeyword.
	 * @param vision	The {@link Application.Domain.Vision} associated with the VisionKeyword.
	 */
	public VisionKeyword(@NotNull Keyword keyword, @NotNull Vision vision) {
		this.keyword = keyword;
		this.vision = vision;
	}

	/**
	 * Returns the unique identifier of the VisionKeyword.
	 * 
	 * @return	The unique identifier of the VisionKeyword.
	 */
	public int getIdVisionKeyword() {
		return idVisionKeyword;
	}

	/**
	 * Sets the unique identifier of the VisionKeyword.
	 * 
	 * @param idVisionKeyword	The unique identifier of the VisionKeyword.
	 */
	public void setIdVisionKeyword(int idVisionKeyword) {
		this.idVisionKeyword = idVisionKeyword;
	}

	/**
	 * Returns the {@link Application.Domain.Keyword} associated with the VisionKeyword.
	 * 
	 * @return	The {@link Application.Domain.Keyword} associated with the VisionKeyword.
	 */
	public Keyword getKeyword() {
		return keyword;
	}

	/**
	 * Sets the {@link Application.Domain.Keyword} associated with the VisionKeyword.
	 * 
	 * @param keyword	The {@link Application.Domain.Keyword} associated with the VisionKeyword.
	 */
	public void setKeyword(Keyword keyword) {
		this.keyword = keyword;
	}

	/**
	 * Returns the {@link Application.Domain.Vision} associated with the VisionKeyword.
	 * 
	 * @return	The {@link Application.Domain.Vision} associated with the VisionKeyword.
	 */	
	public Vision getVision() {
		return vision;
	}

	/**
	 * Sets the {@link Application.Domain.Vision} associated with the VisionKeyword.
	 * 
	 * @param vision	The {@link Application.Domain.Vision} associated with the VisionKeyword.
	 */
	public void setVision(Vision vision) {
		this.vision = vision;
	}
}