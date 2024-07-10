package com.example.bty.Controllers.graphiqueGCP;

import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class ARD extends Application {

    private static Connection connection;
    private static TableView<DemandeItem> demandesTableView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Accepter ou Refuser Demandes");

        // Connexion à la base de données
        connectToDatabase();

        // Création du TableView pour afficher les demandes
        demandesTableView = new TableView<>();
        setupTableView();

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().add(demandesTableView);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/Styles/tableStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/pidevgym";
            String utilisateur = "root";
            String motDePasse = "";

            connection = DriverManager.getConnection(url, utilisateur, motDePasse);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion
        }
    }

    private static void setupTableView() {
        TableColumn<DemandeItem, String> idColumn = new TableColumn<>("ID Demande");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_demande"));

        TableColumn<DemandeItem, String> butColumn = new TableColumn<>("But");
        butColumn.setCellValueFactory(new PropertyValueFactory<>("but"));

        TableColumn<DemandeItem, String> niveauPhysiqueColumn = new TableColumn<>("Niveau Physique");
        niveauPhysiqueColumn.setCellValueFactory(new PropertyValueFactory<>("niveauPhysique"));

        TableColumn<DemandeItem, String> maladieChroniqueColumn = new TableColumn<>("Maladie Chronique");
        maladieChroniqueColumn.setCellValueFactory(new PropertyValueFactory<>("maladieChronique"));

        TableColumn<DemandeItem, String> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

        TableColumn<DemandeItem, String> idUserColumn = new TableColumn<>("ID Utilisateur");
        idUserColumn.setCellValueFactory(new PropertyValueFactory<>("id_user"));

        TableColumn<DemandeItem, String> idOffreColumn = new TableColumn<>("ID Offre");
        idOffreColumn.setCellValueFactory(new PropertyValueFactory<>("id_offre"));

        TableColumn<DemandeItem, String> etatColumn = new TableColumn<>("État");
        etatColumn.setCellValueFactory(new PropertyValueFactory<>("etat"));

        TableColumn<DemandeItem, Time> horaireColumn = new TableColumn<>("Horaire");
        horaireColumn.setCellValueFactory(new PropertyValueFactory<>("horaire"));

        TableColumn<DemandeItem, String> lesjoursColumn = new TableColumn<>("Les Jours");
        lesjoursColumn.setCellValueFactory(new PropertyValueFactory<>("lesjours"));

        TableColumn<DemandeItem, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(param -> new ButtonCell());

        demandesTableView.getColumns().addAll(idColumn, butColumn, niveauPhysiqueColumn, maladieChroniqueColumn,
                ageColumn, idUserColumn, idOffreColumn, etatColumn, horaireColumn, lesjoursColumn, actionColumn);

        refreshDemandesList();
    }

    private static void refreshDemandesList() {
        ObservableList<DemandeItem> data = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM demande");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                data.add(new DemandeItem(
                        resultSet.getString("id_demande"),
                        resultSet.getString("but"),
                        resultSet.getString("NiveauPhysique"),
                        resultSet.getString("MaladieChronique"),
                        resultSet.getString("age"),
                        resultSet.getString("id_user"),
                        resultSet.getString("id_offre"),
                        resultSet.getString("etat"),
                        resultSet.getTime("horaire"),
                        resultSet.getString("lesjours")
                ));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de requête
        }

        demandesTableView.setItems(data);
    }

    public static class DemandeItem {
        private final SimpleStringProperty id_demande;
        private final SimpleStringProperty but;
        private final SimpleStringProperty niveauPhysique;
        private final SimpleStringProperty maladieChronique;
        private final SimpleStringProperty age;
        private final SimpleStringProperty id_user;
        private final SimpleStringProperty id_offre;
        private final SimpleStringProperty etat;
        private final SimpleObjectProperty<Time> horaire;
        private final SimpleStringProperty lesjours;

        public DemandeItem(String id_demande, String but, String niveauPhysique, String maladieChronique,
                           String age, String id_user, String id_offre, String etat, Time horaire, String lesjours) {
            this.id_demande = new SimpleStringProperty(id_demande);
            this.but = new SimpleStringProperty(but);
            this.niveauPhysique = new SimpleStringProperty(niveauPhysique);
            this.maladieChronique = new SimpleStringProperty(maladieChronique);
            this.age = new SimpleStringProperty(age);
            this.id_user = new SimpleStringProperty(id_user);
            this.id_offre = new SimpleStringProperty(id_offre);
            this.etat = new SimpleStringProperty(etat);
            this.horaire = new SimpleObjectProperty<>(horaire);
            this.lesjours = new SimpleStringProperty(lesjours);
        }

        public String getId_demande() {
            return id_demande.get();
        }

        public String getBut() {
            return but.get();
        }

        public String getNiveauPhysique() {
            return niveauPhysique.get();
        }

        public String getMaladieChronique() {
            return maladieChronique.get();
        }

        public String getAge() {
            return age.get();
        }

        public String getId_user() {
            return id_user.get();
        }

        public String getId_offre() {
            return id_offre.get();
        }

        public String getEtat() {
            return etat.get();
        }

        public Time getHoraire() {
            return horaire.get();
        }

        public String getLesjours() {
            return lesjours.get();
        }
    }

    private static class ButtonCell extends TableCell<DemandeItem, Void> {
        private final Button accepterButton = new Button("Accepter");
        private final Button refuserButton = new Button("Refuser");
        private final Button modifierJoursButton = new Button("Modifier les jours");

        ButtonCell() {
            accepterButton.setOnAction(e -> {
                DemandeItem demande = getTableView().getItems().get(getIndex());
                accepterDemande(demande.getId_demande());
            });

            refuserButton.setOnAction(e -> {
                DemandeItem demande = getTableView().getItems().get(getIndex());
                refuserDemande(demande.getId_demande());
            });

            modifierJoursButton.setOnAction(e -> {
                DemandeItem demande = getTableView().getItems().get(getIndex());
                afficherInterfaceModifierJours(demande.getId_demande());
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(new HBox(10, accepterButton, refuserButton, modifierJoursButton));
            }
        }
    }

    private static void accepterDemande(String id_demande) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE demande SET etat = 'Acceptée' WHERE id_demande = ?");
            statement.setString(1, id_demande);
            statement.executeUpdate();
            statement.close();

            // Afficher une interface pour ajouter l'horaire et autre information
            afficherInterfaceAjoutHoraire(id_demande);

            // Afficher un message au client (demande acceptée)
            afficherMessageClientAccepte();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de base de données
        }
    }

    private static void refuserDemande(String id_demande) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE demande SET etat = 'Refusée' WHERE id_demande = ?");
            statement.setString(1, id_demande);
            statement.executeUpdate();
            statement.close();

            // Afficher un message au client (demande refusée)
            afficherMessageClientRefuse();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de base de données
        }
    }

    private static void afficherInterfaceAjoutHoraire(String id_demande) {
        Stage ajoutHoraireStage = new Stage();
        ajoutHoraireStage.setTitle("Ajouter Horaire");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label label = new Label("Entrez l'horaire :");
        TextField horaireField = new TextField();

        Button ajouterButton = new Button("Ajouter");
        ajouterButton.setOnAction(e -> {
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE demande SET horaire = ? WHERE id_demande = ?");
                statement.setString(1, horaireField.getText());
                statement.setString(2, id_demande);
                statement.executeUpdate();
                statement.close();

                ajoutHoraireStage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Gérer les erreurs de base de données
            }
        });

        vbox.getChildren().addAll(label, horaireField, ajouterButton);

        Scene scene = new Scene(vbox, 300, 150);
        ajoutHoraireStage.setScene(scene);
        ajoutHoraireStage.show();
    }

    private static void afficherInterfaceModifierJours(String id_demande) {
        Stage modifierJoursStage = new Stage();
        modifierJoursStage.setTitle("Modifier les jours");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));

        Label label = new Label("Entrez les nouveaux jours :");
        TextField joursField = new TextField();

        Button modifierButton = new Button("Modifier");
        modifierButton.setOnAction(e -> {
            try {
                PreparedStatement statement = connection.prepareStatement("UPDATE demande SET lesjours = ? WHERE id_demande = ?");
                statement.setString(1, joursField.getText());
                statement.setString(2, id_demande);
                statement.executeUpdate();
                statement.close();

                modifierJoursStage.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Gérer les erreurs de base de données
            }
        });

        vbox.getChildren().addAll(label, joursField, modifierButton);

        Scene scene = new Scene(vbox, 300, 150);
        modifierJoursStage.setScene(scene);
        modifierJoursStage.show();
    }

    private static void afficherMessageClientAccepte() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Demande Acceptée");
        alert.setHeaderText(null);
        alert.setContentText("Votre demande est acceptée.");
        alert.showAndWait();
    }

    private static void afficherMessageClientRefuse() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Demande Refusée");
        alert.setHeaderText(null);
        alert.setContentText("Votre demande est refusée.");
        alert.showAndWait();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        // Fermer la connexion à la base de données lors de l'arrêt de l'application
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
