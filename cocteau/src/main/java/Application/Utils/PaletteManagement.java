package Application.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;

import Application.Domain.Vision;

/**
 * Utility class used to manage the colors assigned to each emotion and to manage the combination of {@link Application.Domain.Feeling}.<br>
 * This is reflected in a {@link Application.Domain.User} profile page.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public class PaletteManagement {

	/**
	 * List with the colors assigned to the different emotions that {@link Application.Domain.User}s express via the {@link Application.Domain.Feeling} page.<br>
	 * Each entry is a triple corresponding to the low, medium and high intensity of the emotion expressed.
	 */
	private static final ArrayList<String> colorsFeelingMapping = new ArrayList<String>(Arrays.asList(
			"#FCD88E", "#FDC86A", "#FCB944",
			"#94E5B8", "#70DEA1", "#4CD689",
			"#7BB897", "#4FA073", "#248850",
			"#67CED5", "#35BCC7", "#01ADB9",
			"#A692CF", "#896DC1", "#6C49B1",
			"#EC7ABA", "#E54EA5", "#E1228E",
			"#F59889", "#F94848", "#F91919",
			"#F59889", "#F37461", "#F0533A"
		));
	
	/**
	 * Returns the colors related to the most expressed emotion of a {@link Application.Domain.User}.<br>
	 * 
	 * @param userVisions list of the {@link Application.Domain.Vision}(s) shared by a {@link Application.Domain.User} on the platform.
	 * @return JSON formatted string with the colors to render in a {@link Application.Domain.User} profile page.
	 */
	public static String getProfileColors(Collection<Vision> userVisions) {
		
		ArrayList<Integer> feelings = new ArrayList<Integer>();
		
		for(Vision v : userVisions) {
			feelings.add(v.getFeelings());
		}
		
		String color = maxOccurrences(feelings);
		
		JSONObject profileColors = new JSONObject();
		
		profileColors.put("color", color);
		
		return profileColors.toString();
	}
	
	/**
	 * Returns the color code for the most frequent feeling in the batch.<br>
	 * See {@link Application.Utils.PaletteManagement#colorsFeelingMapping} for the color codes.
	 * 
	 * @param feelings batch of feelings among which to find the most frequent one.
	 * @return the color code for the most frequent feeling.
	 */
	private static String maxOccurrences(ArrayList<Integer> feelings) {
		
		int[] frequency = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; 
		
		for(int feelingNumber : feelings) {
			if(feelingNumber != 0) {
				frequency[feelingNumber - 1] = frequency[feelingNumber - 1] + 1;
			}
		}
		
		List<Integer> freqList = Arrays.asList(ArrayUtils.toObject(frequency));
		
		int maxFreq = Collections.max(freqList);
		int colorPos = 0;
		
		if(maxFreq == 0) {
			return "#FFFFFF";
		}
		
		for(int freq : frequency) {
			if(freq == maxFreq) {
				return colorsFeelingMapping.get(colorPos);
			}
			colorPos++;
		}
				
		return "#FFFFFF";
	}
	
}
