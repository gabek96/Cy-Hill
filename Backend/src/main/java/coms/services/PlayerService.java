package coms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coms.model.*;
import coms.services.EmailService;

/**
 * This class is a service class for the Player object.
 */
@Service
public class PlayerService {
    @Autowired
    private EmailService ES;

    /**
     * Sends the confirmation email to the player's email
     * @param p the player who will be sent a confirmation email
     */
    public void sendConfirmEmail(Player p){
        p.setToken(ES.generateConfirmationToken());
        ES.sendConfirmationEmail(p.getEmail(), p.getToken());

    }
}