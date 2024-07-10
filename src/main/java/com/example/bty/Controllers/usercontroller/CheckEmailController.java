package com.example.bty.Controllers.usercontroller;

import com.example.bty.Entities.User;
import com.example.bty.Entities.Validation;
import com.example.bty.Services.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.Instant;
import java.util.Random;

public class CheckEmailController {
    public Button checkEmail_btn;
    public TextField userEmail_txtfd;
    public Label error_lbl;
    IServiceValidation serviceValidation = new ServiceValidation(); //sna3et instance mel validation Service
    IServiceUser serviceUser = new ServiceUser(); // sna3et instance mel user Service
    MailerService mailerService = new MailerService();
    public void goToCodeVerif(ActionEvent event) {
        String email = userEmail_txtfd.getText().trim();
        if (email.isEmpty()) {
            error_lbl.setText("Email cannot be empty!");
            return;
        }
        // check if email exists in the database
        User user = serviceUser.findByEmail(email);
        if (user!=null) {
            if (!user.isEtat()) {
                error_lbl.setText("Account is disabled!");
                return;
            } else {
                System.out.println("User exist");
                Random random = new Random();
                random.nextInt(9999); // code bin 0 w 8999 [0,9999[
                Validation v = new Validation(random.nextInt(9999), Instant.now(), Instant.now().plusSeconds(600), serviceUser.findByEmail(email));
                serviceValidation.ajouterValidation(v);
                String message = "Your code is : " + v.getCode();
                String subject = "Rénitialisation mot de passe";
                mailerService.sendMail(email, message, subject);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/verifCodeReset.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) checkEmail_btn.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else {
            System.out.println("User not exist");
            error_lbl.setText("Email does not exist!");
        }

        // if it does, send the code to the email
        // if it doesn't, display an error message
    }
}