package at.ebm.ArchivtoolBackend.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import at.ebm.ArchivtoolBackend.entity.DatabaseAttributes;
import at.ebm.ArchivtoolBackend.entity.AttributeId;

public interface AttributeRepository extends CrudRepository<DatabaseAttributes, AttributeId> {
	List<DatabaseAttributes> findByName(String name);
	List<DatabaseAttributes> findByOwner(String owner);
	List<DatabaseAttributes> findByVname(String vName);
	List<DatabaseAttributes> findByEbmprojectnumber(String ebmprojectnumber);
	
	List<DatabaseAttributes> findByNameContaining(String name);
	List<DatabaseAttributes> findByOwnerContaining(String owner);
	List<DatabaseAttributes> findByVnameContaining(String vName);
	List<DatabaseAttributes> findByEbmprojectnumberContaining(String ebmprojectnumber);

}
