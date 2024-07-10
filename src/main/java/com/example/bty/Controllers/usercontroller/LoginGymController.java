package com.example.bty.Controllers.usercontroller;

import com.example.bty.Entities.Role;
import com.example.bty.Entities.User;
import com.example.bty.Entities.Validation;
import com.example.bty.LoginView;
import com.example.bty.Services.*;
import com.example.bty.Utils.Session;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;


public class LoginGymController implements Initializable {
    public TextField su_telephone;
    public FontAwesomeIconView close_icon;
    public Label edit_label;
    public Button sub_loginBtn;
    public Button sub_signupBtn;
    public AnchorPane sub_form;
    public Label errorEmail_lbl;
    public Label errorPwd_lbl;
    public Text restPwd_btn;
    public Label errorTop_lbl;
    @FXML
    private Button si_loginBtn;
    @FXML
    private TextField su_email;
    @FXML
    private TextField su_username;
    @FXML
    private PasswordField su_password;
    @FXML
    private Button su_signupBtn;
    @FXML
    private AnchorPane login_form;
    @FXML
    private AnchorPane signup_form;
    @FXML
    private AnchorPane main_form;
    @FXML
    private TextField si_email; // Champ de texte pour l'email
    public StackPane contentPlaceholder;

    @FXML
    private PasswordField si_password;
    private HashMap<String, Integer> failedAttempts = new HashMap<>();
    IServiceValidation serviceValidation = new ServiceValidation();
    MailerService mailerService = new MailerService();

    public LoginGymController() {
    }

