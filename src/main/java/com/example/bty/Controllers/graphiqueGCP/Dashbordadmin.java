package com.example.bty.Controllers.graphiqueGCP;

import com.example.bty.Controllers.graphiqueGCP.AdminInterface;
import com.example.bty.Services.ServiceOffre;
import com.example.bty.Utils.ConnexionDB;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dashbordadmin {

    public Button ajout_produit;
    public Button actuliser_produit;
    public Label dashboard_NM;
    public Button actuliser;

    @FXML
    private  TableView<AdminInterface.OffreItem> tableView;



    @FXML
    private TableColumn<AdminInterface.OffreItem, String> IdCol;
    @FXML
    public TableColumn<AdminInterface.OffreItem, String> specialiteCol;
    @FXML
    public TableColumn<AdminInterface.OffreItem, String> tarifnCol;
    @FXML
    public TableColumn<AdminInterface.OffreItem, String > idCoachCol;
    @FXML
    public TableColumn<AdminInterface.OffreItem, String> etatCol;



    @FXML
    private TableColumn<AdminInterface.OffreItem, Void> actionColumn;

    @FXML
    private Label dashboard_TI;

    @FXML
    private Label dashboard_NC;
    // Autres déclarations
    private static Connection connection;
    private PreparedStatement pst;
    private Statement ste ;
    public Dashbordadmin() {
        connection = ConnexionDB.getInstance().getConnexion();
    }
    private ServiceOffre serviceOffre = new ServiceOffre();

    @FXML
    private void initialize() {
        IdCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        specialiteCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSpecialite()));
        tarifnCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTarifHeure()));
        idCoachCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdCoach()));
        etatCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtatOffre()));


        actionColumn.setCellFactory(param -> new TableCell<>() {

            private final Button accepterButton = new Button("Accepter");
            private final Button refuserButton = new Button("Refuser");

            {
                // Action du bouton de modification
                accepterButton.setOnAction(e -> {
                    AdminInterface.OffreItem offre = getTableView().getItems().get(getIndex());
                    accepterOffre(offre.getId());
                });

                refuserButton.setOnAction(e -> {
                    AdminInterface.OffreItem offre = getTableView().getItems().get(getIndex());
                    refuserOffre(offre.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(10, accepterButton, refuserButton));
                }
            }

            private Button createIconButton(String text, FontAwesomeIcon icon, String color, double width) {
                Button button = new Button(text, new FontAwesomeIconView(icon));
                button.getStyleClass().add("icon-button");
                button.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white;");
                button.setPrefWidth(width); // Définir la largeur du bouton
                return button;
            }
        });
        // actionColumn.setCellFactory(param -> new AdminInterface.ButtonCell());


        actuliser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Appel de la méthode pour ouvrir la nouvelle fenêtre d'ajout de produit
                actualiserTable();
            }
        });


        // Chargement initial des données dans la table
        actualiserTable();

        // Button ajoutProduitButton = new Button("Ajout");
//        ajout_produit.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                // Appel de la méthode pour ouvrir la nouvelle fenêtre d'ajout de produit
//                ouvrirFenetreAjoutProduit();
//            }
//        });

//        actuliser_produit.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                // Appel de la méthode pour ouvrir la nouvelle fenêtre d'ajout de produit
//                actualiserTable();
//            }
//        });


//        // Appeler le service pour obtenir le chiffre d'affaires
//        double chiffreAffaires = ServiceProduit.calculerChiffreAffaires();
//
//        // Mettre à jour le label avec le chiffre d'affaires
//        dashboard_TI.setText( chiffreAffaires + " Dnt" );
//
//
//
        int produitPlusAchete = getNombreDemandesEnAttente();

// Mettre à jour le Label avec le nom du produit le plus acheté
        if (produitPlusAchete != 0) {
            dashboard_NC.setText(produitPlusAchete+"");
        } else {
            dashboard_NC.setText("Aucun etat en attente");
        }

