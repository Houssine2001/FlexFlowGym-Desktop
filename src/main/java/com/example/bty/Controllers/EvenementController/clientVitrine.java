package com.example.bty.Controllers.EvenementController;

import static com.example.bty.Controllers.EvenementController.DetailsEvenementWindow.createDetailsCard;

import com.example.bty.Controllers.CourController.CourMembre;
import com.example.bty.Controllers.DashboardMembre;
import com.example.bty.Controllers.ProduitController.VitrineClient;
import com.example.bty.Controllers.ReclamationController.AjouterReclamation;
import com.example.bty.Controllers.graphiqueGCP.FD;
import com.example.bty.Controllers.graphiqueGCP.Formoffre;
import com.example.bty.Entities.Evenement;
import com.example.bty.Entities.Role;
import com.example.bty.Entities.User;
import com.example.bty.Utils.ConnexionDB;
import com.example.bty.Utils.Session;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class clientVitrine extends Application {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/pidevgym";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private Connection connection;
    private Map<Evenement, VBox> detailsMap = new HashMap<>();
    private FlowPane flowPane = new FlowPane();
    User user;
    private Map<Integer, Boolean> popupOpenMap = new HashMap<>(); // Track open popups for each event
public clientVitrine(){
    connection = ConnexionDB.getInstance().getConnexion();

}


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vitrine Client");

        // Récupérer la liste des événements (vous devez implémenter cette méthode)
        List<Evenement> evenements = getListeEvenements();

        // Créer une VBox pour afficher les cartes d'événements
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        BorderPane root = new BorderPane();
        VBox topBar = new VBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getStyleClass().add("top-bar");
        topBar.setStyle(" -fx-background-color: #2c3e50;\n" +
                "    -fx-padding: 25;\n" +
                "    -fx-border-radius: 15px;" +
                " -fx-background-insets: 0 0 0 232;"

        );
        // topBar.setSpacing(5);
        TextField rechercheTextField = new TextField();
        rechercheTextField.setPromptText("Rechercher par nom");
        rechercheTextField.getStyleClass().add("search-text-field");

        Button rechercherButton = new Button("Rechercher");
        rechercherButton.getStyleClass().add("search-button");

        rechercherButton.setOnAction(e -> {
            String typeRecherche = rechercheTextField.getText();
            List<Evenement> produitsRecherches = rechercherEvenement(typeRecherche);

            // Clear the existing content in the flow pane
            this.flowPane.getChildren().clear();

            // Display the searched events
            for (Evenement evenement : produitsRecherches) {
                VBox carte = createEventCard(evenement);
                this.flowPane.getChildren().add(carte);
            }

        });
        rechercheTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                // If the search text is empty, reset the FlowPane
                resetFlowPane();
            } else {
                // Otherwise, perform the search
                String typeRecherche = rechercheTextField.getText();
                List<Evenement> produitsRecherches = rechercherEvenement(typeRecherche);

                // Clear the existing content in the flow pane
                flowPane.getChildren().clear();

                // Display the searched events
                for (Evenement evenement : produitsRecherches) {
                    VBox carte = createEventCard(evenement);
                    flowPane.getChildren().add(carte);
                }
            }
        });

        HBox searchBox = new HBox(rechercheTextField, rechercherButton);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(10);

        topBar.getChildren().addAll(searchBox);
        root.setTop(topBar);



        AnchorPane leftDashboard = createLeftDashboard(primaryStage);

        root.setLeft(leftDashboard);





        this.flowPane.setAlignment(Pos.TOP_CENTER);
        this.flowPane.setPadding(new Insets(20));

        this.flowPane.setHgap(30);
        this.flowPane.setVgap(30);







        scrollPane.setContent(this.flowPane);
        root.setCenter(scrollPane);





        Scene scene = new Scene(root, 1366, 700);
        primaryStage.setResizable(true);

        primaryStage.setScene(scene);

        primaryStage.show();
        scene.getStylesheets().add(getClass().getResource("/com/example/bty/CSSmoduleEvenement/VitrineClient.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/dashboardDesign.css").toExternalForm());
