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
 * Scenario is the class that maps the database Entity Scenario.
 * A Scenario is a 
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
public class Scenario {

	/**
	 * The unique identifier of the Scenario.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idScenario;
	
	/**
	 * The description of the Scenario.
	 */
	@NotNull
	private String description;
	
	/**
	 * The first question asked to the {@link Application.Domain.User} about the Scenario.
	 */
	@NotNull
	private String questionVisionFirst;
	
	/**
	 * The title of the Scenario.
	 */
	@NotNull
	private String title;
	
	/**
	 * The group number associated with the Scenario.
	 */
	@NotNull
	private int scenarioGroup;
	
	/**
	 * The language of the Scenario.
	 */
	@NotNull
	private String lang;
	
	/**
	 * The lists of {@link Application.Domain.Vision}(s) created by the {@link Application.Domain.User}(s) in the Scenario.
	 */
	@OneToMany(mappedBy = "scenario")
	private Collection<Vision> visions;
	
	/**
	 * The {@link Application.Domain.Narrative} the Scenario belongs to.
	 */
	@ManyToOne
	@JoinColumn(name = "id_narrative")
	private Narrative narrative;
	
	/**
	 * The {@link Application.Domain.Quiz} associated with the Scenario.
	 */
	@ManyToOne
	@JoinColumn(name = "quiz")
	private Quiz quiz;
	
	/**
	 * Whether the {@link Application.Domain.User} has already completed the {@link Application.Domain.Quiz}.
	 */
	@Transient
	private boolean hasCompletedQuiz;
	
	/**
	 * Whether the {@link Application.Domain.User} has shared their {@link Application.Domain.Feeling} for the first time.
	 */
	@Transient
	private boolean expressedFeelings;
	
	/**
	 * Whether the {@link Application.Domain.User} has already played a specified number of {@link Application.Domain.Match}.
	 */
	@Transient
	private boolean hasPlayedEnoughMatches;
	
	/**
	 * Whether the {@link Application.Domain.User} has already created at least one {@link Application.Domain.Vision}.
	 */
	@Transient
	private boolean hasCreatedAVision;
	
	/**
	 * Whether the {@link Application.Domain.User} has shared their {@link Application.Domain.Feeling} for the second time.
	 */
	@Transient
	private boolean secondFeelingsGiven;
	
	/**
	 * The list of the {@link Application.Domain.ReferralCodeScenario}(s) associated with the Scenario.
	 */
	@OneToMany(mappedBy = "scenario")
	private Collection<ReferralCodeScenario> associatedReferralCode;
	
	/**
	 * The list of the {@link Application.Domain.Feeling} expressed with the Scenario.
	 */
	@OneToMany(mappedBy = "scenario")
	private Collection<Feeling> feelings;
	
	/**
	 * The image associated with the Scenario.
	 */
	@NotNull
	@Column(name = "img_path")
    private byte[] imgPath;
	
	/**
	 * The string of path of the image associated with the Scenario.
	 */
	@Transient
	private String img;
	
	/**
	 * Whether the Scenario is visible to the {@link Application.Domain.User}(s) or not.
	 */
	@NotNull
	private boolean published;
	
	/**
	 * Whether the Scenario is considered deleted.
	 */
	@NotNull
	private boolean deleted;
	
	/**
	 * The {@link Application.Domain.Administrator} who created the Scenario.
	 */
	@ManyToOne
	@JoinColumn(name="creator")
	private Administrator creator;
	
	/**
	 * The name of the first Dimension associated with the Scenario.
	 */
	@NotNull
	private String firstDimName;
	
	/**
	 * The description of the first Dimension associated with the Scenario.
	 */
	@NotNull
	private String firstDimDesc;
	
	/**
	 * The name of the second Dimension associated with the Scenario.
	 */
	@NotNull
	private String secondDimName;
	
	/**
	 * The description of the second Dimension associated with the Scenario.
	 */
	@NotNull
	private String secondDimDesc;
	
	/**
	 * The credits of the image from Unsplash.
	 */
	private String imgCredits;
	
	/**
	 * The name of the author of the image.
	 */
	@Transient
	private String authorName;
	
	/**
	 * Creates a new Scenario.
	 */
	public Scenario() {
		
	}
	
	/**
	 * Creates a new Scenario.
	 * 
	 * @param idScenario	The unique identifier of the Scenario.
	 */
	public Scenario(@NotNull int idScenario) {
		this.idScenario = idScenario;
	}
	
	/**
	 * Creates a new Scenario.
	 * 
	 * @param idScenario	The unique identifier of the Scenario.
	 * @param description	The description of the Scenario.
	 */
	public Scenario(@NotNull int idScenario, @NotNull String description) {
		this.idScenario = idScenario;
		this.description = description;
	}

