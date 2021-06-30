package Application.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import Application.Domain.Achievement;
import Application.Domain.Scenario;
import Application.Domain.User;
import Application.Domain.Vision;
import Application.Application;

/**
 * Utility class used to find images that may have already been loaded. <br>
 * If the image is already present on disk the conversion from the database is skipped.
 * Otherwise the picture is pulled from the database and saved as a file. <br>
 * In both cases the path to the file is returned.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public class ImageFinder {
	
	/**
	 * Retrives the image of an {@link Application.Domain.Achievement}.
	 * 
	 * @param achievement {@link Application.Domain.Achievement} object for which to retrieve the image.
	 * @throws IOException see {@link java.io.IOException}.
	 */
	public static void findImageAchievement(Achievement achievement) throws IOException {
		
		Path path = Paths.get(Application.ACH_DIR);

		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}
		
		File image = new File(Application.ACH_DIR + achievement.getTitle() + ".png");
		boolean exists = image.exists();
		
		if(achievement.getImgAchievement() != null && !exists) {
			byte[] decodedImg = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(achievement.getImgAchievement()));
			Path destinationFile = Paths.get(Application.ACH_DIR, achievement.getTitle() + ".png");
			Files.write(destinationFile, decodedImg);
		}
		
		achievement.setImg("imagesAchievement/" + achievement.getTitle() + ".png");
	}
	
	/**
	 * Retrives the profile picture of a {@link Application.Domain.User}.
	 * 
	 * @param user {@link Application.Domain.User} object for which to retrieve the profile picture.
	 * @throws IOException see {@link java.io.IOException}.
	 */
	public static void findUserPicture(User user) throws IOException {
		
		Path path = Paths.get(Application.PROF_DIR);

		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}
		
		if(user.getProfilePicture() != null) {
			byte[] decodedImg = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(user.getProfilePicture()));
			Path destinationFile = Paths.get(Application.PROF_DIR, user.getCookie() + ".png");
			Files.write(destinationFile, decodedImg);
			
			user.setImg("profilePictures/" + user.getCookie() + ".png");
		} else {
			user.setImg("/appimg/Default-Profile-Picture.jpg");
		}
	}
	
	/**
	 * Retrives the photo collage of a {@link Application.Domain.Vision}.
	 * 
	 * @param vision {@link Application.Domain.Vision} object for which to retrieve the photo collage.
	 * @throws IOException see {@link java.io.IOException}.
	 */
	public static void findImage(Vision vision) throws IOException {
		
		Path path = Paths.get(Application.IMAGE_DIR);

		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}
		
		File image = new File(Application.IMAGE_DIR + vision.getIdVision() + ".png");
		boolean exists = image.exists();
		
		if(vision.getImgPath() != null && !exists) {
			byte[] decodedImg = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(vision.getImgPath()));
			Path destinationFile = Paths.get(Application.IMAGE_DIR, vision.getIdVision() + ".png");
			Files.write(destinationFile, decodedImg);
		}
		
		vision.setImg("images/" + vision.getIdVision() + ".png");
	}
	
	/**
	 * Retrives the cover image of a {@link Application.Domain.Scenario}.
	 * 
	 * @param scenario {@link Application.Domain.Scenario} object for which to retrieve the cover image.
	 * @throws IOException see {@link java.io.IOException}.
	 */
	public static void findImageScenario(Scenario scenario) throws IOException {
		
		Path path = Paths.get(Application.IMAGE_SCEN_DIR);

		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}
		
		File image = new File(Application.IMAGE_SCEN_DIR + scenario.getIdScenario() + ".png");
		boolean exists = image.exists();
		
		if(scenario.getImgPath() != null && !exists) {
			byte[] decodedImg = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(scenario.getImgPath()));
			Path destinationFile = Paths.get(Application.IMAGE_SCEN_DIR, scenario.getIdScenario() + ".png");
			Files.write(destinationFile, decodedImg);
		}
		
		scenario.setImg("imagesScenario/" + scenario.getIdScenario() + ".png");
	}
}
