package coms.controller;


import coms.DataTransferObject.LeaderBoardDTO;
import coms.model.PlayerContribution;
import coms.repository.PlayerContributionRepository;
import coms.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import coms.model.Player;

import coms.DataTransferObject.LeaderBoardDTO;

@RestController
public class LeaderBoardController {
    @Autowired
    private PlayerRepository PR;

    @Autowired
    private PlayerContributionRepository PCR;

    @GetMapping(path = "/get/Leaderboard")
    public List<LeaderBoardDTO> getLeaderBoard(){
        List<LeaderBoardDTO> leaderboard = new ArrayList<>();
        //for each player in the game sum up their donations, create new leaderboard obj, append to list
        List<Player> P_List = PR.findAll();
        List<PlayerContribution> Contrib_List = PCR.findAll();

        if (P_List.isEmpty()) {
            System.out.println("No players found.");
        }
        if (Contrib_List.isEmpty()) {
            System.out.println("No contributions found.");
        }
        for(Player p : P_List){
            int donation = 0;
            for(PlayerContribution pc : Contrib_List){
                //check to see if contrib and player match
                if(Objects.equals(pc.getPlayer().getId(), p.getId())){
                    //match
                    donation += pc.getContribution();
                }
            }

            // Check if stats or team is null
            String team = (p.getStats() != null && p.getStats().getTeam() != null)
                    ? p.getStats().getTeam()
                    : "Unknown";

            //done checking every donation against this player
            LeaderBoardDTO lb = new LeaderBoardDTO(p.getName(), donation, p.getStats().getTeam());
            leaderboard.add(lb);
        }
        return leaderboard;
    }
}