    IServiceUser serviceUser = new ServiceUser();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    //************************************* Login  ******************************************//
    @FXML
    private void login(ActionEvent event) throws IOException {

        String email = si_email.getText(); // Récupérer l'email depuis le champ de texte
        String password = si_password.getText(); // Récupérer le mot de passe depuis le champ de texte

        if (email.isEmpty() || password.isEmpty()) {
            // Reset the style of the text fields
            si_email.setStyle("");
            si_password.setStyle("");

            errorTop_lbl.setText("Tous les champs doivent être remplis!");
            // Change the border color of the text fields to red
            if (email.isEmpty()) {
                si_email.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");

            }
            if (password.isEmpty()) {
                si_password.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            }
            return;

        } else if (!email.contains("@") || !email.contains(".")) {
            errorEmail_lbl.setText("Invalid email format!");
            return;
        } else if (password.length() < 8) {
            errorPwd_lbl.setText("Le mot de passe doit comporter au moins 8 caractères!");
            return;
        } else {
            System.out.println(email + " " + password);

            // Appeler la méthode de connexion avec les valeurs récupérées
            int i = serviceUser.Authentification(email, password);
            System.out.println(i);
            if (i == 1) {
                User user = serviceUser.findByEmail(email);
                System.out.println(user.isMfaEnabled());
                if (user.isMfaEnabled())
                {

                    System.out.println("MFA enabled");
                    // Redirect to the MFA page
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/mfa.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) si_email.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                    mfaController mfaController = loader.getController();
                    mfaController.setUser(user,si_email);

                }
                else {
                    System.out.println("login success");
                    Session s = Session.getInstance();
                    s.setLoggedInUser(user);
                    FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/dashboardX.fxml"));
                    Parent dashboardRoot = dashboardLoader.load();
                    Scene dashboardScene = new Scene(dashboardRoot);

                    // Accéder au contrôleur du tableau de bord si nécessaire
                    // DashboardController dashboardController = dashboardLoader.getController();

                    // Obtenir la scène principale et changer la scène actuelle
                    Stage primaryStage = (Stage) si_email.getScene().getWindow();
                    primaryStage.setScene(dashboardScene);
                    primaryStage.setTitle("Dashboard");
                    loadContent("/Accueil.fxml");
                }
            }
            else if (i == 2) {
                System.out.println("login failed");
                errorTop_lbl.setText("Votre compte est désactivé !");
                //lien

                // nheb nzyd kifeh yab3ethh demande lil admin soit tousel notif soit yab3ethh mail
            } else {
                System.out.println("login failed");

                // Increment the count of failed attempts for this user

                failedAttempts.put(email, failedAttempts.getOrDefault(email, 0) + 1);
                // Check if the user has failed to log in 3 times
                if (failedAttempts.get(email) >= 3) {
                    // Call your method to deactivate the user's account
                    User user01 = serviceUser.findByEmail(email);
                    System.out.println(user01);

                    if (Arrays.equals(user01.getRoles(), new Role[]{Role.MEMBRE}) || Arrays.equals(user01.getRoles(), new Role[]{Role.COACH})){
                        serviceUser.desactiverAcc(user01.getId());
                        errorTop_lbl.setText("Votre compte a été désactivé! Veuillez contacter l'administrateur.");
                        System.out.println("houni user member ou coach");
                    } else {
                        System.out.println("houni user admin");
                        errorPwd_lbl.setText("Invalid password!");
                        si_password.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                    }

                }
                // Reset the style of the text fields
                si_email.setStyle("");
                si_password.setStyle("");
                si_password.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                si_email.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
                errorTop_lbl.setText("Invalid email or password!");

            }
        }
    }
    //************************************* sign up  ******************************************//
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
    ///+****************** content load ***************************
    @FXML
    private void signup() throws IOException {
        String email = su_email.getText(); // Récupérer l'email depuis le champ de texte
        String username = su_username.getText(); // Récupérer le nom d'utilisateur depuis le champ de texte
        String password = su_password.getText();
        String telehone = su_telephone.getText();// Récupérer le mot de passe depuis le champ de texte

        // Check if any of the fields are empty
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || telehone.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("All fields must be filled!");
            alert.showAndWait();

            // Change the border color of the text fields to red
            if (email.isEmpty()) {
                su_email.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
            }
            if (username.isEmpty()) {
                su_username.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
            }
            if (password.isEmpty()) {
                su_password.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
            }
            if (telehone.isEmpty()) {
                su_telephone.setStyle("-fx-border-color: red ; -fx-border-width: 1px ;");
            }
            return;
        } else if (!email.contains("@") || !email.contains(".")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Invalid email format!");
            alert.showAndWait();
            return;
        } else if (password.length() < 8) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Password must be at least 8 characters long!");
            alert.showAndWait();
            return;
        } else if (telehone.length() < 8) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Telephone must be at least 8 characters long!");
            alert.showAndWait();
            return;
        } else {
            // Check if the email already exists
            if (serviceUser.emailExists(email)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Email Already Exists");
                alert.setHeaderText(null);
                alert.setContentText("Email already exists. Please use a different email.");
                alert.showAndWait();
            } else {
                Role[] defaultRole = new Role[]{Role.MEMBRE}; // Définissez le rôle par défaut ici
                User u = new User(username, email, password, telehone, defaultRole,false, null);
                // Appeler la méthode d'inscription avec les valeurs récupérées
                serviceUser.register(u);

                // vérification par email pour activer le compte de l'utilisateur après l'inscription
                Validation v = new Validation();
                Random rand = new Random();
                v.setCode(rand.nextInt(9999));
                v.setCreated_at(Instant.now());
                v.setExpired_at(Instant.now().plusSeconds(600));
                User u1 = serviceUser.findByEmail(email);
                v.setUser(u1);
                System.out.println(v);
                serviceValidation.ajouterValidation(v);
                String msg = "Your validation code is : " + v.getCode();
                String subj = "Validation Code";
                mailerService.sendMail(u.getEmail(), msg, subj);
                //redirect to verification code page
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/verifCode.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) su_signupBtn.getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                System.out.println("signup code envoiyer");

                // Create a hyperlink for the user to request account activation
//                Hyperlink activationLink = new Hyperlink();
//                activationLink.setText("Click here to request account activation");
//                activationLink.setOnAction(e -> {
                // Open the link in the user's default browser
//                    try {
//                        java.awt.Desktop.getDesktop().browse(new URI("http://www.yourwebsite.com/activation_request"));
//                    } catch (IOException | URISyntaxException e1) {
//                        e1.printStackTrace();
//                    }
                //              });

                // Create an alert dialog
//                Alert alert = new Alert(Alert.AlertType.INFORMATION);
//                alert.setTitle("Information Dialog");
//                alert.setHeaderText(null);
//                alert.setContentText("Account created successfully but is deactivated!");
//
//                // Add the hyperlink to the alert dialog
//                alert.getDialogPane().setContent(new VBox(new Label("Account created successfully but is deactivated!"), activationLink));
//
//                alert.showAndWait();

            }
        }

    }


    //*********************************************************************

    public void close() {
        javafx.application.Platform.exit();
    }

    public void signupSlider() {
        TranslateTransition slider1 = new TranslateTransition();
        slider1.setNode(sub_form);
        slider1.setToX(300);
        slider1.setDuration(Duration.seconds(0.5));
        slider1.play();

        slider1.setOnFinished((ActionEvent e) -> {
            edit_label.setText("Login Account");
            sub_signupBtn.setVisible(false);
            sub_loginBtn.setVisible(true);

        });
    }

    public void loginSlider() {
        TranslateTransition slider1 = new TranslateTransition();
        slider1.setNode(sub_form);
        slider1.setToX(0);
        slider1.setDuration(Duration.seconds(0.5));
        slider1.play();

        slider1.setOnFinished((ActionEvent e) -> {
            edit_label.setText("Create Account");
            sub_signupBtn.setVisible(true);
            sub_loginBtn.setVisible(false);
        });
    }

    public void restPwd(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/checkEmail.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) restPwd_btn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}