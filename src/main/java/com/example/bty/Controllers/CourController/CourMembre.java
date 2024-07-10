package com.example.bty.Controllers.CourController;

import com.example.bty.Controllers.DashboardMembre;
import com.example.bty.Controllers.EvenementController.clientVitrine;
import com.example.bty.Controllers.ProduitController.VitrineClient;
import com.example.bty.Controllers.ReclamationController.AjouterReclamation;
import com.example.bty.Controllers.graphiqueGCP.FD;
import com.example.bty.Controllers.graphiqueGCP.Formoffre;
import com.example.bty.Entities.Role;
import com.example.bty.Entities.User;
import com.example.bty.Utils.ConnexionDB;
import com.example.bty.Utils.Session;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.example.bty.Entities.Cours;
import com.example.bty.Entities.Rating;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


public class CourMembre extends Application {

    private static Connection connection;
    private BorderPane root;
    private Label messageLabel;

    Session session = Session.getInstance();
    User u=session.getLoggedInUser();
    User user ;


    public CourMembre() {
        connection = ConnexionDB.getInstance().getConnexion();
    }

    private ObservableList<Cours> coursList;

    @Override
    public void start(Stage primaryStage)  {

        primaryStage.setTitle("Affichage des cours");

        root = new BorderPane(); // Initialisation de root

        // root.setPadding(new Insets(10));

        VBox filterBox = new VBox();
        filterBox.setAlignment(Pos.CENTER_LEFT);

        filterBox.setStyle("-fx-background-color: #2c3e50;\n" +
                "    -fx-padding: 15;\n" +
                "    -fx-border-radius: 15px;" +
                "-fx-background-insets: 0 0 0 219");

        //   filterBox.setSpacing(5);

        ComboBox<String> cibleComboBox = new ComboBox<>();
        cibleComboBox.getItems().addAll("Enfant", "Adulte");
        cibleComboBox.setPromptText("Cible");


        ComboBox<String> categorieComboBox = new ComboBox<>();
        categorieComboBox.getItems().addAll("Aquatique", "Cardio", "Force", "Danse", "Kids Island");
        categorieComboBox.setPromptText("Catégorie");


        ComboBox<String> objectifComboBox = new ComboBox<>();
        objectifComboBox.getItems().addAll("Perdre du poids", "Se défouler", "Se musculer", "S'entrainer en dansant");
        objectifComboBox.setPromptText("Objectif");


        Button filterButton = new Button("Filtrer");
        filterButton.getStyleClass().add("envoyer-button");
        filterButton.setOnAction(e -> {
            String categorie = categorieComboBox.getValue();
            String cible = cibleComboBox.getValue();
            String objectif = objectifComboBox.getValue();
            filtrerCours(categorie, cible, objectif);
        });

        Button resetButton = new Button("Réinitialiser");
        resetButton.getStyleClass().add("reset-button");
        resetButton.setOnAction(e -> {
            // Réinitialiser le texte d'invite pour chaque ComboBox
            cibleComboBox.setPromptText("Cible");
            categorieComboBox.setPromptText("Catégorie");
            objectifComboBox.setPromptText("Objectif");

            // Vider la sélection dans chaque ComboBox
            cibleComboBox.getSelectionModel().clearSelection();
            categorieComboBox.getSelectionModel().clearSelection();
            objectifComboBox.getSelectionModel().clearSelection();

            afficherCours(); // Réafficher tous les cours
        });



        HBox filterHBox = new HBox(cibleComboBox, categorieComboBox, objectifComboBox, filterButton, resetButton);
        filterHBox.setAlignment(Pos.CENTER_LEFT);
        filterHBox.setSpacing(10);

        filterBox.getChildren().addAll(filterHBox);
        root.setTop(filterBox);

        coursList = FXCollections.observableArrayList();
        afficherCours();

        // Ajoutez le contenu principal dans un ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Ajoutez le contenu principal (grille de cours) dans le ScrollPane
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        // Initialisation de messageLabel
        messageLabel = new Label();
        root.setBottom(messageLabel); // Ajout de messageLabel au bas de root

        Scene scene = new Scene(root, 1366, 700);
        primaryStage.setScene(scene);

        String cssFile = getClass().getResource("/com/example/bty/CSSmoduleCours/membre.css").toExternalForm();
        scene.getStylesheets().add(getClass().getResource("/dashboardDesign.css").toExternalForm());
        scene.getStylesheets().add(cssFile);

        primaryStage.setTitle("Consultation des cours");
        primaryStage.show();

        AnchorPane leftDashboard = createLeftDashboard(primaryStage);
        //  leftDashboard.setMinHeight(250);
        root.setLeft(leftDashboard);

        root.setLeft(leftDashboard);
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
        dashboardAdmin.setTranslateY(-69);
        dashboardAdmin.setPrefSize(234, 1500);
        dashboardAdmin.getStyleClass().add("border-pane");


        FontAwesomeIconView usernameAdmin = createFontAwesomeIconView("USER", "WHITE", 50, 82, 91);
        Label welcomeLabel = createLabel("Bienvenue,", "Tahoma", 15, 78, 101,"WHITE");
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


    private VBox createCourseCard(Cours cours) {
        VBox card = new VBox();
        card.getStyleClass().add("product-card");
        card.setAlignment(Pos.TOP_CENTER);
        card.setSpacing(6);
        card.setPrefWidth(200);
        card.setMinHeight(330);

        Image image = null;
        byte[] imageData = cours.getImage();
        if (imageData != null && imageData.length > 0) {
            image = new Image(new ByteArrayInputStream(imageData));
        }

        if (image != null) {
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);
            card.getChildren().add(imageView);
        }

        Label nomLabel = new Label(cours.getNom());
        nomLabel.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;-fx-text-fill: #A62609");
        card.getChildren().add(nomLabel);

        Button participerButton = new Button("Participer");
        participerButton.getStyleClass().add("add-to-cart-button");

        // Vérifier la capacité restante avant de permettre la participation
        participerButton.setOnAction(e -> participerAuCours(cours, participerButton));

        HBox participerHBox = new HBox(participerButton);
        participerHBox.setAlignment(Pos.CENTER);
        participerHBox.setSpacing(10);
        card.getChildren().add(participerHBox);

        // Obtenir le total de likes et de dislikes à partir de la base de données
        int totalLikes = getTotalLikes(cours.getNom());
        int totalDislikes = getTotalDislikes(cours.getNom());

        // Créer des ImageView pour les icônes de pouces vers le haut et vers le bas
        ImageView thumbsUpIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/bty/imagesModuleCours/like.png")));
        ImageView thumbsDownIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/bty/imagesModuleCours/dislikes.png")));
// Définir la taille des icônes de pouces
        thumbsUpIcon.setFitWidth(25); // Largeur souhaitée
        thumbsUpIcon.setFitHeight(25); // Hauteur souhaitée
        thumbsDownIcon.setFitWidth(30); // Largeur souhaitée
        thumbsDownIcon.setFitHeight(30); // Hauteur souhaitée
        // Ajouter les ImageView à la carte
        HBox likesDislikesBox = new HBox(thumbsUpIcon, new Label(String.valueOf(totalLikes)), thumbsDownIcon, new Label(String.valueOf(totalDislikes)));
        likesDislikesBox.setAlignment(Pos.CENTER);
        likesDislikesBox.setSpacing(5);
        card.getChildren().add(likesDislikesBox);

        return card;
    }



