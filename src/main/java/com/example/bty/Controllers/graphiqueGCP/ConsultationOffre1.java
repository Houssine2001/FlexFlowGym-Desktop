package com.example.bty.Controllers.graphiqueGCP;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultationOffre1 extends Application {

    private static Connection connection;
    private static TableView<OffreItem> tableView;
    private static final int MAX_STARS = 5;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Consultation des offres");

        // Connexion à la base de données
        connectToDatabase();

        // Création du TableView et de ses colonnes
        tableView = new TableView<>();

        TableColumn<OffreItem, String> idCol = new TableColumn<>("ID_Offre");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<OffreItem, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));

        TableColumn<OffreItem, String> specialiteCol = new TableColumn<>("Spécialité");
        specialiteCol.setCellValueFactory(new PropertyValueFactory<>("specialite"));

        TableColumn<OffreItem, String> tarifCol = new TableColumn<>("Tarif");
        tarifCol.setCellValueFactory(new PropertyValueFactory<>("tarif"));

        TableColumn<OffreItem, String> coachCol = new TableColumn<>("Coach");
        coachCol.setCellValueFactory(new PropertyValueFactory<>("coach"));

        TableColumn<OffreItem, String> etatCol = new TableColumn<>("État");
        etatCol.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Colonne d'évaluation avec un type Void
        TableColumn<OffreItem, Void> evaluationCol = new TableColumn<>("Évaluation");
        evaluationCol.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    // Créer des étoiles pour l'évaluation
                    HBox starsBox = new HBox();
                    for (int i = 0; i < MAX_STARS; i++) {
                        Label star = new Label("\u2606"); // Utilise le symbole d'étoile vide
                        star.setStyle("-fx-font-size: 24px;");
                        star.setTextFill(Color.GRAY); // Couleur par défaut pour les étoiles non sélectionnées
                        int rating = i + 1;
                        star.setOnMouseClicked(event -> {
                            // Lorsque vous cliquez sur une étoile, mettez à jour l'évaluation et la couleur des étoiles
                            setRating(rating);
                            updateStarsColor(starsBox, rating);
                        });
                        // Ajouter l'événement pour changer la couleur lorsque la souris entre et quitte
                        star.setOnMouseEntered(event -> star.setTextFill(Color.YELLOW));
                        star.setOnMouseExited(event -> {
                            if (starsBox.getChildren().indexOf(star) >= rating) {
                                star.setTextFill(Color.YELLOW); // Étoile sélectionnée
                            } else {
                                star.setTextFill(Color.GRAY); // Étoile non sélectionnée
                            }
                        });
                        starsBox.getChildren().add(star);
                    }
                    setGraphic(starsBox);
                }
            }
        });

        // Colonne de moyenne des notes
        TableColumn<OffreItem, Double> moyenneCol = new TableColumn<>("Moyenne des notes");
        moyenneCol.setCellValueFactory(new PropertyValueFactory<>("moyenne"));

        tableView.getColumns().addAll(idCol, nomCol, specialiteCol, tarifCol, coachCol, etatCol, evaluationCol, moyenneCol);

        // Charger les données dans le TableView
        try {
            List<OffreItem> offreItems = retrieveOffreItemsArray();
            tableView.getItems().addAll(offreItems);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de récupération des données
        }

        // Création de la disposition verticale
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.getChildren().add(tableView);

        Scene scene = new Scene(vbox, 800, 500);
        scene.getStylesheets().add(getClass().getResource("/Styles/eval.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void updateStarsColor(HBox starsBox, int rating) {
        // Mettez à jour la couleur des étoiles en fonction de l'évaluation
        for (Node star : starsBox.getChildren()) {
            if (starsBox.getChildren().indexOf(star) < rating) {
                ((Label) star).setTextFill(Color.YELLOW); // Étoile sélectionnée
            } else {
                ((Label) star).setTextFill(Color.GRAY); // Étoile non sélectionnée
            }
        }
    }

    private void setRating(int rating) {
        OffreItem selectedOffreItem = tableView.getSelectionModel().getSelectedItem(); // Obtenir l'offre sélectionnée
        if (selectedOffreItem != null) {
            // Récupérer le nom de l'offre sélectionnée
            String offreNom = selectedOffreItem.getNom();

            // Enregistrer l'évaluation dans la base de données
            saveRatingToDatabase(offreNom, rating);

            // Afficher un message de confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Évaluation enregistrée");
            alert.setHeaderText(null);
            alert.setContentText("Votre évaluation a été enregistrée avec succès.");
            alert.showAndWait();

            // Mettre à jour la couleur des étoiles pour refléter l'évaluation actuelle
            HBox starsBox = (HBox) tableView.lookup(".table-row-cell:selected .hbox"); // Récupérer la boîte d'étoiles
            if (starsBox != null) {
                updateStarsColor(starsBox, rating);
            } else {
                System.out.println("La boîte d'étoiles n'a pas été trouvée.");
            }
        } else {
            // Afficher un message d'erreur si aucune offre n'est sélectionnée
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une offre pour évaluer.");
            alert.showAndWait();
        }
    }

    private void saveRatingToDatabase(String nomOffre, int rating) {
        String url = "jdbc:mysql://localhost:3306/pidevgym";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO evaluations (nom, note) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, nomOffre);
            statement.setInt(2, rating);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de l'évaluation dans la base de données : " + e.getMessage());
        }
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

    public Node getView() {
        // Retourner la vue de la consultation des offres (dans ce cas, le TableView)
        return tableView;
    }

    public Node start() {
        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.getChildren().add(tableView);

        return vbox;
    }

    private List<OffreItem> retrieveOffreItemsArray() throws SQLException {
        List<OffreItem> offresList = new ArrayList<>();
        try {
            String query = "SELECT Offre.*, (AVG(evaluations.note) * 4) AS moyenne " +
                    "FROM Offre LEFT JOIN evaluations ON Offre.nom = evaluations.nom " +
                    "WHERE Offre.etatOffre = 'Acceptée' " +
                    "GROUP BY Offre.nom";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                OffreItem offreItem = new OffreItem(
                        resultSet.getString("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("specialite"),
                        resultSet.getString("tarif_heure"),
                        resultSet.getString("id_Coach"),
                        resultSet.getString("etatOffre"),
                        resultSet.getDouble("moyenne")
                );
                offresList.add(offreItem);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return offresList;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class OffreItem {
        private String id;
        private String nom;
        private String specialite;
        private String tarif;
        private String coach;
        private String etat;
        private Double moyenne;

        public OffreItem(String id, String nom, String specialite, String tarif, String coach, String etat, Double moyenne) {
            this.id = id;
            this.nom = nom;
            this.specialite = specialite;
            this.tarif = tarif;
            this.coach = coach;
            this.etat = etat;
            this.moyenne = moyenne;
        }

        public String getId() {
            return id;
        }

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

        public Double getMoyenne() {
            return moyenne;
        }
    }
}