package com.example.bty.Controllers.ReclamationController;

import com.example.bty.Entities.Reclamation;
import com.example.bty.Services.ServiceReclamation;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.regex.Pattern;


public class AjouterReclamation extends Application {

    private ServiceReclamation reclamationService;
    private TextField tf_titre_reclamation;
    private TextField tf_description;

    private Label error_titre;
    private Label error_description;


    @Override
    public void start(Stage primaryStage) {
        reclamationService = new ServiceReclamation();

        AnchorPane root = new AnchorPane();
        root.getStyleClass().add("login_container");

        tf_titre_reclamation = new TextField();
        tf_titre_reclamation.setLayoutX(190);
        tf_titre_reclamation.setLayoutY(107);
        tf_titre_reclamation.setPrefHeight(32);
        tf_titre_reclamation.setPrefWidth(210);
        tf_titre_reclamation.setPromptText("titre");
        root.getChildren().add(tf_titre_reclamation);
        tf_titre_reclamation.getStyleClass().add("input-field");

        tf_description = new TextField();
        tf_description.setLayoutX(189);
        tf_description.setLayoutY(200);
        tf_description.setPrefHeight(103);
        tf_description.setPrefWidth(210);
        tf_description.setPromptText("description");
        root.getChildren().add(tf_description);
        tf_description.getStyleClass().add("input_field");

        Button btn_ajouter = new Button();
        btn_ajouter.setLayoutX(211);
        btn_ajouter.setLayoutY(347);
        btn_ajouter.setText("Ajouter Reclamation");
        btn_ajouter.setOnAction(this::AjouterReclamation);
        root.getChildren().add(btn_ajouter);
        btn_ajouter.getStyleClass().add("ajouter-button");

        Label lbl_title = new Label();
        lbl_title.setLayoutX(183);
        lbl_title.setLayoutY(49);
        lbl_title.setText("Remplir votre reclamation");
        root.getChildren().add(lbl_title);
        lbl_title.getStyleClass().add("label-titre");


        Label lbl_titre = new Label();
        lbl_titre.setLayoutX(111);
        lbl_titre.setLayoutY(112);
        lbl_titre.getStyleClass().add("label");
        lbl_titre.setText("Titre");
        root.getChildren().add(lbl_titre);
        lbl_titre.getStyleClass().add("label-titre");

        Label lbl_description = new Label();
        lbl_description.setLayoutX(89);
        lbl_description.setLayoutY(220);
        lbl_description.getStyleClass().add("label");
        lbl_description.setText("Description");
        root.getChildren().add(lbl_description);
        lbl_description.getStyleClass().add("label-titre");


        error_titre = new Label();
        error_titre.setLayoutX(232);
        error_titre.setLayoutY(151);
        error_titre.setText("Le titre est vide !!");
        error_titre.setTextFill(javafx.scene.paint.Color.web("#fc0505"));
        error_titre.setVisible(false); // Masquer le message d'erreur par défaut
        root.getChildren().add(error_titre);
        error_description = new Label();
        error_description.setLayoutX(198);
        error_description.setLayoutY(313);
        error_description.setText("La description est vide !!");
        error_description.setTextFill(javafx.scene.paint.Color.web("#fc0505"));
        error_description.setVisible(false); // Masquer le message d'erreur par défaut
        root.getChildren().add(error_description);



        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add(getClass().getResource("/Styles/AjouterReclamation.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Ajouter Reclamation");
        primaryStage.show();
    }
    //    private boolean validateTitre(String titre) {
//        if (titre != null && !titre.isEmpty()) {
//            error_titre.setVisible(false);
//            return true;
//        } else {
//            error_titre.setVisible(true);
//            return false;
//        }
//    }
    private void AjouterReclamation(ActionEvent event) {
        String titre = tf_titre_reclamation.getText();
        String description = tf_description.getText();

        if (titre.isEmpty() || description.isEmpty()) {
            if (titre.isEmpty()) {
                // Afficher le message d'erreur pour le champ de titre vide
                showErrorLabel("Le titre est vide !!");
            }
            if (description.isEmpty()) {
                // Afficher le message d'erreur pour le champ de description vide
                showErrorLabel("La description est vide !!");
            }
        } else {


            //if (validateTitre(titre) && validateDescription(description) && validateCaractere(titre)) {
            reclamationService.addReclamationv2(new Reclamation(titre, description));
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Reclamation ajoutée");
            alert.showAndWait();
            //}
        }
    }
    private void showErrorLabel(String errorMessage) {
        // Afficher le message d'erreur en dessous du champ correspondant
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
    //Label error_titre = new Label();


//    private boolean validateDescription(String description) {
//        if (description != null && !description.isEmpty()) {
//            return true;
//        } else {
//            return false;
//        }
//    }

//    private boolean validateCaractere(String titre) {
//        if (Pattern.matches("^[a-zA-Z0-9]+$", titre)) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    public static void main(String[] args) {
        launch(args);
    }
}