//
//        String produitMoinsAchete = getProduitMoinsVendu();
//
//// Mettre à jour le Label avec le nom du produit le plus acheté
//        if (produitPlusAchete != null) {
//            dashboard_NM.setText(produitMoinsAchete);
//        } else {
//            dashboard_NM.setText("Aucun produit");
//        }


    }







    private  void accepterOffre(String id) {

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Offre SET etatOffre = 'Acceptée' WHERE id = ?");
            statement.setString(1, id);
            statement.executeUpdate();
            statement.close();
            consulterOffre();
        }catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de base de données
        }
    }

    private  void refuserOffre(String id) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE Offre SET etatOffre = 'Refusée' WHERE id = ?");
            statement.setString(1, id);
            statement.executeUpdate();
            statement.close();
            consulterOffre();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de base de données
        }
    }



    private void modifierOffre(ConsultationOffre.OffreItem offre) {
        // Créer une nouvelle fenêtre de modification
        Stage modificationStage = new Stage();
        modificationStage.setTitle("Modifier l'offre");

        // Créer des champs de texte pour les nouveaux détails de l'offre
        TextField nomField = new TextField(offre.getNom());
        TextField specialiteField = new TextField(offre.getSpecialite());
        TextField tarifField = new TextField(offre.getTarif());
        TextField coachField = new TextField(offre.getCoach());

        // Créer un bouton pour appliquer les modifications
        Button modifierButton = new Button("Modifier");
        modifierButton.setOnAction(event -> {
            try {
                // Récupérer les nouvelles valeurs saisies par l'utilisateur
                String nouveauNom = nomField.getText();
                String nouvelleSpecialite = specialiteField.getText();
                String nouveauTarif = tarifField.getText();
                String nouveauCoach = coachField.getText();

                // Exécuter une requête SQL UPDATE pour mettre à jour l'offre dans la base de données
                String query = "UPDATE Offre SET nom = ?, Specialite = ?, tarif_heure = ?, id_Coach = ? WHERE nom = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, nouveauNom);
                statement.setString(2, nouvelleSpecialite);
                statement.setString(3, nouveauTarif);
                statement.setString(4, nouveauCoach);
                statement.setString(5, offre.getNom()); // Utiliser l'ancien nom pour la clause WHERE
                int rowsAffected = statement.executeUpdate();

                // Vérifier si la mise à jour a réussi
                if (rowsAffected > 0) {
                    // Afficher un message de succès
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Modification réussie");
                    alert.setHeaderText(null);
                    alert.setContentText("L'offre a été modifiée avec succès.");
                    alert.showAndWait();

                    // Rafraîchir les données dans le TableView
                    tableView.getItems().clear();
                    tableView.getItems().addAll(consulterOffre());
                } else {
                    // Afficher un message d'erreur si la mise à jour a échoué
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de modification");
                    alert.setHeaderText(null);
                    alert.setContentText("La modification de l'offre a échoué.");
                    alert.showAndWait();
                }

                // Fermer la fenêtre de modification
                modificationStage.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        // Créer une mise en page pour la fenêtre de modification
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(
                new Label("Nouveau nom :"), nomField,
                new Label("Nouvelle spécialité :"), specialiteField,
                new Label("Nouveau tarif :"), tarifField,
                new Label("Nouveau coach :"), coachField,
                modifierButton
        );
        vbox.setPadding(new Insets(10));

        // Afficher la fenêtre de modification
        Scene scene = new Scene(vbox);
        modificationStage.setScene(scene);
        modificationStage.show();
    }

    // Méthode pour supprimer une offre
    private void supprimerOffre(ConsultationOffre.OffreItem offre) {
        try {
            // Exécuter une requête SQL DELETE pour supprimer l'offre de la base de données
            String query = "DELETE FROM Offre WHERE nom = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, offre.getNom());
            int rowsAffected = statement.executeUpdate();

            // Vérifier si la suppression a réussi
            if (rowsAffected > 0) {
                // Afficher un message de succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Suppression réussie");
                alert.setHeaderText(null);
                alert.setContentText("L'offre a été supprimée avec succès.");
                alert.showAndWait();

                // Rafraîchir les données dans le TableView
                tableView.getItems().clear();
                tableView.getItems().addAll(consulterOffre());
            } else {
                // Afficher un message d'erreur si la suppression a échoué
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de suppression");
                alert.setHeaderText(null);
                alert.setContentText("La suppression de l'offre a échoué.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getNombreDemandesEnAttente() {
        int nombreDemandesEnAttente = 0;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgym", "root", "")) {
            String sql = "SELECT COUNT(*) AS total FROM demande WHERE etat = 'En attente'";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    nombreDemandesEnAttente = resultSet.getInt("total");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de connexion ou de requête SQL ici
        }

        return nombreDemandesEnAttente;
    }








    @FXML
    private void actualiserTable() {
        ObservableList<AdminInterface.OffreItem>

                produits = FXCollections.observableArrayList(consulterOffre());

        tableView.setItems(produits);
    }


    public static List<AdminInterface.OffreItem> consulterOffre() {
        List<AdminInterface.OffreItem> commandes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pidevgym", "root", "")) {
            String query = "SELECT * FROM Offre"; // Assurez-vous que le nom de la table est correct

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {


                while (resultSet.next()) {
                    AdminInterface.OffreItem offreItem = new AdminInterface.OffreItem();

                    offreItem.setId(resultSet.getString("id"));
                    offreItem.setSpecialite(resultSet.getString("specialite"));
                    offreItem.setTarifHeure(resultSet.getString("tarif_heure"));
                    offreItem.setIdCoach(resultSet.getString("id_Coach"));
                    offreItem.setEtatOffre(resultSet.getString("etatOffre"));
                    commandes.add(offreItem);
                }



            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return commandes;
    }

 /*  private  AdminInterface.OffreItem consulterOffre() {
        ObservableList<AdminInterface.OffreItem> data = FXCollections.observableArrayList();

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM offre");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                data.add(new AdminInterface.OffreItem(
                        resultSet.getString("id"),
                        resultSet.getString("specialite"),
                        resultSet.getFloat("tarif_heure"),
                        resultSet.getString("id_coach"),
                        resultSet.getString("etatOffre")
                ));
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer les erreurs de requête
        }

        tableView.setItems(data);
       return data;
    }*/


   /* private void actualiserTable() {
        // Appeler la méthode consulterProduits et charger les données dans la table
        List<Produit> produits = consulterProduits();
        tableView.getItems().setAll(produits);
    }*/

    // Autres méthodes








    private boolean validateFields(TextField... fields) {
        // Check if all fields are non-empty
        for (TextField field : fields) {
            if (field.getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }




    private void showAlert1(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        // Ajoute un bouton OK personnalisé qui ne ferme pas la boîte de dialogue parente
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        // Affiche l'alerte
        alert.showAndWait();
    }








    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /*private void actualiserTable() {
        ObservableList<Produit> produits = FXCollections.observableArrayList(produitServices.consulterProduits());
        tableView.setItems(produits);
    }*/

    private boolean showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        ButtonType yesButton = new ButtonType("Oui");
        ButtonType noButton = new ButtonType("Non");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton;
    }




}