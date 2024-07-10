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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class FD extends Application {

    private TextField ageField;
    private TextField butField;
    private TextField niveauPhysiqueField;
    private TextField maladieChroniqueField;
    private TextField nombreHeureField;
    private TextField idUserField;
    private TextField idOffreField;
    private TextField lesjoursFiled;
    private TextField horaireFiled;
    private TextField nomField;
    private ImageView backgroundImag;
    User user;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Formulaire d'ajout d'une demande");

        VBox cardContainer = new VBox();
        //cardContainer.getStyleClass().add("card-container");
        cardContainer.setPadding(new Insets(20));
        cardContainer.setSpacing(10);
        AnchorPane sidebar = createLeftDashboard(primaryStage);

        //cardContainer.getStyleClass().add("card-container1");
        cardContainer.setPadding(new Insets(20));
        cardContainer.setSpacing(10);

        Label cardTitle = new Label("Ajouter une demande de Coaching privé");
        cardTitle.setStyle("-fx-translate-x: 250;-fx-font-size: 21px; -fx-font-weight: bold; -fx-text-fill: #673AB7;");

        GridPane grid = new GridPane();
        grid.setMaxWidth(800);
        grid.setMaxHeight(670);
        grid.setVgap(18);
        grid.setHgap(5);
        grid.setStyle("-fx-translate-x: 175;-fx-background-color: #D1C4E9; -fx-padding: 25; -fx-border-radius: 10; -fx-background-radius: 10; -fx-alignment: center;");

        Label nomLabel = new Label("Nom:");
        setupStyledLabel(nomLabel);
        nomField = new TextField();
        nomField.setPrefWidth(250);
        setupStyledTextField(nomField);
        GridPane.setConstraints(nomLabel, 0, 0);
        GridPane.setConstraints(nomField, 1, 0);


        Label ageLabel = new Label("Age:");
        setupStyledLabel(ageLabel);
        ageField = new TextField();
        ageField.setPrefWidth(250);
        setupStyledTextField(ageField);
        GridPane.setConstraints(ageLabel, 0, 1);
        GridPane.setConstraints(ageField, 1, 1);

        Label butLabel = new Label("But:");
        setupStyledLabel(butLabel);
        butField = new TextField();
        butField.setPrefWidth(250);
        setupStyledTextField(butField);
        GridPane.setConstraints(butLabel, 0, 2);
        GridPane.setConstraints(butField, 1, 2);

        Label niveauPhysiqueLabel = new Label("Niveau Physique:");
        setupStyledLabel(niveauPhysiqueLabel);
        niveauPhysiqueField = new TextField();
        niveauPhysiqueField.setPrefWidth(250);
        setupStyledTextField(niveauPhysiqueField);
        GridPane.setConstraints(niveauPhysiqueLabel, 0, 3);
        GridPane.setConstraints(niveauPhysiqueField, 1, 3);



        Label maladieChroniqueLabel = new Label("Maladie Chronique:");
        setupStyledLabel(maladieChroniqueLabel);
        maladieChroniqueField = new TextField();
        maladieChroniqueField.setPrefWidth(250);
        setupStyledTextField(maladieChroniqueField);
        GridPane.setConstraints(maladieChroniqueLabel, 0, 4);
        GridPane.setConstraints(maladieChroniqueField, 1, 4);


        Label nombreHeureLabel = new Label("Nombre d'Heures:");
        setupStyledLabel(nombreHeureLabel);
        nombreHeureField = new TextField();
        nombreHeureField.setPrefWidth(250);
        setupStyledTextField(nombreHeureField);
        GridPane.setConstraints(nombreHeureLabel, 0, 5);
        GridPane.setConstraints(nombreHeureField, 1, 5);


        Label idUserLabel = new Label("Identifiant User:");
        setupStyledLabel(idUserLabel);
        idUserField = new TextField();
        idUserField.setPrefWidth(250);
        setupStyledTextField(idUserField);
        GridPane.setConstraints(idUserLabel, 0, 6);
        GridPane.setConstraints(idUserField, 1, 6);


        Label idOffreLabel = new Label("numéro d'Offre:");
        setupStyledLabel(idOffreLabel);
        idOffreField = new TextField();
        idOffreField.setPrefWidth(250);
        setupStyledTextField(idOffreField);
        GridPane.setConstraints(idOffreLabel, 0, 7);
        GridPane.setConstraints(idOffreField, 1, 7);


        Label etatLabel = new Label("Etat:");
        setupStyledLabel(etatLabel);

        TextField etatField = new TextField("En Attente");
        etatField.setDisable(true); // Make the text field non-editable
        etatField.setStyle("-fx-font-size: 15px; -fx-background-color: whitesmoke; -fx-border-color: #3f51b5; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-text-fill: #000000;");
        etatField.setPrefWidth(250);

        GridPane.setConstraints(etatLabel, 0, 8);
        GridPane.setConstraints(etatField, 1, 8);



        Label horaireLabel = new Label("Horaire:");
        setupStyledLabel(horaireLabel);
        horaireFiled = new TextField();
        horaireFiled.setPrefWidth(250);
        setupStyledTextField(horaireFiled);
        GridPane.setConstraints(horaireLabel, 0, 9);
        GridPane.setConstraints(horaireFiled, 1, 9);


        Label lesjoursLabel = new Label("Les jours:");
        setupStyledLabel(lesjoursLabel);
        lesjoursFiled = new TextField();
        lesjoursFiled.setPrefWidth(250);
        setupStyledTextField(lesjoursFiled);
        GridPane.setConstraints(lesjoursLabel, 0, 10);
        GridPane.setConstraints(lesjoursFiled, 1, 10);




        Button sendButton = new Button("Envoyer");
        setupButton(sendButton);
        GridPane.setConstraints(sendButton, 1, 11);
        sendButton.setOnAction(event -> insertDemande());

        Button consulterButton = new Button("Consulter les demandes");
        setupButton(consulterButton);
        GridPane.setConstraints(consulterButton, 2, 11);
        consulterButton.setOnAction(event -> {
            try {
                consulterDemandes();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        grid.getChildren().add(consulterButton);

        Button consulterButton1 = new Button("Voir nos offres");
        grid.add(consulterButton1, 3, 11);
        setupButton(consulterButton1);


        // Ajoutez un gestionnaire d'événements pour le bouton
        consulterButton1.setOnAction(new EventHandler<ActionEvent>() {

            @Override

            public void handle(ActionEvent event) {
                   InterfaceOffre ardInterface = new InterfaceOffre();
                    ardInterface.start(new Stage());


            }
        });



        grid.getChildren().addAll(ageLabel, ageField, butLabel, butField, niveauPhysiqueLabel, niveauPhysiqueField,
                maladieChroniqueLabel, maladieChroniqueField, nombreHeureLabel, nombreHeureField, idUserLabel,
                idUserField, idOffreLabel, idOffreField, etatLabel,etatField,lesjoursLabel, lesjoursFiled, horaireLabel, horaireFiled,nomLabel,nomField,
                sendButton);

        ageField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                ageField.setStyle("-fx-text-inner-color: red;");
            } else {
                ageField.setStyle("-fx-text-inner-color: black;");
            }
        });

        nombreHeureField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                nombreHeureField.setStyle("-fx-text-inner-color: red;");
            } else {
                nombreHeureField.setStyle("-fx-text-inner-color: black;");
            }
        });

        idUserField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                idUserField.setStyle("-fx-text-inner-color: red;");
            } else {
                idUserField.setStyle("-fx-text-inner-color: black;");
            }
        });

        idOffreField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                idOffreField.setStyle("-fx-text-inner-color: red;");
            } else {
                idOffreField.setStyle("-fx-text-inner-color: black;");
            }
        });

        // Appel de la méthode pour configurer la validation du champ horaireFiled
        setupTimeValidation(horaireFiled);
        // Appel de la méthode pour configurer la validation des champs de type String
        setupStringValidation(nomField);
        setupStringValidation(maladieChroniqueField);
        setupStringValidation(butField);
        setupStringValidation(niveauPhysiqueField);
        setupStringValidation(lesjoursFiled);

        // Ajout du sidebar à gauche du BorderPane
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

    private void setupLabel(Label label) {
        label.setStyle("-fx-font-weight: bold;");
    }

    private void setupTextField(TextField textField) {
        textField.setStyle("-fx-text-inner-color: black;");
    }

    private void setupButton(Button button) {
        button.setStyle("-fx-background-color: #673AB7; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 10 20; -fx-border-radius: 5;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #311B92; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 10 20; -fx-border-radius: 5;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #673AB7; -fx-text-fill: #FFFFFF; -fx-font-size: 13px; -fx-padding: 10 20; -fx-border-radius: 5;"));
    }



    private void setupStyledLabel(Label label) {
        label.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #3f51b5; -fx-font-family: 'Arial', sans-serif;");
    }

    private void setupStyledTextField(TextField textField) {
        textField.setStyle("-fx-font-size: 15px; -fx-background-color: whitesmoke; -fx-border-color: #3f51b5; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-text-fill: #000000;");
        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle("-fx-font-size: 15px; -fx-background-color: whitesmoke; -fx-border-color: #303f9f; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-text-fill: #000000;");
            } else {
                textField.setStyle("-fx-font-size: 15px; -fx-background-color: whitesmoke; -fx-border-color: #3f51b5; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-text-fill: #000000;");
            }
        });
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

    private void showAlert1(String title, String content) {
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







    private boolean validateFields() {
        if (ageField.getText().isEmpty() || butField.getText().isEmpty() || niveauPhysiqueField.getText().isEmpty()
                || maladieChroniqueField.getText().isEmpty() || nombreHeureField.getText().isEmpty()
                || idUserField.getText().isEmpty() || idOffreField.getText().isEmpty() || lesjoursFiled.getText().isEmpty()
                || horaireFiled.getText().isEmpty()) {
            System.out.println("Veuillez remplir tous les champs.");
            return false;
        }
        return true;
    }


    private void setupTimeValidation(TextField timeField) {
        timeField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]")) {
                // Vérifie si la nouvelle valeur ne correspond pas au format de temps HH:mm
                timeField.setStyle("-fx-text-inner-color: red;");
            } else {
                timeField.setStyle("-fx-text-inner-color: black;");
            }
        });
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

    private void insertDemande() {
        Session session = Session.getInstance();
        User loggedInUser = session.getLoggedInUser();

        if (loggedInUser == null) {
            showAlert("Erreur", "Aucun utilisateur connecté !");
            return;
        }

        // Vérifier si l'utilisateur connecté a le rôle de membre
        if (!loggedInUser.getRoles().equals(Role.MEMBRE)) {
            showAlert("Erreur", "Vous devez être membre pour insérer une demande !");
            return;
        }

        if (!checkEmptyFields()) {
            showAlertt("Champs vides", "Veuillez remplir tous les champs !");
            return;
        }

        String specialite = getSpecialiteFromOffreId(Integer.parseInt(idOffreField.getText()));

        if (!isMaladieAllowed(specialite, maladieChroniqueField.getText())) {
            showAlert("Impossible de participer", "Vous ne pouvez pas participer en raison de votre maladie.");
            return;
        }

        String url = "jdbc:mysql://localhost:3306/pidevgym";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String query = "INSERT INTO demande (nom,Age, But, NiveauPhysique, MaladieChronique, NombreHeure, ID_User, ID_Offre, Etat, Horaire, lesjours) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            String etat = "En Attente";
            java.sql.Time horaire = java.sql.Time.valueOf("08:00:00");
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, nomField.getText());
            statement.setInt(2, Integer.parseInt(ageField.getText()));
            statement.setString(3, butField.getText());
            statement.setString(4, niveauPhysiqueField.getText());
            statement.setString(5, maladieChroniqueField.getText());
            statement.setInt(6, Integer.parseInt(nombreHeureField.getText()));
            statement.setInt(7, Integer.parseInt(idUserField.getText()));
            statement.setInt(8, Integer.parseInt(idOffreField.getText()));
            statement.setString(9, etat);
            statement.setTime(10, horaire);
            statement.setString(11, lesjoursFiled.getText());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Demande insérée avec succès !");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'insertion de la demande: " + e.getMessage());
            setInvalidStyle();
        }
    }


    // Méthode pour vérifier si des champs sont vides
    private boolean checkEmptyFields() {
        if (nomField.getText().isEmpty() || ageField.getText().isEmpty() || butField.getText().isEmpty() ||
                niveauPhysiqueField.getText().isEmpty() || maladieChroniqueField.getText().isEmpty() ||
                nombreHeureField.getText().isEmpty() || idUserField.getText().isEmpty() || idOffreField.getText().isEmpty() ||
                lesjoursFiled.getText().isEmpty()) {
            return false;
        }
        return true;
    }

    // Méthode pour afficher un message d'alerte dans l'interface utilisateur



    // Méthode pour vérifier si des champs sont vides
