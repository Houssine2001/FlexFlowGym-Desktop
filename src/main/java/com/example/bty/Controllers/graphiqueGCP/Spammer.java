package com.example.bty.Controllers.graphiqueGCP;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Spammer extends Application {

    // Vos identifiants Twilio
    public static final String ACCOUNT_SID = "AC2d5006eb11366ebad1a5aef33f894e09";
    public static final String AUTH_TOKEN = "daa2cdd068933e4d5ca081d5340dbf9c";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Envoyer un SMS");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // Label et champ de saisie pour le numéro de téléphone
        Label phoneLabel = new Label("Numéro de téléphone:");
        TextField phoneField = new TextField();
        GridPane.setConstraints(phoneLabel, 0, 0);
        GridPane.setConstraints(phoneField, 1, 0);

        // Label et champ de saisie pour le message
        Label messageLabel = new Label("Message:");
        TextField messageField = new TextField();
        GridPane.setConstraints(messageLabel, 0, 1);
        GridPane.setConstraints(messageField, 1, 1);

        // Bouton d'envoi du SMS
        Button sendButton = new Button("Envoyer");
        GridPane.setConstraints(sendButton, 1, 2);
        sendButton.setOnAction(e -> {
            String phoneNumber = phoneField.getText();
            String message = messageField.getText();

            // Envoi du SMS via l'API Twilio
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message twilioMessage = Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber("+14159152513"),
                    message
            ).create();

            System.out.println("SMS envoyé avec l'ID : " + twilioMessage.getSid());
        });

        grid.getChildren().addAll(phoneLabel, phoneField, messageLabel, messageField, sendButton);

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
