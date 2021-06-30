package Application.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Application.Domain.VisionKeyword;

/**
 * An interface to manage the data access to the {@link Application.Domain.VisionKeyword} table.
 * 
 * @author	Corti Lorenzo
 * @author  Tocchetti Andrea
 * @since	1.0
 * 
 */

public interface VisionKeywordRepository extends JpaRepository<VisionKeyword, Integer>{

}