	/**
	 * Returns the unique identifier of the Scenario.
	 * 
	 * @return	The unique identifier of the Scenario.
	 */
	public int getIdScenario() {
		return idScenario;
	}

	/**
	 * Sets the unique identifier of the Scenario.
	 * 
	 * @param idScenario	The unique identifier of the Scenario.
	 */
	public void setIdScenario(int idScenario) {
		this.idScenario = idScenario;
	}

	/**
	 * Returns the description of the Scenario.
	 * 
	 * @return	The description of the Scenario.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the Scenario.
	 * 
	 * @param description	The description of the Scenario.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the lists of {@link Application.Domain.Vision}(s) created by the {@link Application.Domain.User}(s) in the Scenario.
	 * 
	 * @return	The lists of {@link Application.Domain.Vision}(s) created by the {@link Application.Domain.User}(s) in the Scenario.
	 */
	public Collection<Vision> getVisions() {
		return visions;
	}

	/**
	 * Sets the lists of {@link Application.Domain.Vision}(s) created by the {@link Application.Domain.User}(s) in the Scenario.
	 * 
	 * @param visions	The lists of {@link Application.Domain.Vision}(s) created by the {@link Application.Domain.User}(s) in the Scenario.
	 */
	public void setVisions(Collection<Vision> visions) {
		this.visions = visions;
	}

	/**
	 * Returns the first question asked to the {@link Application.Domain.User} about the Scenario.
	 * 
	 * @return	The first question asked to the {@link Application.Domain.User} about the Scenario.
	 */
	public String getQuestionVisionFirst() {
		return questionVisionFirst;
	}

	/**
	 * Sets the first question asked to the {@link Application.Domain.User} about the Scenario.
	 * 
	 * @param questionVisionFirst	The first question asked to the {@link Application.Domain.User} about the Scenario.
	 */
	public void setQuestionVisionFirst(String questionVisionFirst) {
		this.questionVisionFirst = questionVisionFirst;
	}
	
	/**
	 * Returns the {@link Application.Domain.Narrative} the Scenario belongs to.
	 * 
	 * @return	The {@link Application.Domain.Narrative} the Scenario belongs to.
	 */
	public Narrative getNarrative() {
		return narrative;
	}

	/**
	 * Sets the {@link Application.Domain.Narrative} the Scenario belongs to.
	 * 
	 * @param narrative	The {@link Application.Domain.Narrative} the Scenario belongs to.
	 */
	public void setNarrative(Narrative narrative) {
		this.narrative = narrative;
	}

	/**
	 * Returns the title of the Scenario.
	 * 
	 * @return	The title of the Scenario.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the Scenario.
	 * 
	 * @param title	The title of the Scenario.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the {@link Application.Domain.Quiz} associated with the Scenario.
	 * 
	 * @return	The {@link Application.Domain.Quiz} associated with the Scenario.
	 */
	public Quiz getQuiz() {
		return quiz;
	}