//    private boolean checkEmptyFields() {
//        if (nomField.getText().isEmpty() || ageField.getText().isEmpty() || butField.getText().isEmpty() ||
//                niveauPhysiqueField.getText().isEmpty() || maladieChroniqueField.getText().isEmpty() ||
//                nombreHeureField.getText().isEmpty() || idUserField.getText().isEmpty() || idOffreField.getText().isEmpty() ||
//                lesjoursFiled.getText().isEmpty()) {
//            showAlert("Champs vides", "Veuillez remplir tous les champs !");
//            return false;
//        }
//        return true;
//    }

    // Méthode pour gérer l'événement du bouton "Consulter"
    private void consulterDemandes() throws SQLException {
        // Ouvrir la fenêtre de consultation des demandes sans demander le nom du client
        ConsultationDemandes consultationDemandes = new ConsultationDemandes();
        consultationDemandes.start(new Stage());
    }
    private String getSpecialiteFromOffreId(int idOffre) {
        String url = "jdbc:mysql://localhost:3306/pidevgym";
        String username = "root";
        String password = "";

        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String query = "SELECT specialite FROM offre WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, idOffre);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("specialite");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la spécialité depuis la base de données: " + e.getMessage());
        }

        return null;  // Retourne null si la spécialité n'est pas trouvée ou en cas d'erreur
    }

    private boolean isMaladieAllowed(String specialite, String maladie) {
        if (specialite == null || maladie == null) {
            return false;  // Si la spécialité ou la maladie est nulle, la participation n'est pas autorisée
        }

        switch (specialite) {
            case "Yoga":
                return !maladie.equals("Maladies infectieuses contagieuses")
                        && !maladie.equals("Maladies oculaires graves")
                        && !maladie.equals("Troubles musculo-squelettiques graves")
                        && !maladie.equals("Problèmes neurologiques")
                        && !maladie.equals("Hypertension artérielle non contrôlée")
                        && !maladie.equals("Problèmes cardiaques graves");

            case "Boxe":
                return !maladie.equals("Problèmes cardiaques graves")
                        && !maladie.equals("Hypertension artérielle non contrôlée")
                        && !maladie.equals("Problèmes musculo-squelettiques graves")
                        && !maladie.equals("Maladies inflammatoires")
                        && !maladie.equals("Maladies infectieuses")
                        && !maladie.equals("Problèmes respiratoires graves")
                        && !maladie.equals("Troubles de l'alimentation");

            case "Musculation":
                return !maladie.equals("Maladies cardiaques graves")
                        && !maladie.equals("Hypertension artérielle non contrôlée")
                        && !maladie.equals("Problèmes respiratoires sévères")
                        && !maladie.equals("Maladies vasculaires périphériques")
                        && !maladie.equals("Problèmes neurologiques graves")
                        && !maladie.equals("Diabète non contrôlé")
                        && !maladie.equals("Infections actives");

            case "Cardio":
                return !maladie.equals("Maladies cardiaques graves")
                        && !maladie.equals("Hypertension artérielle non contrôlée")
                        && !maladie.equals("Problèmes respiratoires sévères")
                        && !maladie.equals("Maladies vasculaires périphériques")
                        && !maladie.equals("Problèmes neurologiques graves")
                        && !maladie.equals("Diabète non contrôlé")
                        && !maladie.equals("Infections actives");

            default:
                return true;  // Si la spécialité n'est pas reconnue, autorise la participation par défaut
        }
    }

    private void setInvalidStyle() {
        nomField.setStyle("-fx-border-color: initial;");
        ageField.setStyle("-fx-border-color: initial;");
        butField.setStyle("-fx-border-color: initial;");
        niveauPhysiqueField.setStyle("-fx-border-color: initial;");
        maladieChroniqueField.setStyle("-fx-border-color: initial;");
        nombreHeureField.setStyle("-fx-border-color: initial;");
        idUserField.setStyle("-fx-border-color: initial;");
        idOffreField.setStyle("-fx-border-color: initial;");
        lesjoursFiled.setStyle("-fx-border-color: initial;");
        horaireFiled.setStyle("-fx-border-color: initial;");

        nomField.setStyle("-fx-border-color: initial;");
        ageField.setStyle("-fx-border-color: red;");
        butField.setStyle("-fx-border-color: red;");
        niveauPhysiqueField.setStyle("-fx-border-color: red;");
        maladieChroniqueField.setStyle("-fx-border-color: red;");
        nombreHeureField.setStyle("-fx-border-color: red;");
        idUserField.setStyle("-fx-border-color: red;");
        idOffreField.setStyle("-fx-border-color: red;");
        lesjoursFiled.setStyle("-fx-border-color: red;");
        horaireFiled.setStyle("-fx-border-color: red;");
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private void showAlertt(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}