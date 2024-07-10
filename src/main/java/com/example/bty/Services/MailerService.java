package com.example.bty.Services;



import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailerService {

    // send email
    public void sendMail(String recipient,String msg,String subj) {
        System.out.println(recipient);
        String host = "bahaeddinedridi1@gmail.com";  //← my email address
        final String user = "bahaeddinedridi1@gmail.com";//← my email address
        final String password = "oman kvgj hdks njqc";//change accordingly   //← my email password

        String to = recipient;//→ the EMAIL i want to send TO →
        Properties props = new Properties();
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });

        //My message :
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user, "Flex Flow"));
            //message.setFrom(new InternetAddress(user, "Flex Flow" ));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subj);
            //Text in emial :
            //message.setText("  → Text message for Your Appointement ← ");
            //Html code in email :
            message.setContent(msg, "text/html");

            //send the message
            Transport.send(message);

            System.out.println("message sent successfully via mail ... !!! ");

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }
}