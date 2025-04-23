package coms.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.UUID;


/**
 * This class represents an Email Service class used to send a confirmation email to a user who created an account
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Sends an email to the user with a link to click which will activate their account.
     * @param toEmail The email a new user entered when creating their account.
     * @param confirmationToken The token which was generated when the new user created their account.
     */
    public void sendConfirmationEmail(String toEmail, String confirmationToken) {
        String subject = "Confirm your email address";
        String confirmationUrl = "http://coms-3090-059.class.las.iastate.edu:8080/confirm/" + confirmationToken;
        String message = "Please click the link below to confirm your email address:\n" + confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(toEmail);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom("cyhill2024@gmail.com");

        mailSender.send(email);
    }

    public int sendResetCode(String username, String email){
        String subject = username + " Reset Your Password for Cy-Hill";
        int resetToken = generateResetToken();
        String message = "Your password reset token is: " + resetToken;


        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(email);
        mail.setSubject(subject);
        mail.setText(message);
        mail.setFrom("cyhill2024@gmail.com");

        mailSender.send(mail);
        return resetToken;
        }

    private int generateResetToken(){
        return (int)(Math.random() * 90000) + 10000; //this generates a 5 digit random number
    }

    public String generateConfirmationToken() {
        return UUID.randomUUID().toString();
    }
}