    public int getTotalLikes(String nomCour) {
        int totalLikes = 0;
        try {
            String query = "SELECT COUNT(*) FROM rating WHERE nom_cour = ? AND liked = true";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nomCour);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalLikes = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalLikes;
    }


    public int getTotalDislikes(String nomCour) {
        int totalDislikes = 0;
        try {
            String query = "SELECT COUNT(*) FROM rating WHERE nom_cour = ? AND disliked = true";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, nomCour);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalDislikes = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return totalDislikes;
    }


    // Ajouter une méthode pour afficher la fenêtre d'évaluation
    private void afficherFenetreEvaluation(Cours cours) {
        Stage evaluationStage = new Stage();
        evaluationStage.initModality(Modality.APPLICATION_MODAL);
        evaluationStage.setTitle("Évaluation du cours");

        VBox evaluationBox = new VBox(10);
        evaluationBox.setPadding(new Insets(10));
        evaluationBox.setAlignment(Pos.CENTER);
        evaluationBox.setStyle("-fx-background-color: #f0f0f0;");

        Label titleLabel = new Label("Évaluez ce cours : " + cours.getNom());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        // Images pour l'icône vide
        ImageView thumbsUpEmptyIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/bty/imagesModuleCours/like.png")));
        ImageView thumbsDownEmptyIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/bty/imagesModuleCours/disEmpty.png")));

        // Images pour l'icône pleine
        ImageView thumbsUpFilledIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/bty/imagesModuleCours/likeFilled.png")));
        ImageView thumbsDownFilledIcon = new ImageView(new Image(getClass().getResourceAsStream("/com/example/bty/imagesModuleCours/dislikes.png")));

        // Définir la taille des icônes de pouces
        thumbsUpEmptyIcon.setFitWidth(45);
        thumbsUpEmptyIcon.setFitHeight(45);
        thumbsDownEmptyIcon.setFitWidth(45);
        thumbsDownEmptyIcon.setFitHeight(45);
        thumbsUpFilledIcon.setFitWidth(45);
        thumbsUpFilledIcon.setFitHeight(45);
        thumbsDownFilledIcon.setFitWidth(45);
        thumbsDownFilledIcon.setFitHeight(45);

        // Gestionnaire d'événements pour l'icône de like
        thumbsUpEmptyIcon.setOnMouseClicked(event -> {
            enregistrerEvaluation(cours, "J'aime");
            thumbsUpEmptyIcon.setImage(thumbsUpFilledIcon.getImage());
            thumbsDownEmptyIcon.setDisable(true);

        });

        // Gestionnaire d'événements pour l'icône de dislike
        thumbsDownEmptyIcon.setOnMouseClicked(event -> {
            enregistrerEvaluation(cours, "Je n'aime pas");
            thumbsDownEmptyIcon.setImage(thumbsDownFilledIcon.getImage());
            thumbsUpEmptyIcon.setDisable(true);

        });

        HBox iconBox = new HBox(20, thumbsUpEmptyIcon, thumbsDownEmptyIcon);
        iconBox.setAlignment(Pos.CENTER);

        evaluationBox.getChildren().addAll(titleLabel, iconBox);

        Scene scene = new Scene(evaluationBox, 250, 150);
        evaluationStage.setScene(scene);
        evaluationStage.showAndWait();
    }


