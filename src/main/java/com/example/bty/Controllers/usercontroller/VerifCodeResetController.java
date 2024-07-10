package com.example.bty.Controllers.usercontroller;

import com.example.bty.Entities.User;
import com.example.bty.Entities.Validation;
import com.example.bty.Services.IServiceUser;
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

public class VerifCodeResetController {

    public Label error_lbl;
    public Label enterCode_lbl;
    public TextField code_txtfd;
    public Button enterCode_btn;

    public void verifCodeReset(ActionEvent event) {
        String codeStr = code_txtfd.getText().trim();
        if (codeStr.isEmpty()) {
            error_lbl.setText("Code cannot be empty!");
            return;
        }
        int code = Integer.parseInt(codeStr);
        ServiceValidation serviceValidation = new ServiceValidation();
        Validation validation = serviceValidation.findByCode(code); //bch njib lvalidation(feha id_user forgien key) eli andou lcode adheka mel validation table
        IServiceUser serviceUser = new ServiceUser();

        if (validation != null) {
            if (Instant.now().isBefore(validation.getExpired_at())) {
                error_lbl.setText("Code is valid!");
                User uservalid = validation.getUser(); //bch njib luser eli aandou lcode adheka mel validation table
                // load resetPwd.fxml
                System.out.println(uservalid);
                serviceValidation.deleteValidation(validation.getCode());
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/resetPwd.fxml"));
                    Parent root = fxmlLoader.load();
                    RestPwdController restPwdController = fxmlLoader.getController();
                    restPwdController.setUser(uservalid);

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