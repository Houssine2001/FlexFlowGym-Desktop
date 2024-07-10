package com.example.bty.Controllers.graphiqueGCP;

import com.example.bty.Controllers.CourController.CourMembre;
import com.example.bty.Controllers.DashboardMembre;
import com.example.bty.Controllers.EvenementController.clientVitrine;
import com.example.bty.Controllers.ProduitController.VitrineClient;
import com.example.bty.Controllers.ReclamationController.AjouterReclamation;
import com.example.bty.Entities.Role;
import com.example.bty.Entities.User;
import com.example.bty.Utils.Session;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Formoffre extends Application {

    private TextField nomField;
    private ChoiceBox<String> specialiteChoice;
    private TextField tarifField;
    private TextField coachField;
    private Label nomLabel;
    Session session = Session.getInstance();
    User u=session.getLoggedInUser();
    User user ;


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Formulaire d'ajout d'une offre ");
        // Card Container
        VBox cardContainer = new VBox();
        cardContainer.getStyleClass().add("card-container");
        cardContainer.setPadding(new Insets(20));
        cardContainer.setSpacing(10);


        AnchorPane sidebar = createLeftDashboard(primaryStage);

        Label cardTitle = new Label("Ajouter une offre");
        cardTitle.setStyle("-fx-translate-x: 250;-fx-translate-y: 98;-fx-font-size: 21px; -fx-font-weight: bold; -fx-text-fill: #673AB7;");

        GridPane grid = new GridPane();
        grid.setMaxWidth(670);
        grid.setMaxHeight(670);
        grid.setVgap(18);
        grid.setHgap(5);
        grid.setStyle("-fx-translate-x: 230;-fx-translate-y: 100;-fx-background-color: #D1C4E9; -fx-padding: 25; -fx-border-radius: 10; -fx-background-radius: 10; -fx-alignment: center;");


        // Label et champ de texte pour l'ID


        // Card Title

        // Label et champ de texte pour le nom de l'offre
        Label nomLabel = new Label("Nom d'offre:");
        nomField = new TextField();
        setupFormField(nomLabel, nomField, grid, 0);

        // Label et choix de spécialité
        Label specialiteLabel = new Label("Spécialité:");
        specialiteChoice = new ChoiceBox<>();
        specialiteChoice.getItems().addAll("Musculation", "Cardio", "Yoga", "Boxe");
        specialiteChoice.setValue("Musculation"); // Spécialité par défaut
        setupFormField(specialiteLabel, specialiteChoice, grid, 1);

        // Label et champ de texte pour le tarif par heure
        Label tarifLabel = new Label("Tarif par heure:");
        tarifField = new TextField();
        setupFormField(tarifLabel, tarifField, grid, 2);

        // Label et champ de texte pour le coach
        Label coachLabel = new Label("id_Coach:");
        coachField = new TextField();
        setupFormField(coachLabel, coachField, grid, 3);

        // Label et champ de texte pour l'état (non modifiable)
        Label etatLabel = new Label("Etat:");
        TextField etatField = new TextField("En Attente");
        etatField.setEditable(false);
        setupFormField(etatLabel, etatField, grid, 4);

        // Bouton d'ajout de l'offre
        Button addButton = new Button("Ajouter");
        grid.add(addButton, 1, 5);
        setupButton(addButton);
        addButton.setOnAction(event -> insertOffre());

        // Bouton pour consulter la liste des offres
        Button consulterButton = new Button("Consulter liste des offres");
        grid.add(consulterButton, 2, 5);
        setupButton(consulterButton);
        consulterButton.setOnAction(event -> consulterOffres());








        tarifField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tarifField.setStyle("-fx-text-inner-color: red;");
            } else {
                tarifField.setStyle("-fx-text-inner-color: black;");
            }
        });

        coachField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                coachField.setStyle("-fx-text-inner-color: red;");
            } else {
                coachField.setStyle("-fx-text-inner-color: black;");
            }
        });

        Button consulterButton1 = new Button("Consulter liste demandes");
        grid.add(consulterButton1, 2, 6);
        setupButton(consulterButton1);
        this.user = session.getLoggedInUser();




        // Ajoutez un gestionnaire d'événements pour le bouton
        consulterButton1.setOnAction(new EventHandler<ActionEvent>() {

            @Override

            public void handle(ActionEvent event) {
                if(user.getRoles().equals(Role.COACH)){
                    ARD ardInterface = new ARD();
                    ardInterface.start(new Stage());
                }
            else {
                    showAlert("Failed", "Vous n'etes pas un coach ");
            }

            }
        });



        setupStringValidation(nomField);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        VBox formContainer = new VBox();
        formContainer.getChildren().addAll(cardTitle, grid);
        root.setCenter(formContainer);
        Scene scene = new Scene(root, 1366, 700);
        scene.getStylesheets().add(getClass().getResource("/dashboardDesign.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();


    }




    private void setupFormField(Label label, Control control, GridPane grid, int row) {
        label.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #3f51b5; -fx-font-family: 'Arial', sans-serif;");
        control.setStyle("-fx-font-size: 15px; -fx-background-color: whitesmoke; -fx-border-color: #3f51b5; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-text-fill: #000000;");
        grid.add(label, 0, row);
        grid.add(control, 1, row);
    }

    // Méthode pour configurer le style des boutons
    private void setupButton(Button button) {
        button.setStyle("-fx-background-color: #673AB7; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 10 20; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #311B92; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 10 20; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #673AB7; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 10 20; -fx-border-radius: 5;"));
    }

    // Méthode pour configurer la validation des champs de type String





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

        Button profileAdminBtn = createButton("Profile", 22, 462);
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
                icons[4], icons[5], icons[6],reportContainer,
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




