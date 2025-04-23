package coms.DataTransferObject;

/**
 * This class represents a hill object that is to be sent to the front end for information about a hill's location in the map.
 * This class is only a constructor because it is expected that this object is created and then sent to the frontend immediately.
 */
public class HillLocationDTO{
    private Long id;
    private String name;
    private double lon;
    private double lat;
    private int totalCoins;
    private int goal;

    /**
     * Constructor
     * @param name the name of the hill
     * @param lon longitude of the hill
     * @param lat latitude of the hill
     * @param totalCoins total coins in the hill
     * @param goal maximum number of coins the hill can hold
     */
    public HillLocationDTO(Long id, String name, double lon, double lat, int totalCoins, int goal){
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.totalCoins = totalCoins;
        this.goal = goal;
    }

    // Getter and Setter for 'Id'
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter and Setter for 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for 'lon'
    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    // Getter and Setter for 'lat'
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    // Getter and Setter for 'totalCoins'
    public int getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(int totalCoins) {
        this.totalCoins = totalCoins;
    }

    // Getter and Setter for 'goal'
    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }
}