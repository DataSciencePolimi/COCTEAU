package Application.Domain;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * User is the class that maps the database Entity User.
 * A User contains all the useful information about the citizens who use the platform.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
public class User extends Credentials {
	
	/**
	 * The Enumeration of the gender of the user (M = Male, F = Female, N = Not provided)
	 */
	public enum Gender {
		M, F, N
	}
	
	/**
	 * The unique identifier of the User.
	 */
	@Id
	@NotNull
	private String cookie;
	
	/**
	 * The age of the User.
	 */
	@Column(columnDefinition = "integer default 0")
	private int age;
	
	/**
	 * The nationality of the User.
	 */
	private String nationality;
	
	/**
	 * The country of the User.
	 */
	private String country;
	
	/**
	 * The region where the User lives.
	 */
	private String region;
	
	/**
	 * Whether the User provided their information or not.
	 */
	@NotNull
	private boolean skip = false;
	
	/**
	 * Whether the account of the User has been activated or not.
	 */
	@NotNull
	private boolean activated = false;
	
	/**
	 * The number representing the level of education of the User.
	 */
	@Column(columnDefinition = "integer default 0")
	private int education;
	
	/**
	 * The number representing how much the User is interested in sharing their opinion.
	 */
	@Column(columnDefinition = "integer default 0")
	private int interest;
	
	/**
	 * The picture of the profile of the User.
	 */
	@Column(name = "profile_picture")
    private byte[] profilePicture;
	
	/**
	 * The gender of the user.
	 */
	@Enumerated(EnumType.STRING)
	private Gender gender = Gender.N;
	
	/**
	 * The e-mail of the user.
	 */
	private String mail;
	
	/**
	 * The code representing the activities the User on the platform.
	 */
	@Column(name = "access_code")
	private String accessCode = "000000010";
	
	/**
	 * The list of the {@link Application.Domain.Vision}(s) created by the User.
	 */
	@OneToMany(mappedBy = "user")
	private Collection<Vision> visionCollection;
	
	/**
	 * The list of the {@link Application.Domain.Match}(s) of the User.
	 */
	@OneToMany(mappedBy = "player")
	private Collection<Match> matchPlayerCollection;
	
	/**
	 * The list of the {@link Application.Domain.Answer}(s) provided by the User to the {@link Application.Domain.Quiz}(s).
	 */
	@OneToMany(mappedBy = "user")
	private Collection<Answer> quizAnswers;
	
	/**
	 * The {@link Application.Domain.ReferralCode} associated with the User.
	 */
	@ManyToOne
	@JoinColumn(name = "referral_code")
	private ReferralCode referralCode;
	
	/**
	 * The list of the {@link Application.Domain.Feeling} expressed by the User.
	 */
	@OneToMany(mappedBy = "user")
	private Collection<Feeling> feelings;
	
	/**
	 * The list of {@link Application.Domain.UserAchievement} linking the User with their achieved {@link Application.Domain.Achievement}(s).
	 */
	@OneToMany(mappedBy = "achiever")
	private Collection<UserAchievement> achievedAchievements;
	
	/**
	 * The string of path of the image associated with the profile of the User.
	 */
	@Transient 
	private String img;
	
	/**
	 * The unique number associated with the User.
	 */
	@NotNull
	private int entryNumber;
	
	/**
	 * Creates a new User.
	 */
	public User() {
		
	}
	
	/**
	 * Creates a new User.
	 * 
	 * @param cookie	The unique identifier of the User.
	 */
	public User(String cookie) {
		this.cookie = cookie;
	}
	
	/**
	 * Creates a new User.
	 * 
	 * @param cookie	The unique identifier of the User.
	 * @param password	The password of {@link Application.Domain.Credentials} of the User.
	 * @param entryNumber	The unique number associated with the User.
	 */
	public User(@NotNull String cookie, String password, int entryNumber) {
		this.cookie = cookie;
		this.username = cookie;
		this.password = password;
		this.entryNumber = entryNumber;
	}
	
	/**
	 * Creates a new User.
	 * 
	 * @param cookie	The unique identifier of the User.
	 * @param age	The age of the User.
	 * @param country	The country of the User.
	 * @param gender	The gender of the User.
	 * @param mail	The e-mail of the User.
	 * @param nationality	The nationality of the User.
	 * @param region	The region of the User.
	 */
	public User(@NotNull String cookie, int age, String country, Gender gender, String mail, String nationality, String region) {
		this.cookie = cookie;
		this.country = country;
		this.gender = gender;
		this.mail = mail;
		this.age = age;
		this.nationality = nationality;
		this.region = region;
		this.password = cookie;
	}

