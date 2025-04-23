package coms.model;
import jakarta.persistence.*;
import org.springframework.web.bind.annotation.*;
import coms.services.*;
import coms.model.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * This class represents a plyer's donation to a hill.
 * @Author Matthew Etnyre
 */
@Entity
@Table(name = "contribution")
public class PlayerContribution{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int contribution;

    @ManyToOne // A donation is tied to one player
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @ManyToOne // A donation is tied to one hill
    @JoinColumn(name = "hill_id", nullable = false)
    private Hill hill;
    /**
     * Default constructor is required
     */
    public PlayerContribution() {}

    /**
     *
     * @param hill hill the player is donating to
     * @param player player who is donating
     * @param contribution number of coins being donated
     */
    public PlayerContribution(Hill hill, Player player, int contribution){
        this.player = player;
        this.hill = hill;
        this.contribution = contribution;
    }

    // Setters and Getters

    /**
     * Gets the ID of the contribution.
     *
     * @return the ID of the contribution.
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets the ID of the contribution.
     *
     * @param id the ID to set for the contribution.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the player associated with the contribution.
     *
     * @return the player associated with the contribution.
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Sets the player associated with the contribution.
     *
     * @param p the player to set for the contribution.
     */
    public void setPlayer(Player p) {
        this.player = p;
    }

    /**
     * Gets the hill associated with the contribution.
     *
     * @return the hill associated with the contribution.
     */
    public Hill getHill() {
        return this.hill;
    }

    /**
     * Sets the hill associated with the contribution.
     *
     * @param h the hill to set for the contribution.
     */
    public void setHill(Hill h) {
        this.hill = h;
    }

    /**
     * Gets the contribution value of the contribution.
     *
     * @return the contribution value of the contribution.
     */
    public int getContribution() {
        return this.contribution;
    }

    /**
     * Sets the contribution value of the contribution.
     *
     * @param contribution the contribution value to set for the contribution.
     */
    public void setContribution(int contribution) {
        this.contribution = contribution;
    }


}