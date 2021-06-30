package Application.Utils;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.opencsv.CSVWriter;

import Application.Exception.DumpCreationException;

/**
 * Utility class used to create and download data related to a {@link Application.Domain.Scenario}.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

@Controller
public class DumpData {
	
	/**
	 * Private instance of {@link javax.sql.DataSource}.
	 */
	@Autowired
	private DataSource dataSource;
	
	/**
	 * List of the names of the cover images of a {@link Application.Domain.Scenario}.
	 */
	private ArrayList<String> scenario_pictures_name = new ArrayList<String>();
	
	/**
	 * List of the names of the images of the {@link Application.Domain.Vision}s of a given {@link Application.Domain.Scenario}.
	 */
	private ArrayList<String> vision_pictures_name = new ArrayList<String>();
	
	/**
	 * List of byte arrays for the cover images of a {@link Application.Domain.Scenario}.
	 */
	private ArrayList<byte[]> scenario_pictures = new ArrayList<byte[]>();
	
	/**
	 * List of byte arrays for the images of the {@link Application.Domain.Vision}s of a given {@link Application.Domain.Scenario}.
	 */
	private ArrayList<byte[]> vision_pictures = new ArrayList<byte[]>();
	
	
	/**
	 * Creates a data dump for a given {@link Application.Domain.Scenario}, including:
	 * <ul>
	 * <li>Details about the {@link Application.Domain.Scenario}</li>
	 * <li>Details about the {@link Application.Domain.Quiz}</li>
	 * <li>Details about the {@link Application.Domain.Question}s and {@link Application.Domain.Answer}s</li>
	 * <li>Details about the {@link Application.Domain.Vision}s</li>
	 * <li>Details about the {@link Application.Domain.Match}es played</li>
	 * <li>Details about the {@link Application.Domain.Feeling} expressed by {@link Application.Domain.User}s</li>
	 * </ul>
	 * 
	 * @param idScenario Integer specifying the identifier for a given {@link Application.Domain.Scenario}.
	 * @param httpServletResponse see {@link javax.servlet.http.HttpServletResponse}.
	 * @throws DumpCreationException DumpCreationException, exception in creating the data dump.
	 */
	@RequestMapping(value = "/get-data", method = {RequestMethod.GET, RequestMethod.POST})
	private void dumpData(@RequestParam("idScenario") int idScenario, HttpServletResponse httpServletResponse) throws DumpCreationException {
		
		scenario_pictures_name = new ArrayList<String>();
		vision_pictures_name = new ArrayList<String>();
		scenario_pictures = new ArrayList<byte[]>();
		vision_pictures = new ArrayList<byte[]>();
		
	    LocalDateTime now = LocalDateTime.now();
	    String fileName = "Scenario_" + idScenario + "_" + now.getYear() + "_" + now.getMonthValue() + "_" + now.getDayOfMonth();
		
	    httpServletResponse.setContentType("application/zip");
	    httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\""+fileName+".zip\"");
		
		ArrayList<String> fileNames = new ArrayList<>(Arrays.asList("Scenario and Narrative", "Keywords", "Query Words", "Users", "Matches", "Feelings", "Questions and Answers", "Visions"));
		
		try {
		    OutputStream servletOutputStream = httpServletResponse.getOutputStream(); // retrieve OutputStream from HttpServletResponse
		    ZipOutputStream zos = new ZipOutputStream(servletOutputStream); // create a ZipOutputStream from servletOutputStream

		    List<List<String[]>> csvFileContents = new ArrayList<List<String[]>>(); // get the list of csv contents. I am assuming the CSV content is generated programmatically

		    csvFileContents.add(getData(idScenario, "SELECT s.id_scenario, s.question_vision_first, s.description, s.title, s.lang, s.quiz, "
		    		+ "s.img_path, s.first_dim_name, s.first_dim_desc, s.second_dim_name, s.second_dim_desc, n.id_narrative, n.name, n.description"
		    		+ " FROM scenario AS s JOIN narrative AS n ON s.id_narrative = n.id_narrative WHERE id_scenario = ?"));
		    csvFileContents.add(getData(idScenario, "SELECT vk.id_vision, k.id_keyword, k.keyword FROM keyword AS k JOIN vision_keyword AS vk ON k.id_keyword = vk.id_keyword WHERE vk.id_vision IN (SELECT id_vision FROM vision WHERE scenario = ?)"));
		    csvFileContents.add(getData(idScenario, "SELECT qw.id_query_word, vqw.vision, qw.query_word FROM query_word AS qw JOIN vision_query_word AS vqw ON qw.id_query_word = vqw.query_word WHERE vqw.vision IN (SELECT id_vision FROM vision WHERE scenario = ?)"));
		    csvFileContents.add(getData(idScenario, "SELECT cookie, country, region, nationality, gender, age, education, interest FROM user WHERE cookie IN (SELECT cookie FROM vision WHERE scenario = ?)"));
		    csvFileContents.add(getData(idScenario, "SELECT * FROM matchmaking WHERE vision_challenger IN (SELECT id_vision FROM vision WHERE scenario = ?)"));
		    csvFileContents.add(getData(idScenario, "SELECT * FROM feeling WHERE scenario = ?"));
		    csvFileContents.add(getData(idScenario, "SELECT * FROM (quiz AS q JOIN question AS qu ON q.id_quiz = qu.id_quiz) JOIN answer AS a ON qu.id_question = a.id_question WHERE q.id_quiz IN (SELECT quiz FROM scenario WHERE id_scenario = ?)"));
		    csvFileContents.add(getData(idScenario, "SELECT * FROM vision WHERE scenario = ?"));
		    
		    int count = 0;
		    for (List<String[]> entries : csvFileContents) {
		        String filename = fileNames.get(count) + ".csv";
		        ZipEntry entry = new ZipEntry("data/" + filename); // create a zip entry and add it to ZipOutputStream
		        zos.putNextEntry(entry);
		        
		        CSVWriter writer = new CSVWriter(new OutputStreamWriter(zos));  // Directly write bytes to the output stream.
		        writer.writeAll(entries);  // write the contents
		        writer.flush(); // flush the writer.
		        zos.closeEntry(); // close the entry. Note : we are not closing the zos just yet as we need to add more files to our ZIP
		        count++;
		    }
		    
		    for(int i = 0; i < scenario_pictures.size(); i++) {
		    	byte[] image = scenario_pictures.get(i);
		    	String name = scenario_pictures_name.get(i);
		    	
		    	ZipEntry entry = new ZipEntry("pictures_scenario/" + name);
		        zos.putNextEntry(entry);
		        
		        byte[] decodedImg = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(image));
		        zos.write(decodedImg);
		    }
		    
		    for(int i = 0; i < vision_pictures.size(); i++) {
		    	byte[] image = vision_pictures.get(i);
		    	String name = vision_pictures_name.get(i);
		    	
		    	ZipEntry entry = new ZipEntry("pictures_vision/" + name);
		        zos.putNextEntry(entry);
		        
		        byte[] decodedImg = Base64.getDecoder().decode(Base64.getEncoder().encodeToString(image));
		        zos.write(decodedImg);
		    }
		    
		    zos.close(); // finally closing the ZipOutputStream to mark completion of ZIP file
		} catch (Exception e) {
			throw new DumpCreationException();
		}
		
	}
	
	/**
	 * Private utility function querying the database separately to get the relevant data of a {@link Application.Domain.Scenario}.
	 * 
	 * @param idScenario Integer specifying the identifier for a given {@link Application.Domain.Scenario}.
	 * @param query String containing the query to be executed.
	 * @return Results of the query formatted a List of Arrays of Strings.
	 * @throws SQLException see {@link java.sql.SQLException}.
	 */
	private List<String[]> getData(int idScenario, String query) throws SQLException {
		
		List<String[]> data = new ArrayList<String[]>();
		
		try {
			Connection conn = dataSource.getConnection();
			
			PreparedStatement scenarioStatement = conn.prepareStatement(query);
			scenarioStatement.setInt(1, idScenario);
			
			ResultSet resultSet = scenarioStatement.executeQuery();
			
			ResultSetMetaData metadata = resultSet.getMetaData();
			
			ArrayList<String> column_names = new ArrayList<String>();
			
			for(int i = 1; i <= metadata.getColumnCount(); i++) {
				String col_name = metadata.getColumnName(i);
				
				column_names.add(col_name);
			}
			
			String[] outputArray = new String[column_names.size()];
        	outputArray = column_names.toArray(outputArray);
        	
        	data.add(outputArray);
			
			while(resultSet.next()) {
				ArrayList<String> row = new ArrayList<String>();
				
				for(int i = 1; i <= metadata.getColumnCount(); i++) {
					String col_name = metadata.getColumnName(i);
					
					if(metadata.getColumnType(i) != -4) {
						if(resultSet.getString(col_name) == null || (resultSet.getString(col_name) != null && resultSet.getString(col_name).isEmpty())) {
							row.add("null");
						} else {
							row.add(resultSet.getString(col_name));
						}
					} else {
						if(metadata.getTableName(i).equals("scenario")) {
							row.add(resultSet.getString("id_scenario") + ".jpg");
							scenario_pictures.add(resultSet.getBytes(col_name));
							scenario_pictures_name.add(resultSet.getString("id_scenario") + ".jpg");
						} else if(metadata.getTableName(i).equals("vision")) {
							row.add(resultSet.getString("id_vision") + ".jpg");
							vision_pictures.add(resultSet.getBytes(col_name));
							vision_pictures_name.add(resultSet.getString("id_vision") + ".jpg");
						}
					}
				}
				
				String[] outputRow = new String[row.size()];
				outputRow = row.toArray(outputRow);
	        	
	        	data.add(outputRow);
			}

        	conn.close();
			
		} catch (Exception e) {
			throw new SQLException();
		}
		
		return data;
	}
}
