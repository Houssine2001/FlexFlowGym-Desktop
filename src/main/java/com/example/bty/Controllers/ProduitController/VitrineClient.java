package com.example.bty.Controllers.ProduitController;

import com.example.bty.Controllers.CourController.CourMembre;
import com.example.bty.Controllers.DashboardMembre;
import com.example.bty.Controllers.EvenementController.DashbordEvenement;
import com.example.bty.Controllers.EvenementController.clientVitrine;
import com.example.bty.Controllers.ReclamationController.AjouterReclamation;
import com.example.bty.Controllers.graphiqueGCP.FD;
import com.example.bty.Controllers.graphiqueGCP.Formoffre;
import com.example.bty.Entities.Commande;
import com.example.bty.Entities.Produit;
import com.example.bty.Entities.Role;
import com.example.bty.Entities.User;
import com.example.bty.Utils.Session;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
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

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.util.List;

public class VitrineClient extends Application {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/pidevgymweb";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    private List<Produit> produits; // Liste des produits à afficher
    private Connection connection;

    private Stage panierStage;
    private static String userName;
    private static int userId;
    Session session = Session.getInstance();
    User u=session.getLoggedInUser();
    User user ;

    private List<Commande> panier; // Panier du client
payer p = new payer();
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Affichage des produits");

        BorderPane root = new BorderPane();
        //root.setPadding(new Insets(10));

        VBox topBar = new VBox();
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setStyle("-fx-background-color: #2c3e50;\n" +
                "-fx-padding: 15;\n" +
                "-fx-border-radius: 15px;" +
                "-fx-background-insets: 0 0 0 218;");
        topBar.setSpacing(5);

        Image panierImage = new Image(getClass().getResourceAsStream("/com/example/bty/imagesModuleProduit/panier.png"));
        ImageView consulterPanierButton = new ImageView(panierImage);
        consulterPanierButton.setOnMouseClicked(event -> consulterPanier());
        consulterPanierButton.setFitWidth(40);
        consulterPanierButton.setFitHeight(40);

        TextField rechercheTextField = new TextField();
        rechercheTextField.setPromptText("Rechercher par nom");
        rechercheTextField.getStyleClass().add("search-text-field");

        Button rechercherButton = new Button("Rechercher");
        rechercherButton.getStyleClass().add("search-button");
        rechercherButton.setOnAction(e -> {
            String typeRecherche = rechercheTextField.getText();
            List<Produit> produitsRecherches = rechercherProduits(typeRecherche);
            afficherProduits(produitsRecherches, root);
        });





        HBox searchBox = new HBox(rechercheTextField, rechercherButton);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        searchBox.setSpacing(10);

        HBox buttonContainer = new HBox(consulterPanierButton);
        buttonContainer.setAlignment(Pos.CENTER_RIGHT);

        topBar.getChildren().addAll(buttonContainer, searchBox);
        root.setTop(topBar);

        ScrollPane scrollPane = new ScrollPane();
        VBox produitsBox = new VBox();
        scrollPane.setContent(produitsBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        root.setCenter(scrollPane);


        // f west methode start
        AnchorPane leftDashboard = createLeftDashboard(primaryStage);
        root.setLeft(leftDashboard);

        Scene scene = new Scene(root, 1366, 700); // Ajustez la largeur selon vos besoins
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.getStylesheets().add(getClass().getResource("/com/example/bty/CSSmoduleProduit/VitrineClient.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/dashboardDesign.css").toExternalForm());
       // scene.getStylesheets().add(getClass().getResource("/com/example/bty/CSSmoduleProduit/style.css").toExternalForm());

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        produits = consulterProduits();
        panier = new ArrayList<>(); // Initialiser le panier

        afficherProduits(produits, root);

       // ServiceProduit.printUserDetails();

    }






//Methode de SideBar a gauche

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
        dashboardAdmin.setTranslateY(-112);
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


