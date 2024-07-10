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

public class RestPwdController {
    public Button resetCode_btn;
    public TextField confrmPwd_txtfd1;
    public TextField newPwd_txtfd;
    public Label error_lbl;
    public TextField userEmail_txtfd;
    public Button resetpwd_btn;

    IServiceValidation serviceValidation = new ServiceValidation(); //sna3et instance mel validation Service
    IServiceUser serviceUser = new ServiceUser(); // sna3et instance mel user Service
    User user = new User();
    MailerService mailerService = new MailerService();
    public void resetPWD(ActionEvent event) {
        // String email = userEmail_txtfd.getText().trim();
        String newPwd = newPwd_txtfd.getText().trim();
        String confrmPwd = confrmPwd_txtfd1.getText().trim();

        if (!newPwd.equals(confrmPwd)) {
            error_lbl.setText("Passwords do not match!");
            return;
        }else if (newPwd.isEmpty() || confrmPwd.isEmpty()) {
            error_lbl.setText("Password cannot be empty!");
            return;
        }else {
            System.out.println("passwords match");
            serviceUser.updatePassword(newPwd,user.getId());
            System.out.println("password updated");
        }

        //redirect to code verification page
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/loginGym.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) resetpwd_btn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setUser(User uservalid) {
        this.user = uservalid;

    }
}