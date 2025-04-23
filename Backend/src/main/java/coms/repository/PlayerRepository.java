package coms.repository;

import coms.model.Player;

import coms.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository for performing CRUD operations on Player objects.
 */
@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>{
    Player findByEmailToken(String emailToken);
    Optional<Player> findByName(String name);
}
