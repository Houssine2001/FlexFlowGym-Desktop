package com.example.bty.Controllers;

import com.example.bty.Controllers.ProduitController.VitrineClient;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DashboardVitrineController extends Application {
   // private VitrineClient vitrineClient;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dashboard");

        AnchorPane mainForm = new AnchorPane();
        mainForm.setPrefSize(1100, 750);

        AnchorPane dashboardAdmin = new AnchorPane();
        dashboardAdmin.setPrefSize(234, 750);
        dashboardAdmin.getStyleClass().add("border-pane");

        FontAwesomeIconView usernameAdmin = createFontAwesomeIconView("USER", "WHITE", 50, 82, 91);
        Label welcomeLabel = createLabel("Welcome,", "Tahoma", 15, 78, 101,"WHITE");
        Label usernameLabel = createLabel("MarcoMan", "Arial Bold", 20, 11, 120,"WHITE");
       // Line line = createLine(-100, 152, 100, 152, 111);
        Line line = createColoredLine(-100, 152, 100, 152, 111, "WHITE");

        Button dashboardBtn = createButton("Dashboard", 21, 186);
        Button coachesBtn = createButton("Coaches", 22, 228);
        Button membersBtn = createButton("Membres", 22, 271);
        Button eventsAdminBtn = createButton("Evenements", 22, 313);
        Button coursAdminBtn = createButton("Commande", 22, 357);
        Button storeAdminBtn = createButton("Store", 22, 405);
        storeAdminBtn.setOnAction(event -> {
            // Instancier et afficher la vue DashboardVitrineController
            VitrineClient v = new VitrineClient();
            v.start(primaryStage);
        });

        coursAdminBtn.setOnAction(event -> {
            // Instancier et afficher la vue DashboardVitrineController
            DashboardVitrineController dashboardVitrineController = new DashboardVitrineController();
            dashboardVitrineController.start(primaryStage);
        });
        Line line2 = createColoredLine(-100, 449, 100, 449, 112, "WHITE");

        Button profileAdminBtn = createButton("Profile", 22, 462);
        Button logoutBtn = createButton("Logout", 22, 503);

        FontAwesomeIconView[] icons = {
                createFontAwesomeIconView("USER", "WHITE", 20, 41, 254),
                createFontAwesomeIconView("USERS", "WHITE", 20, 39, 295),
                createFontAwesomeIconView("CALENDAR", "WHITE", 20, 40, 337),
                createFontAwesomeIconView("BOOK", "WHITE", 20, 39, 382),
                createFontAwesomeIconView("SHOPPING_CART", "WHITE", 20, 40, 429),
                createFontAwesomeIconView("HOME", "WHITE", 20, 40, 212),
                createFontAwesomeIconView("ID_CARD", "WHITE", 20, 37, 486),
                createFontAwesomeIconView("EXTERNAL_LINK", "WHITE", 20, 40, 529)
        };

        VBox reportContainer = new VBox();
        reportContainer.setLayoutX(17);
        reportContainer.setLayoutY(601);
        reportContainer.setPrefSize(180, 91);
        reportContainer.getStyleClass().add("report_container");


        Text reportText = new Text("Report Suggestion/Bug?");
        reportText.getStyleClass().add("report_text");

        Label reportLabel = new Label("Use this to report any errors or suggestions.");
        reportLabel.getStyleClass().add("report_label");

        Button reportButton = createButton("Report", 0, 0);
        reportButton.getStyleClass().add("report_button");



        reportContainer.getChildren().addAll(reportText, reportLabel, reportButton);

        StackPane contentPlaceholder = new StackPane();
        contentPlaceholder.setLayoutX(220);
        contentPlaceholder.setLayoutY(0);

        dashboardAdmin.getChildren().addAll(
                usernameAdmin, welcomeLabel, usernameLabel, line,
                dashboardBtn, coachesBtn, membersBtn, eventsAdminBtn,
                coursAdminBtn, storeAdminBtn, line2, profileAdminBtn,
                logoutBtn, icons[0], icons[1], icons[2], icons[3],
                icons[4], icons[5], icons[6], icons[7], reportContainer,
                contentPlaceholder
        );

        mainForm.getChildren().addAll(dashboardAdmin);

        Scene scene = new Scene(mainForm, 1366, 728);
        scene.getStylesheets().add("/dashboardDesign.css");
        scene.getStylesheets().add("/dashboardStyle.css");

        primaryStage.setScene(scene);
        primaryStage.show();






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
        label.setFont(new Font(fontName, fontSize));
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
