package com.example.bty;

import com.example.bty.Entities.Role;
import com.example.bty.Entities.User;
import com.example.bty.Services.ServiceUser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginView extends Application {
    private double x = 0;
    private double y = 0;

    @Override
    public void start(Stage stage) throws Exception {
     //   FXMLLoader fxmlLoader = new FXMLLoader(LoginView.class.getResource("/LoginGym.fxml"));
           FXMLLoader fxmlLoader = new FXMLLoader(LoginView.class.getResource("/LoginGym.fxml"));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("FitHub Pro");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

//        root.setOnMousePressed(event -> {
//            x = event.getSceneX();
//            y = event.getSceneY();
//        });
//
//        root.setOnMouseDragged((MouseEvent event) ->{
//            stage.setX(event.getScreenX() - x);
//            stage.setY(event.getScreenY() - y);
//
//            stage.setOpacity(.8);
//        });
//
//        root.setOnMouseReleased((MouseEvent event) ->{
//            stage.setOpacity(1);
//        });
//
//        stage.initStyle(StageStyle.TRANSPARENT);


        // Charger votre fichier CSS si nécessaire
        // scene.getStylesheets().add(getClass().getResource("votreCheminVersCSS.css").toExternalForm());


    }

    public static void main(String[] args) {
        launch(args);
    }

    // Méthode appelée lorsqu'un utilisateur tente de se connecter
//    public static void login(String email, String password) {
//        ServiceUser userService = new ServiceUser();
//        int loginStatus = userService.Authentification(email, password);
//
//        if (loginStatus == 1) {
//            // Connexion réussie, vous pouvez ouvrir une nouvelle fenêtre, etc.
//            System.out.println("Login successful!");
//        } else if (loginStatus == 2) {
//            // Compte bloqué, afficher un message approprié, par exemple, avec une alerte
//            System.out.println("Login failed. Account is blocked.");
//        } else {
//            // Afficher un message d'erreur, par exemple, avec une alerte
//            System.out.println("Login failed. Invalid credentials.");
//        }
//    }

    // Méthode appelée lorsqu'un utilisateur tente de s'inscrire
//   public static void signup(String email, String username, String password) {
//        ServiceUser userService = new ServiceUser();
//
//        // Vérifier si l'email existe déjà
//        if (userService.emailExists(email)) {
//            // Afficher un message d'erreur, par exemple, avec une alerte
//            System.out.println("Signup failed. Email already exists.");
//        } else {
//            // L'email n'existe pas, vous pouvez procéder à l'inscription
//            Role defaultRole = Role.MEMBRE; // Définissez le rôle par défaut ici
//            User newUser = new User(username, email, password, "", defaultRole);
//
//            userService.register(newUser);
//
//            // Inscription réussie, vous pouvez afficher un message de succès, etc.
//            System.out.println("Signup successful!");
//        }
//    }
}
