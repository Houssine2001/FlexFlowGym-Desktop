package com.example.bty.Controllers.EvenementController;



import com.example.bty.Entities.Evenement;


import com.example.bty.Entities.User;
import com.example.bty.Services.ServiceEvenement;
import com.example.bty.Utils.ConnexionDB;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;



public class DashbordEvenement {


    public Button ajout_evenement;
    public Button actuliser_Evenement;
    public Label dashboard_NM;
    @FXML
    private TableView<Evenement> tableView;
    @FXML
    private TableColumn<Evenement, Integer> idCol;

    @FXML
    private TableColumn<Evenement, String> nomCol;
    public TableColumn<Evenement, String> categorieCol;
    public TableColumn<Evenement, String> ObjectifCol;
    public TableColumn<Evenement, Integer> placeCol;
    public TableColumn<Evenement, Date> dateCol;
    public TableColumn<Evenement, Time> timeCol;
    public TableColumn<Evenement, Integer> idCoachCol;
    public TableColumn<Evenement, String> nomCoachCol;
    public TableColumn<Evenement, Boolean> Etat;



    @FXML
    private TableColumn<Evenement, Void> actionColumn;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_NC;
    // Autres déclarations
    private Connection connexion;
    private PreparedStatement pst;
    private Statement ste ;
    public DashbordEvenement() {
        connexion = ConnexionDB.getInstance().getConnexion();
            EvenementList = FXCollections.observableArrayList();
    }
    private ServiceEvenement ServiceEvenements = new ServiceEvenement();
    private ObservableList<Evenement> EvenementList ;

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        categorieCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));
        ObjectifCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObjectif()));
        dateCol.setCellValueFactory(cellData ->  new SimpleObjectProperty<>(cellData.getValue().getDate()));
        timeCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTime()));
        placeCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getNbre_place()).asObject());
        Etat.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isEtat()));
        idCoachCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCoach().getId()).asObject());
        nomCoachCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCoach().getName()));

        reloadEvenements(); // Charger initialement les données dans la TableView


        // Ajout de la colonne Actions
        ajout_evenement.setOnAction(event -> ouvrirFenetreAjoutEvenement());
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final ImageView modifierIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/bty/imagesModuleEvenement/edit.png")));
            private final ImageView supprimerIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/bty/imagesModuleEvenement/delete.png")));
            private final Button modifierButton = new Button("", modifierIcon);
            private final Button supprimerButton = new Button("", supprimerIcon);

            {
                modifierButton.setOnAction(event -> {
                    Evenement evenement = getTableView().getItems().get(getIndex());
                    updateCours(evenement);
                });

                supprimerButton.setOnAction(event -> {
                    Evenement evenement = getTableView().getItems().get(getIndex());
                    confirmerSuppression(evenement);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(modifierButton, supprimerButton);
                    buttons.setSpacing(5);
                    setGraphic(buttons);
                    Evenement evenement = getTableView().getItems().get(getIndex());

                }
            }
        });


    }

    @FXML
    private void ouvrirFenetreAjoutEvenement() {
        // Créer une nouvelle instance de la classe Ajoutproduit
        AjouterEvenementInterface ajoutevenement = new AjouterEvenementInterface();

        // Appeler la méthode start pour afficher la fenêtre d'ajout de produit
        ajoutevenement.start(new Stage());
    }



    @FXML
    private void reloadEvenements() {
        try {
            if (connexion.isClosed()) {
                connexion = ConnexionDB.getInstance().getConnexion(); // Réouvrir la connexion si elle est fermée
            }
            List<Evenement> newList = consulterEvenement();
            tableView.getItems().setAll(newList);
            EvenementList.clear();
            EvenementList.addAll(newList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Evenement> consulterEvenement() {
        List<Evenement> EvenementList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgym", "root", "")) {
            String query = "SELECT * FROM evenement";

            try (PreparedStatement statement = connection .prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Evenement E = new Evenement();

                E.setId(resultSet.getInt("id_evenement"));
                E.setNom(resultSet.getString("nomEvenement")+" ");
                E.setCategorie(resultSet.getString("categorie")+" ");
                E.setObjectif(resultSet.getString("Objectif")+"  ");
                E.setNbre_place(Integer.parseInt(resultSet.getInt("nbrPlace") + "".trim()));
//                E.setDate(Date.valueOf(resultSet.getDate("Date")+"  "));
                E.setDate(resultSet.getDate("Date"));

                E.setTime(resultSet.getTime("Time"));
                E.setEtat(resultSet.getBoolean("etat"));

                int coachId = resultSet.getInt("id_user");
                String coachName = null;
                try (Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgym", "root", "")) {

                    try (PreparedStatement preparedStatement = connection1.prepareStatement("SELECT nom FROM user WHERE id = ?")) {
                        preparedStatement.setInt(1, coachId);
                        try (ResultSet coachResultSet = preparedStatement.executeQuery()) {
                            if (coachResultSet.next()) {
                                coachName = coachResultSet.getString("nom");
                            }
                        }
                    }
                }
                User coach = new User();
                coach.setId(coachId);
                coach.setName(coachName);
                E.setCoach(coach);

                EvenementList.add(E);
            }
        } }catch (SQLException e) {
            e.printStackTrace();
        }
        return EvenementList;
    }


//    @FXML
//    private void actualiserTable() {
//        ObservableList<Evenement> evenement = FXCollections.observableArrayList(ServiceEvenements.consulterEvenements());
//        tableView.setItems(evenement);
//    }


    private void confirmerSuppression(Evenement evenement) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation de suppression");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce evenement ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            supprimerCours(evenement);
        }
    }
    private void supprimerCours(Evenement evenement) {
        try {
            // Vérifiez si la connexion est fermée et rouvrez-la si nécessaire
            if (connexion == null || connexion.isClosed()) {
                connexion = ConnexionDB.getInstance().getConnexion();
            }

            // Utilisez un bloc try-with-resources pour gérer les ressources JDBC
            try (PreparedStatement statement = connexion.prepareStatement("DELETE FROM evenement WHERE id_evenement = ?")) {
                statement.setInt(1, evenement.getId());
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    EvenementList.remove(evenement);
                } else {
                    // Gérer l'échec de la suppression
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("La suppression a échoué");
                    alert.setContentText("Impossible de supprimer l'evenement . Veuillez réessayer.");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private void updateCours(Evenement evenement) {
    // Création de la fenêtre de modification
    Stage modificationStage = new Stage();
    modificationStage.setTitle("Modifier le cours");

    TextField nomField = new TextField(evenement.getNom());
    ComboBox<String> CategorieComboBox = new ComboBox<>();
    CategorieComboBox.getItems().addAll("Fitness ", "Cycling ","Powerlifting","Gymnastics","Cardio");
    CategorieComboBox.setValue(evenement.getCategorie());
    ComboBox<String> ObjectiFComboBox = new ComboBox<>();
    ObjectiFComboBox.getItems().addAll("Compétition amicale ", "Gain musculaire ","Perdre du poid","Renforcement de l'esprit d'équipe ");
    ObjectiFComboBox.setValue(evenement.getObjectif());
    TextField nbrPlaceField = new TextField(String.valueOf(evenement.getNbre_place()));
    DatePicker DateField = new DatePicker(evenement.getDate().toLocalDate());
    TextField TimeField = new TextField(evenement.getTime().toString());
    CheckBox etatCheckBox = new CheckBox("Actif");
    etatCheckBox.setSelected(evenement.isEtat());
    TextField idCoachField = new TextField(String.valueOf(evenement.getCoach().getId()));

    // Bouton pour enregistrer les modifications
    Button saveButton = new Button("Enregistrer");
    saveButton.setOnAction(event -> {
        String nomEvenement = nomField.getText();
        String categorie = CategorieComboBox.getValue();
        String objectif = ObjectiFComboBox.getValue();
        int nbrePlace = Integer.parseInt(nbrPlaceField.getText());
        LocalDate date = DateField.getValue();
        Date d = date != null ? Date.valueOf(date) : null;
        String time = TimeField.getText();
        String idCoachText = idCoachField.getText();

        // Vérification si les champs sont vides
        if (nomEvenement.isEmpty()  || time.isEmpty() || idCoachText.isEmpty() ) {
            afficherMessage("Erreur", "Veuillez remplir tous les champs avant de sauvegarder.");

            // Mettre en rouge les bordures des champs vides
            if (nomEvenement.isEmpty()) {
                nomField.setStyle("-fx-border-color: red;");
            } else {
                nomField.setStyle(""); // Remettre le style par défaut si le champ n'est pas vide
            }

            if (time.isEmpty()) {
                TimeField.setStyle("-fx-border-color: red;");
            } else {
                TimeField.setStyle(""); // Remettre le style par défaut si le champ n'est pas vide
            }


            if (idCoachText.isEmpty()) {
                idCoachField.setStyle("-fx-border-color: red;");
            } else {
                idCoachField.setStyle(""); // Remettre le style par défaut si le champ n'est pas vide
            }

            return; // Sortie de la méthode si un champ est vide
        }


        // Vérification si le nom de cours contient des chiffres
        if (nomEvenement.matches(".*\\d.*")) {
            afficherMessage("Erreur", "Le nom de l'evenement ne doit pas contenir de chiffres.");
            nomField.setStyle("-fx-border-color: red;"); // Mettre en rouge en cas de condition non respectée
            return; // Sortie de la méthode si la condition n'est pas respectée
        } else {
            nomField.setStyle("-fx-text-inner-color: black;"); // Remettre en noir
        }
        // Ajout de la validation pour la capacité
        if (nbrePlace < 10 || nbrePlace > 40) {
            afficherMessage("Erreur", "La nombre de place  doit être comprise entre 10 et 30.");
            nbrPlaceField.setStyle("-fx-border-color: red;"); // Mettre en rouge en cas de condition non respectée
            return; // Sortie de la méthode si la condition n'est pas respectée
        } else {
            nbrPlaceField.setStyle("-fx-text-inner-color: black;"); // Remettre en noir si la condition est respectée
        }
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
        // Code pour sauvegarder les modifications dans la base de données
        try (PreparedStatement statement = connexion.prepareStatement(
                "UPDATE evenement SET nomEvenement = ?, categorie = ?, Objectif = ?, nbrPlace = ?, Date = ?, Time = ?,etat = ? ,id_user = ?  WHERE id_evenement  = ?")) {

            statement.setString(1, nomField.getText());
            statement.setString(2, CategorieComboBox.getValue());
            statement.setString(3, ObjectiFComboBox.getValue());
            statement.setString(4, nbrPlaceField.getText());
            statement.setDate(5, Date.valueOf(DateField.getValue()));
            statement.setString(6, TimeField.getText());
            statement.setBoolean(7, etatCheckBox.isSelected());
            statement.setInt(8, Integer.parseInt(idCoachField.getText()));
            statement.setInt(9, evenement.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                // Mise à jour réussie
                evenement.setNom(nomField.getText());
                evenement.setCategorie(CategorieComboBox.getValue());
                evenement.setObjectif(ObjectiFComboBox.getValue());
                evenement.setNbre_place(Integer.parseInt(nbrPlaceField.getText()));
                evenement.setDate(Date.valueOf(DateField.getValue()));
                evenement.setTime(Time.valueOf(TimeField.getText()));
                evenement.setEtat(etatCheckBox.isSelected());

                evenement.getCoach().setId(Integer.parseInt(idCoachField.getText()));
                tableView.refresh();
                reloadEvenements(); // Recharger les données des cours pour mettre à jour la TableView
            } else {
                // Gérer l'échec de la mise à jour
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("La mise à jour a échoué");
                alert.setContentText("Impossible de mettre à jour l'evenement. Veuillez réessayer.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        modificationStage.close(); // Fermeture de la fenêtre de modification après la mise à jour
    });

    // Ajout de la validation pour le champ de nom de cours
    nomField.textProperty().addListener((observable, oldValue, newValue) -> {
        if (newValue.matches(".*\\d.*")) { // Vérifie s'il y a des chiffres dans la nouvelle valeur
            nomField.setStyle("-fx-text-inner-color: red;"); // Change la couleur du texte en rouge
        } else {
            nomField.setStyle("-fx-text-inner-color: black;"); // Remet la couleur du texte en noir si aucun chiffre n'est présent
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
    nbrPlaceField.textProperty().addListener((observable, oldValue, newValue) -> {
        // Vérifie si la nouvelle valeur ne contient que des chiffres
        if (!newValue.matches("\\d*")) {
            // Changement de la couleur du texte en noir si la condition n'est pas respectée
            nbrPlaceField.setStyle("-fx-text-inner-color: black;");
        } else {
            try {
                int dureeValue = Integer.parseInt(newValue.trim());
                // Vérifie si la durée est en dehors de la plage valide
                if (dureeValue < 10 || dureeValue > 40) {
                    // Changement de la couleur du texte en rouge si la condition n'est pas respectée
                    nbrPlaceField.setStyle("-fx-text-inner-color: red;");
                } else {
                    // Remet la couleur du texte en noir si la condition est respectée
                    nbrPlaceField.setStyle("-fx-text-inner-color: black;");
                }
            } catch (NumberFormatException e) {
                // Changement de la couleur du texte en rouge si la conversion échoue
                nbrPlaceField.setStyle("-fx-text-inner-color: red;");
            }
        }
    });


    // Mise en page de la fenêtre de modification
    VBox layout = new VBox(10);
    layout.setPadding(new Insets(10));
    layout.getChildren().addAll(
            new Label("Nom d'evenement :"), nomField,
            new Label("Catégorie  :"), CategorieComboBox,
            new Label("Objectif :"), ObjectiFComboBox,
            new Label("nombre de Place :"), nbrPlaceField,
            new Label("Date :"), DateField,
            new Label("Time :"), TimeField,
            new Label("État  :"), etatCheckBox,
            new Label("ID du coach :"), idCoachField,
            saveButton
    );

    // Affichage de la fenêtre de modification
    Scene scene = new Scene(layout, 400, 500);
    modificationStage.setScene(scene);
    modificationStage.show();
}

    private void afficherMessage(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    private boolean showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        ButtonType yesButton = new ButtonType("Oui");
        ButtonType noButton = new ButtonType("Non");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }



}
