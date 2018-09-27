package service;

import java.security.SecureRandom;

public class SendEmail {

    public boolean SendNewPassword(String email, String newPass) {
        String body = "Your new password is " + newPass + ". Please login to change your password\nPlease do not reply this email.";
        String msg = "";
        try {
            GMailSender sender = new GMailSender("lalalacom1225@gmail.com", "lalala1225");
            sender.sendMail("New Password",
                    body,
                    "lalalacom1225@gmail.com",
                    email);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    
}
