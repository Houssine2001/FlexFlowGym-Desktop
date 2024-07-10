module com.example.bty {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;

    requires com.almasb.fxgl.all;
    requires java.sql;
    requires jbcrypt;
    requires javafx.web;
    requires itextpdf;
    requires stripe.java;
    requires java.desktop;
    requires java.mail;

    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires java.prefs;
    requires twilio;
    requires totp;
    requires com.google.gson;


    opens com.example.bty to javafx.fxml;
    exports com.example.bty;
    exports com.example.bty.Controllers;
    exports com.example.bty.Controllers.Admin;
    exports com.example.bty.Controllers.Coach;
    exports com.example.bty.Entities;
    exports com.example.bty.Services;
    opens com.example.bty.Controllers to javafx.fxml;
    exports com.example.bty.Controllers.ProduitController;
    opens com.example.bty.Controllers.ProduitController to javafx.fxml;
    exports com.example.bty.Controllers.CourController;
    opens com.example.bty.Controllers.CourController to javafx.fxml;
    exports com.example.bty.Controllers.ReclamationController;
    opens com.example.bty.Controllers.ReclamationController to javafx.fxml;
    exports com.example.bty.Views;
    opens com.example.bty.Views to javafx.fxml;

    exports com.example.bty.Controllers.graphiqueGCP;
    opens com.example.bty.Controllers.graphiqueGCP to javafx.fxml;

    exports com.example.bty.Controllers.EvenementController;
    opens com.example.bty.Controllers.EvenementController to javafx.fxml;
    exports com.example.bty.Controllers.usercontroller;
    opens com.example.bty.Controllers.usercontroller to javafx.fxml;

}