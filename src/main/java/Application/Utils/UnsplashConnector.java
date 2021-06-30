package Application.Utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import Application.Exception.TranslateException;
import Application.Exception.UnsplashConnectionException;

import org.json.*;

/**
 * Utility class used to query and return results from Unsplash for creating new {@link Application.Domain.Vision}s.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Component
public class UnsplashConnector {

	/**
	 * Unsplash API key. Loaded from the application properties.
	 */
	@Value("${unsplash.accesskey}")
	private String unsplashAPIKey;
	
	/**
	 * Unsplash API search endpoint.
	 */
	private String baseSearchPath = "https://api.unsplash.com/search/photos?query=";
	
	/**
	 * API version query-string parameter for Unsplash API.<br>
	 * From Unsplash documentation: "... All requests receive the v1 version of the API. We encourage you to specifically request this ...".
	 */
	private String acceptVersion = "&Accept-Version=v1";
	
	/**
	 * Page number query-string parameter for Unsplash API.
	 */
	private String pageSelect = "&page=";
	
	/**
	 * Unsplash API download endpoint.
	 */
	private String baseDownloadEnpoint = "https://api.unsplash.com/photos/";
	
	/**
	 * Query-string parameters for photo attribution. Required by Unsplash.
	 */
	private String attribution = "?utm_source=COCTEAU&utm_medium=referral";
	
	/**
	 * Private instance of {@link Application.Utils.TranslationUtil}.
	 */
	private TranslationUtil translationUtil;
	
	/**
	 * Constructor for the {@link Application.Utils.TranslationUtil} class.
	 * @param translationUtil instance of {@link Application.Utils.TranslationUtil}.
	 */
	@Autowired
	public UnsplashConnector(TranslationUtil translationUtil) {
		this.translationUtil = translationUtil;
	}
	
	/**
	 * Queries Unsplash APIs and returns the results.
	 * 
	 * @param keyword String, search term
	 * @param pageNum Integer, page number
	 * @return JSON formatted string with the response from Unsplash APIs
	 * @throws TranslateException TranslateException, exception when trying to use Cloud Translation API.
	 * @throws UnsplashConnectionException UnsplashConnectionException, exception when trying to use Unsplash API.
	 */
	public String getSearchResults(String keyword, int pageNum) throws TranslateException, UnsplashConnectionException {
		/* Search query example:
		 * https://api.unsplash.com/search/photos?query=KEYWORD&Accept-Version=v1&page=1&client_id=API_KEY
		 * */
		
		// Add keyword translation
		String urlPath = baseSearchPath+translationUtil.translate(keyword).replace(" ", "%20")+acceptVersion+pageSelect+pageNum+"&client_id="+unsplashAPIKey;
		URL url;
		HttpURLConnection con;
		BufferedReader in;
		String inputLine;
		StringBuffer content = new StringBuffer();
		
		try {
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
			throw new UnsplashConnectionException();
		}
		
		JSONObject jsonObj = new JSONObject(content.toString());
		JSONArray results = new JSONArray(jsonObj.getJSONArray("results").toString());
		JSONObject item = new JSONObject();
		
		JSONArray cleanResults = new JSONArray();
		JSONObject cleanItem = new JSONObject();
		JSONObject cleanUserInfo = new JSONObject();
		JSONObject cleanUrls = new JSONObject();
		
		for (int i=0; i<results.length(); i++) {
			item = results.getJSONObject(i);
			cleanItem.put("picID", item.getString("id"));
			cleanUrls.put("small", item.getJSONObject("urls").getString("small"));
			cleanUrls.put("regular", item.getJSONObject("urls").getString("regular"));
			cleanItem.put("urls", cleanUrls);
			cleanUserInfo.put("name", item.getJSONObject("user").getString("name"));
			cleanUserInfo.put("profileURL", item.getJSONObject("user").getJSONObject("links").getString("html")+attribution);
			cleanItem.put("userInfo", cleanUserInfo);
			cleanResults.put(cleanItem);
			cleanUserInfo = new JSONObject();
			cleanUrls = new JSONObject();
			cleanItem = new JSONObject();
		}
		
		int remaining = jsonObj.getInt("total") - results.length() - 10 * (pageNum - 1);
		JSONObject remainingObj = new JSONObject();
		remainingObj.put("remaining", remaining);
		
		cleanResults.put(remainingObj);
		
		return cleanResults.toString();
	}
	
	/**
	 * Downloads from the dedicated Unsplash endpoint the specified images.
	 * 
	 * @param ids list of the picture identifiers from Unsplash
	 * @return list of {@link java.awt.image.BufferedImage}s
	 * @throws UnsplashConnectionException UnsplashConnectionException, exception when trying to use Unsplash API.
	 */
	public List<BufferedImage> downloadPictures(List<String> ids) throws UnsplashConnectionException {
		List<BufferedImage> pictures = new ArrayList<BufferedImage>();
		
		List<String> downloadPaths = new ArrayList<String>();
		for (int i=0; i<ids.size(); i++) {
			downloadPaths.add(baseDownloadEnpoint + ids.get(i) + "/download?client_id=" + unsplashAPIKey);
		}
		
		JSONObject jsonObj = new JSONObject();
		
		String imageURL;
		
		for (int i=0; i<downloadPaths.size(); i++) {
			try {
				URL url = new URL(downloadPaths.get(i));
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer content = new StringBuffer();
				while((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}
				jsonObj = new JSONObject(content.toString());
				in.close();
				con.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			imageURL = jsonObj.getString("url");
			try {
				pictures.add(ImageIO.read(new URL(imageURL+"&w=1080")));
			} catch (Exception e) {
				throw new UnsplashConnectionException();
			}
		}
		
		return pictures;
	}
}