    // Mettre à jour la méthode enregistrerEvaluation pour créer et enregistrer un objet Rating
    private void enregistrerEvaluation(Cours cours, String rating) {
        User loggedInUser = Session.getInstance().getLoggedInUser();
        try {
            int ratingValue;
            boolean liked = false;
            boolean disliked = false;

            // Vérifier si la valeur de rating est valide et définir liked et disliked en conséquence
            if (rating.equals("J'aime")) {
                ratingValue = 1;
                liked = true;
            } else if (rating.equals("Je n'aime pas")) {
                ratingValue = -1;
                disliked = true;
            } else {
                ratingValue = 0; // Par défaut, si aucune des deux options n'est sélectionnée
            }

            // Créer un objet Rating avec les données
            Rating newRating = new Rating(cours.getNom(), loggedInUser.getId(), ratingValue, liked, disliked);

            // Insérer le nouvel objet Rating dans la base de données
            String query = "INSERT INTO rating (nom_cour, user_id, rating, liked, disliked) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newRating.getNomCour());
            statement.setInt(2, newRating.getUserId());
            statement.setInt(3, newRating.getRating());
            statement.setBoolean(4, newRating.isLiked());
            statement.setBoolean(5, newRating.isDisliked());
            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Évaluation enregistrée avec succès !");
            } else {
                System.out.println("Erreur lors de l'enregistrement de l'évaluation.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendConfirmationEmail(User loggedInUser, Cours cours) {
        // Récupérer l'adresse e-mail de l'utilisateur connecté
        String userEmail = loggedInUser.getEmail();

        // Adresse e-mail Gmail et mot de passe Gmail
        final String username = "bahaeddinedridi1@gmail.com";
        final String password = "oman kvgj hdks njqc"; // Remplacez par votre mot de passe Gmail

        // Configuration des propriétés pour l'envoi d'e-mails via SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Création de la session de messagerie en utilisant javax.mail.Session
        javax.mail.Session session = javax.mail.Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Création d'un message MIME
            Message message = new MimeMessage(session);
            // Définition de l'expéditeur avec un nom personnalisé
            message.setFrom(new InternetAddress(username, "Flex Flow"));
            // Définition du destinataire
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            // Définition du sujet du message
            message.setSubject("Confirmation d'inscription au cours");
            // Définition du contenu du message
            String emailContent = "Bonjour " + loggedInUser.getName() + ",\n\n" +
                    "Vous êtes inscrit au cours : " + cours.getNom() + ".\n\n" +
                    "Détails du cours :\n" +
                    "- Intensité : " + cours.getIntensite() + "\n" +
                    "- Durée : " + cours.getDuree() + " minutes" + "\n"+

            "- Catégorie : " + cours.getCategorie() + "\n\n" +
                    "Recommandations :\n" +
                    "- Pensez à ramener une serviette et une bouteille d’eau.\n" +".\n"+
                    "Tenue recommandée :\n" +
                    "- Vêtements confortables\n\n" +
                    "Cordialement,\nFlex Flow";

            message.setText(emailContent);

            // Envoi du message
            Transport.send(message);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("Un e-mail avec les détails du cours vous a été envoyé ❤ ");
            alert.showAndWait();

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }


    private void participerAuCours(Cours cours, Button participerButton) {
        // Récupérer l'utilisateur connecté à partir de la session
        User loggedInUser = Session.getInstance().getLoggedInUser();

        // Vérifier si l'utilisateur est connecté
        if (loggedInUser != null) {
            // Vérifier si l'utilisateur est un membre
            if (loggedInUser.getRoles() != null && loggedInUser.getRoles().length > 0 && loggedInUser.getRoles()[0] == Role.MEMBRE) {
                // Afficher une boîte de dialogue de confirmation
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation de participation");
                alert.setHeaderText(null);
                alert.setContentText("Êtes-vous sûr de vouloir participer à ce cours ?");

                // Personnaliser les boutons de la boîte de dialogue
                ButtonType buttonTypeOui = new ButtonType("Oui");
                ButtonType buttonTypeNon = new ButtonType("Non");
                alert.getButtonTypes().setAll(buttonTypeOui, buttonTypeNon);

                // Attendre la réponse de l'utilisateur
                alert.showAndWait().ifPresent(response -> {
                    if (response == buttonTypeOui) {
                        // L'utilisateur a cliqué sur "Oui", procéder à la participation
                        participerAuCoursEffectif(cours, participerButton,  loggedInUser);

                    }
                });
            } else {
                System.out.println("Seuls les membres peuvent participer aux cours.");
            }
        } else {
            // Gérer le cas où l'utilisateur n'est pas connecté
            System.out.println("Vous devez être connecté pour participer à un cours.");
        }
    }

    private void participerAuCoursEffectif(Cours cours, Button participerButton, User loggedInUser) {
        int userID = loggedInUser.getId();
        String userName = loggedInUser.getName();

        try {
            // Vérifier si l'utilisateur a déjà participé au cours
            if (aDejaParticipe(userID, cours.getNom())) {
                // Afficher une alerte pour informer l'utilisateur
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Avertissement");
                alert.setHeaderText(null);
                alert.setContentText("Vous avez déjà participé à ce cours !");
                alert.showAndWait();
            } else {
                // L'utilisateur n'a pas encore participé, procéder à l'inscription
                if (cours.getCapacite() > 0) {
                    // Insérer l'ID du membre et l'ID du cours dans la table de participation
                    String queryInsert = "INSERT INTO participation (user_id, nom_cour, nom_participant) VALUES (?,?,?)";
                    PreparedStatement statementInsert = connection.prepareStatement(queryInsert);
                    statementInsert.setInt(1, userID);
                    statementInsert.setString(2, cours.getNom());
                    statementInsert.setString(3, userName); // Envoyer le nom du participant à la base de données

                    int rowsInserted = statementInsert.executeUpdate();

                    if (rowsInserted > 0) {


                        // Participation réussie
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Succès");
                        alert.setHeaderText(null);
                        alert.setContentText("Vous avez participé au cours avec succès !");
                        alert.showAndWait();

                        sendConfirmationEmail( loggedInUser,cours);
                        // Diminuer la capacité du cours dans la base de données
                        cours.setCapacite(cours.getCapacite() - 1);
                        String queryUpdate = "UPDATE cours SET capacite = ? WHERE id = ?";
                        PreparedStatement statementUpdate = connection.prepareStatement(queryUpdate);
                        statementUpdate.setInt(1, cours.getCapacite());
                        statementUpdate.setInt(2, cours.getId());
                        statementUpdate.executeUpdate();

                        afficherFenetreEvaluation(cours);

                        // Vérifier si la capacité est épuisée
                        if (cours.getCapacite() == 0) {
                            participerButton.setDisable(true);
                            participerButton.setText("Complet");
                        }
                    } else {
                        System.out.println("Erreur lors de la participation au cours.");
                    }
                } else {
                    System.out.println("La capacité de ce cours est épuisée. Vous ne pouvez plus participer.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean aDejaParticipe(int userID, String coursID) throws SQLException {
        String query = "SELECT COUNT(*) FROM participation WHERE user_id = ? AND nom_cour = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userID);
        statement.setString(2, coursID);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            return count > 0;
        }
        return false;
    }

    private void afficherCoursDansGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setMinHeight(1500);
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(20);
        gridPane.setVgap(20);

        int columnIndex = 0;
        int rowIndex = 0;
        for (Cours cours : coursList) {
            VBox card = createCourseCard(cours);
            gridPane.add(card, columnIndex, rowIndex);
            columnIndex++;
            if (columnIndex == 4) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        root.setCenter(scrollPane); // Ajout du scrollPane au centre de root
    }

    private void afficherCours() {
        coursList.clear();
        try {
            String query = "SELECT * FROM cours WHERE etat = 1 AND capacite > 0"; // Filtrer les cours avec capacité > 0
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom_cour");
                String duree = resultSet.getString("duree");
                String intensite = resultSet.getString("intensite");
                String cible = resultSet.getString("cible");
                String categorie = resultSet.getString("categorie");
                String objectif = resultSet.getString("objectif");
                boolean etat = resultSet.getBoolean("etat");
                int capacite = resultSet.getInt("capacite");
                byte[] imageBytes = resultSet.getBytes("image");

                Cours cours = new Cours();
                cours.setId(id);
                cours.setNom(nom);
                cours.setDuree(duree);
                cours.setIntensite(intensite);
                cours.setCible(cible);
                cours.setCategorie(categorie);
                cours.setObjectif(objectif);
                cours.setEtat(etat);
                cours.setCapacite(capacite);
                cours.setImage(imageBytes);

                coursList.add(cours);
            }

            afficherCoursDansGridPane(); // Ajouter les cours au GridPane
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void filtrerCours(String categorie, String cible, String objectif) {
        coursList.clear();
        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT * FROM cours WHERE etat = 1 "); // Ajoutez cette condition pour filtrer les cours avec un état de 1

            boolean hasFilter = false;

            if (categorie != null && !categorie.isEmpty()) {
                queryBuilder.append("AND categorie = ? ");
                hasFilter = true;
            }

            if (cible != null && !cible.isEmpty()) {
                queryBuilder.append("AND cible = ? ");
                hasFilter = true;
            }

            if (objectif != null && !objectif.isEmpty()) {
                queryBuilder.append("AND objectif = ? ");
                hasFilter = true;
            }

            if (!hasFilter) {
                afficherAucunProduitMessage();
                return;
            }

            PreparedStatement statement = connection.prepareStatement(queryBuilder.toString());
            int parameterIndex = 1;

            if (categorie != null && !categorie.isEmpty()) {
                statement.setString(parameterIndex++, categorie);
            }
            if (cible != null && !cible.isEmpty()) {
                statement.setString(parameterIndex++, cible);
            }
            if (objectif != null && !objectif.isEmpty()) {
                statement.setString(parameterIndex, objectif);
            }

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom_cour");
                String duree = resultSet.getString("duree");
                String intensite = resultSet.getString("intensite");
                String cibleResult = resultSet.getString("cible");
                String categorieResult = resultSet.getString("categorie");
                String objectifResult = resultSet.getString("objectif");
                boolean etat = resultSet.getBoolean("etat");
                int capacite = resultSet.getInt("capacite");
                byte[] imageBytes = resultSet.getBytes("image");

                Cours cours = new Cours();
                cours.setId(id);
                cours.setNom(nom);
                cours.setDuree(duree);
                cours.setIntensite(intensite);
                cours.setCible(cibleResult);
                cours.setCategorie(categorieResult);
                cours.setObjectif(objectifResult);
                cours.setEtat(etat);
                cours.setCapacite(capacite);
                cours.setImage(imageBytes);

                coursList.add(cours);
            }

            if (coursList.isEmpty()) {
                afficherAucunProduitMessage();
            } else {
                afficherCoursDansGridPane();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void afficherAucunProduitMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText("Aucun produit ne correspond à votre sélection.");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}