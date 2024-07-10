package com.example.bty.Controllers;

import com.example.bty.Controllers.CourController.CourMembre;
import com.example.bty.Controllers.EvenementController.DetailsEvenementWindow;
import com.example.bty.Controllers.EvenementController.clientVitrine;
import com.example.bty.Controllers.ProduitController.VitrineClient;
import com.example.bty.Controllers.ReclamationController.AjouterReclamation;
import com.example.bty.Controllers.graphiqueGCP.ConsultationDemandes;
import com.example.bty.Controllers.graphiqueGCP.FD;
import com.example.bty.Controllers.graphiqueGCP.Formoffre;
import com.example.bty.Entities.Reclamation;
import com.example.bty.Entities.User;
import com.example.bty.Utils.ConnexionDB;
import com.example.bty.Utils.Session;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardMembre extends Application {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/pidevgym";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private Connection connection;
    private BorderPane root;
    User user;


    private DetailsEvenementWindow detailsEvenementWindow;

    public DashboardMembre(){
        connection = ConnexionDB.getInstance().getConnexion();
    }
    public void start(Stage primaryStage) throws Exception {

        root = new BorderPane();
        AnchorPane leftDashboard = createLeftDashboard(primaryStage);
        BorderPane root = new BorderPane();
        root.setLeft(leftDashboard);
        detailsEvenementWindow = new DetailsEvenementWindow();
        BarChart<String, Number> eventDateBarChart = createEventDateBarChartForClient();
        //root.setCenter(eventDateBarChart);
// Créer un SplitPane pour diviser la fenêtre horizontalement
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(eventDateBarChart, new AnchorPane()); // Ajouter le graphique et un panneau vide
        //splitPane.setDividerPositions(0.5f); // Positionner le diviseur à mi-chemin

        /////farah
        // Récupérer les données sur les demandes depuis la base de données
        List<ConsultationDemandes.DemandeItem> demandesList = retrieveDemandesList();

        // Calculer les statistiques sur les offres
        Map<String, Integer> offreStats = calculerStatistiquesOffres(demandesList);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setId("offreChart");
        barChart.setTranslateY(109);
        barChart.setMinWidth(680);
        barChart.setMinHeight(150);  // Ajuster la hauteur minimale
        barChart.setMaxHeight(500);

        barChart.setTitle("Statistiques des offres les plus demandées");
        xAxis.setLabel("Offre");
       // barChart.setStyle("-fx-background-color: #E9967A");
        yAxis.setLabel("Nombre de demandes");

// Ajouter les données au graphique à barres
        XYChart.Series<String, Number> dataSeries = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : offreStats.entrySet()) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(entry.getKey(), entry.getValue());
            dataSeries.getData().add(data);


        }

        barChart.getData().add(dataSeries);
