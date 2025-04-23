package coms.model;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import coms.services.HillScreenService;

/**
 * This class represents a hill in the game.
 * @Author Matthew Etnyre
 */
@Entity
@Table(name = "hill")
public class Hill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double lon;
    private Double lat;
    private int radius;
    private String current_owner;
    private int totalCoins;
    private int goal;
    private boolean active;

    /**
     * Default constructor is required
     */
    public Hill(){}

    /**
     *
     * @param name name of hill
     * @param lon longitude of hill
     * @param lat latitude of hill
     * @param radius radius of hill
     * @param goal number of points until the hill is closed
     */
    public Hill(String name, Double lon, Double lat, int radius, int goal){
        this.name = name;
        this.lon = lon;
        this.lat = lat;
        this.goal = goal;
        this.totalCoins =  0; //number of coins in the hill
        this.current_owner = null; //alsways starts out null
        this.active = false; //always starts out false
    }


    public void setOwner(String owner){
        this.current_owner = owner;
    }

    public String getOwner(){
        return this.current_owner;
    }

    // Setters and Getters

    /**
     * Gets the ID of the Hill.
     *
     * @return the ID of the Hill.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the Hill.
     *
     * @param id the ID to set for the Hill.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the Hill.
     *
     * @return the name of the Hill.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the Hill.
     *
     * @param name the name to set for the Hill.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the longitude of the Hill.
     *
     * @return the longitude of the Hill.
     */
    public Double getLon() {
        return this.lon;
    }

    /**
     * Sets the longitude of the Hill.
     *
     * @param lon the longitude to set for the Hill.
     */
    public void setLon(Double lon) {
        this.lon = lon;
    }

    /**
     * Gets the latitude of the Hill.
     *
     * @return the latitude of the Hill.
     */
    public Double getLat() {
        return this.lat;
    }

    /**
     * Sets the latitude of the Hill.
     *
     * @param lat the latitude to set for the Hill.
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * Gets the total number of coins associated with the Hill.
     *
     * @return the total number of coins.
     */
    public int getTotalCoins() {
        return totalCoins;
    }

    /**
     * Sets the total number of coins associated with the Hill.
     *
     * @param totalCoins the total number of coins to set.
     */
    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
    }

    /**
     * Gets the radius of the Hill.
     *
     * @return the radius of the Hill.
     */
    public int getRadius() {
        return this.radius;
    }

    /**
     * Sets the radius of the Hill.
     *
     * @param radius the radius to set for the Hill.
     */
    public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Gets the active status of the Hill.
     *
     * @return true if the Hill is active, false otherwise.
     */
    public boolean getActive() {
        return this.active;
    }

    /**
     * Sets the active status of the Hill.
     *
     * @param active the active status to set for the Hill.
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Gets the goal associated with the Hill.
     *
     * @return the goal value.
     */
    public int getGoal() {
        return this.goal;
    }

    /**
     * Sets the goal associated with the Hill.
     *
     * @param goal the goal to set for the Hill.
     */
    public void setGoal(int goal) {
        this.goal = goal;
    }

}