	/**
	 * Returns the unique identifier of the User.
	 * 
	 * @return The unique identifier of the User.
	 */
	public String getCookie() {
		return cookie;
	}

	/**
	 * Sets the unique identifier of the User.
	 * 
	 * @param cookie	The unique identifier of the User.
	 */
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	/**
	 * Returns the age of the User.
	 * 
	 * @return	The age of the User.
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Sets the age of the User.
	 * 
	 * @param age	The age of the User.
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Returns the country of the User.
	 * 
	 * @return	The country of the User.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Sets the country of the User.
	 * 
	 * @param country	The country of the User.
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	
	/**
	 * Returns the region of the User.
	 * 
	 * @return	The region of the User.
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * Sets the region of the User.
	 * 
	 * @param region	The region of the User.
	 */
	public void setRegion(String region) {
		this.region = region;
	}

	/**
	 * Returns the gender of the User.
	 * 
	 * @return	The gender of the User.
	 */
	public Gender getGender() {
		return gender;
	}

	/**
	 * Sets the gender of the User.
	 * 
	 * @param gender	The gender of the User.
	 */
	public void setGender(Gender gender) {
		this.gender = gender;
	}

	/**
	 * Returns the e-mail of the User.
	 * 
	 * @return	The e-mail of the User.
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * Sets The e-mail of the User.
	 * 
	 * @param mail	The e-mail of the User.
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}
	
	/**
	 * Returns the nationality of the User.
	 * 
	 * @return	The nationality of the User.
	 */
	public String getNationality() {
		return nationality;
	}

	/**
	 * Sets the nationality of the User.
	 * 
	 * @param nationality	The nationality of the User.
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	
	/**
	 * Returns whether the User provided their information or not.
	 * 
	 * @return	Whether the User provided their information or not.
	 */
	public boolean getSkip() {
		return skip;
	}

	/**
	 * Sets whether the User provided their information or not.
	 * 
	 * @param skip	Whether the User provided their information or not.
	 */
	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	/**
	 * Returns the list of the {@link Application.Domain.Vision}(s) created by the User.
	 * 
	 * @return	The list of the {@link Application.Domain.Vision}(s) created by the User.
	 */
	public Collection<Vision> getVisionCollection() {
		return visionCollection;
	}

	/**
	 * Sets the list of the {@link Application.Domain.Vision}(s) created by the User.
	 * 
	 * @param visionCollection	The list of the {@link Application.Domain.Vision}(s) created by the User.
	 */
	public void setVisionCollection(Collection<Vision> visionCollection) {
		this.visionCollection = visionCollection;
	}

	/**
	 * Returns the list of the {@link Application.Domain.Match}(s) of the User.
	 * 
	 * @return	The list of the {@link Application.Domain.Match}(s) of the User.
	 */
	public Collection<Match> getMatchPlayerCollection() {
		return matchPlayerCollection;
	}

	/**
	 * Sets the list of the {@link Application.Domain.Match}(s) of the User.
	 * 
	 * @param matchPlayerCollection	The list of the {@link Application.Domain.Match}(s) of the User.
	 */
	public void setMatchPlayerCollection(Collection<Match> matchPlayerCollection) {
		this.matchPlayerCollection = matchPlayerCollection;
	}

	/**
	 * Returns the list of the {@link Application.Domain.Answer}(s) provided by the User to the {@link Application.Domain.Quiz}(s).
	 * 
	 * @return	The list of the {@link Application.Domain.Answer}(s) provided by the User to the {@link Application.Domain.Quiz}(s).
	 */
	public Collection<Answer> getQuizAnswers() {
		return quizAnswers;
	}

	/**
	 * Sets the list of the {@link Application.Domain.Answer}(s) provided by the User to the {@link Application.Domain.Quiz}(s).
	 * 
	 * @param quizAnswers	The list of the {@link Application.Domain.Answer}(s) provided by the User to the {@link Application.Domain.Quiz}(s).
	 */
	public void setQuizAnswers(Collection<Answer> quizAnswers) {
		this.quizAnswers = quizAnswers;
	}

	/**
	 * Returns the {@link Application.Domain.ReferralCode} associated with the User.
	 * 
	 * @return	The {@link Application.Domain.ReferralCode} associated with the User.
	 */
	public ReferralCode getReferralCode() {
		return referralCode;
	}

