package coms.repository;

import coms.model.Stats;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Repository for performing CRUD operations on Stats objects.
 */
public interface StatsRepository extends JpaRepository<Stats, Long> {
}
