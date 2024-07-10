package com.example.bty.Controllers.graphiqueGCP;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ConsultationDemandes extends Application {

    private static TableView<DemandeItem> tableView;
    private static Connection connection;
    private static ListView<DemandeItem> demandesListView;

    public ConsultationDemandes() {

    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("Consultation des demandes");

        // Connexion à la base de données
        connectToDatabase();

        // Création de la liste des demandes
        demandesListView = new ListView<>();
         // Récupérer les données depuis la base de données et les ajouter à la TableView
        List<DemandeItem> demandesItemList = retrieveDemandesList();
        ObservableList<DemandeItem> demandesList = FXCollections.observableArrayList(demandesItemList);
        tableView = new TableView<>();
        tableView.setItems(demandesList);


        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(15);
        grid.getStyleClass().add("grid-container");


        // Création du TableView et de ses colonnes
        TableColumn<DemandeItem, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        TableColumn<DemandeItem, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id_demande"));
        TableColumn<DemandeItem, String> butCol = new TableColumn<>("But");
        butCol.setCellValueFactory(new PropertyValueFactory<>("but"));
        TableColumn<DemandeItem, String> niveauPhysiqueCol = new TableColumn<>("Niveau physique");
        niveauPhysiqueCol.setCellValueFactory(new PropertyValueFactory<>("niveauPhysique"));
        TableColumn<DemandeItem, String> maladieChroniqueCol = new TableColumn<>("Maladie chronique");
        maladieChroniqueCol.setCellValueFactory(new PropertyValueFactory<>("maladieChronique"));
        TableColumn<DemandeItem, String> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        TableColumn<DemandeItem, String> id_userCol = new TableColumn<>("ID Utilisateur");
        id_userCol.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        TableColumn<DemandeItem, String> id_offreCol = new TableColumn<>("ID Offre");
        id_offreCol.setCellValueFactory(new PropertyValueFactory<>("id_offre"));
        TableColumn<DemandeItem, String> etatCol = new TableColumn<>("État");
        etatCol.setCellValueFactory(new PropertyValueFactory<>("etat"));
        TableColumn<DemandeItem, String> nombreHeureCol = new TableColumn<>("Nombre d'Heures");
        nombreHeureCol.setCellValueFactory(new PropertyValueFactory<>("nombreHeure"));
        TableColumn<DemandeItem, String> horaireCol = new TableColumn<>("Horaire");
        horaireCol.setCellValueFactory(new PropertyValueFactory<>("horaire"));
        TableColumn<DemandeItem, String> lesjoursCol = new TableColumn<>("Lesjours");
        lesjoursCol.setCellValueFactory(new PropertyValueFactory<>("lesjours"));

        TableColumn<DemandeItem, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setMinWidth(100);
        actionsCol.setSortable(false);


        // Définir la cellule de la colonne d'actions
        actionsCol.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");

            private final Button deleteButton = new Button("Supprimer");


            {
                // Action du bouton de modification
                editButton.setOnAction(event -> {
                    DemandeItem demandeItem = getTableView().getItems().get(getIndex());
                    modifierDemande(demandeItem.getId_demande());
                });

                // Action du bouton de suppression
                deleteButton.setOnAction(event -> {
                    DemandeItem demandeItem = getTableView().getItems().get(getIndex());
                    supprimerDemande(demandeItem.getId_demande());
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

        // Ajout des colonnes au TableView
        tableView.getColumns().addAll(nomCol, idCol, butCol, niveauPhysiqueCol, maladieChroniqueCol, ageCol, id_userCol, id_offreCol, etatCol, nombreHeureCol, horaireCol, lesjoursCol, actionsCol);

        // Création de la disposition verticale
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.getChildren().add(tableView);

        Scene scene = new Scene(vbox, 800, 600);
        scene.getStylesheets().add(ConsultationDemandes.class.getResource("/Styles/Modifier.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToDatabase() {
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

    // Méthode pour récupérer les demandes depuis la base de données
    static List<DemandeItem> retrieveDemandesList() throws SQLException {

        List<DemandeItem> demandesList = new ArrayList<>();

        try {
            // Exécuter une requête pour récupérer les demandes du client depuis la base de données
            String query = "SELECT * FROM Demande ";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Ajouter les demandes à la liste
            while (resultSet.next()) {
                DemandeItem demandeItem = new DemandeItem(
                        resultSet.getString("nom"),
                        resultSet.getString("id_demande"),
                        resultSet.getString("but"),
                        resultSet.getString("NiveauPhysique"),
                        resultSet.getString("MaladieChronique"),
                        resultSet.getString("age"),
                        resultSet.getString("id_user"),
                        resultSet.getString("id_offre"),
                        resultSet.getString("etat"),
                        resultSet.getString("nombreHeure"),
                        resultSet.getTime("horaire"),
                        resultSet.getString("lesjours")
                );

                demandesList.add(demandeItem);
            }

            // Fermer les ressources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de requête
            throw e;
        }
        return demandesList;
    }

    // Méthode pour supprimer une demande
    // Méthode pour supprimer une demande
    private static void supprimerDemande(String id_demande) {
        try {
            // Créer la requête SQL pour supprimer la demande
            String query = "DELETE FROM Demande WHERE id_demande = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id_demande); // Utilisation de l'ID de demande passé en paramètre

            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                // Si la demande est supprimée avec succès, rafraîchir la liste des demandes
                ObservableList<DemandeItem> updatedList = FXCollections.observableArrayList(retrieveDemandesList());
                tableView.setItems(updatedList); // Rafraîchir la TableView avec la nouvelle liste de demandes
            } else {
                System.out.println("Aucune demande à supprimer avec l'ID : " + id_demande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de suppression de demande
            System.err.println("Erreur lors de la suppression de la demande : " + e.getMessage());
        }
    }
    // Méthode pour modifier les jours d'une demande
    private static void modifierDemande(String id_demande) {
        try {
            // Récupérer les détails de la demande spécifique à partir de la base de données en utilisant l'ID de la demande
            String query = "SELECT * FROM Demande WHERE id_demande = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id_demande);
            ResultSet resultSet = statement.executeQuery();

            // Vérifier si une demande correspondante est trouvée
            if (resultSet.next()) {
                // Créer une nouvelle fenêtre pour permettre à l'utilisateur de modifier les détails de la demande
                Stage stage = new Stage();
                VBox vbox = new VBox(10);
                vbox.setPadding(new Insets(10));
                vbox.getStyleClass().add("vbox-container"); // Ajout de la classe CSS

                // Créer des champs de texte pour chaque attribut de la demande avec les valeurs actuelles
                TextField nomField = new TextField(resultSet.getString("nom"));
                TextField butField = new TextField(resultSet.getString("but"));
                TextField niveauPhysiqueField = new TextField(resultSet.getString("NiveauPhysique"));
                TextField maladieChroniqueField = new TextField(resultSet.getString("MaladieChronique"));
                TextField ageField = new TextField(resultSet.getString("age"));
                TextField id_userField = new TextField(resultSet.getString("id_user"));
                TextField id_offreField = new TextField(resultSet.getString("id_offre"));
                TextField etatField = new TextField(resultSet.getString("etat"));
                TextField nombreHeureField = new TextField(resultSet.getString("nombreHeure"));
                TextField horaireField = new TextField(resultSet.getTime("horaire").toString());
                TextField lesjoursField = new TextField(resultSet.getString("lesjours"));

                // Ajouter des libellés pour chaque champ
                vbox.getChildren().addAll(
                        new Label("Nom :"), nomField,
                        new Label("But :"), butField,
                        new Label("Niveau Physique :"), niveauPhysiqueField,
                        new Label("Maladie Chronique :"), maladieChroniqueField,
                        new Label("Age :"), ageField,
                        new Label("ID Utilisateur :"), id_userField,
                        new Label("ID Offre :"), id_offreField,
                        new Label("État :"), etatField,
                        new Label("Nombre d'Heures :"), nombreHeureField,
                        new Label("Horaire :"), horaireField,
                        new Label("Lesjours :"), lesjoursField
                );

                // Créer un bouton pour confirmer les modifications
                Button confirmerButton = new Button("Confirmer les modifications");
                confirmerButton.setOnAction(event -> {
                    try {
                        // Mettre à jour les informations de la demande dans la base de données avec les nouvelles valeurs
                        String updateQuery = "UPDATE Demande SET nom=?, but=?, NiveauPhysique=?, MaladieChronique=?, age=?, id_user=?, id_offre=?, etat=?, nombreHeure=?, horaire=?, lesjours=? WHERE id_demande=?";
                        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                        updateStatement.setString(1, nomField.getText());
                        updateStatement.setString(2, butField.getText());
                        updateStatement.setString(3, niveauPhysiqueField.getText());
                        updateStatement.setString(4, maladieChroniqueField.getText());
                        updateStatement.setString(5, ageField.getText());
                        updateStatement.setString(6, id_userField.getText());
                        updateStatement.setString(7, id_offreField.getText());
                        updateStatement.setString(8, etatField.getText());
                        updateStatement.setString(9, nombreHeureField.getText());
                        updateStatement.setString(10, horaireField.getText());
                        updateStatement.setString(11, lesjoursField.getText());
                        updateStatement.setString(12, id_demande);

                        int rowsUpdated = updateStatement.executeUpdate();
                        if (rowsUpdated > 0) {
                            System.out.println("Demande modifiée avec succès !");
                            // Rafraîchir la liste des demandes pour refléter les changements
                            tableView.setItems(FXCollections.observableArrayList(retrieveDemandesList()));
                            // Fermer la fenêtre de modification
                            stage.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        // Gérer les erreurs de mise à jour
                        System.err.println("Erreur lors de la mise à jour de la demande : " + e.getMessage());
                    }
                });

                vbox.getChildren().add(confirmerButton);

                Scene scene = new Scene(vbox, 400, 400);
                scene.getStylesheets().add(ConsultationDemandes.class.getResource("/Styles/StyleFDO.css").toExternalForm());
                stage.setScene(scene);
                stage.setTitle("Modifier la demande");
                stage.show();
            } else {
                System.out.println("Aucune demande trouvée avec l'ID : " + id_demande);
            }

            // Fermer les ressources
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de requête
            System.err.println("Erreur lors de la récupération des détails de la demande : " + e.getMessage());
        }
    }

    public static class DemandeItem extends HBox {
        private final String nom;
        private final String id_demande;
        private final String but;
        private final String niveauPhysique;
        private final String maladieChronique;
        private final String age;
        private final String id_user;
        private final String id_offre;
        private final String etat;
        private final String nombreHeure;
        private final Time horaire;
        private final String lesjours;

        public DemandeItem(String nom, String id_demande, String but, String niveauPhysique, String maladieChronique,
                           String age, String id_user, String id_offre, String etat, String nombreHeure, Time horaire, String lesjours) {
            super();

            this.nom = nom;
            this.id_demande = id_demande;
            this.but = but;
            this.niveauPhysique = niveauPhysique;
            this.maladieChronique = maladieChronique;
            this.age = age;
            this.id_user = id_user;
            this.id_offre = id_offre;
            this.etat = etat;
            this.nombreHeure = nombreHeure;
            this.horaire = horaire;
            this.lesjours = lesjours;

            Label label = new Label("ID demande : " + id_demande + ", But : " + but + ", Niveau physique : " +
                    niveauPhysique + ", Maladie chronique : " + maladieChronique + ", Age : " + age +
                    ", ID utilisateur : " + id_user + ", ID offre : " + id_offre + ", État : " + etat + ", nombreHeure : " + nombreHeure + ", Horaire : " + horaire + ", Lesjours : " + lesjours + ", Nom : " + nom);

            Button supprimerButton = new Button("Supprimer");
            Button modifierButton = new Button("Modifier");

            supprimerButton.setOnAction(event -> {
                DemandeItem demandeItem = (DemandeItem) supprimerButton.getParent();
                String id_demandeToDelete = demandeItem.getId_demande();

                // Passer le nom du client à la méthode supprimerDemande
                supprimerDemande(id_demandeToDelete);
            });

            modifierButton.setOnAction(event -> modifierDemande(id_demande)); // Passer la demande à la méthode modifierJoursDemande

            this.getChildren().addAll(label, supprimerButton, modifierButton);
            this.setSpacing(10);
        }

        public String getId_demande() {
            return id_demande;
        }

        public String getNom() {
            return nom;
        }

        public String getBut() {
            return but;
        }

        public String getNiveauPhysique() {
            return niveauPhysique;
        }

        public String getMaladieChronique() {
            return maladieChronique;
        }

        public String getAge() {
            return age;
        }

        public String getId_user() {
            return id_user;
        }

        public String getId_offre() {
            return id_offre;
        }

        public String getEtat() {
            return etat;
        }

        public String getNombreHeure() {
            return nombreHeure;
        }

        public Time getHoraire() {
            return horaire;
        }

        public String getLesjours() {
            return lesjours;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
