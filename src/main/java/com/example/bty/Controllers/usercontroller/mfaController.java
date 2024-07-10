package com.example.bty.Controllers.usercontroller;

import com.example.bty.Entities.User;
import com.example.bty.Services.TwoAuthenticationService;
import com.example.bty.Utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class mfaController {
    public Label error_lbl;
    public Label enterCode_lbl;
    public TextField code_txtfd;
    public Button enterCode_btn;
    public User user;
    private TextField si_email;
    TwoAuthenticationService twoAuthenticationService = new TwoAuthenticationService();

    public void verifierCode(ActionEvent event) throws IOException {
        if (user.getMfaSecret() != null) {

            if (twoAuthenticationService.verifyCode(user.getMfaSecret(), code_txtfd.getText())){
                error_lbl.setText("Code correct");
                Session.getInstance().setLoggedInUser(user);
                FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/dashboardX.fxml"));
                Parent dashboardRoot = dashboardLoader.load();
                Scene dashboardScene = new Scene(dashboardRoot);
                Stage dashboardStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                dashboardStage.setScene(dashboardScene);
                dashboardStage.show();

            } else {
                error_lbl.setText("Code incorrect");
            }
        }


    }

    public void setUser(User user, TextField si_email) {
        this.user = user;
        this.si_email = si_email;

    }
}