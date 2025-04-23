package coms.repository;

import coms.model.Hill;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;


/**
 * Repository for performing CRUD operations on Hill objects.
 */
@Repository
public interface HillRepository extends JpaRepository<coms.model.Hill, Long>{
    List<Hill> findByActive(boolean active);
    Optional<Hill> findByName(String name);
}
