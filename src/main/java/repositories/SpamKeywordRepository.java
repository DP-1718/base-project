
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.SpamKeyword;

@Repository
public interface SpamKeywordRepository extends JpaRepository<SpamKeyword, Integer> {

	@Query("select s from SpamKeyword s " + "where (?1 like concat('%', s.word, '%')) " + "or (?2 like concat('%', s.word, '%'))")
	Collection<SpamKeyword> findSpamKeywords(String subject, String body);

}
