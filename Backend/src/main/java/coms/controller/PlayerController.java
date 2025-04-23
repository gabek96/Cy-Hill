package coms.controller;

import coms.model.*;
import coms.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import coms.websocket.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

import coms.repository.*;
import coms.services.*;

/**
 * This class is the controller for the player class
 * @Author Matthew Etnyre
 * @Author Gabe Kiveu
 */
@RestController
public class PlayerController {
    @Autowired
    private HillWS hillWS;

    @Autowired
    private StatsRepository SR;

    @Autowired
    private PlayerRepository PR;

    @Autowired
    private HillRepository HR;

    @Autowired
    private PlayerContributionRepository PCR;

    @Autowired
    private HillScreenService HSS;

    @Autowired
    private NewsServer NEWS;

    @Autowired
    private EmailService ES;
    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";
    private String created = "{\"message\":\"Player Created\"}";
    private String deleted = "{\"message\":\"Profile Deleted\"}";

    /**
     * This is a GetMapping which returns a JSON list of every player in the data base,
     * responds to /players endpoint
     * @Return a list of every player in the data base.
     */
    @GetMapping(path = "/players")
    public List<Player> getAllPersons() {
        return PR.findAll();
    }

    /**
     * This is a GetMapping which returns a JSON of a player with the given ID.
     * responds to /player/{id} endpoint
     * @Param id The id of the player we want to grab
     * @Return a JSON of the person with given id.
     */
    @GetMapping("/player/{id}")
    public Optional<Player> getUser(@PathVariable Long id) {
        return PR.findById(id);
    }

    @GetMapping("/login/{username}/{password}")
    public ResponseEntity<String> userLogin(@PathVariable String username, @PathVariable String password){
        Optional<Player> p = PR.findByName(username);
        if(p.isEmpty()){
            return ResponseEntity.badRequest().body("{\"id\":\"-1\"}"); //player doesnt exist so we exit and return -1
        }
        Player player = p.get();
        String realPassword = player.getPassword();
        if(Objects.equals(realPassword, password)){
            return ResponseEntity.ok().body("{\"id\":\"" + player.getId() + "\"}");//return the players id;
        }
        return ResponseEntity.badRequest().body("{\"id\":\"0\"}");

    }

    /**
     * this is the first part of the email confirmation process, this function will send a token to the user's
     * email for resetting their password
     * @param username username of person who forgot password
     * @param email emial of person who forgot password
     * @return returns a success or failure message
     */
    @PostMapping(path = "/{username}/forgotpassword/{email}")
    public ResponseEntity<String> sendResetPasswordToken(@PathVariable String username, @PathVariable String email){
        //check to see if the person exits in the db
        Optional<Player> p = PR.findByName(username);
        if(p.isEmpty()){
            return ResponseEntity.badRequest().body("Player doesn't exist");
        }
        //if player exists check that their email matches
        Player player = p.get();
        String playerEmail = player.getEmail();
        if(!Objects.equals(playerEmail, email)){
            return ResponseEntity.badRequest().body("Wrong email for player");
        }
        //at this point the email matches
        int token = ES.sendResetCode(username, email);
        //save the code in the db
        player.setForgotPasswordToekn(token);
        PR.save(player);

        return ResponseEntity.ok().body("Email containing reset code has been sent!");
    }

    @PostMapping(path = "/{username}/resetpassword/{token}/{newPassword}")
    public ResponseEntity<String > resetPassword(@PathVariable String username, @PathVariable int token, @PathVariable String newPassword){
        //check to see if the person exits in the db
        Optional<Player> p = PR.findByName(username);
        if(p.isEmpty()){
            return ResponseEntity.badRequest().body("Player doesn't exist");
        }
        //if player exists check that their token matches
        Player player = p.get();
        int playerToken = player.getForgotPasswordToekn();
        if(!Objects.equals(playerToken, token)){
            return ResponseEntity.badRequest().body("Wrong token");
        }
        //at this point the token matches, update password
        player.setPassword(newPassword);
        PR.save(player);
        return ResponseEntity.ok().body("Players password was updated");
    }

