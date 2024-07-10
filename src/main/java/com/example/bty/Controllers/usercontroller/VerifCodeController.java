package com.example.bty.Controllers.usercontroller;

import com.example.bty.Entities.User;
import com.example.bty.Entities.Validation;
import com.example.bty.Services.IServiceUser;
import com.example.bty.Services.IServiceValidation;
import com.example.bty.Services.ServiceUser;
import com.example.bty.Services.ServiceValidation;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.Instant;

public class VerifCodeController {
    public Label enterCode_lbl;
    public TextField code_txtfd;
    public Button enterCode_btn;
    public Label error_lbl;

    public void verifierCode(ActionEvent event) {
        String codeStr = code_txtfd.getText().trim();
        if (codeStr.isEmpty()) {
            error_lbl.setText("Code cannot be empty!");
            return;
        }

        int code = Integer.parseInt(codeStr);
        ServiceValidation serviceValidation = new ServiceValidation();
        Validation validation = serviceValidation.findByCode(code);
        IServiceUser serviceUser = new ServiceUser();

        if (validation != null) {
            if (Instant.now().isBefore(validation.getExpired_at())) {
                error_lbl.setText("Code is valid!");
                User user = validation.getUser(); //bch njib luser eli aandou lcode adheka mel validation table
                //u.setEtat(true); // nbadalou letat true pour activer son compte
                serviceUser.desactiverAcc(user.getId()); // bch na3mlou update luser

                // System.out.println(" user is activated after verification");
                // load loginGym.fxml
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginGym.fxml"));
                    Parent root = fxmlLoader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) enterCode_btn.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            } else {
                error_lbl.setText("Code is expired!");
            }
        } else {
            error_lbl.setText("Invalid code!");
        }
    }
}