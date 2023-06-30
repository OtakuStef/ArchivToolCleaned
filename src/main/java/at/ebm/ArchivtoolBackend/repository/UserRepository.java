package at.ebm.ArchivtoolBackend.repository;

import org.springframework.data.repository.CrudRepository;

import at.ebm.ArchivtoolBackend.entity.User;

public interface UserRepository extends CrudRepository<User, String> {
	User findByUsername(String username);
}
