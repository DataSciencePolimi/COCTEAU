package Application.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Application.Domain.VisionQueryWord;

/**
 * An interface to manage the data access to the {@link Application.Domain.VisionQueryWord} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface VisionQueryWordRepository extends JpaRepository<VisionQueryWord, Integer>{

}
