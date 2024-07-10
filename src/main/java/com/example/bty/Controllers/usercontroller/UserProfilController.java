package com.example.bty.Controllers.usercontroller;

import com.example.bty.Entities.Role;
import com.example.bty.Services.TwoAuthenticationService;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import com.example.bty.Entities.User;
import com.example.bty.Services.IServiceUser;
import com.example.bty.Services.MailerService;
import com.example.bty.Services.ServiceUser;
import com.example.bty.Utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class UserProfilController implements Initializable {
    public AnchorPane main_form;
    public AnchorPane dashboard_form;
    public AnchorPane user_profil;
    public AnchorPane box_1;
    @FXML
    public TextField profil_name;
    public TextField profil_email;
    public TextField profil_telephone;
    public Button coaches_updateBtn1;
    public AnchorPane box_11;
    public PasswordField new_password;
    public PasswordField confirm_password;
    public Button profil_update;
    public ImageView profile_img;
    public Button update_img_btn;
    public ImageView image;
    public Button enable_btn;
    public ImageView QrCode;
    public TextField CodeInput;
    public Button verifCode_btn;
    @FXML
    public ToggleButton mfa_btn;
    public RadioButton enable2fa;
    public CheckBox checkMfa_btn;
    public Label error2fa_lbl;

    public AnchorPane coaches_form;
    public TextField coaches_name;
    public TextField coaches_email;
    public TextField coaches_telephone;
    public Button coaches_createBtn;
    public Button coaches_updateBtn;
    IServiceUser serviceUser00 = new ServiceUser();
    MailerService mailerService = new MailerService();
    Session session = Session.getInstance();
    User u = session.getLoggedInUser();
    User user;
    TwoAuthenticationService twoAuthenticationService = new TwoAuthenticationService();

    public static String pathImage;
    public StackPane contentPlaceholder;


    public void updateImgBtn(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        pathImage = fileChooser.showOpenDialog(null).getAbsolutePath();
        Image img = new Image("file:" + pathImage);
        profile_img.setImage(img);
        serviceUser00.updateImage(pathImage, Session.getInstance().getLoggedInUser().getId());
    }

    public void updatePassword(ActionEvent event) {
        if (!Objects.equals(new_password.getText(), confirm_password.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Password");
            alert.setHeaderText(null);
            alert.setContentText("Password not match");
            alert.showAndWait();
        } else {
            serviceUser00.updatePassword(new_password.getText(), Session.getInstance().getLoggedInUser().getId());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password");
            alert.setHeaderText(null);
            alert.setContentText("Password updated");
            alert.showAndWait();
            new_password.clear();
            confirm_password.clear();
        }
    }

    public void coachesUpdateBtn(ActionEvent actionEvent) {
        User loggedInUser = Session.getInstance().getLoggedInUser();
        System.out.println(profil_name.getText()+" "+profil_email.getText()+" "+profil_email.getText());

        User coach = new User(loggedInUser.getId(), profil_name.getText(), profil_email.getText(), profil_telephone.getText(), true);
        serviceUser00.update(coach);
        //Session.setLoggedInUser(coach);



        loadContent("/UserProfil.fxml");

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Stage primaryStage = new Stage();
        // fetch the status of the checkbox
        if (session.getLoggedInUser().isMfaEnabled()) {
            checkMfa_btn.setSelected(true);
            checkMfa_btn.setText("ON");
            checkMfa_btn.setFont(Font.font("System", FontWeight.BOLD, 12)); // Set the font to bold
//            String secret = session.getLoggedInUser().getMfaSecret(); //generates a secret key for the user
//            String qrCode = twoAuthenticationService.getQRBarcodeURL(secret); //generates url for the qr code
//            Image img = new Image(qrCode);//convert the qr code to an image
//            QrCode.setImage(img); //set the image in the image view
        } else {
            checkMfa_btn.setSelected(false);
            checkMfa_btn.setText("OFF");
            checkMfa_btn.setFont(Font.font("System", FontWeight.BOLD, 12)); // Set the font to bold
        }
        CodeInput.setVisible(false);
        CodeInput.setManaged(false);
        verifCode_btn.setVisible(false);
        verifCode_btn.setManaged(false);
        SetUser();
    }



    private void loadContent(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent content = loader.load();
            contentPlaceholder.getChildren().clear();
            contentPlaceholder.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur de chargement du fichier FXML
        }

    }

    public void SetUser() {
        User loggedInUser = Session.getInstance().getLoggedInUser();
        profil_name.setText(loggedInUser.getName());
        profil_email.setText(loggedInUser.getEmail());
        profil_telephone.setText(loggedInUser.getTelephone());
        image.setImage(new Image("file:" + loggedInUser.getImage()));
        System.out.println(profil_email.getText()+" "+profil_name.getText()+" "+profil_telephone.getText());

    }
    //enable btn loula ***********
    public void enable(ActionEvent event) {
        String secret = twoAuthenticationService.generateSecretKey(); //generates a secret key for the user
        //serviceUser00.setSecretKey(secret,Session.getInstance().getLoggedInUser().getId()); //save the secret key in the database
        //serviceUser00.EnableOrDisablemfa(Session.getInstance().getLoggedInUser().getId()); //activate the two factor authentication
        String qrCode = twoAuthenticationService.getQRBarcodeURL(secret); //generates url for the qr code
        Image img = new Image(qrCode);//convert the qr code to an image
        QrCode.setImage(img); //set the image in the image view

        verifCode_btn.setVisible(true);//make the verify code button visible
        verifCode_btn.setManaged(true);
        CodeInput.setVisible(true);//make the code input field visible
        CodeInput.setManaged(true);


    }

    public void verifCode(ActionEvent event) {
        System.out.println("Code: " + CodeInput.getText());
        System.out.println("User"+Session.getInstance().getLoggedInUser());
        System.out.println("Secret: " + Session.getInstance().getLoggedInUser().getMfaSecret());
        if (twoAuthenticationService.verifyCode(Session.getInstance().getLoggedInUser().getMfaSecret(), CodeInput.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Code");
            alert.setHeaderText(null);
            alert.setContentText("Code correct");
            alert.showAndWait();


            verifCode_btn.setVisible(false);
            verifCode_btn.setManaged(false);
            CodeInput.setVisible(false);
            CodeInput.setManaged(false);
            QrCode.setVisible(false);
            QrCode.setManaged(false);
            error2fa_lbl.setText("");
        } else {
            error2fa_lbl.setText("Code incorrect");
        }


    }


//    public void enabled(ActionEvent event) {
//        if (enable2fa.isSelected()) {
//            if(Session.getInstance().getLoggedInUser().isMfaEnabled())
//            {
//                Alert alert = new Alert(Alert.AlertType.WARNING);
//                alert.setTitle("Two Factor Authentication");
//                alert.setHeaderText(null);
//                alert.setContentText("Two Factor Authentication is already enabled");
//                alert.showAndWait();
//
//            }
//            else
//            {
//                System.out.println("Im here");
//                String secret = twoAuthenticationService.generateSecretKey(); //generates a secret key for the user
//                System.out.println("Secret: " + secret);
//                String qrCode = twoAuthenticationService.getQRBarcodeURL(secret); //generates url for the qr code
//                serviceUser00.setSecretKey(secret, Session.getInstance().getLoggedInUser().getId());
//                serviceUser00.EnableOrDisablemfa(Session.getInstance().getLoggedInUser().getId());
//                User user=serviceUser00.findByEmail(Session.getInstance().getLoggedInUser().getEmail());
//                Session.getInstance().setLoggedInUser(user);
//                Image img = new Image(qrCode);//convert the qr code to an image
//                QrCode.setImage(img); //set the image in the image view
//                verifCode_btn.setVisible(true);//make the verify code button visible
//                verifCode_btn.setManaged(true);
//                CodeInput.setVisible(true);//make the code input field visible
//                CodeInput.setManaged(true);
//            }
//
//        } else {
//            serviceUser00.setSecretKey(null, Session.getInstance().getLoggedInUser().getId());
//            serviceUser00.EnableOrDisablemfa(Session.getInstance().getLoggedInUser().getId());
//            verifCode_btn.setVisible(false);
//            verifCode_btn.setManaged(false);
//            CodeInput.setVisible(false);
//            CodeInput.setManaged(false);
//        }
//    }


    public void checked(ActionEvent event) {
        checkMfa_btn.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                checkMfa_btn.setText("ON");
            } else {
                checkMfa_btn.setText("OFF");
            }
            checkMfa_btn.setFont(Font.font("System", FontWeight.BOLD, 12)); // Set the font to bold

        });
        if (checkMfa_btn.isSelected()) {
            if(Session.getInstance().getLoggedInUser().isMfaEnabled())
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Two Factor Authentication");
                alert.setHeaderText(null);
                alert.setContentText("Two Factor Authentication is already enabled");
                alert.showAndWait();
            }
            else
            {
                System.out.println("Im here");
                String secret = twoAuthenticationService.generateSecretKey(); //generates a secret key for the user
                System.out.println("Secret: " + secret);
                String qrCode = twoAuthenticationService.getQRBarcodeURL(secret); //generates url for the qr code
                serviceUser00.setSecretKey(secret, Session.getInstance().getLoggedInUser().getId());
                serviceUser00.EnableOrDisablemfa(Session.getInstance().getLoggedInUser().getId());
                User user=serviceUser00.findByEmail(Session.getInstance().getLoggedInUser().getEmail());
                Session.getInstance().setLoggedInUser(user);
                Image img = new Image(qrCode);//convert the qr code to an image
                QrCode.setImage(img); //set the image in the image view
                verifCode_btn.setVisible(true);//make the verify code button visible
                verifCode_btn.setManaged(true);
                CodeInput.setVisible(true);//make the code input field visible
                CodeInput.setManaged(true);
            }
        } else {
            serviceUser00.setSecretKey(null, Session.getInstance().getLoggedInUser().getId());
            serviceUser00.EnableOrDisablemfa(Session.getInstance().getLoggedInUser().getId());
            verifCode_btn.setVisible(false);
            verifCode_btn.setManaged(false);
            CodeInput.setVisible(false);
            CodeInput.setManaged(false);
        }
    }
}