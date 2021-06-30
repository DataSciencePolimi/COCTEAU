package Application.Domain;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Vision is the class that maps the database Entity Vision.
 * A Vision is a representation of the thoughts of the {@link Application.Domain.User} on a {@link Application.Domain.Scenario}.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
public class Vision {
	
	/**
	 * The unique identifier of the Vision.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idVision;
	
	/**
	 * The first Dimension of the Vision.
	 */
	@Column(name="first_dim")
	private double firstDim;
	
	/**
	 * The second Dimension of the Vision.
	 */
	@Column(name="second_dim")
	private double secondDim;
	
	/**
	 * The description of the Vision.
	 */
	private String description;
	
	/**
	 * The string version of the {@link Application.Domain.Keyword}(s) associated with the Vision.
	 */
	@Transient
	private Collection<String> keywordStringCollection;
	
	/**
	 * Transient property for a {@link Application.Domain.Vision} used as a relay when going from a quick match to in-depth match.
	 */
	@Transient
	private boolean showInDepth;
	
	/**
	 * The {@link Application.Domain.Scenario} the Vision is associated with.
	 */
	@ManyToOne
	@JoinColumn(name = "scenario")
	private Scenario scenario;
	
	/**
	 * The list of {@link Application.Domain.VisionKeyword}(s) that maps the Vision to its {@link Application.Domain.Keyword}(s).
	 */
	@OneToMany(mappedBy = "vision")
	private Collection<VisionKeyword> keywordCollection;
	
	/**
	 * The {@link Application.Domain.User} who created the Vision.
	 */
	@ManyToOne
	@JoinColumn(name = "cookie")
	private User user;
	
	/**
	 * The list of {@link Application.Domain.Match}(s) including the Vision.
	 */
	@OneToMany(mappedBy="visionChallenger")
	private Collection<Match> matchOpponentCollection;
	
	/**
	 * The image associated with the Vision.
	 */
	@NotNull
	@Column(name = "img_path")
    private byte[] imgPath;
	
	/**
	 * The path of the image associated with the Vision.
	 */
	@Transient
	private String img;
	
	/**
	 * The list of {@link Application.Domain.VisionQueryWord}(s) that maps the Vision to the {@link Application.Domain.QueryWord}(s) searched by the {@link Application.Domain.User} when creating the picture associated with the Vision.
	 */
	@OneToMany(mappedBy = "vision")
	private Collection<VisionQueryWord> visionQueryCollection;
	
	/**
	 * Creates a new Vision.
	 */
	public Vision() {
		
	}
	
	/**
	 * Creates a new Vision.
	 * 
	 * @param scenario	The {@link Application.Domain.Scenario} the Vision is associated with.
	 */
	public Vision(Scenario scenario) {
		this.scenario = scenario;
	}
	
	/**
	 * Creates a new Vision.
	 * 
	 * @param idVision	The unique identifier of the Vision.
	 * @param firstDim	The first Dimension of the Vision.
	 * @param secondDim	The second Dimension of the Vision.
	 */
	public Vision(@NotNull int idVision, double firstDim, double secondDim) {
		this.idVision = idVision;
		this.firstDim = firstDim;
		this.secondDim = secondDim;
	}
	
	/**
	 * Creates a new Vision.
	 * 
	 * @param idVision	The unique identifier of the Vision.
	 * @param firstDim	The first Dimension of the Vision.
	 * @param secondDim	The second Dimension of the Vision.
	 * @param user	The {@link Application.Domain.User} who created the Vision.
	 * @param matchOpponentCollection	The list of {@link Application.Domain.Match}(s) including the Vision.
	 * @param scenario	The {@link Application.Domain.Scenario} the Vision is associated with.
	 * @param imgPath	The image associated with the Vision.
	 */
	public Vision(@NotNull int idVision, double firstDim, double secondDim, User user,
			Collection<Match> matchOpponentCollection, Scenario scenario,
			byte[] imgPath) {
		this.idVision = idVision;
		this.firstDim = firstDim;
		this.secondDim = secondDim;
		this.user = user;
		this.matchOpponentCollection = matchOpponentCollection;
		this.scenario = scenario;
		this.imgPath = imgPath;
	}

	/**
	 * Returns the unique identifier of the Vision.
	 * 
	 * @return	The unique identifier of the Vision.
	 */
	public int getIdVision() {
		return idVision;
	}

	/**
	 * Sets the unique identifier of the Vision.
	 * 
	 * @param idVision	The unique identifier of the Vision.
	 */
	public void setIdVision(int idVision) {
		this.idVision = idVision;
	}

	/**
	 * Returns the string version of the {@link Application.Domain.Keyword}(s) associated with the Vision.
	 * 
	 * @return	The string version of the {@link Application.Domain.Keyword}(s) associated with the Vision.
	 */
	public Collection<String> getKeywordStringCollection() {
		return keywordStringCollection;
	}

	/**
	 * Sets the string version of the {@link Application.Domain.Keyword}(s) associated with the Vision.
	 * 
	 * @param keywordStringCollection	The string version of the {@link Application.Domain.Keyword}(s) associated with the Vision.
	 */
	public void setKeywordStringCollection(Collection<String> keywordStringCollection) {
		this.keywordStringCollection = keywordStringCollection;
	}

	/**
	 * Returns the list of {@link Application.Domain.VisionKeyword}(s) that maps the Vision to its {@link Application.Domain.Keyword}(s).
	 * 
	 * @return	The list of {@link Application.Domain.VisionKeyword}(s) that maps the Vision to its {@link Application.Domain.Keyword}(s).
	 */
	public Collection<VisionKeyword> getKeywordCollection() {
		return keywordCollection;
	}

