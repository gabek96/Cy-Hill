package coms.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.util.List;

import coms.repository.*;
import coms.model.*;

@Component
public class DeleteInactive{

    @Autowired
    private PlayerRepository PR;

    //this is scheduled to happen on the 1st of each month
    @Scheduled(cron = "0 0 1 1 * ?")

    //this sets its to run for every minute just for testing lol
    //@Scheduled(cron = "0 * * * * ?")
    public void DeleteInactive(){
        //grab all of the players
        List<Player> players = PR.findAll();

        for(Player p : players){
            //if the player is not active then delete them
            if(p.getIsActive() == false){
                PR.deleteById(p.getId());
                //log the deletions
                System.out.println("Deleted inactive player with ID: " + p.getId());
            }
        }
    }
}