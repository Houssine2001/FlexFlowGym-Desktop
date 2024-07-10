package com.example.bty.Controllers.CourController;

import com.example.bty.Entities.Cours;
import com.example.bty.Entities.User;
import com.example.bty.Utils.ConnexionDB;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// Import des classes supplémentaires
import javafx.scene.paint.Color;

//import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Ajouter extends Application {
    private static Connection connexion;
    private PreparedStatement pst;

    private Cours cours;

    private Button uploadButton;
    private ImageView uploadedImageView;
    private File selectedImage;

    public Ajouter() {
        connexion = ConnexionDB.getInstance().getConnexion();
        uploadedImageView = new ImageView(); // Initialisation de uploadedImageView
        uploadButton = new Button("Upload Image"); // Initialisation de uploadButton
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Formulaire d'ajout d'un cours");

        // Card Container
        VBox cardContainer = new VBox();
        cardContainer.getStyleClass().add("card-container");
        cardContainer.setPadding(new Insets(20));
        cardContainer.setSpacing(10);

        // Ajout du ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(cardContainer);
        scrollPane.setFitToWidth(true); // Ajuster la largeur du ScrollPane

        // Card Title
        Label cardTitle = new Label("Ajouter un cours");
        cardTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #DDE2C6; -fx-font-family: 'Arial', sans-serif;");

        // Form Grid
        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(15);
        grid.setStyle("-fx-background-color: #DDE2C6; -fx-padding: 25; -fx-border-radius: 10; -fx-background-radius: 10; -fx-alignment: center;");


        // Labels and ComboBoxes
        ComboBox<String> categorieComboBox = new ComboBox<>();
        categorieComboBox.getItems().addAll("Aquatique", "Cardio", "Force", "Danse", "Kids Island");

        ComboBox<String> objectifComboBox = new ComboBox<>();
        objectifComboBox.getItems().addAll("Perdre du poids", "Se défouler", "Se musculer", "S'entrainer en dansant");
        // Labels and TextFields
        TextField nomField = new TextField();
        TextField dureeField = new TextField();
        ComboBox<String> cibleComboBox = new ComboBox<>();
        cibleComboBox.getItems().addAll("Enfant", "Adulte");
        ComboBox<String> intensiteComboBox = new ComboBox<>();
        intensiteComboBox.getItems().addAll("Forte", "Moyenne", "Faible");
        TextField categorieField = new TextField();
        TextField objectifField = new TextField();
        CheckBox etatCheckBox = new CheckBox("Actif");
        TextField capaciteField = new TextField();
        TextField nomCoachField = new TextField(); // Champ pour le nom du coach

        Button addButton = new Button("Ajouter");
        addButton.getStyleClass().add("envoyer-button");
        addButton.setOnAction(event -> {
            cours = new Cours();
            // Récupérer les données du formulaire
            String nom = nomField.getText();
            String duree = dureeField.getText();
            String cible = cibleComboBox.getValue();
            String intensite = intensiteComboBox.getValue();
            String categorie = categorieComboBox.getValue();
            String objectif = objectifComboBox.getValue();
            boolean etat = etatCheckBox.isSelected();
            int capacite = Integer.parseInt(capaciteField.getText());
            String nomCoach = nomCoachField.getText(); // Récupérer le nom du coach depuis le champ
            int idCoach = getCoachIdByName(nomCoach); // Récupérer l'ID du coach


            String nomText = nomField.getText();

            // Vérifier si le nom du cours est unique
            if (!isNomCoursUnique(nomText, cours.getId())) {
                afficherMessage("Erreur", "Ce cour existe déjà !");
                return; // Sortir de la méthode si le nom n'est pas unique
            }

            String dureeText = dureeField.getText();
            String capaciteText = capaciteField.getText();
            String idCoachText = nomCoachField.getText();
            // Vérification si les champs sont vides
            if (nomText.isEmpty() || dureeText.isEmpty() || capaciteText.isEmpty() || idCoachText.isEmpty()) {
                afficherMessage("Erreur", "Veuillez remplir tous les champs avant de sauvegarder.");

                // Mettre en rouge les bordures des champs vides
                if (nomText.isEmpty()) {
                    nomField.setStyle("-fx-border-color: red;");
                } else {
                    nomField.setStyle(""); // Remettre le style par défaut si le champ n'est pas vide
                }

                if (dureeText.isEmpty()) {
                    dureeField.setStyle("-fx-border-color: red;");
                } else {
                    dureeField.setStyle(""); // Remettre le style par défaut si le champ n'est pas vide
                }

                if (capaciteText.isEmpty()) {
                    capaciteField.setStyle("-fx-border-color: red;");
                } else {
                    capaciteField.setStyle(""); // Remettre le style par défaut si le champ n'est pas vide
                }

                if (idCoachText.isEmpty()) {
                    nomCoachField.setStyle("-fx-border-color: red;");
                } else {
                    nomCoachField.setStyle(""); // Remettre le style par défaut si le champ n'est pas vide
                }

                return; // Sortie de la méthode si un champ est vide
            }



            // Ajout de la validation pour la durée
            int dureeValue = Integer.parseInt(duree);
            if (dureeValue < 30 || dureeValue > 75) {
                afficherMessage("Erreur", "La durée doit être comprise entre 30 et 75 minutes.");
                dureeField.setStyle("-fx-text-inner-color: red;"); // Mettre en rouge en cas de condition non respectée
                return; // Sortie de la méthode si la condition n'est pas respectée
            } else {
                dureeField.setStyle("-fx-text-inner-color: black;"); // Remettre en noir si la condition est respectée
            }

            // Ajout de la validation pour la capacité
            if (capacite < 10 || capacite > 30) {
                afficherMessage("Erreur", "La capacité doit être comprise entre 10 et 30.");
                capaciteField.setStyle("-fx-text-inner-color: red;"); // Mettre en rouge en cas de condition non respectée
                return; // Sortie de la méthode si la condition n'est pas respectée
            } else {
                capaciteField.setStyle("-fx-text-inner-color: black;"); // Remettre en noir si la condition est respectée
            }

            // Vérification si le nom de cours contient des chiffres
            if (nom.matches(".*\\d.*")) {
                afficherMessage("Erreur", "Le nom du cours ne doit pas contenir de chiffres.");
                nomField.setStyle("-fx-text-inner-color: red;"); // Mettre en rouge en cas de condition non respectée
                return; // Sortie de la méthode si la condition n'est pas respectée
            } else {
                nomField.setStyle("-fx-text-inner-color: black;"); // Remettre en noir si la condition est respectée
            }




            // Créer un objet User pour le coach
            User coach = new User();
            coach.setId(idCoach);

            // Créer un objet Cours
            Cours cours = new Cours(nom, duree, intensite, cible, categorie, objectif, etat, capacite, coach, readImageFile(selectedImage));

            // Appeler la méthode addPst avec l'objet Cours
            if (addPst(cours, selectedImage)) {
                afficherMessage("Succès", "Le cour a été ajouté avec succès.");
                // Effacer les champs du formulaire
                nomField.clear();
                dureeField.clear();
                cibleComboBox.setValue(null);
                intensiteComboBox.setValue(null);
                categorieComboBox.setValue(null);
                objectifComboBox.setValue(null);
                etatCheckBox.setSelected(false);
                capaciteField.clear();
                nomCoachField.clear();

            } else {
                afficherMessage("Échec", "L'ajout du cour a échoué.");
            }
        });

        uploadButton.getStyleClass().add("upload-button");
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner une image");
            selectedImage = fileChooser.showOpenDialog(primaryStage);

            if (selectedImage != null) {
                try {
                    FileInputStream input = new FileInputStream(selectedImage);
                    Image image = new Image(input);
                    uploadedImageView.setImage(image);  // Mettez à jour l'ImageView
                    uploadedImageView.setFitWidth(100);
                    uploadedImageView.setFitHeight(100);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        HBox uploadBox = new HBox(11, uploadButton, uploadedImageView);
        grid.add(uploadBox, 0, 9);

        grid.add(new Label("Nom du cours:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Durée:"), 0, 1);
        grid.add(dureeField, 1, 1);
        grid.add(new Label("Cible:"), 0, 2);
        grid.add(cibleComboBox, 1, 2);
        grid.add(new Label("Intensité:"), 0, 3);
        grid.add(intensiteComboBox, 1, 3);
        grid.add(new Label("Catégorie:"), 0, 4);
        grid.add(categorieComboBox, 1, 4);
        grid.add(new Label("Objectif:"), 0, 5);
        grid.add(objectifComboBox, 1, 5);
        grid.add(new Label("État:"), 0, 6);
        grid.add(etatCheckBox, 1, 6);
        grid.add(new Label("Capacité:"), 0, 7);
        grid.add(capaciteField, 1, 7);
        grid.add(new Label("Nom du coach:"), 0, 8); // Label pour le champ du nom du coach
        grid.add(nomCoachField, 1, 8); // Champ pour le nom du coach
        grid.add(new Label("Ajouter"), 0, 10); // Label pour le champ du nom du coach
        grid.add(addButton, 0, 10); // Champ pour le nom du coach



        // Ajout de la validation pour le champ de nom de cours
        nomField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches(".*\\d.*")) { // Vérifie s'il y a des chiffres dans la nouvelle valeur
                nomField.setStyle("-fx-text-inner-color: red;"); // Change la couleur du texte en rouge
            } else {
                nomField.setStyle("-fx-text-inner-color: black;"); // Remet la couleur du texte en noir si aucun chiffre n'est présent
            }
        });


        capaciteField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Vérifie si la nouvelle valeur ne contient que des chiffres
                capaciteField.setStyle("-fx-text-inner-color: black;"); // Change la couleur du texte en rouge
            } else {
                try {
                    int capaciteValue = Integer.parseInt(newValue); // Convertit la nouvelle valeur en entier
                    if (capaciteValue < 10 || capaciteValue > 30) { // Vérifie si la capacité est en dehors de la plage valide
                        capaciteField.setStyle("-fx-text-inner-color: red;"); // Change la couleur du texte en rouge
                    } else {
                        capaciteField.setStyle("-fx-text-inner-color: black;"); // Remet la couleur du texte en noir si la condition est respectée
                    }
                } catch (NumberFormatException e) {
                    capaciteField.setStyle("-fx-text-inner-color: red;"); // Change la couleur du texte en rouge si la conversion échoue
                }
            }
        });

        dureeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Vérifie si la nouvelle valeur ne contient que des chiffres
                dureeField.setStyle("-fx-text-inner-color: black;"); // Change la couleur du texte en rouge
            } else {
                try {
                    int dureeValue = Integer.parseInt(newValue); // Convertit la nouvelle valeur en entier
                    if (dureeValue < 30 || dureeValue > 75) { // Vérifie si la capacité est en dehors de la plage valide
                        dureeField.setStyle("-fx-text-inner-color: red;"); // Change la couleur du texte en rouge
                    } else {
                        dureeField.setStyle("-fx-text-inner-color: black;"); // Remet la couleur du texte en noir si la condition est respectée
                    }
                } catch (NumberFormatException e) {
                    dureeField.setStyle("-fx-text-inner-color: red;"); // Change la couleur du texte en rouge si la conversion échoue
                }
            }
        });
        // Adding elements to the VBox (Card Container)
        cardContainer.getChildren().addAll(cardTitle, grid);



        Scene scene = new Scene(scrollPane,  800, 620);
        scene.getStylesheets().add(getClass().getResource("/com/example/bty/CSSmoduleCours/ajout.css").toExternalForm());
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private boolean isNomCoursUnique(String nomCours, int courseId) {
        try {
            // Préparez votre requête SQL pour vérifier l'unicité du nom du cours
            String query = "SELECT COUNT(*) AS count FROM cours WHERE nom_cour = ? AND id <> ?";
            PreparedStatement statement = connexion.prepareStatement(query);
            statement.setString(1, nomCours);
            statement.setInt(2, courseId); // Exclure le cours actuel de la vérification
            ResultSet resultSet = statement.executeQuery();

            // Récupérer le résultat de la requête
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count == 0; // Retourne vrai si le nombre de cours avec ce nom est 0 (donc unique)
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // En cas d'erreur, retournez faux par défaut
    }

    public boolean addPst(Cours c, File imageFile){
        String requete="insert into cours (nom_cour,duree,intensite,cible,categorie,objectif,etat,capacite,user_id,image) values(?,?,?,?,?,?,?,?,?,?)";

        try {
            FileInputStream fis = new FileInputStream(imageFile);
            pst=connexion.prepareStatement(requete);
            pst.setString(1,c.getNom());
            pst.setString(2,c.getDuree());
            pst.setString(3,c.getIntensite());
            pst.setString(4,c.getCible());
            pst.setString(5,c.getCategorie());
            pst.setString(6,c.getObjectif());
            pst.setBoolean(7,c.isEtat());
            pst.setInt(8,c.getCapacite());
            pst.setInt(9,c.getCoach().getId());
            pst.setBinaryStream(10, fis, (int) imageFile.length());
            pst.executeUpdate();
            return true;
        } catch (SQLException | FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private byte[] readImageFile(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void afficherMessage(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public int getCoachIdByName(String coachName) {
        int coachId = -1; // Valeur par défaut si le coach n'est pas trouvé
        String query = "SELECT id FROM user WHERE nom = ?";
        try (PreparedStatement preparedStatement = connexion.prepareStatement(query)) {
            preparedStatement.setString(1, coachName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    coachId = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coachId;
    }



}