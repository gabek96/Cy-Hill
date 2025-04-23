package coms.DataTransferObject;

public class LeaderBoardDTO {
    String playerName;
    int totalDonation;
    String team;

    public LeaderBoardDTO(String playerName, int totalDonation, String team){
        this.playerName = playerName;
        this.totalDonation = totalDonation;
        this.team = team;
    }

    // Getter for playerName
    public String getPlayerName() {
        return playerName;
    }

    // Setter for playerName
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    // Getter for totalDonation
    public int getTotalDonation() {
        return totalDonation;
    }

    // Setter for totalDonation
    public void setTotalDonation(int totalDonation) {
        this.totalDonation = totalDonation;
    }

    // Getter for team
    public String getTeam() {
        return team;
    }

    // Setter for team
    public void setTeam(String team) {
        this.team = team;
    }


}
