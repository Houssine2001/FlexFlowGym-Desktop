package com.example.bty.Controllers;

import com.example.bty.Controllers.ProduitController.ActionButtonTableCell;
import com.example.bty.Controllers.ProduitController.Ajoutproduit;
import com.example.bty.Entities.Commmande;
import com.example.bty.Entities.Produit;
import com.example.bty.Services.ServiceProduit;
import com.example.bty.Utils.ConnexionDB;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class DasboardCommande {
    public TextField searchField;
    @FXML
    private AnchorPane dashboardAnchorPane;


    @FXML
    private LineChart<String, Number> chart;

    @FXML
    private TableView<Commmande> tableView;

    @FXML
    private TableColumn<Commmande, Integer> idCol;

    @FXML
    private TableColumn<Commmande, Timestamp> dateCol;

    @FXML
    private TableColumn<Commmande, Integer> idProduitCol;

    @FXML
    private TableColumn<Commmande, String> nomCol;

    @FXML
    private TableColumn<Commmande, Double> montantCol;


    private Connection connexion;
    private PreparedStatement pst;
    private Statement ste ;
    public DasboardCommande() {
        connexion = ConnexionDB.getInstance().getConnexion();
    }
    private ServiceProduit produitServices = new ServiceProduit();

    @FXML
    private void initialize() {
        idCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdProduit()).asObject());
        dateCol.setCellValueFactory(cellData -> {
            Timestamp date = cellData.getValue().getDateCommande();
            return new SimpleObjectProperty<>(date);
        });
        idProduitCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdProduit()).asObject());

        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));

        montantCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getMontant()).asObject());




        // Chargement initial des données dans la table
        actualiserTable();

        // Button ajoutProduitButton = new Button("Ajout");




        // Appeler le service pour obtenir le chiffre d'affaires

      //  chargerDonneesGraphique();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filtrerParNom(newValue));
    }



    @FXML
    private void filtrerParNom(String nom) {
        ObservableList<Commmande> filteredList = FXCollections.observableArrayList();
        for (Commmande commande : produitServices.consulterCommandes()) {
            if (commande.getNom().toLowerCase().contains(nom.toLowerCase())) {
                filteredList.add(commande);
            }
        }
        tableView.setItems(filteredList);
    }

//    @FXML
//    private void chargerDonneesGraphique() {
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//        series.setName("Chiffre d'affaires par jour");
//
//        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgym", "root", "");
//             Statement statement = connection.createStatement();
//             ResultSet resultSet = statement.executeQuery("SELECT dateCommande, SUM(montant) FROM commande GROUP BY dateCommande")) {
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//            while (resultSet.next()) {
//                Timestamp timestamp = resultSet.getTimestamp("dateCommande");
//                Date date = new Date(timestamp.getTime());
//
//                double montant = resultSet.getDouble("SUM(montant)");
//
//                series.getData().add(new XYChart.Data<>(dateFormat.format(date), montant));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        dashboardAnchorPane.getChildren().add(chart); // Ajoutez le graphique à l'AnchorPane
//        chart.getData().add(series);
//    }





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
        ObservableList<Commmande> commmandes = FXCollections.observableArrayList(produitServices.consulterCommandes());
        tableView.setItems(commmandes);
    }




   /* private void actualiserTable() {
        // Appeler la méthode consulterProduits et charger les données dans la table
        List<Produit> produits = consulterProduits();
        tableView.getItems().setAll(produits);
    }*/

    // Autres méthodes




    private void modifierProduit(Produit produit) {
        // Mettre à jour le produit dans la base de données en utilisant votre service
        produitServices.modifierProduit(produit);
    }




    /*private void actualiserTable() {
        ObservableList<Produit> produits = FXCollections.observableArrayList(produitServices.consulterProduits());
        tableView.setItems(produits);
    }*/




}


