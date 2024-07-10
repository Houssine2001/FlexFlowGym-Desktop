package com.example.bty.Controllers.ProduitController;

import com.example.bty.Entities.Commande;
import com.example.bty.Entities.Commmande;

import com.example.bty.Services.ServiceProduit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.function.Consumer;

public class ConsulterCommande extends Application {

    private ServiceProduit commandeServices = new ServiceProduit();
    private TableView<Commmande> tableView = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Commandes");

        // Création des colonnes de la TableView
        TableColumn<Commmande, Integer> idCommandeCol = new TableColumn<>("ID Commande");
        idCommandeCol.setCellValueFactory(cellData -> cellData.getValue().getIdCommandeProperty().asObject());

        TableColumn<Commmande, Timestamp> dateCommandeCol = new TableColumn<>("Date Commande");
        dateCommandeCol.setCellValueFactory(cellData -> cellData.getValue().getDateCommandeProperty());

        TableColumn<Commmande, Integer> idProduitCol = new TableColumn<>("ID Produit");
        idProduitCol.setCellValueFactory(cellData -> cellData.getValue().getIdProduitProperty().asObject());

        TableColumn<Commmande, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(cellData -> cellData.getValue().getNomProperty());

        TableColumn<Commmande, Double> montantCol = new TableColumn<>("Montant");
        montantCol.setCellValueFactory(cellData -> cellData.getValue().getMontantProperty().asObject());


        tableView.getColumns().addAll(idCommandeCol, dateCommandeCol, idProduitCol, nomCol, montantCol);

        VBox vbox = new VBox();
        vbox.getStyleClass().add("container");
        vbox.getChildren().addAll(tableView);

        Scene scene = new Scene(vbox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        actualiserTable();
    }

    private void actualiserTable() {
        Platform.runLater(() -> {
            ObservableList<Commmande> commandes = FXCollections.observableArrayList(commandeServices.consulterCommandes());
            tableView.setItems(commandes);
        });
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