    /**
     * Helper method
     * Updates the player's XP based on the donation amount and checks for level-up.
     * @param stats the player's stats
     * @param donation the donation amount
     */
    private void updateXP(Stats stats, int donation) {
        int xpGained = calculateXPGained(donation);
        stats.setXp(stats.getXp() + xpGained);
        System.out.println("Player gained " + xpGained + " XP. Total XP: " + stats.getXp());

        // Check if the player's XP exceeds the threshold for the next level
        while (stats.getXp() >= stats.xpRequiredForNextLevel()) {
            stats.levelUp(); // Level up the player
            System.out.println("Player leveled up! New Level: " + stats.getLevel() + ". Remaining XP: " + stats.getXp());
        }
    }

    /**
     * Calculates the XP gained based on the donation amount.
     * @param donation the donation amount
     * @return XP gained
     */
    private int calculateXPGained(int donation) {
        return donation * 10; // Example: 10 XP per coin donated
    }


    /**
     * Handles a POST request for when a player wants to donate to a hill.
     * Responds to /{playerId}/donate/{hillId}/{donation} endpoint.
     *
     * @param playerId the ID of the donating player
     * @param hillId the ID of the hill which is going to be donated to
     * @param donation the amount being donated
     * @return a response entity indicating success or failure with XP gained and remaining coins
     */
    @PostMapping(path = "/{playerId}/donate/{hillId}/{donation}")
    @Transactional
    public ResponseEntity<String> donateToHill(@PathVariable Long playerId, @PathVariable Long hillId, @PathVariable int donation) {
        Optional<Player> playerOpt = PR.findById(playerId); // grab the player
        Optional<Hill> hillOpt = HR.findById(hillId); // grab the hill

        // Ensure the player and hill are not null
        if (playerOpt.isEmpty() || hillOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("{\"message\":\"Player or Hill not found\"}");
        }

        Hill hill = hillOpt.get();
        Player player = playerOpt.get();
        Stats stats = player.getStats();

        // Ensure the hill is active
        if (!hill.getActive()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\":\"Hill is not active, can't donate\"}");
        }

        // Ensure the player has stats
        if (stats == null) {
            logger.warn("Stats not found for player with ID: {}", playerId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Stats not found for the player\"}");
        }

        int playerCoins = stats.getCoins(); // grab the player's stats
        int coinsToFull = hill.getGoal() - hill.getTotalCoins();

        System.out.println("CoinsToFull = " + coinsToFull);

        int xpGained = 0; // Initialize XP gained variable

        if (playerCoins <= 0) { // if the player has no coins, donation fails
            logger.warn("Player has no coins: {}", playerId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("{\"message\":\"Player has no coins\"}");
        }
        // Determine donation logic and update XP
        if (playerCoins > donation && coinsToFull > donation) {
            System.out.println("Normal Donation");
            donateNormal(player, stats, hill, donation);
            xpGained = calculateXPGained(donation);
            updateXP(stats, donation);
        } else if (playerCoins > donation) {
            System.out.println("Fill Hill Donation");
            donateToFillHill(player, stats, hill, coinsToFull);
            xpGained = calculateXPGained(coinsToFull);
            updateXP(stats, coinsToFull);
        } else if (playerCoins < donation && coinsToFull > donation) {
            donateAllCoins(player, stats, hill);
            System.out.println("Donating all of player's coins");
            xpGained = calculateXPGained(playerCoins);
            updateXP(stats, playerCoins);
        } else if (playerCoins < donation && coinsToFull < donation) {
            donateToFillHill(player, stats, hill, coinsToFull);
            System.out.println("Fill Hill Donation");
            xpGained = calculateXPGained(coinsToFull);
            updateXP(stats, coinsToFull);
        } else {
            logger.error("Unexpected case: PlayerCoins={}, donation={}, CoinsToFull={}", playerCoins, donation, coinsToFull);
        }


        // Update XP and handle level-up logic using Stats model
        stats.gainXp(xpGained);

        // Send out new hill information
        HSS.sendHillData();

        //send out hill location data
        HSS.sendHillLocationData();

        // Return response including remaining coins and XP gained
        return ResponseEntity.ok("{\"Remaining coins\": " + stats.getCoins() + ", \"Xp Gained\": " + xpGained + "}");

    }


