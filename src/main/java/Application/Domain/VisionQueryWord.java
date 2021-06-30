package Application.Domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * VisionQueryWord is the class that maps the database Entity VisionQueryWord.
 * A VisionQueryWord represents the mapping between a {@link Application.Domain.Vision} and a {@link Application.Domain.QueryWord}. It also contains the information to clip the chosen pictures correctly. 
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
@Table(name = "vision_query_word")
public class VisionQueryWord {

	/**
	 * The unique identifier of the VisionQueryWord.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idVisionQueryWord;
	
	/**
	 * Whether the picture associated with the {@link Application.Domain.QueryWord} has been chosen.
	 */
	@NotNull
	private boolean chosen;
	
	/**
	 * The horizontal clipping position of the picture.
	 */
	@Column(name = "position_x")
	private double positionX;
	
	/**
	 * The vertical clipping position of the picture.
	 */
	@Column(name = "position_y")
	private double positionY;
	
	/**
	 * The size multiplier associated with the picture.
	 */
	@Column(name = "size_multiplier")
	private double sizeMultiplier;
	
	/**
	 * The url of the picture.
	 */
	private String url;
	
	/**
	 * The text used as query to look for the picture.
	 */
	@ManyToOne
	@JoinColumn(name = "query_word")
	private QueryWord queryWord;
	
	/**
	 * The {@link Application.Domain.Vision} the picture is associated with.
	 */
	@ManyToOne
	@JoinColumn(name = "vision")
	private Vision vision;
	
	/**
	 * Creates a new VisionQueryWord.
	 */
	public VisionQueryWord() {
		
	}
	
	/**
	 * Creates a new VisionQueryWord.
	 * 
	 * @param chosen	Whether the picture associated with the {@link Application.Domain.QueryWord} has been chosen.
	 * @param queryWord	The text used as query to look for the picture.
	 * @param vision	The {@link Application.Domain.Vision} the picture is associated with.
	 */
	public VisionQueryWord(@NotNull boolean chosen, QueryWord queryWord, Vision vision) {
		this.chosen = chosen;
		this.queryWord = queryWord;
		this.vision = vision;
	}
	
	/**
	 * Creates a new VisionQueryWord.
	 * 
	 * @param chosen	Whether the picture associated with the {@link Application.Domain.QueryWord} has been chosen.
	 * @param positionX	The horizontal clipping position of the picture.
	 * @param positionY	The vertical clipping position of the picture.
	 * @param sizeMultiplier	The size multiplier associated with the picture.
	 * @param url	The url of the picture.
	 * @param queryWord	The text used as query to look for the picture.
	 * @param vision	The {@link Application.Domain.Vision} the picture is associated with.
	 */
	public VisionQueryWord(@NotNull boolean chosen, double positionX, double positionY, double sizeMultiplier,
			String url, QueryWord queryWord, Vision vision) {
		this.chosen = chosen;
		this.positionX = positionX;
		this.positionY = positionY;
		this.sizeMultiplier = sizeMultiplier;
		this.url = url;
		this.queryWord = queryWord;
		this.vision = vision;
	}

	/**
	 * Returns the unique identifier of the VisionQueryWord.
	 * 
	 * @return	The unique identifier of the VisionQueryWord.
	 */
	public int getIdVisionQueryWord() {
		return idVisionQueryWord;
	}

	/**
	 * Sets the unique identifier of the VisionQueryWord.
	 * 
	 * @param idVisionQueryWord	The unique identifier of the VisionQueryWord.
	 */
	public void setIdVisionQueryWord(int idVisionQueryWord) {
		this.idVisionQueryWord = idVisionQueryWord;
	}

	/**
	 * Returns whether the picture associated with the {@link Application.Domain.QueryWord} has been chosen.
	 * 
	 * @return	Whether the picture associated with the {@link Application.Domain.QueryWord} has been chosen.
	 */
	public boolean isChosen() {
		return chosen;
	}

	/**
	 * Sets whether the picture associated with the {@link Application.Domain.QueryWord} has been chosen.
	 * 
	 * @param chosen	Whether the picture associated with the {@link Application.Domain.QueryWord} has been chosen.
	 */
	public void setChosen(boolean chosen) {
		this.chosen = chosen;
	}

	/**
	 * Returns the text used as query to look for the picture.
	 * 
	 * @return	The text used as query to look for the picture.
	 */
	public QueryWord getQueryWord() {
		return queryWord;
	}

	/**
	 * Sets the text used as query to look for the picture.
	 * 
	 * @param queryWord	The text used as query to look for the picture.
	 */
	public void setQueryWord(QueryWord queryWord) {
		this.queryWord = queryWord;
	}

	/**
	 * Returns the {@link Application.Domain.Vision} the picture is associated with.
	 * 
	 * @return	The {@link Application.Domain.Vision} the picture is associated with.
	 */
	public Vision getVision() {
		return vision;
	}

	/**
	 * Sets the {@link Application.Domain.Vision} the picture is associated with.
	 * 
	 * @param vision	The {@link Application.Domain.Vision} the picture is associated with.
	 */
	public void setVision(Vision vision) {
		this.vision = vision;
	}

	/**
	 * Returns the horizontal clipping position of the picture.
	 * 
	 * @return	The horizontal clipping position of the picture.
	 */
	public double getPositionX() {
		return positionX;
	}

	/**
	 * Sets the horizontal clipping position of the picture.
	 * 
	 * @param positionX	The horizontal clipping position of the picture.
	 */
	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	/**
	 * Returns the vertical clipping position of the picture.
	 * 
	 * @return	The vertical clipping position of the picture.
	 */
	public double getPositionY() {
		return positionY;
	}

	/**
	 * Sets he vertical clipping position of the picture.
	 * 
	 * @param positionY	The vertical clipping position of the picture.
	 */
	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

	/**
	 * Returns the size multiplier associated with the picture.
	 * 
	 * @return	The size multiplier associated with the picture.
	 */
	public double getSizeMultiplier() {
		return sizeMultiplier;
	}

	/**
	 * Sets the size multiplier associated with the picture.
	 * 
	 * @param sizeMultiplyer	The size multiplier associated with the picture.
	 */
	public void setSizeMultiplier(double sizeMultiplyer) {
		this.sizeMultiplier = sizeMultiplyer;
	}

	/**
	 * Returns the url of the picture.
	 * 
	 * @return	The url of the picture.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url of the picture.
	 * 
	 * @param url	The url of the picture.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
