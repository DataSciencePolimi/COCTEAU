package Application.Domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.hibernate.annotations.Immutable;

/**
 * Leaderboard is the class that maps the database Vision Leaderboard.
 * The Leaderboard of all {@link Application.Domain.User}s based on the points they achieved as recorded in the {@link Application.Domain.Match} object.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
@Immutable
public class Leaderboard {

	/**
	 * The unique identifier of the {@link Application.Domain.User}.
	 */
	@Id
	private String cookie;
	
	/**
	 * The sum of all the points achieved by the {@link Application.Domain.User} as recorded in the {@link Application.Domain.Match} object.
	 */
	private int points;
	
	private String username;
	
	private byte[] picture;
	
	@Transient
	private String img;
	
	/**
	 * Creates a new Leaderboard.
	 */
	public Leaderboard() {
		
	}

	/**
	 * Creates a new Leaderboard.
	 * 
	 * @param cookie	The unique identifier of the {@link Application.Domain.User}.
	 * @param points	The sum of all the points achieved by the {@link Application.Domain.User} as recorded in the {@link Application.Domain.Match} object.
	 */
	public Leaderboard(String cookie, int points) {
		this.cookie = cookie;
		this.points = points;
	}
	
	/**
	 * Returns the unique identifier of the {@link Application.Domain.User}.
	 * 
	 * @return	The unique identifier of the {@link Application.Domain.User}.
	 */
	public String getCookie() {
		return cookie;
	}

	/**
	 * Sets the unique identifier of the {@link Application.Domain.User}.
	 * 
	 * @param cookie	The unique identifier of the {@link Application.Domain.User}.
	 */
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	/**
	 * Returns the sum of all the points achieved by the {@link Application.Domain.User} as recorded in the {@link Application.Domain.Match} object.
	 * 
	 * @return	The sum of all the points achieved by the {@link Application.Domain.User} as recorded in the {@link Application.Domain.Match} object.
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Sets the sum of all the points achieved by the {@link Application.Domain.User} as recorded in the {@link Application.Domain.Match} object.
	 * 
	 * @param points	The sum of all the points achieved by the {@link Application.Domain.User} as recorded in the {@link Application.Domain.Match} object.
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public byte[] getPicture() {
		return picture;
	}

	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}
	
}
