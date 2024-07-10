package com.example.bty.Controllers.graphiqueGCP;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class AdminInterface extends Application {

    private static Connection connection;
    private static TableView<OffreItem> tableView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Accepter ou Refuser Offres");

        // Connexion à la base de données
        connectToDatabase();

        // Création du TableView pour afficher les offres
        tableView = new TableView<>();
        setupTableView();

        VBox root = new VBox(10);
        root.getChildren().add(tableView);


        Scene scene = new Scene(root, 700, 400);
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
        TableColumn<OffreItem, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<OffreItem, String> specialiteColumn = new TableColumn<>("Spécialité");
        specialiteColumn.setCellValueFactory(new PropertyValueFactory<>("specialite"));

        TableColumn<OffreItem, Float> tarifHeureColumn = new TableColumn<>("Tarif par Heure");
        tarifHeureColumn.setCellValueFactory(new PropertyValueFactory<>("tarifHeure"));

        TableColumn<OffreItem, String> idCoachColumn = new TableColumn<>("ID Coach");
        idCoachColumn.setCellValueFactory(new PropertyValueFactory<>("idCoach"));

        TableColumn<OffreItem, String> etatOffreColumn = new TableColumn<>("État Offre");
        etatOffreColumn.setCellValueFactory(new PropertyValueFactory<>("etatOffre"));

        TableColumn<OffreItem, Void> actionColumn = new TableColumn<>("Action");
        actionColumn.setCellFactory(param -> new ButtonCell());

        tableView.getColumns().addAll(idColumn, specialiteColumn, tarifHeureColumn, idCoachColumn, etatOffreColumn, actionColumn);


    }

  /*  private static void refreshOffresList() {
        ObservableList<OffreItem> data = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM offre");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                data.add(new OffreItem(
                        resultSet.getString("id"),
                        resultSet.getString("specialite"),
                        resultSet.getFloat("tarif_heure"),
                        resultSet.getString("id_coach"),
                        resultSet.getString("etatOffre")
                ));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de requête
        }

        tableView.setItems(data);
    }*/

    public static class OffreItem {


        private final StringProperty id = new SimpleStringProperty();
        private final StringProperty specialite = new SimpleStringProperty();
        private final StringProperty tarifHeure = new SimpleStringProperty();
        private final StringProperty idCoach = new SimpleStringProperty();
        private final StringProperty etatOffre = new SimpleStringProperty();

        public OffreItem() {

        }

        public String getId() {
            return id.get();
        }

        public String getSpecialite() {
            return specialite.get();
        }

        public String getTarifHeure() {
            return tarifHeure.get();
        }

        public String getIdCoach() {
            return idCoach.get();
        }

        public String getEtatOffre() {
            return etatOffre.get();
        }




        public StringProperty idProperty() {
            return id;
        }

        public StringProperty specialiteProperty() {
            return specialite;
        }

        public StringProperty tarifHeureProperty() {
            return tarifHeure;
        }

        public StringProperty idCoachProperty() {
            return idCoach;
        }

        public StringProperty etatOffreProperty() {
            return etatOffre;
        }

        public void setId(String id) {
            this.id.set(id);
        }

        public void setSpecialite(String specialite) {
            this.specialite.set(specialite);
        }

        public void setTarifHeure(String tarifHeure) {
            this.tarifHeure.set(tarifHeure);
        }

        public void setIdCoach(String idCoach) {
            this.idCoach.set(idCoach);
        }

        public void setEtatOffre(String etatOffre) {
            this.etatOffre.set(etatOffre);
        }
    }

    private static class ButtonCell extends TableCell<OffreItem, Void> {
        private final Button accepterButton = new Button("Accepter");
        private final Button refuserButton = new Button("Refuser");

        ButtonCell() {
            accepterButton.setOnAction(e -> {
                OffreItem offre = getTableView().getItems().get(getIndex());
                accepterOffre(offre.getId());
            });

            refuserButton.setOnAction(e -> {
                OffreItem offre = getTableView().getItems().get(getIndex());
                refuserOffre(offre.getId());
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                setGraphic(new HBox(10, accepterButton, refuserButton));
            }
        }
    }

    private static void accepterOffre(String id) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Offre SET etatOffre = 'Acceptée' WHERE id = ?");
            statement.setString(1, id);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de base de données
        }
    }

    private static void refuserOffre(String id) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Offre SET etatOffre = 'Refusée' WHERE id = ?");
            statement.setString(1, id);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de base de données
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}