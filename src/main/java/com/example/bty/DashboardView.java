package com.example.bty;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DashboardView extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginView.class.getResource("/Accueil.fxml"));

        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("FitHub Pro");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();



        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        System.out.println("Largeur de l'écran : " + screenWidth);
        System.out.println("Hauteur de l'écran : " + screenHeight);
    }
}
