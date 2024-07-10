package com.example.bty.Controllers.CourController;


import com.example.bty.Controllers.CourController.Ajouter;
import com.example.bty.Controllers.CourController.consulter;
import com.example.bty.Entities.Produit;
import com.example.bty.Entities.User;
import com.example.bty.Services.ServiceCours;
import com.example.bty.Entities.Cours;
import com.example.bty.Utils.ConnexionDB;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.paint.Color;


import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class DashboardCour {

    public Button ajout_produit;
    public Button reloadCours;
    public Label dashboard_NM;
    public TextField searchField;

    @FXML
    private TableView<Cours> tableView;

    @FXML
    private TableColumn<Cours, Integer> idCol;

    @FXML
    private TableColumn<  Cours, String> nomCol;

    @FXML
    private TableColumn<  Cours, String> dureeCol;
    @FXML
    private TableColumn<  Cours, String> intensiteCol;
    @FXML
    private TableColumn<  Cours, String> cibleCol;
    @FXML
    private TableColumn<  Cours, String> categorieCol;
    @FXML
    private TableColumn<  Cours, String> objectifCol;
    @FXML
    private TableColumn<  Cours, Boolean> etatCol;
    @FXML
    private TableColumn<  Cours, Integer> capaciteCol;
    @FXML
    private TableColumn<  Cours, Integer> coachCol;
    @FXML
    private TableColumn<  Cours, String> nomcoachCol;

    //private consulter reloadCours = new consulter();
    @FXML
    private TableColumn<Cours, Void> actionColumn;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_NC;
    // Autres déclarations
    private Connection connexion;
    private PreparedStatement pst;
    private Statement ste ;


    public DashboardCour() {
        connexion = ConnexionDB.getInstance().getConnexion();
        coursList = FXCollections.observableArrayList();
    }
    private ServiceCours produitServices = new ServiceCours();
    private ObservableList<Cours> coursList;

    @FXML

    private void initialize() {
        idCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        dureeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDuree()));
        intensiteCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIntensite()));
        cibleCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCible()));
        categorieCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategorie()));
        objectifCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getObjectif()));
        etatCol.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isEtat()));
        capaciteCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCapacite()).asObject());
        coachCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCoach().getId()).asObject());
        nomcoachCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCoach().getName()));

        reloadCours(); // Charger initialement les données dans la TableView

        ajout_produit.setOnAction(event -> ouvrirFenetreAjoutProduit());

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button modifierButton = new Button("", new ImageView(new Image(getClass().getResourceAsStream("/com/example/bty/imagesModuleCours/modifier.png"))));
            private final Button supprimerButton = new Button("", new ImageView(new Image(getClass().getResourceAsStream("/com/example/bty/imagesModuleCours/supprimer.png"))));

            {
                modifierButton.setOnAction(event -> {
                    Cours cours = getTableView().getItems().get(getIndex());
                    updateCours(cours);
                });

                supprimerButton.setOnAction(event -> {
                    Cours cours = getTableView().getItems().get(getIndex());
                    confirmerSuppression(cours);
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
                    Cours cours = getTableView().getItems().get(getIndex());
                    int dureeValue = Integer.parseInt(cours.getDuree().trim());
                    if (dureeValue < 30 || dureeValue > 75) {
                        setTextFill(Color.RED); // Définir la couleur du texte en rouge
                    } else {
                        setTextFill(Color.BLACK); // Définir la couleur du texte en noir
                    }
                }
            }
        });


        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Appeler une méthode de mise à jour de la TableView avec le nouveau texte de recherche
            updateTableView(newValue);
        });

        Platform.runLater(() -> {
            checkZeroCapacityCourses();
        });
    }



    private void checkZeroCapacityCourses() {
        for (Cours cours : coursList) {
            if (cours.getCapacite() == 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Capacité épuisée");
                alert.setHeaderText(null);
                alert.setContentText("La capacité du cours \"" + cours.getNom().trim() + "\" est maintenant épuisée.");
                alert.showAndWait();
            }
        }
    }

    // Création de la fenêtre de modification
    private void updateTableView(String searchText) {
        List<Cours> filteredCours = new ArrayList<>();
        for (Cours cours : coursList) {
            if (cours.getNom().toLowerCase().startsWith(searchText.toLowerCase())) {
                filteredCours.add(cours);
            }
        }
        tableView.getItems().setAll(filteredCours);
    }

    private void confirmerSuppression(Cours cours) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmation de suppression");
        alert.setContentText("Êtes-vous sûr de vouloir supprimer ce cours ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            supprimerCours(cours);
        }
    }

    private void supprimerCours(Cours cours) {
        try {
            // Vérifiez si la connexion est fermée et rouvrez-la si nécessaire
            if (connexion == null || connexion.isClosed()) {
                connexion = ConnexionDB.getInstance().getConnexion();
            }

            // Utilisez un bloc try-with-resources pour gérer les ressources JDBC
            try (PreparedStatement statement = connexion.prepareStatement("DELETE FROM cours WHERE id = ?")) {
                statement.setInt(1, cours.getId());
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    coursList.remove(cours);
                } else {
                    // Gérer l'échec de la suppression
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("La suppression a échoué");
                    alert.setContentText("Impossible de supprimer le cours. Veuillez réessayer.");
                    alert.showAndWait();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void ouvrirFenetreAjoutProduit() {
        // Créer une nouvelle instance de la classe Ajoutproduit
        Ajouter ajoutproduit = new Ajouter();

        // Appeler la méthode start pour afficher la fenêtre d'ajout de produit
        ajoutproduit.start(new Stage());
    }





    @FXML
    private void handleProduitBtnClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardProduit.fxml"));
            Parent dashboardProduitView = loader.load();

            // Si vous avez besoin d'accéder au contrôleur du tableau de bord produit, vous pouvez le faire ici
            // DashboardProduitController dashboardProduitController = loader.getController();
            // dashboardProduitController.initData();

            Stage stage = new Stage();
            stage.setTitle("Dashboard Produit");
            stage.setScene(new Scene(dashboardProduitView));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs de chargement de la vue
        }
    }





  /*  private void actualiserTable() {
        // Call the method to fetch the updated data from the database
        List<Produit> produits = consulterProduits();

        // Clear the existing items in the TableView
        tableView.getItems().clear();

        // Add the updated data to the TableView
        tableView.getItems().addAll(produits);
    }*/




   /* private void actualiserTable() {
        // Appeler la méthode consulterProduits et charger les données dans la table
        List<Produit> produits = consulterProduits();
        tableView.getItems().setAll(produits);
    }*/

    // Autres méthodes
    @FXML
    private void reloadCours() {
        try {
            if (connexion.isClosed()) {
                connexion = ConnexionDB.getInstance().getConnexion(); // Réouvrir la connexion si elle est fermée
            }
            List<Cours> Cours = consulterCours();
            tableView.getItems().setAll(Cours);
            coursList.clear();
            coursList.addAll(Cours);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private List<Cours> consulterCours() {
        List<Cours> coursList = new ArrayList<>();
        String query = "SELECT * FROM cours";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "")) {
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Cours cr = new Cours();
                cr.setId(resultSet.getInt("id"));
                cr.setNom(resultSet.getString("nom_cour") + "    ");
                cr.setCategorie(resultSet.getString("categorie") + "    ");
                cr.setCible(resultSet.getString("cible") + "    ");
                cr.setDuree(resultSet.getString("duree") + "    ");
                cr.setIntensite(resultSet.getString("intensite") + "    ");
                cr.setObjectif(resultSet.getString("objectif") + "    ");
                cr.setEtat(resultSet.getBoolean("etat"));
                cr.setCapacite(resultSet.getInt("capacite"));
                int coachId = resultSet.getInt("user_id");

                String coachName = null;
                try (Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "")) {

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
                cr.setCoach(coach);

                coursList.add(cr);
            }
        } }catch (SQLException e) {
            e.printStackTrace();
        }
        return coursList;
    }

    private void updateCours(Cours cours) {
        // Création de la fenêtre de modification
        Stage modificationStage = new Stage();
        modificationStage.setTitle("Modifier le cours");

        // Champ de texte pour le nom du cours
        TextField nomField = new TextField(cours.getNom());
        // Vérification si le nom de cours contient des chiffres

        // Champ de texte pour le reste des champs
        TextField dureeField = new TextField(cours.getDuree());

        ComboBox<String> intensiteComboBox = new ComboBox<>();
        intensiteComboBox.getItems().addAll("Forte", "Moyenne", "Faible");
        intensiteComboBox.setValue(cours.getIntensite());

        ComboBox<String> cibleComboBox = new ComboBox<>();
        cibleComboBox.getItems().addAll("Enfant", "Adulte");
        cibleComboBox.setValue(cours.getCible());


        // Menu déroulant pour la catégorie
        ComboBox<String> categorieComboBox = new ComboBox<>();
        categorieComboBox.getItems().addAll("Aquatique", "Cardio", "Force", "Danse", "Kids Island");
        categorieComboBox.setValue(cours.getCategorie());

        // Menu déroulant pour l'objectif
        ComboBox<String> objectifComboBox = new ComboBox<>();
        objectifComboBox.getItems().addAll("Perdre du poids", "Se défouler", "Se musculer", "S'entrainer en dansant");
        objectifComboBox.setValue(cours.getObjectif());

        CheckBox etatCheckBox = new CheckBox("Actif");
        etatCheckBox.setSelected(cours.isEtat());
        TextField capaciteField = new TextField(String.valueOf(cours.getCapacite()));
        TextField idCoachField = new TextField(String.valueOf(cours.getCoach().getId()));

        // Bouton pour enregistrer les modifications
        Button saveButton = new Button("Enregistrer");
        saveButton.setOnAction(event -> {

            // Vérification des conditions avant de sauvegarder les modifications
            String nomText = nomField.getText();

            // Vérifier si le nom du cours est unique
            if (!isNomCoursUnique(nomText, cours.getId())) {
                afficherMessage("Erreur", "Ce cours existe déjà!");
                return; // Sortir de la méthode si le nom n'est pas unique
            }

            String dureeText = dureeField.getText();
            String capaciteText = capaciteField.getText();
            String idCoachText = idCoachField.getText();

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
                    idCoachField.setStyle("-fx-border-color: red;");
                } else {
                    idCoachField.setStyle(""); // Remettre le style par défaut si le champ n'est pas vide
                }

                return; // Sortie de la méthode si un champ est vide
            }



            String nom = nomField.getText();
            String duree = dureeField.getText();
            int capacite = Integer.parseInt(capaciteField.getText());

            // Vérification si le nom de cours contient des chiffres
            if (nom.matches(".*\\d.*")) {
                afficherMessage("Erreur", "Le nom du cours ne doit pas contenir de chiffres.");
                nomField.setStyle("-fx-text-inner-color: red;");
                return; // Sortie de la méthode si la condition n'est pas respectée
            }else {
                nomField.setStyle("-fx-text-inner-color: black;"); // Remettre en noir si la condition est respectée
            }

            // Ajout de la validation pour la durée


            int dureeValue = Integer.parseInt(duree.trim());
            if (dureeValue < 30 || dureeValue > 75) {
                afficherMessage("Erreur", "La durée doit être comprise entre 30 et 75 minutes.");
                dureeField.setStyle("-fx-text-inner-color: red;");
                return; // Sortie de la méthode si la condition n'est pas respectée
            }else {
                dureeField.setStyle("-fx-text-inner-color: black;"); // Remettre en noir si la condition est respectée
            }

            // Ajout de la validation pour la capacité
            if (capacite < 10 || capacite > 30) {
                afficherMessage("Erreur", "La capacité doit être comprise entre 10 et 30.");
                capaciteField.setStyle("-fx-text-inner-color: red;");
                return; // Sortie de la méthode si la condition n'est pas respectée
            }else {
                capaciteField.setStyle("-fx-text-inner-color: black;"); // Remettre en noir si la condition est respectée
            }
            // Code pour sauvegarder les modifications dans la base de données
            try (PreparedStatement statement = connexion.prepareStatement(
                    "UPDATE cours SET nom_cour = ?, duree = ?, intensite = ?, cible = ?, categorie = ?, objectif = ?, etat = ?, capacite = ?, user_id = ? WHERE id = ?")) {

                statement.setString(1, nomField.getText());
                statement.setString(2, dureeField.getText());
                statement.setString(3, intensiteComboBox.getValue());
                statement.setString(4, cibleComboBox.getValue());
                statement.setString(5, categorieComboBox.getValue());
                statement.setString(6, objectifComboBox.getValue());
                statement.setBoolean(7, etatCheckBox.isSelected());
                statement.setInt(8, Integer.parseInt(capaciteField.getText()));
                statement.setInt(9, Integer.parseInt(idCoachField.getText()));
                statement.setInt(10, cours.getId());
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    // Mise à jour réussie
                    cours.setNom(nomField.getText());
                    cours.setDuree(dureeField.getText());
                    cours.setIntensite(intensiteComboBox.getValue());
                    cours.setCible(cibleComboBox.getValue());
                    cours.setCategorie(categorieComboBox.getValue());
                    cours.setObjectif(objectifComboBox.getValue());
                    cours.setEtat(etatCheckBox.isSelected());
                    cours.setCapacite(Integer.parseInt(capaciteField.getText()));
                    cours.getCoach().setId(Integer.parseInt(idCoachField.getText()));
                    tableView.refresh();
                    reloadCours(); // Recharger les données des cours pour mettre à jour la TableView
                } else {
                    // Gérer l'échec de la mise à jour
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("La mise à jour a échoué");
                    alert.setContentText("Impossible de mettre à jour le cours. Veuillez réessayer.");
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

        // Ajout de la validation pour le champ de capacité
        capaciteField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Vérifie si la nouvelle valeur ne contient que des chiffres
                capaciteField.setStyle("-fx-text-inner-color: black;"); // Change la couleur du texte en noir
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

        // Ajout de l'écouteur de changement de texte sur le champ de durée
        dureeField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Vérifie si la nouvelle valeur ne contient que des chiffres
            if (!newValue.matches("\\d*")) {
                // Changement de la couleur du texte en noir si la condition n'est pas respectée
                dureeField.setStyle("-fx-text-inner-color: black;");
            } else {
                try {
                    int dureeValue = Integer.parseInt(newValue.trim());
                    // Vérifie si la durée est en dehors de la plage valide
                    if (dureeValue < 30 || dureeValue > 75) {
                        // Changement de la couleur du texte en rouge si la condition n'est pas respectée
                        dureeField.setStyle("-fx-text-inner-color: red;");
                    } else {
                        // Remet la couleur du texte en noir si la condition est respectée
                        dureeField.setStyle("-fx-text-inner-color: black;");
                    }
                } catch (NumberFormatException e) {
                    // Changement de la couleur du texte en rouge si la conversion échoue
                    dureeField.setStyle("-fx-text-inner-color: red;");
                }
            }
        });


        // Mise en page de la fenêtre de modification
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(
                new Label("Nom du cours :"), nomField,
                new Label("Durée du cours :"), dureeField,
                new Label("Intensité :"), intensiteComboBox,
                new Label("Cible :"), cibleComboBox,
                new Label("Catégorie :"), categorieComboBox,
                new Label("Objectif :"), objectifComboBox,
                new Label("État :"), etatCheckBox,
                new Label("Capacité :"), capaciteField,
                new Label("ID du coach :"), idCoachField,
                saveButton
        );


        // Encapsulez votre mise en page dans un ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(layout); // Remplacez 'layout' par le nom de votre mise en page de modification

        // Ajoutez le ScrollPane à votre scène
        Scene scene = new Scene(scrollPane, 400, 500); // Ajustez la taille selon vos besoins

        // Affichez la fenêtre de modification
        modificationStage.setScene(scene);
        modificationStage.show();

    }


    private String getCoachNameById(int coachId) {
        String coachName = null;
        try (PreparedStatement statement = connexion.prepareStatement(
                "SELECT nom FROM user WHERE id = ?")) {
            statement.setInt(1, coachId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                coachName = resultSet.getString("nom");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coachName;
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

    private void afficherMessage(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }


 /*  private void modifierProduit(Produit produit) {
        // Mettre à jour le produit dans la base de données en utilisant votre service
        produitServices.modifierProduit(produit);
    }*/

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /*private void actualiserTable() {
        ObservableList<Produit> produits = FXCollections.observableArrayList(produitServices.consulterProduits());
        tableView.setItems(produits);
    }*/

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

  /*  public class ActionButtonTableCellFactory<S> implements Callback<TableColumn<S, Void>, TableCell<S, Void>> {
        private final Consumer<S> deleteAction;
        private final Consumer<S> editAction;

        public ActionButtonTableCellFactory(Consumer<S> deleteAction, Consumer<S> editAction) {
            this.deleteAction = deleteAction;
            this.editAction = editAction;
        }

        @Override
        public TableCell<S, Void> call(TableColumn<S, Void> param) {
            return new ActionButtonTableCell<>(deleteAction, editAction);
        }
    }*/
}
