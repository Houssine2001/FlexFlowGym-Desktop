package com.example.bty.Controllers.graphiqueGCP;

import com.example.bty.Controllers.CourController.CourMembre;
import com.example.bty.Controllers.EvenementController.clientVitrine;
import com.example.bty.Controllers.ProduitController.VitrineClient;
import com.example.bty.Entities.User;
import com.example.bty.Utils.Session;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


public class InterfaceOffre extends Application {
    private ImageView backgroundImag;

    @Override
    public void start(Stage primaryStage) {


        Image image = null;
        try {
            image = new Image(new FileInputStream("C:\\Users\\hp\\Desktop\\pidev_Project\\FlexFlow\\src\\main\\resources\\images\\FlexFlow.png"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.backgroundImag = new ImageView(image);
        backgroundImag.setFitHeight(700);
        backgroundImag.setFitWidth(900);

        Button consulterButton = new Button("Consulter liste des offres");
        consulterButton.setOnAction(event -> {
            consulterOffres();
        });

//        //Création du titre
//      Label titleLabel = new Label("Clique sur cette bouton pour savoir nos offres ");
//    titleLabel.setStyle("-fx-font-size: 40px; -fx-font-weight: bold; -fx-padding: 50px 0  0  2px;");

        // Intégration du système de like/dislike
        VBox vbox = new VBox(10);
        createLikeDislikeStars(vbox);
        vbox.getChildren().addAll( consulterButton);
        vbox.setAlignment(Pos.CENTER); // Alignement du VBo
        // Création du bouton


// Ajout de la classe CSS pour le style
        consulterButton.getStyleClass().add("consulter-button");



        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(backgroundImag, vbox);
        StackPane.setAlignment(vbox, Pos.CENTER); // Alignement au centre

        Scene scene = new Scene(stackPane, 800, 650);


        scene.getStylesheets().add(getClass().getResource("/Styles/StyleAR.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }







    private void consulterOffres() {
        ConsultationOffre1 consultationOffre = new ConsultationOffre1();
        consultationOffre.start(new Stage());
    }

    // Méthode pour créer et afficher le système de like/dislike
    private void createLikeDislikeStars(VBox parentBox) {
        AtomicInteger starsSelected = new AtomicInteger();
        final int maxStars = 5;
        Label selectionLabel = new Label("Selection: " + starsSelected + "/" + maxStars);

        // Create star polygons
        Polygon[] stars = new Polygon[maxStars];
        for (int i = 0; i < maxStars; i++) {
            stars[i] = createStar();
            int index = i;
            stars[i].setOnMouseClicked(e -> {
                starsSelected.set(index + 1);
                updateSelectionLabel(selectionLabel, starsSelected.get(), maxStars);
                updateStarColors(stars, starsSelected.get(), maxStars);
            });
        }

        HBox starBox = new HBox(3); // Utilisation de HBox pour les étoiles
        starBox.getChildren().addAll(stars);
        starBox.setAlignment(Pos.CENTER_RIGHT); // Aligner à droite

        // Label pour indiquer "Combien vous aimez nos offres"
        Label likeLabel = new Label("Combien vous aimez nos offres:");

        // Ajout de la classe CSS pour le style
        likeLabel.getStyleClass().add("like-label");

        likeLabel.setTextFill(Color.BLUE); // Définit la couleur du texte en bleu

        // Création d'une boîte pour les étoiles et la section 0/5
        HBox starAndSelectionBox = new HBox(5);
        starAndSelectionBox.getChildren().addAll(starBox, selectionLabel);
        starAndSelectionBox.setAlignment(Pos.BOTTOM_RIGHT); // Aligner en bas à droite

        // Bouton pour soumettre la sélection
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            // Do something with the selected stars
            System.out.println("User selected " + starsSelected + " stars.");
            // Reset the selection
            starsSelected.set(0);
            updateSelectionLabel(selectionLabel, starsSelected.get(), maxStars);
            updateStarColors(stars, starsSelected.get(), maxStars);
        });

        // Création d'une boîte pour le bouton Submit et la section 0/5
        VBox submitBox = new VBox(5);
        submitBox.getChildren().addAll(likeLabel, starAndSelectionBox, submitButton);
        submitBox.setAlignment(Pos.BOTTOM_RIGHT); // Aligner en bas à droite

        parentBox.getChildren().addAll(submitBox); // Ajouter cette boîte à la boîte principale
        parentBox.setAlignment(Pos.CENTER); // Aligner au centre
    }

    // Method to create a star polygon
    private Polygon createStar() {
        Polygon star = new Polygon();
        star.getPoints().addAll(
                0.0, 0.0,
                5.0, 15.0,
                20.0, 20.0,
                10.0, 35.0,
                15.0, 50.0,
                0.0, 40.0,
                -15.0, 50.0,
                -10.0, 35.0,
                -20.0, 20.0,
                -5.0, 15.0
        );
        star.setFill(Color.GRAY);
        return star;
    }

    // Method to update the selection label
    private void updateSelectionLabel(Label selectionLabel, int starsSelected, int maxStars) {
        selectionLabel.setText("Selection: " + starsSelected + "/" + maxStars);
    }

    // Method to update the colors of the stars based on the selection
    private void updateStarColors(Polygon[] stars, int selectedStars, int maxStars) {
        for (int i = 0; i < maxStars; i++) {
            if (i < selectedStars) {
                stars[i].setFill(Color.YELLOWGREEN); // Colored star
            } else {
                stars[i].setFill(Color.GRAY); // White star
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}