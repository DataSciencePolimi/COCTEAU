package Application.Domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Achievement is the class that maps the database Entity Achievement.
 * An Achievement is a trophy awarded to the user when some requirements are met.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * @see		UserAchievement
 * 
 */

@Entity
@Table(name = "achievement")
public class Achievement {
	
	/**
	 * The unique identifier of the Achievement.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int id_achievement;
	
	/**
	 * The title of the Achievement.
	 */
	@NotNull
	private String title;
	
	/**
	 * The description of the Achievement.
	 */
	@NotNull
	private String description;
	
	/**
	 * The picture associated with the Achievement.
	 */
	@NotNull
	private byte[] imgAchievement;
	
	/**
	 * The mapping between the Achievement and the {@link Application.Domain.User}(s) who achieved it.
	 */
	@OneToMany(mappedBy = "achievement")
	private Collection<UserAchievement> achieverCollection;
	
	/**
	 * The String representing the position of the picture associated with the Achievement on disk.
	 */
	@Transient
	private String img;
	
	/**
	 * Constructs a new Achievement.
	 */
	public Achievement() {
		
	}
	
	/**
	 * Constructs a new Achievement.
	 * 
	 * @param	id_achievement	The unique identifier of the Achievement.
	 */
	public Achievement(int id_achievement) {
		this.id_achievement = id_achievement;
	}

	/**
	 * Returns the unique identifier of an Achievement.
	 * 
	 * @return	The unique identifier of the Achievement.
	 */
	public int getId_achievement() {
		return id_achievement;
	}
	
	/**
	 * Sets the unique identifier of an Achievement.
	 * 
	 * @param	id_achievement	The unique identifier of the Achievement.
	 */
	public void setId_achievement(int id_achievement) {
		this.id_achievement = id_achievement;
	}

	/**
	 * Returns the title of the Achievement.
	 * 
	 * @return	The title of the Achievement.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title of the Achievement.
	 * 
	 * @param	title	The title of the Achievement.
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the description of the Achievement.
	 * 
	 * @return	The description of the Achievement.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description of the Achievement.
	 * 
	 * @param	description	The description of the Achievement.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the mapping between the Achievement and the {@link Application.Domain.User}(s) who achieved it.
	 * 
	 * @return	The mapping between the Achievement and the {@link Application.Domain.User}(s) who achieved it.
	 */
	public Collection<UserAchievement> getAchieverCollection() {
		return achieverCollection;
	}

	/**
	 * Sets the mapping between the Achievement and the {@link Application.Domain.User}(s) who achieved it.
	 * 
	 * @param achieverCollection	The mapping between the Achievement and the {@link Application.Domain.User}(s) who achieved it.
	 */
	public void setAchieverCollection(Collection<UserAchievement> achieverCollection) {
		this.achieverCollection = achieverCollection;
	}

	/**
	 * Returns the picture associated with the Achievement.
	 * 
	 * @return	The picture associated with the Achievement.
	 */
	public byte[] getImgAchievement() {
		return imgAchievement;
	}

	/**
	 * Sets the picture associated with the Achievement.
	 * 
	 * @param imgAchievement	The picture associated with the Achievement.
	 */
	public void setImgAchievement(byte[] imgAchievement) {
		this.imgAchievement = imgAchievement;
	}

	/**
	 * Returns the String representing the position of the picture associated with the Achievement on disk.
	 * 
	 * @return The String representing the position of the picture associated with the Achievement on disk.
	 */
	public String getImg() {
		return img;
	}

	/**
	 * Sets the String representing the position of the picture associated with the Achievement on disk.
	 * 
	 * @param img	The String representing the position of the picture associated with the Achievement on disk.
	 */
	public void setImg(String img) {
		this.img = img;
	}

}