    /**
     * This is a Put request to update the player object with a new player
     * responds to /update/{id} endpoint
     * @param id the id of the player we want to update
     * @param p the player we want to use to update
     * @return returns a response of success or failure
     */
    @PutMapping("/player/update/{id}")
    public ResponseEntity<Player> updateProfile(@PathVariable Long id, @RequestBody Player p) {
        Optional<Player> optionalStats = PR.findById(id); // Assuming SR is your repository/service

        if (!optionalStats.isPresent()) {
            return ResponseEntity.notFound().build(); // Return 404 if the Stats object is not found
        }

        Player updatePlayer = optionalStats.get();
        updatePlayer.setName(p.getName());
        updatePlayer.setBirthday(p.getBirthday());
        updatePlayer.setEmail(p.getEmail());
        updatePlayer.setPassword(p.getPassword());
        updatePlayer.setIsAdmin(p.getIsAdmin());


        // Save the updated object back to the repository/service
        PR.save(updatePlayer); // Ensure you have a save method to persist changes

        return ResponseEntity.ok(updatePlayer); // Return the updated object with 200 status
    }

    /**
     * This is a Delete request to delete the player by id
     * responds to /delete/{id} endpoint
     * @param id the id of the player we want to delete
     * @return returns a success or failure message
     */
    @DeleteMapping(path = "/delete/{id}")
    String deleteUser(@PathVariable Long id) {
        PR.deleteById(id);
        return deleted;
    }