//        for (XYChart.Data<String, Number> data : dataSeries.getData()) {
//            Node node = data.getNode();
//            String style = "-fx-bar-fill: #000000 !important;"; // Ajout de !important
//            node.setStyle(style);
//        }
        HBox root1 = new HBox(barChart);

        ///////////////


        root.setCenter(splitPane);
        root.setRight(root1);
        Scene scene = new Scene(root, 1366, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dashboard membre");
        primaryStage.show();
        scene.getStylesheets().add(getClass().getResource("/dashboardDesign.css").toExternalForm());

    }




    public BarChart<String, Number> createEventDateBarChartForClient() {
        ObservableList<XYChart.Series<String, Number>> seriesList = FXCollections.observableArrayList();
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> eventDateBarChart = new BarChart<>(xAxis, yAxis);

        try {
            String query = "SELECT nomEvenement, Date FROM evenement";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            Map<String, XYChart.Series<String, Number>> eventSeriesMap = new HashMap<>();
            eventDateBarChart.setMinHeight(150);  // Ajuster la hauteur minimale
            eventDateBarChart.setMaxHeight(500);
            eventDateBarChart.setMaxWidth(1900);
            eventDateBarChart.setMinWidth(550);

            // Ajuster la hauteur maximale


            while (resultSet.next()) {
                String eventName = resultSet.getString("nomEvenement");
                java.sql.Date eventDate = resultSet.getDate("Date");
                LocalDate eventLocalDate = eventDate.toLocalDate();
                LocalDate currentDate = LocalDate.now();

                // Filtrer les événements passés
                if (eventLocalDate.isAfter(currentDate)) {
                    long daysUntilEvent = calculateDaysUntilEvent(eventDate);

                    XYChart.Series<String, Number> series = eventSeriesMap.get(eventName);
                    if (series == null) {
                        series = new XYChart.Series<>();
                        series.setName(eventName);
                        eventSeriesMap.put(eventName, series);
                        seriesList.add(series);
                    }

                    series.getData().add(new XYChart.Data<>(eventName, daysUntilEvent));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        eventDateBarChart.setData(seriesList);
        eventDateBarChart.setTitle("Durée jusqu'à la Date des Événements");
        xAxis.setLabel("Événements");
        yAxis.setLabel("Nombre du jour restant");

        return eventDateBarChart;
    }



    private long calculateDaysUntilEvent(Date eventDate) {
        // Convertir java.sql.Date en java.time.LocalDate
        LocalDate eventLocalDate = eventDate.toLocalDate();

        // Obtenir la date actuelle
        LocalDate currentDate = LocalDate.now();

        // Calculer la différence de jours entre la date actuelle et la date de l'événement
        return Math.abs(currentDate.until(eventLocalDate).getDays());
    }

    private Map<String, Integer> calculerStatistiquesOffres(List<ConsultationDemandes.DemandeItem> demandesList) {
        Map<String, Integer> offreStats = new HashMap<>();

        // Parcourir chaque demande dans la liste
        for (ConsultationDemandes.DemandeItem demande : demandesList) {

            String offreId = demande.getNom(); // Récupérer l'ID de l'offre

            // Mettre à jour le compteur pour cet ID d'offre
            offreStats.put(offreId, offreStats.getOrDefault(offreId, 0) + 1);
        }

        return offreStats;
    }

    // Méthode pour récupérer les demandes depuis la base de données
    List<ConsultationDemandes.DemandeItem> retrieveDemandesList() {
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

    private AnchorPane createLeftDashboard(Stage primaryStage) {
      User loggedInUser = Session.getInstance().getLoggedInUser();
        AnchorPane mainForm = new AnchorPane();
        mainForm.setPrefSize(1100, 900);

        //AnchorPane dashboardAdmin = new AnchorPane();



        AnchorPane dashboardAdmin = new AnchorPane();
        //leftAnchorPane.setTranslateY(-80);
        dashboardAdmin.setTranslateX(0);
        //leftAnchorPane.setPrefSize(70, 280);
//        dashboardAdmin.setBottomAnchor(mainForm, 40.0);
//        dashboardAdmin.setLeftAnchor(mainForm, 40.0);
        dashboardAdmin.setTranslateY(0);
        dashboardAdmin.setPrefSize(234, 1500);
        dashboardAdmin.getStyleClass().add("border-pane");


        FontAwesomeIconView usernameAdmin = createFontAwesomeIconView("USER", "WHITE", 50, 82, 91);
        Label welcomeLabel = createLabel("Bienvenue, " , "Arial Bold", 15, 78, 101, "WHITE");
        Label usernameLabel = createLabel(loggedInUser.getName(), "Arial Bold", 20, 78, 120,"WHITE");
        // Line line = createLine(-100, 152, 100, 152, 111);
        Line line = createColoredLine(-100, 152, 100, 152, 111, "WHITE");

        Button DashboardBtn = createButton("Acceuil", 22, 186);
        Button CoursBtn = createButton("Cours", 22, 234);
        Button eventsBtn = createButton("Evenements", 22, 276);
        Button demandeBtn = createButton("Demande Coahing", 22, 319);
        Button offreAdminBtn = createButton("Ajouter Offre", 22, 361);
        Button storeAdminBtn = createButton("Store", 22, 405);

        DashboardBtn.setOnAction(event -> {
            // Instancier et afficher la vue DashboardVitrineController
            DashboardMembre m = new DashboardMembre();
            try {
                m.start(primaryStage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        CoursBtn.setOnAction(event -> {
            // Instancier et afficher la vue DashboardVitrineController
            CourMembre c = new CourMembre();
            c.start(primaryStage);
        });


        eventsBtn.setOnAction(event -> {
            /*DashbordEvenement e = new DashbordEvenement();
            e.start(primaryStage);*/
            clientVitrine e = new clientVitrine();
            e.start(primaryStage);

        });


        demandeBtn.setOnAction(event -> {
            // Instancier et afficher la vue DashboardVitrineController
            FD f = new FD();
            f.start(primaryStage);
        });


        offreAdminBtn.setOnAction(event -> {



            Formoffre o = new Formoffre();
            o.start(primaryStage);


        });


        storeAdminBtn.setOnAction(event -> {
            // Instancier et afficher la vue DashboardVitrineController
            VitrineClient v = new VitrineClient();
            v.start(primaryStage);
        });


        Line line2 = createColoredLine(-100, 449, 100, 449, 112, "WHITE");

//        Button profileAdminBtn = createButton("Profile", 22, 462);
        Button logoutBtn = createButton("Logout", 22, 503);

// Add event handler to logoutBtn

// Add event handler to logoutBtn
        logoutBtn.setOnAction(event -> {
            // Close all open interfaces
            Stage primaryStage1 = (Stage) logoutBtn.getScene().getWindow(); // Assuming logoutBtn is in the same scene as the primaryStage
            primaryStage1.close();

            // Open the LoginGYM.fxml interface
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginGym.fxml"));
            Parent root;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        });


        FontAwesomeIconView[] icons = {
                createFontAwesomeIconView("HOME", "WHITE", 20, 38, 212),
                createFontAwesomeIconView("USER", "WHITE", 20, 38, 258),
                createFontAwesomeIconView("USERS", "WHITE", 20, 38, 300),
                createFontAwesomeIconView("BOOK", "WHITE", 20, 38, 343),
                createFontAwesomeIconView("CALENDAR", "WHITE", 20, 38, 385),

                createFontAwesomeIconView("SHOPPING_CART", "WHITE", 20, 38, 429),

//                createFontAwesomeIconView("ID_CARD", "WHITE", 20, 38, 486),
                createFontAwesomeIconView("EXTERNAL_LINK", "WHITE", 20, 38, 529)
        };

        VBox reportContainer = new VBox();
        reportContainer.setLayoutX(13);
        reportContainer.setLayoutY(608);
        reportContainer.setPrefSize(180, 91);
        reportContainer.getStyleClass().add("report_container");


        Text reportText = new Text("Signaler une suggestion?");
        reportText.getStyleClass().add("report_text");



        Button reportButton = createButton("Signaler", 0, 0);
        reportButton.setOnAction(event -> openAjouterReclamationInterface(primaryStage));

        reportButton.getStyleClass().add("report_button");



        reportContainer.getChildren().addAll(reportText, reportButton);

        StackPane contentPlaceholder = new StackPane();
        contentPlaceholder.setLayoutX(220);
        contentPlaceholder.setLayoutY(0);

        dashboardAdmin.getChildren().addAll(
                usernameAdmin, welcomeLabel, usernameLabel, line,
                DashboardBtn,CoursBtn, eventsBtn, demandeBtn, offreAdminBtn,
                storeAdminBtn, line2,
                logoutBtn, icons[0], icons[1], icons[2], icons[3],
                icons[4], icons[5], icons[6], reportContainer,
                contentPlaceholder
        );

        mainForm.getChildren().addAll(dashboardAdmin);
        return dashboardAdmin;


    }
    private void openAjouterReclamationInterface(Stage primaryStage) {
        AjouterReclamation ajouterReclamation = new AjouterReclamation();
        try {
            ajouterReclamation.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FontAwesomeIconView createFontAwesomeIconView(String glyphName, String fill, double size, double layoutX, double layoutY) {
        FontAwesomeIconView iconView = new FontAwesomeIconView();
        iconView.setGlyphName(glyphName);

        // Définir la couleur de remplissage ici
        iconView.setFill(javafx.scene.paint.Paint.valueOf(fill));

        iconView.setSize(String.valueOf(size));

        iconView.setLayoutX(layoutX);
        iconView.setLayoutY(layoutY);
        return iconView;
    }


    private Label createLabel(String text, String fontName, double fontSize, double layoutX, double layoutY, String textFill) {
        Label label = new Label(text);
        label.setFont(new javafx.scene.text.Font(fontName, fontSize));
        label.setLayoutX(layoutX);
        label.setLayoutY(layoutY);

        // Définir la couleur du texte ici
        label.setTextFill(javafx.scene.paint.Paint.valueOf(textFill));

        return label;
    }


    private Line createLine(double startX, double startY, double endX, double endY, double layoutX) {
        Line line = new Line(startX, startY, endX, endY);
        line.setLayoutX(layoutX);
        return line;
    }


    private Line createColoredLine(double startX, double startY, double endX, double endY, double layoutX, String strokeColor) {
        Line line = new Line(startX, startY, endX, endY);
        line.setLayoutX(layoutX);

        // Définir la couleur de la ligne ici
        line.setStroke(javafx.scene.paint.Paint.valueOf(strokeColor));

        return line;
    }

    private Button createButton(String text, double layoutX, double layoutY) {
        Button button = new Button(text);
        button.setLayoutX(layoutX);
        button.setLayoutY(layoutY);
        button.setMnemonicParsing(false);
        button.getStyleClass().add("nav-btn");
        button.setPrefSize(180, 35);
        return button;
    }

    public static void main(String[] args) {
        launch(args);
    }
}