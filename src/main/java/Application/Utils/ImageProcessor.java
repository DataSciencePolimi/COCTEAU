package Application.Utils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import Application.Exception.PictureCreationException;
import Application.Exception.UnsplashConnectionException;

/**
 * Utility class used to create the photo collage of a given {@link Application.Domain.Vision}.<br>
 * Information about the size are pulled from the application's property file.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Component
public class ImageProcessor {
	
	/**
	 * Width of the photo collage.
	 */
	@Value("${postWidth}")
	private int postWidth;
	
	/**
	 * Height of the photo collage.
	 */
	@Value("${postHeight}")
	private int postHeight;
	
	/**
	 * Private instance of {@link Application.Utils.UnsplashConnector}.
	 */
	private UnsplashConnector unsplashConnector;
	
	/**
	 * Constructor for the {@link Application.Utils.ImageProcessor} class.
	 * 
	 * @param unsplashConnector instance of {@link Application.Utils.UnsplashConnector}.
	 */
	@Autowired
	public ImageProcessor(UnsplashConnector unsplashConnector) {
		this.unsplashConnector = unsplashConnector;
	}
	
	/**
	 * Utility functions that checks that enough picture's URLs are present depending on the layout.
	 * 
	 * @param layout Integer representing the layout (1 and 2 are vertical and horizontal respectively, 3 is squared).
	 * @param picturesListStr String with the URLs of the pictures, the slot they belong in, the start coordinates and the magnifying factor. <br>
	 * Data is ";" separated at the higher level. Other information is separated by ":".
	 * @return boolean depending whether or not there are enough picture's URLs depending on the layout.
	 */
	public boolean checkPictureListStr(int layout, String picturesListStr) {		
		if (layout == 1 || layout == 2) {
			if ( picturesListStr.split(Pattern.quote(";")).length != 3 ) {
				return false;
			}
		} else if (layout == 3) {
			if ( picturesListStr.split(Pattern.quote(";")).length != 4 ) {
				return false;
			}
		} else {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Creates the photo collage for a new {@link Application.Domain.Vision}.
	 * 
	 * @param layout Integer representing the layout (1 and 2 are vertical and horizontal respectively, 3 is squared).
	 * @param picturesListStr String with the URLs of the pictures, the slot they belong in, the start coordinates and the magnifying factor. <br>
	 * Data is ";" separated at the higher level. Other information is separated by ":".
	 * @return byte[] with the final photo collage to be saved in the database.
	 * @throws PictureCreationException PictureCreationException, exception in creating photo collage for the new {@link Application.Domain.Vision}.
	 * @throws UnsplashConnectionException UnsplashConnectionException, exception in connecting to Unsplash's APIs.
	 */
	public byte[] createPhotoCollage(int layout, String picturesListStr) throws PictureCreationException, UnsplashConnectionException {
		/*
		 * picturesListStr: string in form of position:img_id:starting_pos_x:starting_pos_y;position2...
		 * */
		String[] imagesStr = picturesListStr.split(Pattern.quote(";"));
		
		// Positions in the collage
		List<String> imagesPos = new ArrayList<String>();
		
		// Links to download endpoints
		List<String> imagesPaths = new ArrayList<String>();
		List<Integer> xPositions = new ArrayList<Integer>();
		List<Integer> yPositions = new ArrayList<Integer>();
		List<Float> captureSizes = new ArrayList<Float>();
 		
		List<BufferedImage> imagesToMerge = new ArrayList<BufferedImage>();
		
		// Variables used to merge images
		float scale = (float) 1.0;
		
		int w = 0;
		int h = 0;
		int slotW = 0;
		int slotH = 0;
		
		int startXDest = 0;
		int startYDest = 0;
		int startXSource = 0;
		int startYSource = 0;
		
		int endXDest = 0;
		int endYDest = 0;
		int endXSource = 0;
		int endYSource = 0;
		
		BufferedImage collage;
		Graphics g;
			
		for (String elem : imagesStr) {
			String[] slotInfo = elem.split(Pattern.quote(":")); // 6 elements
			imagesPos.add(slotInfo[0]);
			imagesPaths.add(slotInfo[1].toString());
			
			xPositions.add((int) Float.parseFloat(slotInfo[2]));
			yPositions.add((int) Float.parseFloat(slotInfo[3]));
			captureSizes.add((float) Float.parseFloat(slotInfo[4]));
		}
		
		imagesToMerge = unsplashConnector.downloadPictures(imagesPaths);
		
		w = postWidth;
		h = postHeight;
		
		collage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		g = collage.getGraphics();
		
		switch(layout){
			case 1:
				slotW = w / 3;
				slotH = h;
				break;
			case 2:
				slotW = w;
				slotH = h / 3;
				break;
			default:
				slotW = w / 2;
				slotH = h / 2;
		}
	
		endXDest = slotW;
		endYDest = slotH;
	
		for (int i=0; i<imagesToMerge.size(); i++){
			startXSource = xPositions.get(i);
			startYSource = yPositions.get(i);
			
			endXSource = Math.round(startXSource + slotW * captureSizes.get(i));
			endYSource = Math.round(startYSource + slotH * captureSizes.get(i));
			
			// Check boundaries and scale accordingly
			if (!(imagesToMerge.get(i).getHeight() < endYSource || imagesToMerge.get(i).getWidth() < endXSource)) {
				
				g.drawImage(imagesToMerge.get(i), startXDest, startYDest, endXDest, endYDest, 
						startXSource, startYSource, endXSource, endYSource, null);
			} else {
				if ((imagesToMerge.get(i).getHeight() < endYSource) && (imagesToMerge.get(i).getWidth() < endXSource)) {
					float scaleH = (float)(endYSource)/imagesToMerge.get(i).getHeight();
					float scaleW = (float)(endXSource)/imagesToMerge.get(i).getWidth();
					scale = Math.max(scaleH, scaleW);
					
				} else if (imagesToMerge.get(i).getHeight() < endYSource){
					scale = (float)(endYSource)/imagesToMerge.get(i).getHeight();
					
					startXSource = (int) (startXSource * scale);
					endXSource = Math.round(startXSource + slotW * captureSizes.get(i));
					
				}else if (imagesToMerge.get(i).getWidth() < endXSource) {
					scale = (float)(endXSource)/imagesToMerge.get(i).getWidth();
					
					startYSource = (int) (startYSource * scale);
					endYSource = Math.round(startYSource + slotH * captureSizes.get(i));
				}
				
				BufferedImage tempImage = scaleImage(imagesToMerge.get(i), scale);
				g.drawImage(tempImage, startXDest, startYDest, endXDest, endYDest, 
						startXSource, startYSource, endXSource, endYSource, null);
			}
	
			// Update image endpoints based on the layout
			switch(layout){
			case 1:
				// 1st Layout
				startXDest += slotW;
				endXDest += slotW;
				break;
			case 2:
				// 2nd Layout
				startYDest += slotH;
				endYDest += slotH;
				break;
			default:
				// 3rd Layout
				if (i%2 != 0) {
					// Move down to new row
					startXDest = 0;
					startYDest += slotH;
	
					endXDest = slotW;
					endYDest += slotH;					
				} else {
					// Move right
					startXDest += slotW;
					endXDest += slotW;					
				}
			}
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] imageInByte = null;
		
		try {
			ImageIO.write(collage, "PNG", baos);
			g.dispose();
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
		} catch(Exception e) {
			throw new PictureCreationException();
		}
		
		return imageInByte;
		
	}
	
	/**
	 * Private utility function to scale the original image from Unsplash to fit <strong> at least </strong> either the
	 * height or the width of the slot in the photo collage.
	 * 
	 * @param before original {@link java.awt.image.BufferedImage}.
	 * @param scale float representing the scaling factor for the image.
	 * @return {@link java.awt.image.BufferedImage} with the rescaled image.
	 */
	private BufferedImage scaleImage(BufferedImage before, float scale) {
		int w = before.getWidth();
		int h = before.getHeight();
		
		int newW = (int) (w * scale);
		int newH = (int) (h * scale);
		
		BufferedImage after = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) after.getGraphics();
		g.drawImage(before, 0, 0, newW, newH, 0, 0, w, h, null);
		g.dispose();
		return after;
	}

}
