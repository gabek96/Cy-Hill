package coms.model;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.*;
import coms.services.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import coms.model.*;

/**
 * Represents the Stats Entity for track player's stats
 * @author Matthew Etnyre
 * @author Gabe Kiveu
 */

/**
 *{
 *     "id": 2,
 *     "name": "Student G",
 *     "coins": 100,
 *     "team": "BUSSINESS",
 *     "level": 4,
 *     "xp": -90,
 * }
 */
@Entity
@Table(name = "stats")
public class Stats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int coins;
    private String team;
    private int level ;
    private int xp;


    //stops the infinite loop
    @JsonBackReference
    @OneToOne(mappedBy = "stats")
    private Player player;

    public Stats(){
        //Default level and XP fo every new Stats Object
        this.level = 1;
        this.xp = 0;
    }


    /**
     * Constructs a Stats object with the specified parameters.
     * @param coins the number of coins the player has
     * @param team  the team of the player
     * @param level  the level of the player
     * @param xp  the experience points of the player
     */
    public Stats(int coins, String team, int level, int xp){
        this.coins = coins;
        this.team = team;
        this.level = level;
        this.xp = xp;

    }



    //setters and getters
    /**
     * Gets the  player
     * @return the player
     */
    public Player getPlayer() {return this.player;}

    /**
     * sets the player
     * @param player
     */
    public void setPlayer(Player player){this.player = player;}

    /**
     * Gets the number of coins
     * @return
     */
    public int getCoins() {return this.coins;}

    /**
     *Sets the number of coins
     * @param coins
     */
    public void setCoins(int coins){this.coins = coins;}

    /**
     *Gets the name of the player
     * @return
     */
    public Long getId() {return this.id;}

    /**
     * Sets the name of the player
     * @param Id
     */
    public void setName(Long Id) {this.id = Id;}

    /**
     * Gets the team associated with the player
     * @return
     */
    public String getTeam() {return this.team;}

    /**
     * Sets the team associated with the player
     * @param team
     */
    public void setTeam(String team){this.team = team;}

    /**
     *  Gets the level of the player
     * @return return the level of the player
     */
    public int getLevel() {return level;}

    /**
     *  Sets the level of the player
     * @param level
     */
    public void setLevel(int level) {this.level = level;}

    /**
     *  Gets of the experience points of the player
     * @return
     */
    public int getXp() {return xp;}

    /**
     *  Sets the experience points of the player
     * @param xp
     */
    public void setXp(int xp) {this.xp = xp;}

    /**
     * Increases XP by the specified amount and levels up if necessary.
     * @param xpGained the amount of XP to gain
     */
    public void gainXp(int xpGained) {
        this.xp += xpGained;

        // Level up as long as XP exceeds the required threshold
        while (this.xp >= xpRequiredForNextLevel()) {
            levelUp();
        }
    }

    /**
     * Levels up the player and adjusts XP for the next level.
     */
    public void levelUp() {
        this.level++; // Increment player's level

        // Calculate the new threshold and carry over excess XP
        int xpThreshold = xpRequiredForNextLevel();
        this.xp -= xpThreshold;

    }

    /**
     * Calculates the XP required for the next level.
     * @return XP required for the next level
     */
    public int xpRequiredForNextLevel() {
        return (int) Math.pow(this.level, 2) * 10;
    }

    /**
     * Returns a string representation of the Stats object.
     *
     * @return a string representation of the Stats
     */
    public String toString() {
        return this.team + " "
                + this.coins + " "
                + this.level + " "
                + this.xp + " "
                ;
    }


}
