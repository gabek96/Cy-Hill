package coms.repository;

import coms.model.PlayerContribution;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import coms.model.Hill;




/**
 * Repository for performing CRUD operations on PlayerContribution objects.
 */
@Repository
public interface PlayerContributionRepository extends JpaRepository<PlayerContribution, Long>{
    List<PlayerContribution> findByHill(Hill hill);
    void deleteAllByHill(Hill h);
}
