package coms.controller;

import coms.model.Stats;
import coms.repository.StatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @Author Gabriel Kiveu
 * Controller class for managing Stats CRUDL operations
 */
@RestController
public class StatsController {

    @Autowired
    private StatsRepository SR;

    // Responses messages
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
    private String created = "{\"message\":\"Stats Created\"}";
    private String deleted = "{\"message\":\"Stats Cleared\"}";


    /**
     * THIS IS THE LIST OPERATION
     * // gets all the users in the list and returns it in JSON format
     * // This controller takes no input.
     * // Springboot automatically converts the list to JSON format
     * // in this case because of @ResponseBody
     * // Note: To LIST, we use the GET method
     * @return the list of all the stats create for each player
     */
    @GetMapping(path = "/stats/get")
    public List<Stats> getAllStats() {
        return SR.findAll();
    }



    /**
     * This is the READ Operation
     * Gets player's stats based on there id
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Optional<Stats> getStats(@PathVariable Long id) {
        return SR.findById(id);
    }

    /**
     * Creates news stats for each new player
     * @param s the Stats to call the getters to create new stats
     * @return Response meesage indicating if it was successful or failure
     */
    @PostMapping(path = "/stats/apply")
    public String createStats(@RequestBody Stats s) {
        if (s == null) { // if the stats are empty it will return a message failure
            return failure;
        } else { // Otherwise, it will get you the stats
            Stats newStats = new Stats(s.getCoins(),s.getTeam(), s.getLevel(), s.getXp());
            SR.save(newStats);
            return created;
        }
    }


    /**
     * THIS IS THE UPDATE OPERATION
     * Updates an existing Stats record by ID.
     * @param id the ID of the Stats to update
     * @param s the updated Stats data
     * @return Updated Stats record or 404 if not found
     */
    @PutMapping("/change/{id}")
    public ResponseEntity<Stats> updateStats(@PathVariable Long id, @RequestBody Stats s) {
        Optional<Stats> optionalStats = SR.findById(id);

        if (!optionalStats.isPresent()) { //if the stats are not present it will return not found
            return ResponseEntity.notFound().build();
        }

        Stats statsToUpdate = optionalStats.get();
        statsToUpdate.setCoins(s.getCoins());
        statsToUpdate.setLevel(s.getLevel());
        statsToUpdate.setXp(s.getXp());
        SR.save(statsToUpdate);

        return ResponseEntity.ok(statsToUpdate);
    }



    /**
     *  // GAIN XP Operation: Adds XP to a player's xp total and handles leveling up
     * @param id
     * @param xpGained
     * @return
     */
    @PutMapping("/stats/{id}/gain-xp")
    public ResponseEntity<?> gainXp(@PathVariable Long id, @RequestParam(required = false) Integer xpGained) {
        if(xpGained == null || xpGained < 0){
            return ResponseEntity.badRequest().body("{\"error\":\"xpGained must be a positive integer\"}");
        }

        Optional<Stats> optionalStats = SR.findById(id);
        if (!optionalStats.isPresent()) {
            return ResponseEntity.notFound().build();
        }


        Stats stats = optionalStats.get();
        stats.gainXp(xpGained); // Handles XP addition and leveling up (method in Stats entity)
        SR.save(stats);


        return ResponseEntity.ok(stats);
    }



    /**
     * // OPTIONAL: Manually Level Up a Player
     * @param id
     * @return
     */
    @PutMapping("/stats/{id}/level-up")
    public ResponseEntity<Stats> levelUp(@PathVariable Long id) {
        Optional<Stats> optionalStats = SR.findById(id);


        if (!optionalStats.isPresent()) {
            return ResponseEntity.notFound().build();
        }


        Stats stats = optionalStats.get();
        stats.levelUp(); // Directly level up the player (method in Stats entity)
        SR.save(stats);


        return ResponseEntity.ok(stats);
    }

    /**
     * Updates the coin count for a specific player's stats.
     *
     * @param id the ID of the stats record to update
     * @param coins the new coin count to set; must be a non-negative integer
     * @return a {@link ResponseEntity} containing:
     *         <ul>
     *           <li>HTTP 200 OK with the updated stats if the operation is successful</li>
     *           <li>HTTP 400 Bad Request if the coins value is null or negative</li>
     *           <li>HTTP 404 Not Found if no stats record exists for the given ID</li>
     *         </ul>
     */
    @PutMapping("/stats/{id}/update-coins")
    public ResponseEntity<?> updateCoins(@PathVariable Long id, @RequestParam(required = true) Integer coins) {
        if (coins == null || coins < 0) {
            return ResponseEntity.badRequest().body("Coins must be a non-negative integer.");
        }

        Optional<Stats> optionalStats = SR.findById(id);
        if (!optionalStats.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stats not found for ID: " + id);
        }

        Stats stats = optionalStats.get();
        stats.setCoins(coins);
        SR.save(stats);

        return ResponseEntity.ok(stats);
    }




    /**
     *DELETE OPERATION
     * This clears the stats off a player by there id
     * @param id
     * @return
     */
    @DeleteMapping(path = "/clear/{id}")
    public String deleteStats(@PathVariable Long id) {
        SR.deleteById(id);
        return deleted;
    }
}
