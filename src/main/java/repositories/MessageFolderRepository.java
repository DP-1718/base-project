package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.MessageFolder;

@Repository
public interface MessageFolderRepository extends JpaRepository<MessageFolder,Integer>{
	
	
	
	
	@Query("select m from MessageFolder m where m.actor.name = ?1")
	Collection<MessageFolder> findAllMessageFoldersByUser(String actor);

	@Query("select f from MessageFolder f where f.actor.userAccount.id = ?1 and f.name = ?2")
	MessageFolder findOneByUserAccountAndName(Integer userAccountId, String nameFolder);

	@Query("select m from MessageFolder m where m.actor.id = ?1")
	Collection<MessageFolder> findAllByUserId(Integer actorId);

}