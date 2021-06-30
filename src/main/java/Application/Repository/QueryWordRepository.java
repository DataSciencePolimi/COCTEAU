package Application.Repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import Application.Domain.QueryWord;

/**
 * An interface to manage the data access to the {@link Application.Domain.QueryWord} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface QueryWordRepository extends JpaRepository<QueryWord, Integer>{

	/**
	 * Returns the texts of all the {@link Application.Domain.QueryWord}(s).
	 * 
	 * @return	The texts of all the {@link Application.Domain.QueryWord}(s).
	 */
	@Query(value = "SELECT query_word FROM query_word", nativeQuery = true)
	ArrayList<String> findAllString();
	
	/**
	 * Returns the {@link Application.Domain.QueryWord} with the provided text.
	 * 
	 * @param query_word	The text of the {@link Application.Domain.QueryWord}.
	 * @return	The {@link Application.Domain.QueryWord} with the provided text.
	 */
	@Query(value = "SELECT * FROM query_word as qw WHERE qw.query_word = :query_word", nativeQuery = true)
	QueryWord findByName(@Param("query_word") String query_word);
}