	/**
	 * Sets the {@link Application.Domain.Quiz} associated with the Scenario.
	 * 
	 * @param quiz	The {@link Application.Domain.Quiz} associated with the Scenario.
	 */
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}

	/**
	 * Returns whether the {@link Application.Domain.User} has already completed the {@link Application.Domain.Quiz}.
	 * 
	 * @return	Whether the {@link Application.Domain.User} has already completed the {@link Application.Domain.Quiz}.
	 */
	public boolean isHasCompletedQuiz() {
		return hasCompletedQuiz;
	}

	/**
	 * Sets whether the {@link Application.Domain.User} has already completed the {@link Application.Domain.Quiz}.
	 * 
	 * @param hasCompletedQuiz	Whether the {@link Application.Domain.User} has already completed the {@link Application.Domain.Quiz}.
	 */
	public void setHasCompletedQuiz(boolean hasCompletedQuiz) {
		this.hasCompletedQuiz = hasCompletedQuiz;
	}

	/**
	 * Returns the language of the Scenario.
	 * 
	 * @return	The language of the Scenario.
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * Sets the language of the Scenario.
	 * 
	 * @param lang	The language of the Scenario.
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	/**
	 * Returns the list of the {@link Application.Domain.ReferralCodeScenario}(s) associated with the Scenario.
	 * 
	 * @return	The list of the {@link Application.Domain.ReferralCodeScenario}(s) associated with the Scenario.
	 */
	public Collection<ReferralCodeScenario> getAssociatedReferralCode() {
		return associatedReferralCode;
	}

	/**
	 * Sets the list of the {@link Application.Domain.ReferralCodeScenario}(s) associated with the Scenario.
	 * 
	 * @param associatedReferralCode	The list of the {@link Application.Domain.ReferralCodeScenario}(s) associated with the Scenario.
	 */
	public void setAssociatedReferralCode(Collection<ReferralCodeScenario> associatedReferralCode) {
		this.associatedReferralCode = associatedReferralCode;
	}
	
	/**
	 * Returns the list of the {@link Application.Domain.Feeling} expressed with the Scenario.
	 * 
	 * @return	The list of the {@link Application.Domain.Feeling} expressed with the Scenario.
	 */
	public Collection<Feeling> getFeelings() {
		return feelings;
	}

	/**
	 * Sets the list of the {@link Application.Domain.Feeling} expressed with the Scenario.
	 * 
	 * @param feelings	The list of the {@link Application.Domain.Feeling} expressed with the Scenario.
	 */
	public void setFeelings(Collection<Feeling> feelings) {
		this.feelings = feelings;
	}

	/**
	 * Returns whether the {@link Application.Domain.User} has shared their {@link Application.Domain.Feeling} for the first time.
	 * 
	 * @return	Whether the {@link Application.Domain.User} has shared their {@link Application.Domain.Feeling} for the first time.
	 */
	public boolean isExpressedFeelings() {
		return expressedFeelings;
	}

	/**
	 * Sets whether the {@link Application.Domain.User} has shared their {@link Application.Domain.Feeling} for the first time.
	 * 
	 * @param expressedFeelings	Whether the {@link Application.Domain.User} has shared their {@link Application.Domain.Feeling} for the first time.
	 */
	public void setExpressedFeelings(boolean expressedFeelings) {
		this.expressedFeelings = expressedFeelings;
	}

	/**
	 * Returns the image associated with the Scenario.
	 * 
	 * @return	The image associated with the Scenario.
	 */
	public byte[] getImgPath() {
		return imgPath;
	}

	/**
	 * Sets the image associated with the Scenario.
	 * 
	 * @param imgPath	The image associated with the Scenario.
	 */
	public void setImgPath(byte[] imgPath) {
		this.imgPath = imgPath;
	}

	/**
	 * Returns the string of path of the image associated with the Scenario.
	 * 
	 * @return	The string of path of the image associated with the Scenario.
	 */
	public String getImg() {
		return img;
	}

	/**
	 * Sets the string of path of the image associated with the Scenario.
	 * 
	 * @param img	The string of path of the image associated with the Scenario.
	 */
	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * Returns the group number associated with the Scenario.
	 * 
	 * @return	The group number associated with the Scenario.
	 */
	public int getScenarioGroup() {
		return scenarioGroup;
	}

	/**
	 * Sets the group number associated with the Scenario.
	 * 
	 * @param scenarioGroup	The group number associated with the Scenario.
	 */
	public void setScenarioGroup(int scenarioGroup) {
		this.scenarioGroup = scenarioGroup;
	}

	/**
	 * Returns whether the Scenario is visible to the {@link Application.Domain.User}(s) or not.
	 * 
	 * @return	Whether the Scenario is visible to the {@link Application.Domain.User}(s) or not.
	 */
	public boolean isPublished() {
		return published;
	}

	/**
	 * Sets whether the Scenario is visible to the {@link Application.Domain.User}(s) or not.
	 * 
	 * @param published	Whether the Scenario is visible to the {@link Application.Domain.User}(s) or not.
	 */
	public void setPublished(boolean published) {
		this.published = published;
	}
	
	/**
	 * Returns whether the Scenario is considered deleted.
	 * 
	 * @return	Whether the Scenario is considered deleted.
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Sets whether the Scenario is considered deleted.
	 * 
	 * @param deleted	Whether the Scenario is considered deleted.
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Returns the {@link Application.Domain.Administrator} who created the Scenario.
	 * 
	 * @return	The {@link Application.Domain.Administrator} who created the Scenario.
	 */
	public Administrator getCreator() {
		return creator;
	}

	/**
	 * Sets the {@link Application.Domain.Administrator} who created the Scenario.
	 * 
	 * @param creator	The {@link Application.Domain.Administrator} who created the Scenario.
	 */
	public void setCreator(Administrator creator) {
		this.creator = creator;
	}

	/**
	 * Returns whether the {@link Application.Domain.User} has already played a specified number of {@link Application.Domain.Match}.
	 * 
	 * @return	Whether the {@link Application.Domain.User} has already played a specified number of {@link Application.Domain.Match}.
	 */
	public boolean isHasPlayedEnoughMatches() {
		return hasPlayedEnoughMatches;
	}

	/**
	 * Sets whether the {@link Application.Domain.User} has already played a specified number of {@link Application.Domain.Match}.
	 * 
	 * @param hasPlayedEnoughMatches	Whether the {@link Application.Domain.User} has already played a specified number of {@link Application.Domain.Match}.
	 */
	public void setHasPlayedEnoughMatches(boolean hasPlayedEnoughMatches) {
		this.hasPlayedEnoughMatches = hasPlayedEnoughMatches;
	}

	/**
	 * Returns whether the {@link Application.Domain.User} has already created at least one {@link Application.Domain.Vision}.
	 * 
	 * @return	Whether the {@link Application.Domain.User} has already created at least one {@link Application.Domain.Vision}.
	 */
	public boolean isHasCreatedAVision() {
		return hasCreatedAVision;
	}

	/**
	 * Sets whether the {@link Application.Domain.User} has already created at least one {@link Application.Domain.Vision}.
	 * 
	 * @param hasCreatedAVision	Whether the {@link Application.Domain.User} has already created at least one {@link Application.Domain.Vision}.
	 */
	public void setHasCreatedAVision(boolean hasCreatedAVision) {
		this.hasCreatedAVision = hasCreatedAVision;
	}

	/**
	 * Returns whether the {@link Application.Domain.User} has shared their {@link Application.Domain.Feeling} for the second time.
	 * 
	 * @return	Whether the {@link Application.Domain.User} has shared their {@link Application.Domain.Feeling} for the second time.
	 */
	public boolean isSecondFeelingsGiven() {
		return secondFeelingsGiven;
	}

	/**
	 * Sets whether the {@link Application.Domain.User} has shared their {@link Application.Domain.Feeling} for the second time.
	 * 
	 * @param secondFeelingsGiven	Whether the {@link Application.Domain.User} has shared their {@link Application.Domain.Feeling} for the second time.
	 */
	public void setSecondFeelingsGiven(boolean secondFeelingsGiven) {
		this.secondFeelingsGiven = secondFeelingsGiven;
	}

	/**
	 * Returns the name of the first Dimension associated with the Scenario.
	 * 
	 * @return	The name of the first Dimension associated with the Scenario.
	 */
	public String getFirstDimName() {
		return firstDimName;
	}

	/**
	 * Sets the name of the first Dimension associated with the Scenario.
	 * 
	 * @param firstDimName	The name of the first Dimension associated with the Scenario.
	 */
	public void setFirstDimName(String firstDimName) {
		this.firstDimName = firstDimName;
	}

	/**
	 * Returns the description of the first Dimension associated with the Scenario.
	 * 
	 * @return	The description of the first Dimension associated with the Scenario.
	 */
	public String getFirstDimDesc() {
		return firstDimDesc;
	}

	/**
	 * Sets the description of the first Dimension associated with the Scenario.
	 * 
	 * @param firstDimDesc	The description of the first Dimension associated with the Scenario.
	 */
	public void setFirstDimDesc(String firstDimDesc) {
		this.firstDimDesc = firstDimDesc;
	}

	/**
	 * Returns the name of the second Dimension associated with the Scenario.
	 * 
	 * @return	The name of the second Dimension associated with the Scenario.
	 */
	public String getSecondDimName() {
		return secondDimName;
	}

	/**
	 * Sets the name of the second Dimension associated with the Scenario.
	 * 
	 * @param secondDimName	The name of the second Dimension associated with the Scenario.
	 */
	public void setSecondDimName(String secondDimName) {
		this.secondDimName = secondDimName;
	}

	/**
	 * Returns the description of the second Dimension associated with the Scenario.
	 * 
	 * @return	The description of the second Dimension associated with the Scenario.
	 */
	public String getSecondDimDesc() {
		return secondDimDesc;
	}

	/**
	 * Sets the description of the second Dimension associated with the Scenario.
	 * 
	 * @param secondDimDesc	The description of the second Dimension associated with the Scenario.
	 */
	public void setSecondDimDesc(String secondDimDesc) {
		this.secondDimDesc = secondDimDesc;
	}

	/**
	 * Returns the credits of the image from Unsplash.
	 * 
	 * @return	The credits of the image from Unsplash.
	 */
	public String getImgCredits() {
		return imgCredits;
	}

	/**
	 * Sets the credits of the image from Unsplash.
	 * 
	 * @param imgCredits	The credits of the image from Unsplash.
	 */
	public void setImgCredits(String imgCredits) {
		this.imgCredits = imgCredits;
	}

	/**
	 * Returns the name of the author of the image.
	 * 
	 * @return	The name of the author of the image.
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * Sets the name of the author of the image.
	 * 
	 * @param authorName	The name of the author of the image.
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
}
