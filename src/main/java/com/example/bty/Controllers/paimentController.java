package com.example.bty.Controllers;

import com.example.bty.Controllers.ProduitController.ActionButtonTableCell;
import com.example.bty.Controllers.ProduitController.Ajoutproduit;
import com.example.bty.Entities.Produit;
import com.example.bty.Services.ServiceProduit;
import com.example.bty.Utils.ConnexionDB;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import com.example.bty.Entities.paiment;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class paimentController {

    public Button ajout_produit;
    public Button actuliser_produit;
    //public Label dashboard_NM;

    @FXML
    private TableView<paiment> tableView;



    @FXML
    private TableColumn<paiment, String> nomCol;

    @FXML
    private TableColumn<paiment, String> emailCol;

    @FXML
    private TableColumn<paiment, String> carteCol;

    @FXML
    private TableColumn<paiment, Integer> mmCol;

    @FXML
    private TableColumn<paiment, Integer> yyCol;

    @FXML
    private TableColumn<paiment, Integer> cvcCol;

    @FXML
    private TableColumn<paiment, Integer> totalCol;




    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_NC;
    // Autres déclarations
    private Connection connexion;
    private PreparedStatement pst;
    private Statement ste ;
    public paimentController() {
        connexion = ConnexionDB.getInstance().getConnexion();
    }
    private ServiceProduit produitServices = new ServiceProduit();

    @FXML
    private void initialize() {
        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));

        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        carteCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCarte()));
        mmCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMm()).asObject());
        yyCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getYy()).asObject());
        cvcCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCvc()).asObject());
        totalCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotal()).asObject());


        // Ajout de la colonne Actions
        actualiserTable();

       /* actuliser_produit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Appel de la méthode pour ouvrir la nouvelle fenêtre d'ajout de produit
                actualiserTable();
            }
        });*/





        String produitMoinsAchete = getProduitMoinsVendu();

// Mettre à jour le Label avec le nom du produit le plus acheté


    }




    public String getProduitMoinsVendu() {
        String produitMoinsVendu = null;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "")) {
//            String sql = "SELECT nom FROM produit ORDER BY prix * quantite ASC LIMIT 1";
            String sql = "SELECT nom FROM produit ORDER BY quantite_vendues ASC LIMIT 1";


            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    produitMoinsVendu = resultSet.getString("nom");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion ou de requête SQL ici
        }

        return produitMoinsVendu;
    }



    public String getProduitPlusAchete() {
        String produitPlusAchete = null;
        // Connexion à la base de données
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "")) {

            // Requête SQL pour obtenir le produit le plus acheté
//            String sql = "SELECT nom FROM produit ORDER BY prix * quantite DESC LIMIT 1";
            String sql = "SELECT nom FROM produit ORDER BY quantite_vendues DESC LIMIT 1";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    produitPlusAchete = resultSet.getString("nom");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion ou de requête SQL ici
        }
        return produitPlusAchete;
    }

    @FXML
    private void ouvrirFenetreAjoutProduit() {
        // Créer une nouvelle instance de la classe Ajoutproduit
        Ajoutproduit ajoutproduit = new Ajoutproduit();

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




    @FXML
    private void actualiserTable() {
        ObservableList<paiment> paiments = FXCollections.observableArrayList(produitServices.consulterPaiment());
        tableView.setItems(paiments);
    }




   /* private void actualiserTable() {
        // Appeler la méthode consulterProduits et charger les données dans la table
        List<Produit> produits = consulterProduits();
        tableView.getItems().setAll(produits);
    }*/

    // Autres méthodes




    private void afficherMessage(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    private void afficherMessage1(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }




    private boolean validateFields(TextField... fields) {
        // Check if all fields are non-empty
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }




    private void showAlert1(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Ajoute un bouton OK personnalisé qui ne ferme pas la boîte de dialogue parente
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        // Affiche l'alerte
        alert.showAndWait();
    }





    private void modifierProduit(Produit produit) {
        // Mettre à jour le produit dans la base de données en utilisant votre service
        produitServices.modifierProduit(produit);
    }


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

    public class ActionButtonTableCellFactory<S> implements Callback<TableColumn<S, Void>, TableCell<S, Void>> {
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
    }
}