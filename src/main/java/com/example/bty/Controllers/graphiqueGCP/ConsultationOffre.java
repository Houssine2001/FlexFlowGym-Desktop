package com.example.bty.Controllers.graphiqueGCP;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultationOffre extends Application {

    private static Connection connection;
    private static TableView<OffreItem> tableView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Consultation des offres");

        // Connexion à la base de données
        connectToDatabase();

        // Création de la table des offres sous forme de TableView
        tableView = new TableView<>();

        // Création du TableView et de ses colonnes
        TableColumn<OffreItem, String> nomCol = new TableColumn<>("Nom coach");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<OffreItem, String> specialiteCol = new TableColumn<>("Spécialité");
        specialiteCol.setCellValueFactory(new PropertyValueFactory<>("specialite"));

        TableColumn<OffreItem, String> tarifCol = new TableColumn<>("Tarif");
        tarifCol.setCellValueFactory(new PropertyValueFactory<>("tarif"));

        TableColumn<OffreItem, String> coachCol = new TableColumn<>("Coach");
        coachCol.setCellValueFactory(new PropertyValueFactory<>("coach"));

        TableColumn<OffreItem, String> etatCol = new TableColumn<>("État");
        etatCol.setCellValueFactory(new PropertyValueFactory<>("etat"));

        TableColumn<OffreItem, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setMinWidth(100);
        actionsCol.setSortable(false);

        // Définir la cellule de la colonne d'actions
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            {
                // Action du bouton de modification
                editButton.setOnAction(event -> {
                    OffreItem offreItem = getTableView().getItems().get(getIndex());
                    modifierOffre(offreItem);
                });

                // Action du bouton de suppression
                deleteButton.setOnAction(event -> {
                    OffreItem offreItem = getTableView().getItems().get(getIndex());
                    supprimerOffre(offreItem);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttonsContainer = new HBox(5, editButton, deleteButton);
                    setGraphic(buttonsContainer);
                }
            }
        });

        // Ajouter les colonnes au TableView
        tableView.getColumns().addAll(nomCol, specialiteCol, tarifCol, coachCol, etatCol, actionsCol);

        // Charger les données dans le TableView
        try {
            tableView.getItems().addAll(retrieveOffreItemsArray());
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de récupération des données
        }

        // Création de la disposition verticale
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.setAlignment(Pos.CENTER_RIGHT); // Align to the center-right
        vbox.getChildren().add(tableView);



        Scene scene = new Scene(vbox, 800, 600);
        scene.getStylesheets().add(ConsultationDemandes.class.getResource("/Styles/tableStyle.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void connectToDatabase() {
        try {
            // Charger le pilote JDBC MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir la connexion à la base de données MySQL
            String url = "jdbc:mysql://localhost:3306/pidevgym";
            String utilisateur = "root";
            String motDePasse = "";

            connection = DriverManager.getConnection(url, utilisateur, motDePasse);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion
        }
    }

    // Méthode pour récupérer les offres depuis la base de données et les placer dans un tableau
    private List<OffreItem> retrieveOffreItemsArray() throws SQLException {
        List<OffreItem> offresList = new ArrayList<>();
        try {
            // Exécuter une requête pour récupérer les offres depuis la base de données
            String query = "SELECT * FROM Offre";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Parcourir les résultats de la requête
            while (resultSet.next()) {
                // Créer un objet OffreItem pour chaque ligne de la base de données
                OffreItem offreItem = new OffreItem(
                        resultSet.getString("nom"),
                        resultSet.getString("Specialite"),
                        resultSet.getString("tarif_heure"),
                        resultSet.getString("id_Coach"),
                        resultSet.getString("etatOffre")
                );
                offresList.add(offreItem); // Ajouter l'objet à la liste
            }

            // Fermer les ressources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de requête
            throw e;
        }

        return offresList;
    }

    // Ajoutez ici la méthode modifierOffre et la classe OffreItem

    public static void main(String[] args) {
        launch(args);
    }

    // Méthode pour modifier une offre
    private void modifierOffre(OffreItem offre) {
        // Créer une nouvelle fenêtre de modification
        Stage modificationStage = new Stage();
        modificationStage.setTitle("Modifier l'offre");

        // Créer des champs de texte pour les nouveaux détails de l'offre
        TextField nomField = new TextField(offre.getNom());
        TextField specialiteField = new TextField(offre.getSpecialite());
        TextField tarifField = new TextField(offre.getTarif());
        TextField coachField = new TextField(offre.getCoach());

        // Créer un bouton pour appliquer les modifications
        Button modifierButton = new Button("Modifier");
        modifierButton.setOnAction(event -> {
            try {
                // Récupérer les nouvelles valeurs saisies par l'utilisateur
                String nouveauNom = nomField.getText();
                String nouvelleSpecialite = specialiteField.getText();
                String nouveauTarif = tarifField.getText();
                String nouveauCoach = coachField.getText();

                // Exécuter une requête SQL UPDATE pour mettre à jour l'offre dans la base de données
                String query = "UPDATE Offre SET nom = ?, Specialite = ?, tarif_heure = ?, id_Coach = ? WHERE nom = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, nouveauNom);
                statement.setString(2, nouvelleSpecialite);
                statement.setString(3, nouveauTarif);
                statement.setString(4, nouveauCoach);
                statement.setString(5, offre.getNom()); // Utiliser l'ancien nom pour la clause WHERE
                int rowsAffected = statement.executeUpdate();

                // Vérifier si la mise à jour a réussi
                if (rowsAffected > 0) {
                    // Afficher un message de succès
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Modification réussie");
                    alert.setHeaderText(null);
                    alert.setContentText("L'offre a été modifiée avec succès.");
                    alert.showAndWait();

                    // Rafraîchir les données dans le TableView
                    tableView.getItems().clear();
                    tableView.getItems().addAll(retrieveOffreItemsArray());
                } else {
                    // Afficher un message d'erreur si la mise à jour a échoué
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de modification");
                    alert.setHeaderText(null);
                    alert.setContentText("La modification de l'offre a échoué.");
                    alert.showAndWait();
                }

                // Fermer la fenêtre de modification
                modificationStage.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Créer une mise en page pour la fenêtre de modification
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label("Nouveau nom :"), nomField,
                new Label("Nouvelle spécialité :"), specialiteField,
                new Label("Nouveau tarif :"), tarifField,
                new Label("Nouveau coach :"), coachField,
                modifierButton
        );
        vbox.setPadding(new Insets(10));

        // Afficher la fenêtre de modification
        Scene scene = new Scene(vbox);
        modificationStage.setScene(scene);
        modificationStage.show();
    }

    // Méthode pour supprimer une offre
    private void supprimerOffre(OffreItem offre) {
        try {
            // Exécuter une requête SQL DELETE pour supprimer l'offre de la base de données
            String query = "DELETE FROM Offre WHERE nom = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, offre.getNom());
            int rowsAffected = statement.executeUpdate();

            // Vérifier si la suppression a réussi
            if (rowsAffected > 0) {
                // Afficher un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Suppression réussie");
                alert.setHeaderText(null);
                alert.setContentText("L'offre a été supprimée avec succès.");
                alert.showAndWait();

                // Rafraîchir les données dans le TableView
                tableView.getItems().clear();
                tableView.getItems().addAll(retrieveOffreItemsArray());
            } else {
                // Afficher un message d'erreur si la suppression a échoué
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de suppression");
                alert.setHeaderText(null);
                alert.setContentText("La suppression de l'offre a échoué.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Classe interne représentant un élément d'offre
    public static class OffreItem {
        private String nom;
        private String specialite;
        private String tarif;
        private String coach;
        private String etat;

        public OffreItem(String nom, String specialite, String tarif, String coach, String etat) {
            this.nom = nom;
            this.specialite = specialite;
            this.tarif = tarif;
            this.coach = coach;
            this.etat = etat;
        }

        public OffreItem() {

        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public void setSpecialite(String specialite) {
            this.specialite = specialite;
        }

        public void setTarif(String tarif) {
            this.tarif = tarif;
        }

        public void setCoach(String coach) {
            this.coach = coach;
        }

        public void setEtat(String etat) {
            this.etat = etat;
        }

        // Getters pour accéder aux champs de l'offre
        public String getNom() {
            return nom;
        }

        public String getSpecialite() {
            return specialite;
        }

        public String getTarif() {
            return tarif;
        }

        public String getCoach() {
            return coach;
        }

        public String getEtat() {
            return etat;
        }
    }
}
