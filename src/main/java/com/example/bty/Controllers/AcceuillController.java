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
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class AcceuillController {

    public Label dashboard_NM;
    public Label dashboard_NC;
    public Label dashboard_TI;
    public Text mostRequestedEventText;
    public Text mostLikedcours;
    public Text LastLikedCourse;
    @FXML
    private AnchorPane dashboardAnchorPane;


    @FXML
    private LineChart<String, Number> chart;




    private Connection connexion;
    private PreparedStatement pst;
    private Statement ste ;
    public AcceuillController() {
        connexion = ConnexionDB.getInstance().getConnexion();
    }
    private ServiceProduit produitServices = new ServiceProduit();

    @FXML
    private void initialize() {


        int nombreUtilisateursMembre = getNombreUtilisateursMembre();

        if (nombreUtilisateursMembre != 0) {
            dashboard_NM.setText( nombreUtilisateursMembre+"");
        } else {
            dashboard_NM.setText("Aucun membre");
        }


        int nombreCoach = getNombreCoach();

        if (nombreCoach  != 0) {
            dashboard_NC.setText( nombreCoach+"");
        } else {
            dashboard_NC.setText("Aucun Coach");
        }


        int traite = getUnresolvedComplaintCount();
        if (traite  != 0) {
            dashboard_TI.setText( traite+"");
        } else {
            dashboard_TI.setText("Aucun reclamation non traite");
        }



                // Chargement initial des données dans la table


        // Button ajoutProduitButton = new Button("Ajout");




        // Appeler le service pour obtenir le chiffre d'affaires

        chargerDonneesGraphique();

        String mostRequestedEvent = getMostRequestedEvent();

        // Update the Text element in the FXML file with the retrieved event name
        mostRequestedEventText.setText("L'évenement le plus demandé : " + mostRequestedEvent);

        String mostLikedCours = getMostLikedCourse();

        // Update the Text element in the FXML file with the retrieved event name
        mostLikedcours.setText("Le cour le plus aimé : "+ mostLikedCours);


    }


    private String getMostRequestedEvent() {
        // Implement logic to query the database and get the most requested event name
        // You might need to replace the following placeholder code with your actual database query

        String mostRequestedEvent = "Aucun evenement"; // Default value
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgym", "root", "")) {
            String query = "SELECT nomEvenement, COUNT(*) as count FROM evenement GROUP BY nomEvenement ORDER BY count DESC LIMIT 1";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()){


            if (resultSet.next()) {
                mostRequestedEvent = resultSet.getString("nomEvenement");
            }

        } }catch (SQLException e) {
            e.printStackTrace();
        }

        return mostRequestedEvent;
    }


    private int getUnresolvedComplaintCount() {
        // Implement logic to query the database and get the count of unresolved complaints
        // You might need to replace the following placeholder code with your actual database query

        int unresolvedComplaintCount = 0; // Default value

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgym", "root", "")) {
            String query = "SELECT COUNT(*) as count FROM reclamation WHERE etat = 'Non traite'";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    unresolvedComplaintCount = resultSet.getInt("count");
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return unresolvedComplaintCount;
    }



    private String getMostLikedCourse() {
        // Implement logic to query the database and get the most liked course name
        // You might need to replace the following placeholder code with your actual database query

        String mostLikedCourse = "Aucun cours"; // Default value
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgym", "root", "")) {
            String query = "SELECT nomCour, SUM(rating) as totalLikes FROM rating GROUP BY nomCour ORDER BY totalLikes DESC LIMIT 1";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()){


            if (resultSet.next()) {
                mostLikedCourse = resultSet.getString("nomCour");
            }

        } }catch (SQLException e) {
            e.printStackTrace();
        }

        return mostLikedCourse;
    }






    public int getNombreUtilisateursMembre() {
        int nombreUtilisateursMembre = 0;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgym", "root", "")) {
            String sql = "SELECT COUNT(*) AS nombre FROM user WHERE role = 'MEMBRE'";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    nombreUtilisateursMembre = resultSet.getInt("nombre");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion ou de requête SQL ici
        }

        return nombreUtilisateursMembre;
    }

    public int getNombreCoach() {
        int nombreCoach = 0;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgym", "root", "")) {
            String sql = "SELECT COUNT(*) AS nombre FROM user WHERE role = 'COACH'";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    nombreCoach = resultSet.getInt("nombre");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion ou de requête SQL ici
        }

        return nombreCoach;
    }




    @FXML
    private void chargerDonneesGraphique() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Chiffre d'affaires par jour");

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgymweb", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT date_commande, SUM(montant) FROM commande GROUP BY date_commande")) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            while (resultSet.next()) {
                Timestamp timestamp = resultSet.getTimestamp("date_commande");
                Date date = new Date(timestamp.getTime());

                double montant = resultSet.getDouble("SUM(montant)");

                series.getData().add(new XYChart.Data<>(dateFormat.format(date), montant));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        dashboardAnchorPane.getChildren().add(chart); // Ajoutez le graphique à l'AnchorPane
        chart.getData().add(series);

        // Ajouter la légende personnalisée
        Circle legendCircle = new Circle(5, Color.BLUE); // Ajustez la couleur du cercle selon vos préférences
        Label legendLabel = new Label("Chiffre d'affaires par jour");
        legendLabel.setTextFill(Color.BLACK); // Ajustez la couleur du texte selon vos préférences

        HBox legendBox = new HBox(10, legendCircle, legendLabel);
        legendBox.setAlignment(Pos.CENTER);

        dashboardAnchorPane.getChildren().add(legendBox);
        AnchorPane.setBottomAnchor(legendBox, 18.0); // Ajustez la position verticale selon vos préférences
        AnchorPane.setLeftAnchor(legendBox, (dashboardAnchorPane.getWidth() + 1000) / 2); // Ajustez la position horizontale selon vos préférences
    }
















   /* private void actualiserTable() {
        // Appeler la méthode consulterProduits et charger les données dans la table
        List<Produit> produits = consulterProduits();
        tableView.getItems().setAll(produits);
    }*/

    // Autres méthodes









    /*private void actualiserTable() {
        ObservableList<Produit> produits = FXCollections.observableArrayList(produitServices.consulterProduits());
        tableView.setItems(produits);
    }*/




}


