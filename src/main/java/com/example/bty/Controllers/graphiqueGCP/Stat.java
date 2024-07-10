package com.example.bty.Controllers.graphiqueGCP;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stat extends Application {

    private static Connection connection;

    // Méthode pour établir la connexion à la base de données
    private static void connectToDatabase() throws SQLException {
        // Remplacez les informations de connexion par les vôtres
        String url = "jdbc:mysql://localhost:3306/pidevgym";
        String user = "root";
        String password = "";
        connection = DriverManager.getConnection(url, user, password);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("Statistiques des offres");

        // Établir la connexion à la base de données
        connectToDatabase();

        // Récupérer les données sur les demandes depuis la base de données
        List<ConsultationDemandes.DemandeItem> demandesList = retrieveDemandesList();

        // Calculer les statistiques sur les offres
        Map<String, Integer> offreStats = calculerStatistiquesOffres(demandesList);

        // Créer le graphique à barres pour afficher les statistiques des offres
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Statistiques des offres les plus demandées");
        xAxis.setLabel("Offre");
        yAxis.setLabel("Nombre de demandes");

        // Ajouter les données au graphique à barres
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : offreStats.entrySet()) {
            dataSeries.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        barChart.getData().add(dataSeries);

        HBox root = new HBox(barChart);
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Map<String, Integer> calculerStatistiquesOffres(List<ConsultationDemandes.DemandeItem> demandesList) {
        Map<String, Integer> offreStats = new HashMap<>();

        // Parcourir chaque demande dans la liste
        for (ConsultationDemandes.DemandeItem demande : demandesList) {

            String offreId = demande.getId_offre(); // Récupérer l'ID de l'offre

            // Mettre à jour le compteur pour cet ID d'offre
            offreStats.put(offreId, offreStats.getOrDefault(offreId, 0) + 1);
        }

        return offreStats;
    }

    // Méthode pour récupérer les demandes depuis la base de données
    static List<ConsultationDemandes.DemandeItem> retrieveDemandesList() {
        List<ConsultationDemandes.DemandeItem> demandesList = new ArrayList<>();
        try {
            // Vérifier si la connexion est établie
            if (connection == null || connection.isClosed()) {
                // Si la connexion n'est pas établie ou est fermée, tenter de se reconnecter
                connectToDatabase();
            }

            // Vérifier à nouveau la connexion après la tentative de reconnexion
            if (connection != null) {
                // Exécuter une requête pour récupérer les demandes du client depuis la base de données
                String query = "SELECT * FROM Demande ";
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                // Ajouter les demandes à la liste
                while (resultSet.next()) {
                    ConsultationDemandes.DemandeItem demandeItem = new ConsultationDemandes.DemandeItem(
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
            } else {
                System.err.println("La connexion à la base de données a échoué.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de requête
            System.err.println("Erreur lors de la récupération des demandes : " + e.getMessage());
        }
        return demandesList;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