        Text reportText = new Text("Signaler une suggestion");
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


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) {
        launch(args);
    }




    private List<Produit> consulterProduits() {
        List<Produit> produits = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM produit WHERE quantite > 0")) {

            while (resultSet.next()) {
                Produit produit = new Produit();
                produit.setIdProduit(resultSet.getInt("id"));
                produit.setNom(resultSet.getString("nom"));
                produit.setDescription(resultSet.getString("description"));
                produit.setPrix(resultSet.getDouble("prix"));
                produit.setType(resultSet.getString("type"));
                produit.setQuantite(resultSet.getInt("quantite"));
                produit.setQuantiteVendues(resultSet.getInt("quantite_vendues"));
                produit.setQuantiteDisponible(resultSet.getInt("quantite") - resultSet.getInt("quantite_vendues"));
                produit.setImage(resultSet.getBytes("image"));
                produits.add(produit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return produits;
    }




    private List<Produit> rechercherProduits(String typeRecherche) {
        List<Produit> produitsRecherches = new ArrayList<>();

        String query = "SELECT * FROM produit WHERE nom LIKE ? AND quantite > 0";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "%" + typeRecherche + "%");
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Produit produit = new Produit();
                    produit.setIdProduit(resultSet.getInt("id"));
                    produit.setNom(resultSet.getString("nom"));
                    produit.setDescription(resultSet.getString("description"));
                    produit.setPrix(resultSet.getDouble("prix"));
                    produit.setType(resultSet.getString("type"));
                    produit.setQuantite(resultSet.getInt("quantite"));
                    produit.setQuantiteVendues(resultSet.getInt("quantite_vendues"));
                    produit.setQuantiteDisponible(resultSet.getInt("quantite") - resultSet.getInt("quantite_vendues"));
                    produit.setImage(resultSet.getBytes("image"));

                    produitsRecherches.add(produit);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return produitsRecherches;
    }


    private void afficherProduits(List<Produit> produits, BorderPane root) {
        FlowPane produitsPane = new FlowPane();
        produitsPane.setMinHeight(1500);
        produitsPane.setAlignment(Pos.TOP_CENTER);
        produitsPane.setPadding(new Insets(10));
        produitsPane.setHgap(20);
        produitsPane.setVgap(20);

        for (Produit produit : produits) {
            VBox card = createProductCard(produit);
            produitsPane.getChildren().add(card);
        }

        ((ScrollPane) root.getCenter()).setContent(produitsPane);
    }



    private VBox createProductCard(Produit produit) {
        VBox card = new VBox();

        card.getStyleClass().add("product-card");
        card.setAlignment(Pos.TOP_CENTER);
        card.setSpacing(6);
        card.setPrefWidth(200); // Définir une largeur préférée fixe
        card.setMinHeight(330); // Définir une hauteur minimale fixe

        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(produit.getImage())));
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);

        Label nomLabel = new Label("Nom: " + produit.getNom());
        nomLabel.setStyle("-fx-font-size: 17px; -fx-font-weight: bold;-fx-text-fill: #1E0F1C");
        Label typeLabel = new Label("Type: " + produit.getType());
        typeLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;-fx-text-fill: #2c3e50;");
        Label prixLabel = new Label("Prix: " + produit.getPrix() + " Dt");
        prixLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #A62609;");

        // Ajouter une icône de description
        ImageView descriptionIcon = new ImageView(new Image(getClass().getResource("/com/example/bty/imagesModuleProduit/d.png").toExternalForm()));
        descriptionIcon.setFitWidth(20);
        descriptionIcon.setFitHeight(20);
        descriptionIcon.setOnMouseClicked(e -> afficherDescriptionPrompt(produit));

        card.getChildren().addAll(imageView, nomLabel, typeLabel, prixLabel, descriptionIcon);

        Button addToCartButton = new Button("Ajouter au panier");
        addToCartButton.getStyleClass().add("add-to-cart-button");

        ObservableList<Integer> quantiteOptions = FXCollections.observableArrayList(1, 2, 3, 4, 5);
        ComboBox<Integer> quantiteComboBox = new ComboBox<>(quantiteOptions);
        quantiteComboBox.getStyleClass().add("quantity-combo-box");
        quantiteComboBox.setValue(1);

        addToCartButton.setOnAction(e -> {
            int quantiteChoisie = quantiteComboBox.getValue();
            if (quantiteChoisie <= produit.getQuantite()) {
                ajouterAuPanier(produit, quantiteChoisie);
                showAlert("Succès", "Produit ajouté au panier !");
            } else {
                showAlert("Rupture de stock", "La quantité demandée n'est pas disponible en stock.");
            }
        });

        HBox addToCartBox = new HBox(addToCartButton, quantiteComboBox);
        addToCartBox.setAlignment(Pos.CENTER);
        addToCartBox.setSpacing(10);
        card.getChildren().add(addToCartBox);

        return card;
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void afficherDescriptionPrompt(Produit produit) {
        Stage descriptionStage = new Stage();
        descriptionStage.initModality(Modality.APPLICATION_MODAL);
        descriptionStage.setTitle("Description du Produit");

        Label descriptionLabel = new Label(produit.getDescription());
        descriptionLabel.setWrapText(true); // Permet le retour à la ligne automatique

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().add(descriptionLabel);

        Scene scene = new Scene(layout, 300, 200);
        descriptionStage.setScene(scene);

        descriptionStage.showAndWait();
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void ajouterAuPanier(Produit produit, int quantite) {
        Commande commande = new Commande(produit.getIdProduit(), produit.getNom(), quantite, (int) (produit.getPrix() * quantite));
        panier.add(commande);
    }





    private void consulterPanier() {
        if (panier.isEmpty()) {
            showAlert("Info", "Le panier est vide.");
        } else {
            afficherPanier();
        }
    }




    private void afficherPanier() {
        panierStage = new Stage();
        panierStage.setTitle("Consulter le Panier");

        VBox panierLayout = new VBox();
        panierLayout.setSpacing(10);
        panierLayout.setPadding(new Insets(10));

        for (Commande commande : panier) {
            Label produitLabel = new Label("Produit: " + commande.getNomProduit());
            Label quantiteLabel = new Label("Quantité: " + commande.getQuantite());
            Label montantLabel = new Label("Montant: " + commande.getMontantTotale() + " Dt");
            Separator separator = new Separator();

            panierLayout.getChildren().addAll(produitLabel, quantiteLabel, montantLabel, separator);
        }



        Button confirmerAchatButton = new Button("Confirmer l'achat");
        Button annulerAchatButton = new Button("Annuler l'achat");

        confirmerAchatButton.setOnAction(e -> confirmerAchat(calculerMontantTotal()));
        annulerAchatButton.setOnAction(e -> annulerAchat());

        HBox buttonsBox = new HBox(confirmerAchatButton, annulerAchatButton);
        buttonsBox.setSpacing(10);

        panierLayout.getChildren().add(buttonsBox);

        Scene panierScene = new Scene(panierLayout, 300, 400);
        panierStage.setScene(panierScene);
        panierStage.show();
    }



    private long calculerMontantTotal() {
        // Calculate the total amount from panier
        long montantTotal = panier.stream().mapToLong(Commande::getMontantTotale).sum();

        // Divide by 0.3 and multiply by 10
        return (long) (montantTotal / 0.3 * 10);
    }




    private void confirmerAchat(long montantTotal) {
        User loggedInUser = Session.getInstance().getLoggedInUser();
//        loggedInUser.getId();
//        System.out.println( loggedInUser.getId());

        for (Commande commande : panier) {
            enregistrerCommande(commande);
            mettreAJourQuantiteProduit(commande.getIdProduit(), commande.getQuantite());
        }

        long montantTotal1 = calculerMontantTotal();
        String nomFichierFacture = imprimerFacture(panier);

        Stage paymentStage = new Stage();
        paymentStage.setTitle("Paiement");

        payer paymentController = new payer(montantTotal1);

        AnchorPane paymentRoot = paymentController.createPaymentInterface();
        Scene paymentScene = new Scene(paymentRoot, 800, 600);
        paymentScene.getStylesheets().add(getClass().getResource("/com/example/bty/CSSmoduleProduit/style.css").toExternalForm());
        paymentStage.setScene(paymentScene);
        paymentStage.showAndWait();

        boolean paymentSuccess = paymentController.isPaymentSuccessful();

        if (!paymentSuccess) {
            showAlert("Erreur", "Le paiement a échoué ou a été annulé. Veuillez réessayer.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Impression de la facture");
        confirmDialog.setHeaderText("Voulez-vous imprimer votre facture ?");
        confirmDialog.setContentText("Choisissez votre option :");

        ButtonType buttonTypeOui = new ButtonType("Oui");
        ButtonType buttonTypeNon = new ButtonType("Non");

        confirmDialog.getButtonTypes().setAll(buttonTypeOui, buttonTypeNon);
        String userEmail = loggedInUser.getEmail();
        sendConfirmationEmail(loggedInUser, nomFichierFacture, userEmail);
        showAlert("Succès", "Paiement confirmé, Vérifie votre email");
        confirmDialog.showAndWait().ifPresent(buttonType -> {
            boolean paymentSuccesss = processPayment(montantTotal);

            if (buttonType == buttonTypeOui) {
              /*  if (paymentSuccesss) {
                    String userEmail = loggedInUser.getEmail();
                    sendConfirmationEmail(loggedInUser, nomFichierFacture, userEmail);
                    showAlert("Succès", "Paiement et facture envoyés avec succès !");
                } else {
                    showAlert("Erreur", "Le paiement a échoué. Veuillez réessayer.");
                }*/

            } else {
               /* if (paymentSuccesss) {
                    String userEmail = loggedInUser.getEmail();
                    sendConfirmationEmail(loggedInUser, nomFichierFacture, userEmail);
                    showAlert("Succès", "Paiement confirmé, Vérifie votre email");
                } else {
                    showAlert("Erreur", "Le paiement a échoué. Veuillez réessayer.");
                }*/
            }

            if (panierStage != null) {
                panierStage.hide();
            }

            viderPanier();
        });
    }






    private void sendConfirmationEmail(User loggedInUser, String nomFichierFacture, String userEmail) {
        final String username = "bahaeddinedridi1@gmail.com";
        final String password = "oman kvgj hdks njqc";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        javax.mail.Session session = javax.mail.Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username, "Flex Flow"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject("Confirmation d'achat");

            BodyPart textBodyPart = new MimeBodyPart();
            textBodyPart.setText("Bonjour " + loggedInUser.getName() + ","
                    + "\n\nVotre commande sera prête à être retirée. Vous pouvez venir la récupérer à tout moment."
                    + "\n\nVotre commande est valable pendant une semaine à partir d'aujourd'hui (" + getCurrentDate() + ")."
                    + "\n\nVous pouvez consulter votre facture dans la pièce jointe."
                    + "\n\nCordialement,\nVotre application");

            MimeBodyPart pdfAttachment = new MimeBodyPart();
            pdfAttachment.attachFile(new File(nomFichierFacture));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textBodyPart);
            multipart.addBodyPart(pdfAttachment);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("E-mail de confirmation envoyé avec succès à : " + userEmail);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(new Date());
    }


    private static boolean processPayment(long amount) {
        try {
            // Set your secret key here
            Stripe.apiKey = "sk_test_51OopTSDtHS4Nu6kaTroMy6hwN1MKCBKitrzK3lm26xblje6CYwCiHccuY5VB1uNQppoCOSn6C6u92jn7i6zjLikl00zSebwoIU";

            // Create a PaymentIntent with other payment details
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency("usd")
                    .build();

            PaymentIntent intent = PaymentIntent.create(params);

            // If the payment was successful, return true
            System.out.println("Payment successful. PaymentIntent ID: " + intent.getId());
            return true;
        } catch (StripeException e) {
            // If there was an error processing the payment, display the error message and return false
            System.out.println("Payment failed. Error: " + e.getMessage());
            return false;
        }
    }




    private String imprimerFacture(List<Commande> panier) {
        Document document = new Document();
        String nomFichier = "";

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String timestamp = dateFormat.format(new Date());
            String codeAchat1 = generateUniqueCode1();
            String codeAchat = generateUniqueCode();

            nomFichier = "facture_" + codeAchat + ".pdf";

            PdfWriter.getInstance(document, new FileOutputStream(nomFichier));
            document.open();

            Font fontEnTete = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Paragraph enTete = new Paragraph("Facture d'achat - " + timestamp + " (Code d'achat: " + codeAchat1 + ")", fontEnTete);
            enTete.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(enTete);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20f);

            Font fontDetails = new Font(Font.FontFamily.HELVETICA, 12);

            PdfPCell cellProduit = new PdfPCell(new Phrase("Produit", fontDetails));
            PdfPCell cellQuantite = new PdfPCell(new Phrase("Quantité", fontDetails));
            PdfPCell cellMontant = new PdfPCell(new Phrase("Montant (Dt)", fontDetails));

            table.addCell(cellProduit);
            table.addCell(cellQuantite);
            table.addCell(cellMontant);

            for (Commande commande : panier) {
                table.addCell(commande.getNomProduit());
                table.addCell(String.valueOf(commande.getQuantite()));
                table.addCell(String.valueOf(commande.getMontantTotale()));
            }

            document.add(table);

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        return nomFichier;
    }






    private String generateUniqueCode() {
        // Générer un code unique en utilisant la date et un identifiant aléatoire
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String datePart = dateFormat.format(new Date());

        // Ajouter un identifiant aléatoire (par exemple, 4 chiffres)
        String randomPart = String.format("%05d", new Random().nextInt(10000));

        return datePart + randomPart;
    }







    private String generateUniqueCode1() {
        // Générer un code unique en utilisant la date et un identifiant aléatoire
       // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
       // String datePart = dateFormat.format(new Date());

        // Ajouter un identifiant aléatoire (par exemple, 4 chiffres)
        String randomPart = String.format("%05d", new Random().nextInt(10000));

        return  randomPart;
    }





    private void mettreAJourQuantiteProduit(int idProduit, int quantiteAchete) {
        try {
            String query = "UPDATE produit SET Quantite = Quantite - ?, quantite_vendues = quantite_vendues + ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, quantiteAchete);
                statement.setInt(2, quantiteAchete);
                statement.setInt(3, idProduit);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }







    private void enregistrerCommande(Commande commande) {
        User loggedInUser = Session.getInstance().getLoggedInUser();

        try {
            String query = "INSERT INTO commande (date_commande, id_produit, nom, montant,nom_user) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                statement.setInt(2, commande.getIdProduit());
                statement.setString(3, commande.getNomProduit());
                statement.setDouble(4, commande.getMontantTotale());
                statement.setString(5,  loggedInUser.getName());

                statement.executeUpdate();

                System.out.println("Commande enregistrée avec succès !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erreur lors de l'insertion de la commande : " + e.getMessage());

        }
    }


    private void annulerAchat() {
        showAlert("Info", "Achat annulé.");

        if (panierStage != null) {
            viderPanier();
            panierStage.hide();
        }
    }

    private void viderPanier() {
        System.out.println("Vidage du panier...");
        panier.clear();
        //afficherPanier();
        System.out.println("Panier vidé.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}