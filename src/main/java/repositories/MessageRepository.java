package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Message;
import domain.MessageFolder;

@Repository
public interface MessageRepository extends JpaRepository<Message,Integer>{


	@Query("select m from MessageFolder f join f.messages m where f = ?1")
	Collection<Message> findAllByFolder(MessageFolder messageFolder);


}