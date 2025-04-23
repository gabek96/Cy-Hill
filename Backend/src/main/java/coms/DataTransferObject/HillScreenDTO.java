package coms.DataTransferObject;


/**
 * This class represents a hill object that is to be sent to the front end for information about a hill's current donations.
 * This class is only a constructor because it is expected that this object is created and then sent to the frontend immediately.
 */
public class HillScreenDTO{
    private String name;
    private String leader;
    private int goal;
    private int agPoints;
    private int busPoints;
    private int desPoints;
    private int engPoints;
    private int humPoints;
    private int libPoints;
    private int vetsPoints;
    private int gradPoints;
    private int eduPoints;
    private int progress;

    /**
     *
     * @param name name of the hill
     * @param leader team with most points in the hill
     * @param goal the number of points this hill will take (all points from all teams)
     * @param agPoints School of Agriculture current points
     * @param busPoints School of Business current points
     * @param desPoints School of Design current points
     * @param engPoints School of Engineering current points
     * @param humPoints School of Humanities current points
     * @param libPoints School of Liber Arts and Sciences current points
     * @param vetsPoints School of Veterinary Medicine current points
     * @param gradPoints Grad Students current points
     * @param eduPoints School of Education current points
     */
    public HillScreenDTO(String name, String leader, int goal, int agPoints,
                      int busPoints, int desPoints, int engPoints, int humPoints,
                      int libPoints, int vetsPoints, int gradPoints, int eduPoints){
        this.name = name;
        this.leader = leader;
        this.goal = goal;
        this.agPoints = agPoints;
        this.busPoints = busPoints;
        this.desPoints = desPoints;
        this.engPoints = engPoints;
        this.humPoints = humPoints;
        this.libPoints = libPoints;
        this.vetsPoints = vetsPoints;
        this.gradPoints = gradPoints;
        this.eduPoints = eduPoints;
    }
    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for leader
    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    // Getter and setter for goal
    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    // Getter and setter for agPoints
    public int getAgPoints() {
        return agPoints;
    }

    public void setAgPoints(int agPoints) {
        this.agPoints = agPoints;
    }

    // Getter and setter for busPoints
    public int getBusPoints() {
        return busPoints;
    }

    public void setBusPoints(int busPoints) {
        this.busPoints = busPoints;
    }

    // Getter and setter for desPoints
    public int getDesPoints() {
        return desPoints;
    }

    public void setDesPoints(int desPoints) {
        this.desPoints = desPoints;
    }

    // Getter and setter for engPoints
    public int getEngPoints() {
        return engPoints;
    }

    public void setEngPoints(int engPoints) {
        this.engPoints = engPoints;
    }

    // Getter and setter for humPoints
    public int getHumPoints() {
        return humPoints;
    }

    public void setHumPoints(int humPoints) {
        this.humPoints = humPoints;
    }

    // Getter and setter for libPoints
    public int getLibPoints() {
        return libPoints;
    }

    public void setLibPoints(int libPoints) {
        this.libPoints = libPoints;
    }

    // Getter and setter for vetsPoints
    public int getVetsPoints() {
        return vetsPoints;
    }

    public void setVetsPoints(int vetsPoints) {
        this.vetsPoints = vetsPoints;
    }

    // Getter and setter for gradPoints
    public int getGradPoints() {
        return gradPoints;
    }

    public void setGradPoints(int gradPoints) {
        this.gradPoints = gradPoints;
    }

    // Getter and setter for eduPoints
    public int getEduPoints() {
        return eduPoints;
    }

    public void setEduPoints(int eduPoints) {
        this.eduPoints = eduPoints;
    }
}