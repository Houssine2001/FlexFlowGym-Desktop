package com.example.bty.Controllers.EvenementController;

import com.example.bty.Entities.Evenement;
import com.example.bty.Entities.Role;
import com.example.bty.Entities.User;
import com.example.bty.Utils.ConnexionDB;
import com.example.bty.Utils.Session;
import com.google.zxing.BarcodeFormat;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
public class DetailsEvenementWindow extends Stage {
    private static ToggleButton scannerButton;
    private ImageView qrCodeImageView;
    private static Connection connection;
    private PreparedStatement statementInsert;



    private static Map<Evenement, VBox> detailsMap = new HashMap<>();
    private static final String TWILIO_ACCOUNT_SID = "AC8802c2e9768e4876ace30c6beb9ba980";
    private static final String TWILIO_AUTH_TOKEN = "6ecb828326a724faf557c7f259f06ef5";
    private static final String TWILIO_PHONE_NUMBER = "+19497102963";

    public DetailsEvenementWindow() {
        connection = ConnexionDB.getInstance().getConnexion();



    }

    public static void display(Evenement evenement) {
        DetailsEvenementWindow instance = new DetailsEvenementWindow(); // Create an instance


        Stage detailsStage = new Stage();
        detailsStage.setTitle("Détails de l'événement");
        detailsStage.initModality(Modality.APPLICATION_MODAL);
        detailsStage.setResizable(false);
        VBox detailsCard = createDetailsCard(evenement);


        Scene scene = new Scene(detailsCard, 400, 300);
        scene.getStylesheets().add(DetailsEvenementWindow.class.getResource("/com/example/bty/CSSmoduleEvenement/VitrineClient.css").toExternalForm());

        detailsStage.setScene(scene);
        detailsStage.show();
    }

