package socialweb.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import socialweb.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long>{

}

