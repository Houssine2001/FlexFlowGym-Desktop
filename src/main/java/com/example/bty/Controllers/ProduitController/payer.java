package com.example.bty.Controllers.ProduitController;

import com.example.bty.Services.ServiceProduit;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class payer extends Application {
    private boolean paymentSuccessful = false;
    private long montantTotal;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = createPaymentInterface();

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/com/example/bty/CSSmoduleProduit/style.css").toExternalForm());

        primaryStage.setTitle("Payment");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public payer(){}
    public payer(long montantTotal) {
        this.montantTotal = montantTotal;
    }

    public AnchorPane createPaymentInterface() {
        AnchorPane root = new AnchorPane();
        root.setPrefSize(800, 600);

        Label titleLabel = new Label("Paiment ");
        titleLabel.setStyle("-fx-font-size: 30;");
        titleLabel.setLayoutX(316);
        titleLabel.setLayoutY(34);

        Pane totalPane = new Pane();
        totalPane.setPrefSize(199, 53);
        totalPane.setStyle("-fx-background-radius: 20; -fx-background-color: #dfcdc3;");
        totalPane.setLayoutX(76);
        totalPane.setLayoutY(533);
        System.out.println("Montant total : " + (Math.ceil(montantTotal * 0.3 / 10))); // Vérifiez si montantTotal a une valeur

        Label totalLabel = new Label("Total : " + (Math.ceil(montantTotal * 0.3 / 10)) + " Dt");
        // Utiliser le montant total passé lors de la création de l'instance de payer
        totalLabel.setStyle("-fx-font-size: 20;-fx-text-fill: #2c3e50;");
        totalLabel.setLayoutX(14);
        totalLabel.setLayoutY(11);
        totalPane.getChildren().add(totalLabel);

        Pane paymentPane = new Pane();
        paymentPane.setPrefSize(656, 395);
        paymentPane.setStyle("-fx-background-radius: 20; -fx-background-color: #dfcdc3;");
        paymentPane.setLayoutX(76);
        paymentPane.setLayoutY(105);

        Button payButton = new Button("Payer");
        payButton.getStyleClass().add("pay-button");

        payButton.setLayoutX(510);
        payButton.setLayoutY(343);

        GridPane paymentGrid = new GridPane();
        paymentGrid.setHgap(5);
        paymentGrid.setLayoutX(50);
        paymentGrid.setLayoutY(25);

        Label nameLabel = new Label("Nom :");
        nameLabel.setStyle("-fx-text-fill: #2c3e50;");

        TextField nameTextField = new TextField();
        nameTextField.setPrefHeight(37);
        nameTextField.setPromptText("Nom sur la carte");

        paymentGrid.add(nameLabel, 0, 0);
        paymentGrid.add(nameTextField, 1, 0);

        paymentPane.getChildren().addAll(payButton, paymentGrid);

        GridPane emailGrid = new GridPane();
        emailGrid.setHgap(5);
        emailGrid.setLayoutX(127);
        emailGrid.setLayoutY(174);

        Label emailLabel = new Label("Email :");
        emailLabel.setStyle("-fx-text-fill:#2c3e50;");

        TextField emailTextField = new TextField();
        emailTextField.setPrefHeight(37);
        emailTextField.setPromptText("exemple@gmail.com");

        emailGrid.add(emailLabel, 0, 0);
        emailGrid.add(emailTextField, 1, 0);

        Label cardLabel = new Label("N° de carte:");
        cardLabel.setStyle("-fx-text-fill: #2c3e50;");
        cardLabel.setLayoutX(127);
        cardLabel.setLayoutY(233);

        TextField numCardTextField = new TextField();
        numCardTextField.setLayoutX(127);
        numCardTextField.setLayoutY(275);
        numCardTextField.setPrefHeight(37);
        numCardTextField.setPromptText("**** **** **** ****");

        ImageView cardImageView = new ImageView(new Image("/card_pay.png"));
        cardImageView.setFitHeight(38);
        cardImageView.setFitWidth(115);
        cardImageView.setLayoutX(336);
        cardImageView.setLayoutY(281);

        GridPane dateGrid = new GridPane();
        dateGrid.setHgap(5);
        dateGrid.setLayoutX(127);
        dateGrid.setLayoutY(335);
        dateGrid.setPrefSize(300, 37);

        Spinner<Integer> mmSpinner = new Spinner<>(1, 12, 1);
        mmSpinner.setPrefHeight(37);
        mmSpinner.setPrefWidth(102);

        Spinner<Integer> yySpinner = new Spinner<>(2022, 2030, 2022);
        yySpinner.setPrefHeight(37);
        yySpinner.setPrefWidth(102);

        Label mmLabel = new Label("MM :");
        mmLabel.setStyle("-fx-text-fill: #2c3e50;");

        Label yyLabel = new Label("YY :");
        yyLabel.setStyle("-fx-text-fill: #2c3e50;");

        dateGrid.add(mmSpinner, 1, 0);
        dateGrid.add(yySpinner, 3, 0);
        dateGrid.add(mmLabel, 0, 0);
        dateGrid.add(yyLabel, 2, 0);

        GridPane cvcGrid = new GridPane();
        cvcGrid.setHgap(5);
        cvcGrid.setLayoutX(127);
        cvcGrid.setLayoutY(388);

        Label cvcLabel = new Label("CVC  :");
        cvcLabel.setStyle("-fx-text-fill: #2c3e50;");

        Spinner<Integer> cvcSpinner = new Spinner<>(100, 999, 100);
        cvcSpinner.setPrefHeight(37);
        cvcSpinner.setPrefWidth(102);

        cvcGrid.add(cvcLabel, 0, 0);
        cvcGrid.add(cvcSpinner, 1, 0);

        Button backButton = new Button("Retour ");
        backButton.setLayoutX(28);
        backButton.setLayoutY(33);
        backButton.setMnemonicParsing(false);

        ImageView backImageView = new ImageView(new Image("/card_pay.png"));
        backImageView.setFitHeight(24);
        backImageView.setFitWidth(38);
        backButton.setGraphic(backImageView);

        root.getChildren().addAll(titleLabel, totalPane, paymentPane, emailGrid, cardLabel, numCardTextField,
                cardImageView, dateGrid, cvcGrid, backButton);

        payButton.setOnAction(event -> {
            String name = nameTextField.getText();
            String email = emailTextField.getText();
            String cardInfo = numCardTextField.getText();
            int mm = mmSpinner.getValue();
            int yy = yySpinner.getValue();
            int cvc = cvcSpinner.getValue();

            // Vérifier que tous les champs sont saisis
            if (name.isEmpty() || email.isEmpty() || cardInfo.isEmpty()) {
                showAlert("Erreur de saisie", "Veuillez saisir tous les champs.");
                return;
            }

            // Vérifier le format de l'email
            if (!isValidEmail(email)) {
                showAlert("Erreur de saisie", "Veuillez saisir une adresse e-mail valide.");
                return;
            }

            // Vérifier le format du numéro de carte
            if (!isValidCardNumber(cardInfo)) {
                showAlert("Erreur de saisie", "Le numéro de carte doit être composé de 16 chiffres.");
                return;
            }

            double total = Math.ceil(montantTotal * 0.3 / 10);


            // Insérer dans la base de données
            boolean insertionSuccess = ServiceProduit.insertPayment(name, email, cardInfo, mm, yy, cvc, total);
            paymentSuccessful = insertionSuccess;

            // Afficher un message en fonction du résultat de l'insertion
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Résultat de l'insertion");

            if (insertionSuccess) {
                alert.setHeaderText("Succès !");
                alert.setContentText("Vos données ont été sauvegardées avec succès et en toute sécurité.");
            } else {
                alert.setHeaderText("Échec !");
                alert.setContentText("La sauvegarde des données a échoué. Veuillez réessayer.");
            }

            alert.showAndWait();
            // Obtenir la scène parente et la fermer
            if (insertionSuccess) {
                Stage stage = (Stage) payButton.getScene().getWindow();
                stage.close();
            }
        });

        return root;
    }


    private boolean isValidEmail(String email) {
        // Ajoutez ici votre logique de validation d'email
        // Par exemple, utilisez une expression régulière
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    // Méthode pour vérifier le format du numéro de carte
    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber.matches("\\d{16}");
    }

    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }
    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
