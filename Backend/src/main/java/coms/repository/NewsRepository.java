package coms.repository;

import coms.model.Hill;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository for performing CRUD operations on News objects.
 */
@Repository
public interface NewsRepository extends JpaRepository<coms.model.News, Long>{
}
