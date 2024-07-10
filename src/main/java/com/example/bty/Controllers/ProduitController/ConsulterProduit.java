package com.example.bty.Controllers.ProduitController;

import com.example.bty.Entities.Produit;
import com.example.bty.Services.ServiceProduit;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.Optional;
import java.util.function.Consumer;

public class ConsulterProduit extends Application {

    private ServiceProduit produitServices = new ServiceProduit();
    private TableView<Produit> tableView = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {


        primaryStage.setTitle("Gestion des Produits");

        // Création des colonnes de la TableView
        TableColumn<Produit, Integer> idCol = new TableColumn<>("ID");
        idCol.getStyleClass().add("column-id");
        idCol.setCellValueFactory(cellData -> cellData.getValue().idProduitProperty().asObject());

        TableColumn<Produit, String> nomCol = new TableColumn<>("Nom");
        nomCol.getStyleClass().add("column-nom");
        nomCol.setCellValueFactory(cellData -> cellData.getValue().nomProperty());

        TableColumn<Produit, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.getStyleClass().add("column-description");
        descriptionCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        TableColumn<Produit, Double> prixCol = new TableColumn<>("Prix");
        prixCol.getStyleClass().add("column-id");
        prixCol.setCellValueFactory(cellData -> cellData.getValue().prixProperty().asObject());

        TableColumn<Produit, String> typeCol = new TableColumn<>("Type");
        typeCol.getStyleClass().add("column-prix");
        typeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        TableColumn<Produit, Integer> quantiteCol = new TableColumn<>("Quantité");
        quantiteCol.getStyleClass().add("column-quantite");
        quantiteCol.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty().asObject());

        TableColumn<Produit, Integer> quantiteVenduesCol = new TableColumn<>("Quantité Vendue");
        quantiteVenduesCol.getStyleClass().add("column-quantiteVendues");
        quantiteVenduesCol.setCellValueFactory(cellData -> cellData.getValue().quantiteVenduesProperty().asObject());

        // Ajout de la colonne Actions pour l'édition
        TableColumn<Produit, Void> actionColumn = new TableColumn<>("Actions");
        actionColumn.setCellFactory(new ActionButtonTableCellFactory<>(

                (Produit produit) -> {
                    // Logique de suppression
                    if (showConfirmationDialog("Suppression", "Voulez-vous supprimer ce produit ?")) {
                        if (produitServices.supprimerProduit(produit.getIdProduit())) {
                            showAlert("Sucess", "La suppression a réussi");
                            actualiserTable();
                        } else {
                            showAlert("Échec", "La suppression a échoué");
                        }
                    }
                },
                (Produit produit) -> {
                    // Logique d'édition
                    openEditForm(produit);
                }
        ));
        tableView.getColumns().addAll(idCol, nomCol, descriptionCol, prixCol, typeCol, quantiteCol, quantiteVenduesCol, actionColumn);

        // Bouton pour actualiser la table
        Button actualiserBtn = new Button("Actualiser");
        actualiserBtn.setOnAction(e -> actualiserTable());

        VBox vbox = new VBox();
        vbox.getStyleClass().add("container");
        vbox.getChildren().addAll(tableView);









        Scene scene = new Scene(vbox, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/com/example/bty/CSSmoduleProduit/ConsulterProduit.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        actualiserTable();
    }

    private void openEditForm(Produit produit) {
        Dialog<Produit> editDialog = new Dialog<>();
        editDialog.setTitle("Modifier le produit");
        editDialog.setHeaderText(null);

        // Créer des champs de formulaire pour les attributs du produit
        TextField nomField = new TextField(produit.getNom());
        TextField descriptionField = new TextField(produit.getDescription());
        TextField prixField = new TextField(String.valueOf(produit.getPrix()));
        TextField typeField = new TextField(produit.getType());
        TextField quantiteField = new TextField(String.valueOf(produit.getQuantite()));
        TextField quantiteVenduesField = new TextField(String.valueOf(produit.getQuantiteVendues()));






        // Créer le contenu du dialogue
        VBox content = new VBox();
        content.setSpacing(10);
        content.getChildren().addAll(
                new Label("Nom: "), nomField,
                new Label("Description: "), descriptionField,
                new Label("Prix: "), prixField,
                new Label("Type: "), typeField,
                new Label("Quantité: "), quantiteField,
                new Label("Quantité Vendue: "), quantiteVenduesField
        );

        // Ajouter les boutons au dialogue :
        ButtonType modifierButton = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
        editDialog.getDialogPane().getButtonTypes().addAll(modifierButton, cancelButton);

       // editDialog.getDialogPane().getButtonTypes().addAll(modifierButton, ButtonType.CANCEL);

        Node modifierButtonNode = editDialog.getDialogPane().lookupButton(modifierButton);
        Node cancelButtonNode = editDialog.getDialogPane().lookupButton(cancelButton);



        editDialog.getDialogPane().setContent(content);

        // Définir la logique de conversion du résultat du dialogue
        editDialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifierButton) {
                Produit editedProduit = new Produit();
                editedProduit.setIdProduit(produit.getIdProduit());
                editedProduit.setNom(nomField.getText());
                editedProduit.setDescription(descriptionField.getText());
                editedProduit.setPrix(Double.parseDouble(prixField.getText()));
                editedProduit.setType(typeField.getText());
                editedProduit.setQuantite(Integer.parseInt(quantiteField.getText()));
                editedProduit.setQuantiteVendues(Integer.parseInt(quantiteVenduesField.getText()));
                return editedProduit;
            }
            return null;
        });

        // Afficher le dialogue et traiter le résultat
        Optional<Produit> result = editDialog.showAndWait();
        result.ifPresent(editedProduit -> {
            // Mettre à jour le produit dans la base de données
            modifierProduit(editedProduit);
            actualiserTable();
        });
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

    private void actualiserTable() {
        ObservableList<Produit> produits = FXCollections.observableArrayList(produitServices.consulterProduits());
        tableView.setItems(produits);
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
