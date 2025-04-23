package coms.services;

import coms.model.*;
import coms.websocket.*;
import coms.repository.*;
import coms.DataTransferObject.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import java.util.Random;


/**
 * This class is a service class for the Hill object
 */
@Service
public class HillScreenService {

    private final HillRepository HR;
    private final PlayerContributionRepository PCR;

    @Lazy
    @Autowired
    private HillWS hillWS;

    @Lazy
    @Autowired
    private HillLocationWS hilllocationWS;

    @Lazy
    @Autowired
    private NewsServer NEWS;

    /**
     * Constructor injection for Spring-managed beans
     * @param HR Hill Repository
     * @param PCR Player Contribution Repository
     */
    @Autowired
    public HillScreenService(HillRepository HR, PlayerContributionRepository PCR) {
        this.HR = HR;
        this.PCR = PCR;
    }

    /**
     *
     * @return returns a list of every Hill in the DB
     */
    public List<Hill> getAllHills() {
        if(HR != null) {
            return HR.findAll();
        }
        return null;
    }

    /**
     * This function takes every Hill that is active and returns how many points each college has put into it.
     * @return Returns a list of HillScreenDTO objects which contains each teams number of coins donated to every hill
     */
    public List<HillScreenDTO> sendHillData() {
        System.out.print("made it to data function");
        Map<String, Integer> teamToIndexMap = new HashMap<>();
        teamToIndexMap.put("ag", 0);
        teamToIndexMap.put("bus", 1);
        teamToIndexMap.put("des", 2);
        teamToIndexMap.put("eng", 3);
        teamToIndexMap.put("hum", 4);
        teamToIndexMap.put("lib", 5);
        teamToIndexMap.put("vet", 6);
        teamToIndexMap.put("grad", 7);
        teamToIndexMap.put("ed", 8);

        List<Hill> H_List = getAllHills();//grab all of the Hills
        for(Hill h : H_List){
            System.out.print(h.getId() + ", ");
        }
        List<PlayerContribution> C_List = PCR.findAll(); //grab all donations
        List<HillScreenDTO> returnList = new ArrayList<>();
       // System.out.println("made it to data function");
        for(Hill h : H_List) { // loop through the hills
            int[] scores = new int[9];
            if(h.getActive()) {
                for (PlayerContribution c : C_List) {
                    // if Hill_id and clist id match, add the donation to the counter
                    //System.out.println("seeing if hills match");
                    //System.out.println(h.getId() + ", " + c.getHill().getId());
                    if (Objects.equals(h.getId(), c.getHill().getId())) {
                        //System.out.println("hills match!");
                        String team = c.getPlayer().getStats().getTeam().toLowerCase();
                        int teamIndex = teamToIndexMap.getOrDefault(team, -1);
                        if (teamIndex == -1) {
                            System.out.println("Team not found " + team);
                        } else {
                            scores[teamToIndexMap.get(team)] += c.getContribution();
                        }
                    }
                }
                // at this point, we have gone through every contribution for this one hill, so the scores array should be filled
                int total = 0;
                // add the array up for the total
                for (int s : scores) {
                    total += s;
                }
                //System.out.print("This is the scores table");
                for (int s : scores) {
                    System.out.print(s);
                }
                // Create a new HillScreen object and add it to the list
                HillScreenDTO HS = new HillScreenDTO(h.getName(), h.getOwner(), h.getGoal(), scores[0], scores[1], scores[2], scores[3], scores[4], scores[5], scores[6], scores[7], scores[8]);
                returnList.add(HS);
            }
        }
        hillWS.HillScreenData(returnList);
        return returnList;
    }


    /**
     * This function takes every Hill that is active and returns their locations in a list.
     * @return a list of HillLocationDTO
     */
    public List<HillLocationDTO> sendHillLocationData() {
        System.out.println("Sending hill location information");
        List<Hill> H_List = getAllHills();//grab all of the Hills
        //System.out.print(h.getId() + ", ");
        List<HillLocationDTO> returnList = new ArrayList<>();
        for(Hill h : H_List) { // loop through the hills
            //for each hill we will need to create a new HillLocation obj and add it to the return list
            if(h.getActive()) {
                HillLocationDTO hl = new HillLocationDTO(h.getId(),h.getName(), h.getLat(), h.getLon(), h.getTotalCoins(), h.getGoal());
                returnList.add(hl);
                //System.out.println("Just added "+ hl);
            }
        }
        hilllocationWS.HillScreenLocal(returnList);
        return returnList;
    }





    /**
     * this function is used to replace a hill that has just been filled with a new hill
     * @param h the hill that is going to be replaced
     */
    public void replaceHill(Hill h){
        System.out.println("Activating a new hill");
        //grab a list of every inactive hill
        List<Hill> InactiveHills = HR.findByActive(false);

        //grab a random hill
        Random rand = new Random();
        int inactiveHill = rand.nextInt(InactiveHills.size());

        //set that hill to active
        Hill HilltoActivate = InactiveHills.get(inactiveHill);
        HilltoActivate.setActive(true);

        //set old hill to inactive
        h.setActive(false);
        h.setTotalCoins(0);

        System.out.println("The winners of hill " + h.getName() + " is " + h.getOwner());

        h.setOwner(null);

        //remove hill donations
        PCR.deleteAllByHill(h);

        System.out.println("The " + h.getName() + " Hill is now inactive");
        System.out.println("The " + HilltoActivate.getName() + " Hill is now active");

        //Send winner of hill
        NEWS.NewHill(HilltoActivate);
    }
}