    public static VBox createDetailsCard(Evenement evenement) {
        DetailsEvenementWindow instance = new DetailsEvenementWindow();
        User loggedInUser = Session.getInstance().getLoggedInUser();

        VBox detailsCard = detailsMap.get(evenement);

        if (detailsCard == null) {
            detailsCard = new VBox();
            detailsCard.getStyleClass().add("product-card");
            detailsCard.setSpacing(10);
            detailsCard.setPadding(new Insets(20));

            Label dateLabel = new Label("Date : " + evenement.getDate());
            Label timeLabel = new Label("Time : " + evenement.getTime());
            Label categorieLabel = new Label("Catégorie : " + evenement.getCategorie());
            Label objectifLabel = new Label("Objectif : " + evenement.getObjectif());
            Label placeLabel = new Label("Nombre de places : " + evenement.getNbre_place());
            Button ReserverButton = new Button("Reserver");
            ReserverButton.getStyleClass().add("add-to-cart-button");

            instance.scannerButton = new ToggleButton("Scanner Passe");
            instance.scannerButton.getStyleClass().add("add-to-cart-button");
            instance.scannerButton.setDisable(true);

            ReserverButton.setOnAction(event -> {
                instance.participerAuCours(evenement, ReserverButton);
            });

            instance.scannerButton.setOnAction(event -> {
                if (instance.scannerButton.isSelected()) {
                    // Generate QR Code for reservation details
                    try {
                        generateQRCode(evenement, loggedInUser);
                    } catch (IOException | WriterException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Scanner Code désactivé");
                }
            });
        //    instance.countdownLabel = new Label(); // Initialize countdownLabel here

            boolean userHasReserved = hasUserReservedEvent(loggedInUser.getId(), evenement.getNom());
            instance.scannerButton.setDisable(!userHasReserved);
            scannerButton = instance.scannerButton; // Add this line to initialize scannerButton correctly


                detailsCard.getChildren().addAll(dateLabel, timeLabel, categorieLabel, objectifLabel, placeLabel, ReserverButton, instance.scannerButton);


            detailsMap.put(evenement, detailsCard);
        }
        return detailsCard;
    }

    private static void generateQRCode(Evenement evenement, User user) throws IOException, WriterException {
        String reservationDetails = "Nom de l'événement: " + evenement.getNom() + "\n" +
                "Nom du participant: " + user.getName() + "\n" +
                "Date: " + evenement.getDate() + "\n" +
                "Heure: " + evenement.getTime();

        // Convertir la chaîne de caractères en octets en spécifiant l'encodage UTF-8
        byte[] reservationBytes = reservationDetails.getBytes("UTF-8");

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(new String(reservationBytes, "UTF-8"), BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();
        afficherQRCode(qrCodeBytes);
    }


    private static void afficherQRCode(byte[] qrCodeBytes) {
        Stage qrCodeStage = new Stage();
        qrCodeStage.setTitle("Code QR");

        ImageView qrCodeImageView = new ImageView(new Image(new ByteArrayInputStream(qrCodeBytes)));

        VBox qrCodeLayout = new VBox();
        qrCodeLayout.getChildren().add(qrCodeImageView);
        qrCodeLayout.setAlignment(Pos.CENTER);
        qrCodeLayout.setPadding(new Insets(20));

        Scene qrCodeScene = new Scene(qrCodeLayout);
        qrCodeStage.setScene(qrCodeScene);
        qrCodeStage.show();
    }
    private void participerAuCours(Evenement evenement, Button ReserverButton) {
        User loggedInUser = Session.getInstance().getLoggedInUser();

        if (loggedInUser != null) {
            if (loggedInUser.getRoles() != null && loggedInUser.getRoles().length > 0 && loggedInUser.getRoles()[0] == Role.MEMBRE) {
                if (!hasUserReservedEvent(loggedInUser.getId(), evenement.getNom())) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation de Reservation");
                    alert.setHeaderText(null);
                    alert.setContentText("Veuillez confirmer votre inscription à cet événement.");

                    ButtonType buttonTypeOui = new ButtonType("Oui");
                    ButtonType buttonTypeNon = new ButtonType("Non");
                    alert.getButtonTypes().setAll(buttonTypeOui, buttonTypeNon);

                    alert.showAndWait().ifPresent(response -> {
                        if (response == buttonTypeOui) {
                            participerAuCoursEffectif(evenement, ReserverButton, loggedInUser);
                        }
                    });
                } else {
                    afficherMessage("Information", "Vous avez déjà réservé cet événement.");
                }
            } else {
                System.out.println("Seuls les membres peuvent réserver cet événement.");
            }
        } else {
            System.out.println("Vous devez être connecté pour réserver cet événement.");
        }
    }

    private void participerAuCoursEffectif(Evenement evenement, Button ReserverButton, User loggedInUser) {
        int userID = loggedInUser.getId();
        String userName = loggedInUser.getName();

        try {
            if (evenement.getNbre_place() > 0) {
                String queryInsert = "INSERT INTO reservation (id_user, nomEvenement, nomParticipant,date_reservation) VALUES (?,?,?,?)";
                PreparedStatement statementInsert = connection.prepareStatement(queryInsert);
                statementInsert.setInt(1, userID);
                statementInsert.setString(2, evenement.getNom());
                statementInsert.setString(3, userName);
                statementInsert.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                int rowsInserted = statementInsert.executeUpdate();

                if (rowsInserted > 0) {
                    afficherMessage("Succès", "Vous avez participé à l'evenement avec succès !");

                    evenement.setNbre_place(evenement.getNbre_place() - 1);
                    String queryUpdate = "UPDATE evenement SET nbrPlace = ? WHERE id_evenement  = ?";
                    PreparedStatement statementUpdate = connection.prepareStatement(queryUpdate);
                    statementUpdate.setInt(1, evenement.getNbre_place());
                    statementUpdate.setInt(2, evenement.getId());
                    statementUpdate.executeUpdate();

                    if (evenement.getNbre_place() == 0) {
                        ReserverButton.setDisable(true);
                        ReserverButton.setText("Complet");
                        ReserverButton.setStyle("-fx-opacity: 0.5");
                    }


                    scannerButton.setDisable(false);
                    sendReservationConfirmationSMS(loggedInUser.getName(), evenement.getNom());

                } else {
                    afficherMessage("Échec", "Erreur lors de la participation à l'evenement.");
                }
            } else {
                afficherMessage("INFORMATION", "La capacité de cet evenement est épuisée. Vous ne pouvez plus reserver.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void sendReservationConfirmationSMS(String participantName, String eventName) {
        Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);

        // Replace "toPhoneNumber" with the recipient's phone number
        // Format: "+1234567890"
        String toPhoneNumber = "+21653602680";  // Remplacez par le numéro de téléphone réel du participant

        String smsBody = "Bienvenue " + participantName + " à l'événement " + eventName;

        Message message = Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(TWILIO_PHONE_NUMBER),
                smsBody
        ).create();

        System.out.println("SMS sent with SID: " + message.getSid());
    }



    private void afficherMessage(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    private static boolean hasUserReservedEvent(int userId, String eventName) {
        try {
            String queryCheckReservation = "SELECT COUNT(*) FROM reservation WHERE id_user = ? AND nomEvenement = ?";
            PreparedStatement statementCheckReservation = connection.prepareStatement(queryCheckReservation);
            statementCheckReservation.setInt(1, userId);
            statementCheckReservation.setString(2, eventName);
            ResultSet resultSet = statementCheckReservation.executeQuery();

            if (resultSet.next()) {
                int reservationCount = resultSet.getInt(1);
                return reservationCount > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }



}