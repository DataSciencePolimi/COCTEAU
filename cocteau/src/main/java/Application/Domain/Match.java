package Application.Domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Match is the class that maps the database Entity Matchmaking.
 * The Match is the representation of a Match of a {@link Application.Domain.User} with the {@link Application.Domain.Vision} of another {@link Application.Domain.User}. 
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Entity
@Table(name = "matchmaking")
public class Match {

	/**
	 * The unique identifier of the Match.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NotNull
	private int idMatch;
	
	/**
	 * The {@link Application.Domain.User} who played the Match.
	 */
	@NotNull
	@ManyToOne
	@JoinColumn(name = "player")
	private User player;
	
	/**
	 * The {@link Application.Domain.Vision} the {@link Application.Domain.User} played with.
	 */
	@NotNull
	@ManyToOne
	@JoinColumn(name = "vision_challenger")
	private Vision visionChallenger;
	
	/**
	 * The feeling guessed by the {@link Application.Domain.User} playing the Match.
	 */
	@NotNull
	private int guessedFeeling;
	
	/**
	 * The points earned by the {@link Application.Domain.User} by guessing the feeling.
	 */
	@NotNull
	private int points;
	
	/**
	 * The thoughts of the {@link Application.Domain.User} about the {@link Application.Domain.Vision}.
	 */
	private String thoughts;
	
	private Date playedDate;
	
	/**
	 * Creates a new Match.
	 */
	public Match() {
		
	}
	
	/**
	 * Creates a new Match.
	 * 
	 * @param idMatch	The unique identifier of the Match.
	 * @param player	The {@link Application.Domain.Vision} the {@link Application.Domain.User} played with.
	 * @param visionChallenger	The {@link Application.Domain.Vision} the {@link Application.Domain.User} played with.
	 * @param points	The points earned by the {@link Application.Domain.User} by guessing the feeling.
	 */
	public Match(@NotNull int idMatch, @NotNull User player, @NotNull Vision visionChallenger, @NotNull int points) {
		this.idMatch = idMatch;
		this.player = player;
		this.visionChallenger = visionChallenger;
		this.points = points;
	}

	/**
	 * Returns the unique identifier of the Match.
	 * 
	 * @return	The unique identifier of the Match.
	 */
	public int getIdMatch() {
		return idMatch;
	}

	/**
	 * Sets the unique identifier of the Match.
	 * 
	 * @param idMatch	The unique identifier of the Match.
	 */
	public void setIdMatch(int idMatch) {
		this.idMatch = idMatch;
	}

	/**
	 * Returns the {@link Application.Domain.Vision} the {@link Application.Domain.User} played with.
	 * 
	 * @return	The {@link Application.Domain.Vision} the {@link Application.Domain.User} played with.
	 */
	public Vision getVisionChallenger() {
		return visionChallenger;
	}

	/**
	 * Sets the {@link Application.Domain.Vision} the {@link Application.Domain.User} played with.
	 * 
	 * @param visionChallenger	The {@link Application.Domain.Vision} the {@link Application.Domain.User} played with.
	 */
	public void setVisionChallenger(Vision visionChallenger) {
		this.visionChallenger = visionChallenger;
	}
	
	/**
	 * Returns the points earned by the {@link Application.Domain.User} by guessing the feeling.
	 * 
	 * @return	The points earned by the {@link Application.Domain.User} by guessing the feeling.
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Sets the points earned by the {@link Application.Domain.User} by guessing the feeling.
	 * 
	 * @param points	The points earned by the {@link Application.Domain.User} by guessing the feeling.
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * Returns the {@link Application.Domain.Vision} the {@link Application.Domain.User} played with.
	 * 
	 * @return	The {@link Application.Domain.Vision} the {@link Application.Domain.User} played with.
	 */
	public User getPlayer() {
		return player;
	}

	/**
	 * Sets the {@link Application.Domain.Vision} the {@link Application.Domain.User} played with.
	 * 
	 * @param player	The {@link Application.Domain.Vision} the {@link Application.Domain.User} played with.
	 */
	public void setPlayer(User player) {
		this.player = player;
	}

	/**
	 * Returns the thoughts of the {@link Application.Domain.User} about the {@link Application.Domain.Vision}.
	 * 
	 * @return	The thoughts of the {@link Application.Domain.User} about the {@link Application.Domain.Vision}.
	 */
	public String getThoughts() {
		return thoughts;
	}

	/**
	 * Sets the thoughts of the {@link Application.Domain.User} about the {@link Application.Domain.Vision}.
	 * 
	 * @param thoughts	The thoughts of the {@link Application.Domain.User} about the {@link Application.Domain.Vision}.
	 */
	public void setThoughts(String thoughts) {
		this.thoughts = thoughts;
	}

	/**
	 * Returns the feeling guessed by the {@link Application.Domain.User} playing the Match.
	 * 
	 * @return	The feeling guessed by the {@link Application.Domain.User} playing the Match.
	 */
	public int getGuessedFeeling() {
		return guessedFeeling;
	}

	/**
	 * Sets the feeling guessed by the {@link Application.Domain.User} playing the Match.
	 * 
	 * @param guessedFeeling	The feeling guessed by the {@link Application.Domain.User} playing the Match.
	 */
	public void setGuessedFeeling(int guessedFeeling) {
		this.guessedFeeling = guessedFeeling;
	}

	public Date getPlayedDate() {
		return playedDate;
	}

	public void setPlayedDate(Date playedDate) {
		this.playedDate = playedDate;
	}
}