//    private void insertOffre() {
//        this.user = session.getLoggedInUser();
//
//        if (user == null) {
//            showAlert(AlertType.ERROR, "Utilisateur non connecté !");
//            return;
//        }
//
//        String url = "jdbc:mysql://localhost:3306/pidevgym";
//        String username = "root";
//        String password = "";
//
//        try (Connection conn = DriverManager.getConnection(url, username, password)) {
//            if(user.getRole().equals(Role.COACH)){
//                showAlert(AlertType.INFORMATION, "Coach connecté !");
//                // Le reste du code...
//            } else {
//                showAlert(AlertType.WARNING, "Vous n'êtes pas autorisé, vous n'êtes pas un coach !");
//            }
//        } catch (SQLException e) {
//            showAlert(AlertType.ERROR, "Erreur lors de l'insertion de l'offre: " + e.getMessage());
//        }
//    }






    private void insertOffre() {
        this.user = session.getLoggedInUser();

        String url = "jdbc:mysql://localhost:3306/pidevgym";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            if(user.getRoles().equals(Role.COACH)){
                System.out.println("coach connected !!");
                String id = nomField.getText();
                // Vérifier si l'ID existe déjà
                if (offreExists(conn, id)) {
                    // Afficher un message à l'utilisateur dans l'interface
                    showAlert("Erreur", "Une offre avec cet ID existe déjà !");
                    return; // Arrêter l'exécution de la méthode car l'offre existe déjà
                }

                String query = "INSERT INTO Offre (nom, Specialite, tarif_heure, id_Coach,etatOffre) VALUES (?, ?, ?, ?,?)";
                String etatOffre = "En Attente";
                PreparedStatement statement = conn.prepareStatement(query);
                statement.setString(1, nomField.getText());
                statement.setString(2, specialiteChoice.getValue());
                statement.setDouble(3, Double.parseDouble(tarifField.getText()));
                statement.setInt(4, Integer.parseInt(coachField.getText()));
                statement.setString(5, etatOffre);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    showAlert("Succès", "Offre insérée avec succès !");
                }
            }else {
                System.out.println("t as pas le droit you are not a coach !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de l'offre: " + e.getMessage());
        }
    }



    private void showAlert(AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private boolean offreExists(Connection conn, String nom) throws SQLException {
        String query = "SELECT COUNT(*) FROM Offre WHERE nom = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setString(1, nom);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count > 0;
    }
    private void setupStringValidation(TextField textField) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.isEmpty() && newValue.matches(".*\\d.*")) {
                // Vérifie si la nouvelle valeur n'est pas vide et contient des chiffres
                textField.setStyle("-fx-text-inner-color: red;"); // Change la couleur du texte en rouge
            } else {
                textField.setStyle("-fx-text-inner-color: black;"); // Remet la couleur du texte en noir si aucun chiffre n'est présent
            }
        });
    }

    private void consulterOffres() {
        ConsultationOffre consultationOffre = new ConsultationOffre();
        consultationOffre.start(new Stage());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



    public static void main(String[] args) {
        launch(args);
    }
}