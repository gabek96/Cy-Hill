package coms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


import coms.repository.*;
import coms.model.Player;


/**
 * @Author Matthew Etnyre
 * This class is a controller for confirming a player clicked the link in theh email sent when they created the account.
 */
@RestController
public class EmailConfirmationController {

    @Autowired
    private PlayerRepository PR;

    /**
     * This is a GetMapping which finds the player by their unique token and then sets their status to active.
     *
     * @param token email token generated when an account was created
     * @return returns a message depending on if the operation fails or succeeds
     */
    @GetMapping("/confirm/{token}")
    public String confirmEmail(@PathVariable String token) {
        Player p = PR.findByEmailToken(token);
        if(p != null){
            p.setIsActive(true);
            PR.save(p);
            return "Email confirmed successfully!";
        }
        return "Player doesnt exist";
    }
}