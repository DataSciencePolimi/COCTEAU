package Application.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import Application.Exception.TranslateException;

/**
 * Utility class used to translate terms for the photo collage of a new {@link Application.Domain.Vision}.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Component
public class TranslationUtil {
	
	/**
	 * Google Cloud Translation API key. Loaded from the application properties.
	 */
	@Value("${gcptranslate.key}")
	private String translateKey;
	
	/**
	 * Cloud Translate API endpoint.
	 */
	private String basePath = "https://www.googleapis.com/language/translate/v2";
	
	/**
	 * Key query-string parameter for Cloud Translate API.
	 */
	private String keyParam = "?key=";
	
	/**
	 * Query query-string parameter for Cloud Translate API (word or sentence to translate).
	 */
	private String textParam = "&q=";
	
	/**
	 * Source language query-string parameter for Cloud Translate API.
	 */
	private String sourceLang = "&source=";
	
	/**
	 * Target language query-string parameter for Cloud Translate API.
	 */
	private String targetLang = "&target=en";
	
	/**
	 * Model type query-string parameter for Cloud Translate API.
	 * Available models are:
	 * <ul>
	 * <li>base - NO LONGER SUPPORTED AFTER AUGUST 2021</li>
	 * <li>nmt</li>
	 * </ul>
	 */
	private String modelType = "&model=nmt";
	
	/**
	 * Returns the English translation of a string.
	 * 
	 * @param word String representing the word to translate.
	 * @return The English translation of a string.
	 * @throws TranslateException TranslateException, exception when trying to use Cloud Translation API.
	 */
	public String translate(String word) throws TranslateException {
		
		String translation;
		URL url;
		HttpURLConnection con;
		BufferedReader in;
		String inputLine;
		StringBuffer content = new StringBuffer();
		
		String language = detectLanguage(word);
		
		if (language.equals("en")) {
			return word;
		}
		
		try {
			String urlPath = basePath+keyParam+translateKey+textParam+(word.replaceAll(" ", "+"))+targetLang+sourceLang+language+modelType;
			
			url = new URL(urlPath);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();
		} catch (Exception e) {
			throw new TranslateException();
		}
		
		JSONObject jsonResponse = new JSONObject(content.toString());
		translation = jsonResponse.getJSONObject("data").getJSONArray("translations").getJSONObject(0).getString("translatedText");
		
		return translation;
	}
	
	/**
	 * Detect the language of a word.
	 * 
	 * @param word String with the text token to detect language for.
	 * @return The detected language code.
	 * @throws TranslateException TranslateException, exception when trying to use Cloud Translation API.
	 */
	private String detectLanguage(String word) throws TranslateException {
		
		URL url;
		HttpURLConnection con;
		BufferedReader in;
		String inputLine;
		StringBuffer content = new StringBuffer();
		
		try {
			word = URLEncoder.encode(word, "UTF-8");
			
			String languageCheckUrl = basePath+"/detect"+keyParam+translateKey+textParam+word;
			
			url = new URL(languageCheckUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			while((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();
		} catch(Exception e) {
			throw new TranslateException();
		}
		
		JSONObject jsonResponseDetect = new JSONObject(content.toString());
		String language = jsonResponseDetect.getJSONObject("data").getJSONArray("detections").getJSONArray(0).getJSONObject(0).getString("language");
		
		return language;
		
	}
	
}