    /**
     * This is a Post request to create a Player
     * responds to /new/create/{team} endpoint
     *
     * @param p    The player we want to create
     * @param team the team of the player
     * @return returns a success or failure message
     */
    @PostMapping(path = "player/create/{team}")
    Player createNewUser(@RequestBody Player p, @PathVariable String team) {
        if (p == null) {
            throw new IllegalArgumentException("Player object cannot be null");
        } else {

            Optional<Player> usernameMatch = PR.findByName(p.getName());
            if(usernameMatch.isPresent()){
                throw new IllegalArgumentException("Username is already taken");
            }

            //create the player
            Player newPlayer = new Player(p.getName(), p.getBirthday(), p.getEmail(), p.getPassword(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

            //create the Stats for player
            Stats stats = new Stats(100, team,1, 0);

            //send email confirmation
            if(newPlayer.getEmail() != null) {
                //I dont know why I did it this way but it works so dont touch it
                newPlayer.setToken(ES.generateConfirmationToken());
                ES.sendConfirmationEmail(newPlayer.getEmail(), newPlayer.getToken());
            }
            stats.setPlayer(newPlayer);
            newPlayer.setStats(stats);

            return PR.save(newPlayer);
        }
    }

    /**
     *This is a helper method for the donation Post mapping. This is for when the donation amount is less than the number of coins
     * the player has and the hill will not be filled by that amount.
     * @param player the Player who is donating
     * @param stats the Stats related to the donating player
     * @param hill The Hill being donated to
     * @param donation the amount being donated
     */
    private void donateNormal(Player player, Stats stats, Hill hill, int donation) {

        //create a new donation object
        PlayerContribution pc = new PlayerContribution(hill, player, donation);

        //update the hill's total coins
        hill.setTotalCoins(hill.getTotalCoins() + donation);

        //remove the coins from the player stats
        stats.setCoins(stats.getCoins() - donation);

        //save the donation
        PCR.save(pc);

        //after donation update the owner if nessisary
        updateHillOwner(hill, player);

        //send news for almost complete hill if >10% of the hill needs to be filled
        float rem = (float) hill.getTotalCoins() / hill.getGoal();
        if(rem >= .9){
            NEWS.AlmostFinished(hill);
        }
    }

    /**
     *This is a helper method for the donation Post mapping. This is for when the donation amount will fill the hill, so
     * it is only the minimum required amount is donated
     * @param player the Player who is donating
     * @param stats the Stats related to the donating player
     * @param hill the Hill being donated to
     * @param amount the minimum requred amount to fill the hill
     */
    private void donateToFillHill(Player player, Stats stats, Hill hill, int amount) {
        //create donation object
        PlayerContribution pc = new PlayerContribution(hill, player, amount);

        //update the hill's total coins
        hill.setTotalCoins(hill.getTotalCoins() + amount);

        //remove the coins from players stats
        stats.setCoins(stats.getCoins() - amount);

        //send out news of hill being filled
        NEWS.Hillcaptured(hill);

        //news is called first

        //update the hill status
        HSS.replaceHill(hill);
    }

    /**
     * This is a helper method for the donating Post mapping. This is for when the player is trying to donate more coins
     * than they have in their stats. It will donate all of the players coins.
     * @param player The Player who is donatin
     * @param stats the Stats related to the donating player
     * @param hill the Hill being donated to
     */
    private void donateAllCoins(Player player, Stats stats, Hill hill) {

        //create a new donatoin object
        PlayerContribution pc = new PlayerContribution(hill, player, player.getStats().getCoins());

        //update the hill's total coins
        hill.setTotalCoins(hill.getTotalCoins() + player.getStats().getCoins());

        //set player's coins to 0 since they donated everything
        stats.setCoins(0);

        //after donation update the owner if nessisary
        updateHillOwner(hill, player);

        PCR.save(pc);
    }

    /**
     * This is a helper function that will check to see if a new team owns the hill after a donation
     */
    private void updateHillOwner(Hill h, Player p){
        System.out.println("Updating Hill Owner");
        String[] teams = new String[9];
        teams[0] = "ag";teams[1] = "bus";teams[2] = "des";teams[3] = "eng";teams[4] = "hum";
        teams[5] = "lib";teams[6] = "vet";teams[7] = "grad"; teams[8] = "ed";
        //grab every donation from the hill
        //ag, bus, des, eng, hum, lib, vet, grad, ed
        //0 , 1  , 2  , 3  , 4  ,  5 , 6  , 7   , 8

        String currentOwner = h.getOwner();
        String playerTeam = p.getStats().getTeam();

        //if player is in the current leading team then just return as there is nothing to do
        if(Objects.equals(currentOwner, playerTeam)){
            System.out.println("Nothing to update");
            return; //no need to do any further checking, the donating player is in the team that is leading
        }
        //calculate everyones scores
        int[] teamScores = new int[9];

        List<PlayerContribution> donationList = PCR.findByHill(h);

        System.out.println("List of donations to hill");
        System.out.println("Player | Amount | Team ");
        for(PlayerContribution pc : donationList){
            //System.out.println(pc.getPlayer().getName() + " , " + pc.getContribution() + " , " + pc.getPlayer().getStats().getTeam());
            //sum up all donations
            String team = pc.getPlayer().getStats().getTeam();
            int amount = pc.getContribution();
            switch(team) {
                case "ag":
                    teamScores[0] += amount;
                    break;
                case "bus":
                    teamScores[1] += amount;
                    break;
                case "des":
                    teamScores[2] += amount;
                    break;
                case "eng":
                    teamScores[3] += amount;
                    break;
                case "hum":
                    teamScores[4] += amount;
                    break;
                case "lib":
                    teamScores[5] += amount;
                    break;
                case "vet":
                    teamScores[6] += amount;
                    break;
                case "grad":
                    teamScores[7] += amount;
                    break;
                case "ed":
                    teamScores[8] += amount;
                    break;
            }
        }
        //System.out.println("ag , bus, des, eng, hum, lib, vet, grad, ed");
//        for(int s : teamScores){
//            System.out.print(s + " ,");
//        }
//        System.out.println("");

        //find the team with most coins donated
        int ownerIndex = TeamScoreMax(teamScores);

        //if after all of this the owner is the same then we don't need to announce anything
        if(!Objects.equals(teams[ownerIndex], currentOwner)) {
            //set the owner
            h.setOwner(teams[ownerIndex]);

            NEWS.NewLeader(h, h.getOwner());

            System.out.println("Ownewr of hill " + h.getName() + " is " + h.getOwner());
        }
    }


    /**
     * Helper method to find the max value in an array and return the index of that element
     */
    private int TeamScoreMax(int[] a){
        int maxIndex = 0;
        for(int i = 0; i < a.length; i++){
            if(a[i] > a[maxIndex]){
                maxIndex = i;
            }
        }
        //returns the index of largest int
        return maxIndex;
    }



}

