package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

	@Query("select c from Configuration c where c.name = ?1")
	public Configuration findByName(String name);

}
