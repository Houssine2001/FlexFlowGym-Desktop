package com.example.bty.Controllers.EvenementController;

import com.example.bty.Entities.Evenement;
import com.example.bty.Entities.User;
import com.example.bty.Utils.ConnexionDB;
import javafx.scene.control.Button;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

//import javax.swing.text.html.StyleSheet;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.chrono.Chronology;
import java.time.format.DateTimeParseException;

public class AjouterEvenementInterface extends Application {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/pidevgym";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static Connection connexion;
    private PreparedStatement pde;

    private Button uploadButton;
    private ImageView uploadedImageView;
    private File selectedImage;

    public AjouterEvenementInterface() {
        connexion = ConnexionDB.getInstance().getConnexion();
        uploadedImageView = new ImageView(); // Initialisation de uploadedImageView
        uploadButton = new Button("Upload Image"); // Initialisation de uploadButton
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Formulaire d'ajout d'un evenement'");

            VBox cardContainer = new VBox();
            cardContainer.getStyleClass().add("card-container");
            cardContainer.setPadding(new Insets(20));
            cardContainer.setSpacing(10);

            Label cardTitle = new Label("Ajouter un Evenement");
            cardTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #DDE2C6; -fx-font-family: 'Arial', sans-serif;");




            GridPane grid = new GridPane();
            grid.setPadding(new Insets(20));
            grid.setVgap(10);
            grid.setHgap(10);
            grid.setStyle("-fx-background-color: #DDE2C6; -fx-padding: 25; -fx-border-radius: 10; -fx-background-radius: 10; -fx-alignment: center;");






            TextField nomField = new TextField();
            ComboBox<String> CategorieComboBox = new ComboBox<>();
            CategorieComboBox.getItems().addAll("Fitness ", "Cycling ","Powerlifting","Gymnastics","Cardio");
            CategorieComboBox.setValue("Fitness");
            ComboBox<String> ObjectiFComboBox = new ComboBox<>();
            ObjectiFComboBox.getItems().addAll("Compétition amicale ", "Gain musculaire ","Perdre du poid","Renforcement de l'esprit d'équipe ");
            ObjectiFComboBox.setValue("Perdre du poid");
            TextField NbrPlaceField = new TextField();
            DatePicker DateField = new DatePicker();
            TextField TimeField = new TextField();
            TextField nomCoachField = new TextField();
            CheckBox etatCheckBox = new CheckBox("Actif");

            Button addButton = new Button("Ajouter");
            addButton.getStyleClass().add("envoyer-button");


            addButton.setOnAction(event -> {
                String nomEvenement = nomField.getText();
                String categorie = CategorieComboBox.getValue();
                String objectif = ObjectiFComboBox.getValue();
                int nbrePlace = Integer.parseInt(NbrPlaceField.getText());
                LocalDate date = DateField.getValue();
                Date d = date != null ? Date.valueOf(date) : null;
                String time = TimeField.getText();
// Vérification du format de l'heure
                if (!time.matches("\\d{2}:\\d{2}:\\d{2}")) {
                    afficherMessage("Erreur", "Format d'heure invalide. Utilisez le format HH:MM:SS.");

                    return; // Sortie de la méthode si le format n'est pas respecté
                }
                // Division de la chaîne de temps en ses composants
                String[] timeComponents = time.split(":");
                int hours = Integer.parseInt(timeComponents[0]);
                int minutes = Integer.parseInt(timeComponents[1]);
                int seconds = Integer.parseInt(timeComponents[2]);

                // Vérification des heures, minutes et secondes dans des intervalles logiques
                if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59 || seconds < 0 || seconds > 59) {
                    afficherMessage("Erreur", "Heure invalide.");
                    return; // Sortie de la méthode si l'heure est invalide
                }


                Time t = Time.valueOf(time);
                String nomCoach = nomCoachField.getText();
                int idCoach = getCoachIdByName(nomCoach);
                // Vérification si le nom de cours contient des chiffres
                if (nomEvenement.matches(".*\\d.*")) {
                    afficherMessage("Erreur", "Le nom du cours ne doit pas contenir de chiffres.");
                    nomField.setStyle("-fx-text-inner-color: red;"); // Mettre en rouge en cas de condition non respectée
                    return; // Sortie de la méthode si la condition n'est pas respectée
                } else {
                    nomField.setStyle("-fx-text-inner-color: black;"); // Remettre en noir
                }
                if (nomCoach.matches(".*\\d.*")) {
                    afficherMessage("Erreur", "Le nom du coach ne doit pas contenir de chiffres.");
                    nomCoachField.setStyle("-fx-text-inner-color: red;"); // Mettre en rouge en cas de condition non respectée
                    return; // Sortie de la méthode si la condition n'est pas respectée
                } else {
                    nomCoachField.setStyle("-fx-text-inner-color: black;"); // Remettre en noir
                }
                // Ajout de la validation pour la capacité
                if (nbrePlace < 10 || nbrePlace > 40) {
                    afficherMessage("Erreur", "La capacité doit être comprise entre 10 et 30.");
                    NbrPlaceField.setStyle("-fx-text-inner-color: red;"); // Mettre en rouge en cas de condition non respectée
                    return; // Sortie de la méthode si la condition n'est pas respectée
                } else {
                    NbrPlaceField.setStyle("-fx-text-inner-color: black;"); // Remettre en noir si la condition est respectée
                }
//                 //  Ajout de la validation pour le champ de saisie de l'heure
//                TimeField.textProperty().addListener((observable, oldValue, newValue) -> {
//                    if (!newValue.matches("\\d{2}:\\d{2}:\\d{2}")) { // Vérifie le format HH:MM:SS
//                        TimeField.setStyle("-fx-text-fill: red; -fx-control-inner-background: #ffffff; -fx-text-inner-color: red;"); // Mettre en rouge si le format est incorrect
//                    } else {
//                        TimeField.setStyle("-fx-text-fill: black; -fx-control-inner-background: #ffffff; -fx-text-inner-color: black;"); // Remettre en noir si le format est correct
//                    }
//                });
                User coach = new User();
                coach.setId(idCoach);
                boolean etat = etatCheckBox.isSelected();

                Evenement evenement = new Evenement(nomEvenement, categorie, objectif, nbrePlace, d, t, coach, etat,readImageFile(selectedImage)
                );

                if (ajouterEvenement(evenement, selectedImage)) {
                    afficherMessage("Succès", "L'événement a été ajouté avec succès.");
                    nomField.clear();
                    CategorieComboBox.setValue(null);
                    ObjectiFComboBox.setValue(null);
                    NbrPlaceField.clear();
                    DateField.setValue(null);
                    TimeField.clear();
                    nomCoachField.clear();
                    etatCheckBox.setSelected(false);


                } else {
                    afficherMessage("Échec", "L'ajout de l'événement a échoué.");
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

            HBox uploadBox = new HBox(10, uploadButton, uploadedImageView);
            grid.add(uploadBox, 0, 8);

            grid.add(new Label("Nom du l'événement:"), 0, 0);
            grid.add(nomField, 1, 0);
            grid.add(new Label("Catégorie:"), 0, 1);
            grid.add(CategorieComboBox, 1, 1);
            grid.add(new Label("Objectif:"), 0, 2);
            grid.add(ObjectiFComboBox, 1, 2);
            grid.add(new Label("Nombre de Place:"), 0, 3);
            grid.add(NbrPlaceField, 1, 3);
            grid.add(new Label("Date:"), 0, 4);
            grid.add(DateField, 1, 4);
            grid.add(new Label("Time:"), 0, 5);
            grid.add(TimeField, 1, 5);
            grid.add(new Label("Nom du coach:"), 0, 6);
            grid.add(nomCoachField, 1, 6);
            grid.add(new Label("État:"), 0, 7);
            grid.add(etatCheckBox, 1, 7);
            grid.add(addButton, 0, 9, 2, 1);
            // Ajout de la validation pour le champ de nom de cours
            nomField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.matches(".*\\d.*")) { // Vérifie s'il y a des chiffres dans la nouvelle valeur
                    nomField.setStyle("-fx-text-inner-color: red;"); // Change la couleur du texte en rouge
                } else {
                    nomField.setStyle("-fx-text-inner-color: black;"); // Remet la couleur du texte en noir si aucun chiffre n'est présent
                }
            });
            nomCoachField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.matches(".*\\d.*")) { // Vérifie s'il y a des chiffres dans la nouvelle valeur
                    nomCoachField.setStyle("-fx-text-inner-color: red;"); // Change la couleur du texte en rouge
                } else {
                    nomCoachField.setStyle("-fx-text-inner-color: black;"); // Remet la couleur du texte en noir si aucun chiffre n'est présent
                }
            });
            TimeField.textProperty().addListener((observable, oldValue, newValue) ->{
                if (!newValue.matches("\\d{2}:\\d{2}:\\d{2}")){

                    TimeField.setStyle("-fx-text-inner-color: red;");
                }
                else{
                    try {
                        LocalTime time = LocalTime.parse(newValue);
                        TimeField.setStyle("-fx-text-inner-color: black;");
                    }
                    catch (DateTimeParseException e) {
                        TimeField.setStyle("-fx-text-inner-color: red;"); // Change la couleur du texte en rouge si la conversion échoue
                    }
                }
            });
            // Ajout de l'écouteur de changement de texte sur le champ de durée
            NbrPlaceField.textProperty().addListener((observable, oldValue, newValue) -> {
                // Vérifie si la nouvelle valeur ne contient que des chiffres
                if (!newValue.matches("\\d*")) {
                    // Changement de la couleur du texte en noir si la condition n'est pas respectée
                    NbrPlaceField.setStyle("-fx-text-inner-color: black;");
                } else {
                    try {
                        int dureeValue = Integer.parseInt(newValue.trim());
                        // Vérifie si la durée est en dehors de la plage valide
                        if (dureeValue < 10 || dureeValue > 40) {
                            // Changement de la couleur du texte en rouge si la condition n'est pas respectée
                            NbrPlaceField.setStyle("-fx-text-inner-color: red;");
                        } else {
                            // Remet la couleur du texte en noir si la condition est respectée
                            NbrPlaceField.setStyle("-fx-text-inner-color: black;");
                        }
                    } catch (NumberFormatException e) {
                        // Changement de la couleur du texte en rouge si la conversion échoue
                        NbrPlaceField.setStyle("-fx-text-inner-color: red;");
                    }
                }
            });
            cardContainer.getChildren().addAll(cardTitle, grid);

            Scene scene = new Scene(cardContainer, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/example/bty/CSSmoduleEvenement/style.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Boolean ajouterEvenement(Evenement e, File imageFile) {
        String req = "INSERT INTO evenement (nomEvenement, categorie, objectif, nbrplace, Date, Time, id_user, etat, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            FileInputStream fis = new FileInputStream(imageFile);

            pde = connexion.prepareStatement(req);
            pde.setString(1, e.getNom());
            pde.setString(2, e.getCategorie());
            pde.setString(3, e.getObjectif());
            pde.setInt(4, e.getNbre_place());
            pde.setDate(5, e.getDate());
            pde.setTime(6, e.getTime());
            pde.setInt(7, e.getCoach().getId());
            pde.setBoolean(8, e.isEtat());
            pde.setBinaryStream(9, fis, (int) imageFile.length());

            pde.executeUpdate();
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
        int coachId = -1;
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
