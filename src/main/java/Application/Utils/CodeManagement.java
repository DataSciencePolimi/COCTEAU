package Application.Utils;

/**
 * Utility class used to check and update the access code of a {@link Application.Domain.User}.<br>
 * See {@link Application.Domain.User#accessCode}.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public class CodeManagement {
	
	/**
	 * Updates the {@link Application.Domain.User#accessCode} given the initial access code and the page that has been visited.
	 * 
	 * @param code String with the initial code.
	 * @param page String with the name of the page visited.
	 * @param newValue Char with the updated value for the given page.
	 * @return the String representation of the {@link Application.Domain.User#accessCode}.
	 */
	public static String updateCode(String code, String page, char newValue) {
		
		char[] codeArray = code.toCharArray();
		int pos = -1;
		
		switch(page) {
			case "Scenario":
				pos = 0;
				break;
			case "Quiz":
				pos = 1;
				break;
			case "Feelings":
				pos = 2;
				break;
			case "Feed":
				pos = 3;
				break;
			case "Vision":
				pos = 4;
				break;
			case "Match":
				pos = 5;
				break;
			case "Indepth":
				pos = 6;
				break;
			case "FeelingsII":
				pos = 7;
				break;
			case "Profile":
				pos = 8;
				break;
		}
		
		codeArray[pos] = newValue;
		
		return String.valueOf(codeArray);
	}
	
	/**
	 * Returns the {@link Application.Domain.User#accessCode} value for a given page.
	 * 
	 * @param code String with the {@link Application.Domain.User#accessCode}.
	 * @param page String with the name of the visited page.
	 * @return Integer, single value of {@link Application.Domain.User#accessCode} for a given page.
	 */
	public static int getValueAt(String code, String page) {
		
		int pos = -1;
		
		switch(page) {
			case "Scenario":
				pos = 0;
				break;
			case "Quiz":
				pos = 1;
				break;
			case "Feelings":
				pos = 2;
				break;
			case "Feed":
				pos = 3;
				break;
			case "Vision":
				pos = 4;
				break;
			case "Match":
				pos = 5;
				break;
			case "Indepth":
				pos = 6;
				break;
			case "FeelingsII":
				pos = 7;
				break;
			case "Profile":
				pos = 8;
				break;
		}
		
		return Integer.valueOf(code.toCharArray()[pos] + "");
	}

}