	/**
	 * Sets the {@link Application.Domain.ReferralCode} associated with the User.
	 * 
	 * @param referralCode	The {@link Application.Domain.ReferralCode} associated with the User.
	 */
	public void setReferralCode(ReferralCode referralCode) {
		this.referralCode = referralCode;
	}

	/**
	 * Returns the number representing the level of education of the User.
	 * 
	 * @return	The number representing the level of education of the User.
	 */
	public int getEducation() {
		return education;
	}

	/**
	 * Sets the number representing the level of education of the User.
	 * 
	 * @param education	The number representing the level of education of the User.
	 */
	public void setEducation(int education) {
		this.education = education;
	}

	/**
	 * Returns the number representing how much the User is interested in sharing their opinion.
	 * 
	 * @return	The number representing how much the User is interested in sharing their opinion.
	 */
	public int getInterest() {
		return interest;
	}

	/**
	 * Sets the number representing how much the User is interested in sharing their opinion.
	 * 
	 * @param interest	The number representing how much the User is interested in sharing their opinion.
	 */
	public void setInterest(int interest) {
		this.interest = interest;
	}

	/**
	 * Returns the list of the {@link Application.Domain.Feeling} expressed by the User.
	 * 
	 * @return	The list of the {@link Application.Domain.Feeling} expressed by the User.
	 */
	public Collection<Feeling> getFeelings() {
		return feelings;
	}

	/**
	 * Sets the list of the {@link Application.Domain.Feeling} expressed by the User.
	 * 
	 * @param feelings	The list of the {@link Application.Domain.Feeling} expressed by the User.
	 */
	public void setFeelings(Collection<Feeling> feelings) {
		this.feelings = feelings;
	}

	/**
	 * Returns the code representing the activities the User on the platform.
	 * 
	 * @return	The code representing the activities the User on the platform.
	 */
	public String getAccessCode() {
		return accessCode;
	}

	/**
	 * Sets the code representing the activities the User on the platform.
	 * 
	 * @param accessCode	The code representing the activities the User on the platform.
	 */
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	/**
	 * Returns the picture of the profile of the User.
	 * 
	 * @return	The picture of the profile of the User.
	 */
	public byte[] getProfilePicture() {
		return profilePicture;
	}

	/**
	 * Sets the picture of the profile of the User.
	 * 
	 * @param profilePicture	The picture of the profile of the User.
	 */
	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = profilePicture;
	}

	/**
	 * Returns the string of path of the image associated with the profile of the User.
	 * 
	 * @return	The string of path of the image associated with the profile of the User.
	 */
	public String getImg() {
		return img;
	}

	/**
	 * Sets the string of path of the image associated with the profile of the User.
	 * 
	 * @param img	The string of path of the image associated with the profile of the User.
	 */
	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * Returns the list of {@link Application.Domain.UserAchievement} linking the User with their achieved {@link Application.Domain.Achievement}(s).
	 * 
	 * @return	The list of {@link Application.Domain.UserAchievement} linking the User with their achieved {@link Application.Domain.Achievement}(s).
	 */
	public Collection<UserAchievement> getAchievedAchievements() {
		return achievedAchievements;
	}

	/**
	 * Sets the list of {@link Application.Domain.UserAchievement} linking the User with their achieved {@link Application.Domain.Achievement}(s).
	 * 
	 * @param achievedAchievements	The list of {@link Application.Domain.UserAchievement} linking the User with their achieved {@link Application.Domain.Achievement}(s).
	 */
	public void setAchievedAchievements(Collection<UserAchievement> achievedAchievements) {
		this.achievedAchievements = achievedAchievements;
	}

	/**
	 * Returns whether the account of the User has been activated or not.
	 * 
	 * @return	Whether the account of the User has been activated or not.
	 */
	public boolean isActivated() {
		return activated;
	}

	/**
	 * Sets whether the account of the User has been activated or not.
	 * 
	 * @param activated	Whether the account of the User has been activated or not.
	 */
	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	/**
	 * Returns the unique number associated with the User.
	 * 
	 * @return	The unique number associated with the User.
	 */
	public int getEntryNumber() {
		return entryNumber;
	}

	/**
	 * Sets the unique number associated with the User.
	 * 
	 * @param entryNumber	The unique number associated with the User.
	 */
	public void setEntryNumber(int entryNumber) {
		this.entryNumber = entryNumber;
	}
}