	/**
	 * Sets the list of {@link Application.Domain.VisionKeyword}(s) that maps the Vision to its {@link Application.Domain.Keyword}(s).
	 * 
	 * @param keywordCollection	The list of {@link Application.Domain.VisionKeyword}(s) that maps the Vision to its {@link Application.Domain.Keyword}(s).
	 */
	public void setKeywordCollection(Collection<VisionKeyword> keywordCollection) {
		this.keywordCollection = keywordCollection;
	}

	/**
	 * Returns the {@link Application.Domain.User} who created the Vision.
	 * 
	 * @return	The {@link Application.Domain.User} who created the Vision.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets the {@link Application.Domain.User} who created the Vision.
	 * 
	 * @param user	The {@link Application.Domain.User} who created the Vision.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * Returns the list of {@link Application.Domain.Match}(s) including the Vision.
	 * 
	 * @return	The list of {@link Application.Domain.Match}(s) including the Vision.
	 */
	public Collection<Match> getMatchOpponentCollection() {
		return matchOpponentCollection;
	}

	/**
	 * Sets the list of {@link Application.Domain.Match}(s) including the Vision.
	 * 
	 * @param matchOpponentCollection	The list of {@link Application.Domain.Match}(s) including the Vision.
	 */
	public void setMatchOpponentCollection(Collection<Match> matchOpponentCollection) {
		this.matchOpponentCollection = matchOpponentCollection;
	}

	/**
	 * Returns the {@link Application.Domain.Scenario} the Vision is associated with.
	 * 
	 * @return	The {@link Application.Domain.Scenario} the Vision is associated with.
	 */
	public Scenario getScenario() {
		return scenario;
	}

	/**
	 * Sets the {@link Application.Domain.Scenario} the Vision is associated with.
	 * 
	 * @param scenario	The {@link Application.Domain.Scenario} the Vision is associated with.
	 */
	public void setScenario(Scenario scenario) {
		this.scenario = scenario;
	}

	/**
	 * Returns the description of the Vision.
	 * 
	 * @return	The description of the Vision.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the Vision.
	 * 
	 * @param description	The description of the Vision.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the image associated with the Vision.
	 * 
	 * @return	The image associated with the Vision.
	 */
	public byte[] getImgPath() {
		return imgPath;
	}

	/**
	 * Sets the image associated with the Vision.
	 * 
	 * @param imgPath	The image associated with the Vision.
	 */
	public void setImgPath(byte[] imgPath) {
		this.imgPath = imgPath;
	}

	/**
	 * Returns the path of the image associated with the Vision.
	 * 
	 * @return	The path of the image associated with the Vision.
	 */
	public String getImg() {
		return img;
	}

	/**
	 * Sets the path of the image associated with the Vision.
	 * 
	 * @param img	The path of the image associated with the Vision.
	 */
	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * 
	 * 
	 * @return showInDepth transient property for a {@link Application.Domain.Vision}
	 */
	public boolean isShowInDepth() {
		return showInDepth;
	}

	/**
	 * Sets the showInDepth transient property
	 * 
	 * @param showInDepth transient property for a {@link Application.Domain.Vision}
	 */
	public void setShowInDepth(boolean showInDepth) {
		this.showInDepth = showInDepth;
	}

	/**
	 * Returns the list of {@link Application.Domain.VisionQueryWord}(s) that maps the Vision to the {@link Application.Domain.QueryWord}(s) searched by the {@link Application.Domain.User} when creating the picture associated with the Vision.
	 * 
	 * @return	The list of {@link Application.Domain.VisionQueryWord}(s) that maps the Vision to the {@link Application.Domain.QueryWord}(s) searched by the {@link Application.Domain.User} when creating the picture associated with the Vision.
	 */
	public Collection<VisionQueryWord> getVisionQueryCollection() {
		return visionQueryCollection;
	}

	/**
	 * Sets the list of {@link Application.Domain.VisionQueryWord}(s) that maps the Vision to the {@link Application.Domain.QueryWord}(s) searched by the {@link Application.Domain.User} when creating the picture associated with the Vision.
	 * 
	 * @param visionQueryCollection	The list of {@link Application.Domain.VisionQueryWord}(s) that maps the Vision to the {@link Application.Domain.QueryWord}(s) searched by the {@link Application.Domain.User} when creating the picture associated with the Vision.
	 */
	public void setVisionQueryCollection(Collection<VisionQueryWord> visionQueryCollection) {
		this.visionQueryCollection = visionQueryCollection;
	}

	/**
	 * Returns the first Dimension of the Vision.
	 * 
	 * @return	The first Dimension of the Vision.
	 */
	public double getFirstDim() {
		return firstDim;
	}

	/**
	 * Sets the first Dimension of the Vision.
	 * 
	 * @param firstDim	The first Dimension of the Vision.
	 */
	public void setFirstDim(double firstDim) {
		this.firstDim = firstDim;
	}

	/**
	 * Returns the second Dimension of the Vision.
	 * 
	 * @return	The second Dimension of the Vision.
	 */
	public double getSecondDim() {
		return secondDim;
	}

	/**
	 * Sets the second Dimension of the Vision.
	 * 
	 * @param secondDim	The second Dimension of the Vision.
	 */
	public void setSecondDim(double secondDim) {
		this.secondDim = secondDim;
	}
}