// Parcourir la liste des événements et créer une carte pour chaque événement
        for (Evenement evenement : evenements) {

            VBox carte = createEventCard(evenement);
            this.flowPane.getChildren().add(carte);
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
        dashboardAdmin.setTranslateY(-87);
        dashboardAdmin.setPrefSize(234, 1500);
        dashboardAdmin.getStyleClass().add("border-pane");


        FontAwesomeIconView usernameAdmin = createFontAwesomeIconView("USER", "WHITE", 50, 82, 91);
        Label welcomeLabel = createLabel("Bienvenue, " , "Arial Bold", 15, 78, 101, "WHITE");
        Label usernameLabel = createLabel( loggedInUser.getName(), "Arial Bold", 20, 78, 120,"WHITE");
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
                icons[4], icons[5], icons[6] ,reportContainer,
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

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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

    private void resetFlowPane() {
        flowPane.getChildren().clear();
        List<Evenement> evenements = getListeEvenements();
        for (Evenement evenement : evenements) {
            VBox carte = createEventCard(evenement);
            flowPane.getChildren().add(carte);
        }
    }


    private VBox createEventCard(Evenement evenement) {
//        VBox carte = new VBox(10);
//        carte.setPadding(new Insets(10));
        DetailsEvenementWindow detailsWindow = new DetailsEvenementWindow(); // Créer une instance de DetailsEvenementWindow
        //  VBox detailsCard = detailsWindow.createDetailsCard(evenement);

        VBox card = new VBox();
        card.getStyleClass().add("product-card");
        card.setAlignment(Pos.TOP_CENTER);
        card.setSpacing(10);
        card.setPrefWidth(220); // Définir une largeur préférée fixe
        card.setMinHeight(250);

        // Afficher l'image de l'événement
        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(evenement.getImage())));

        imageView.setFitWidth(200);
        imageView.setFitHeight(170);

        // Afficher le nom de l'événement
        Label nomLabel = new Label( evenement.getNom());
        nomLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;-fx-text-fill: #A62609;");




        // Ajouter un bouton "See More"
        Button SeeButton = new Button("Details");
        SeeButton.getStyleClass().add("add-to-cart-button");


        boolean[] isPopupShowing = {false}; // Using an array to make it effectively final

        SeeButton.setOnAction(e -> {
            // Retrieve or create the details card for the specific event
            VBox detailsCard = detailsMap.computeIfAbsent(evenement, k -> createDetailsCard(k));

            // Create a new Popup
            Popup popup = new Popup();
            popup.getContent().add(detailsCard);

            // Set the position of the Popup relative to the button
            popup.setOnShowing(ev -> {
                Bounds bounds = SeeButton.localToScreen(SeeButton.getBoundsInLocal());
                popup.setX(bounds.getMaxX());
                popup.setY(bounds.getMinY());
            });


            // Close the Popup on double-click
            if (isPopupShowing[0]) {
                popup.hide();
            } else {
                popup.show(SeeButton.getScene().getWindow());
            }

            // Update the visibility flag
            isPopupShowing[0] = !isPopupShowing[0];
        });



        // Ajouter les éléments à la carte
        card.getChildren().addAll(imageView, nomLabel, SeeButton);



        return card;
    }


    // Méthode pour afficher plus de détails sur l'événement (vous devez implémenter cette méthode)
    private void SeeMoreEvenement(Evenement evenement) {
        // Implémentez le code pour afficher plus de détails sur l'événement
        // Vous pouvez utiliser une nouvelle fenêtre ou un panneau pour afficher les détails
        createDetailsCard(evenement);

    }

    private List<Evenement> getListeEvenements() {
        List<Evenement> evenements = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM evenement WHERE etat=1 ";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id_evenement");
                    String nom = resultSet.getString("nomEvenement");
                    byte[] imageBytes = resultSet.getBytes("image");
                    String categorie = resultSet.getString("categorie");
                    String Objectif =resultSet.getString("Objectif");
                    Date date =resultSet.getDate("Date");
                    Time time=resultSet.getTime("Time");
                    int nbrPlace = resultSet.getInt("nbrPlace");


                    try {
                        Evenement evenement = new Evenement(id, nom, imageBytes,categorie,Objectif,date,time,nbrPlace);
                        evenements.add(evenement);
                    } catch (Exception e) {
                        System.err.println("Erreur lors de la conversion de l'image pour l'événement avec l'ID : " + id);
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return evenements;
    }

    private List<Evenement> rechercherEvenement(String typeRecherche) {
        List<Evenement> produitsRecherches = new ArrayList<>();

        String query = "SELECT * FROM evenement WHERE nomEvenement LIKE ?";


        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + typeRecherche + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Evenement E = new Evenement();
                    E.setId(resultSet.getInt("id_evenement"));
                    E.setNom(resultSet.getString("nomEvenement"));
                    E.setCategorie(resultSet.getString("categorie"));
                    E.setObjectif(resultSet.getString("Objectif"));
                    E.setNbre_place(resultSet.getInt("nbrPlace"));
                    E.setDate(resultSet.getDate("Date"));
                    E.setTime(resultSet.getTime("Time"));
                    int coachId = resultSet.getInt("id_user");
                    E.setImage(resultSet.getBytes("image"));

                    produitsRecherches.add(E);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produitsRecherches;
    }
    public static void main(String[] args) {
        launch(args);
    }
}