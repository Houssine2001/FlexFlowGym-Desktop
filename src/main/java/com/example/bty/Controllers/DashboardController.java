package com.example.bty.Controllers;

import com.example.bty.Controllers.EvenementController.clientVitrine;
import com.example.bty.Entities.Role;
import com.example.bty.Entities.User;
import com.example.bty.Services.IServiceUser;
import com.example.bty.Services.ServiceUser;
import com.example.bty.Utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    // **************** MEMBERS ****************
    @FXML
    public AnchorPane members_form;
    @FXML
    public TextField members_customerId;
    @FXML
    public TextField members_name;
    @FXML
    public TextField members_phoneNum;
    @FXML
    public ComboBox members_status;
    @FXML
    public Button members_addBtn;
    @FXML
    public Button members_updateBtn;
    @FXML
    public Button members_deleteBtn;
    @FXML
    public TableView members_tableView;
    @FXML
    public TableColumn members_col_customerID;
    @FXML
    public TableColumn members_col_name;
    @FXML
    public TableColumn members_col_phoneNum;
    @FXML
    public TableColumn members_col_status;
    @FXML
    public TableColumn members_col_email;
    @FXML
    public AnchorPane main_form;
    @FXML
    public Label username;
    @FXML
    public Button dashboard_btn;
    @FXML
    public Button coaches_btn;
    @FXML
    public Button members_btn;
    @FXML
    public Button logout;
    @FXML
    public Button payment_btn;

    // **************** COACHES *****************
    @FXML
    public AnchorPane coaches_form;
    @FXML
    public TextField coaches_coachID;
    @FXML
    public TextField coaches_name;
    @FXML
    public TextField coaches_phoneNum;
    @FXML
    public Button coaches_createBtn;
    @FXML
    public Button coaches_updateBtn;
    @FXML
    public Button coaches_deleteBtn;
    @FXML
    public TableView<User> coaches_tableView;
    @FXML
    public TableColumn<User, Integer> coaches_col_coachID;
    @FXML
    public TableColumn<User, String> coaches_col_nom;
    @FXML
    public TableColumn<User, String> coaches_col_email;
    @FXML
    public TableColumn<User, String> coaches_col_telephone;
    @FXML
    public TableColumn<User, Boolean> coaches_col_etat;
    @FXML
    public TextField coaches_password;
    @FXML
    public ComboBox coaches_etat;
    @FXML
    public TextField coaches_telephone;
    @FXML
    public TextField coaches_email;
    public Button user_btn;
    public Button cours_btn;
    public Button evenement_btn;
    public Button produit_btn;
    public Button coach_btn;
    public Button reclamation_btn;
    public AnchorPane coaches_list;
    public TableColumn coaches_col_action;
    public AnchorPane dashboard_coach;
    public AnchorPane dashboard_membre;
    public AnchorPane dashboard_Admin;
    public Label usernameAdmin;
    public Label usernameMembre;
    public Label usernameCoach;
    public Button Events_btn;
    public Button Produtcts_btn;
    public Button Reclamations_btn;
    public Button Cours_btn;
    public Button logoutMembre;
    public Button CoursCoach_btn;
    public Button membreCoach_btn;
    public Button logoutCoach;
    public Button Produits_btn;
    public Button eventsAdmin_btn;
    public Button coursAdmin_btn;
    public Button storeAdmin_btn;
    public Button profileAdmin_btn;
    public Button logout00_btn;
    public Button report_btn;
    public BorderPane mainBorderPane;
    public BorderPane border;
    public StackPane contentPlaceholder;
    Session session = Session.getInstance();
    User u=session.getLoggedInUser();
    User user ;


    // ****************  //  fin MEMBERS ****************

    IServiceUser serviceUser00=new ServiceUser();

    public void consulterMembers() {
        // Fetch all members
        List<User> members = serviceUser00.getAllMembers();

        // Clear the table
        members_tableView.getItems().clear();

        // Define how to populate the columns
        members_col_customerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        members_col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        members_col_phoneNum.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        members_col_status.setCellValueFactory(new PropertyValueFactory<>("etat"));
        members_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Add fetched members to the table
        members_tableView.getItems().addAll(members);
    }
    public void membersSelect(MouseEvent mouseEvent) {
    }


    public void membersAddBtn(ActionEvent actionEvent) {

    }

    public void membersUpdate(ActionEvent actionEvent) {
    }
    public void membersDelete(ActionEvent actionEvent) {
    }

    public void consulterCoaches() {
        // Fetch all coaches
        List<User> coaches = serviceUser00.getAllCoaches();

        // Clear the table
        coaches_tableView.getItems().clear();

        // Define how to populate the columns
        coaches_col_coachID.setCellValueFactory(new PropertyValueFactory<>("id"));
        coaches_col_nom.setCellValueFactory(new PropertyValueFactory<>("name"));
        coaches_col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        coaches_col_telephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        coaches_col_etat.setCellValueFactory(new PropertyValueFactory<>("etat"));

        // Add fetched coaches to the table
        coaches_tableView.getItems().addAll(coaches);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



       // border.getStyleClass().add("border-pane");
        this.user = session.getLoggedInUser();
        if(Arrays.equals(user.getRoles(), new Role[]{Role.ADMIN})){
            dashboard_Admin.setVisible(true);
            //dashboard_coach.setVisible(false);
           // dashboard_membre.setVisible(false);
            usernameAdmin.setText(u.getName());
           /* String u1 = u.getName();
            System.out.println(u1);
            int id = u.getId();
            System.out.println(id);
            ServiceProduit.setUserDetails(u1, id);*/
           // consulterCoaches();


        }
        else if(Arrays.equals(user.getRoles(), new Role[]{Role.COACH})){
            System.out.println("coach found");
            // Instancier la classe VitrineClient
            DashboardMembre v = new DashboardMembre();

            // Appeler la méthode start (ou toute autre méthode pour démarrer l'interface)
            try {
                v.start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            dashboard_coach.setVisible(false);
            dashboard_Admin.setVisible(false);

//            LoginGymController v1 = new LoginGymController();
//            v1.signupSlider1();
        }

        else if(Arrays.equals(user.getRoles(), new Role[]{Role.MEMBRE})){
            System.out.println("membre found");
            // Instancier la classe VitrineClient
            DashboardMembre v = new DashboardMembre();

            // Appeler la méthode start (ou toute autre méthode pour démarrer l'interface)
            try {
                v.start(new Stage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            //dashboard_coach.setVisible(false);
            //dashboard_Admin.setVisible(false);
            dashboard_membre.setVisible(true);






        }
        else{
            System.out.println("user not found");
        }



    // consulterMembers();
    }


    @FXML
    void handleLogoutButton(ActionEvent event) {
        try {
            // Charger le fichier FXML de l'interface LoginGym
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginGym.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec l'interface LoginGym
            Scene scene = new Scene(root);

            // Obtenir la fenêtre actuelle à partir de l'événement
            Stage stage = (Stage) logout00_btn.getScene().getWindow();

            // Mettre à jour la scène de la fenêtre avec l'interface LoginGym
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchForm(ActionEvent actionEvent) {
 if (actionEvent.getSource().equals(dashboard_btn)) {
            main_form.setVisible(true);
            members_form.setVisible(false);
            coaches_form.setVisible(false);
        } else if (actionEvent.getSource().equals(members_btn)) {
            main_form.setVisible(false);
            members_form.setVisible(true);
            coaches_form.setVisible(false);
        } else if (actionEvent.getSource().equals(coaches_btn)) {
            main_form.setVisible(false);
            members_form.setVisible(false);
            coaches_form.setVisible(true);
        }
 //ne9ssa ***********
    }

    public void logout(ActionEvent actionEvent) {
    }



    //*********** COACHES  METHODS ***********


    public void coachesCreateBtn(ActionEvent actionEvent) {
        // Gather data from form fields
        String name = coaches_name.getText();
        String email = coaches_email.getText();
        String password = coaches_password.getText();
        String telephone = coaches_telephone.getText();
        Role [] defaultRole = new Role[1]; // Définissez le rôle par défaut ici

        // Check if the email already exists
        if (serviceUser00.emailExists(email)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Email Already Exists");
            alert.setHeaderText(null);
            alert.setContentText("Email already exists. Please use a different email.");
            alert.showAndWait();
        } else {
            User newCoach = new User(name, email, password, telephone, defaultRole,null);
            // Appeler la méthode d'inscription avec les valeurs récupérées
            serviceUser00.register(newCoach);
            // serviceUser00.ActiverOrDesactiver(newCoach.getId());
            System.out.println("Coach created successfully! and activated ");
        }
    }




    public void goToEvents(ActionEvent actionEvent) {
        loadContent("/com/example/bty/FxmlEvenement/dashbordEvenement.fxml");
    }

    public void goToStore(ActionEvent actionEvent) throws IOException {
        // Charger le contenu de DashboardProduit.fxml
        loadContent("/DashboardProduit.fxml");
    }

    public void goToCommande(ActionEvent actionEvent) throws IOException {
        // Charger le contenu de DashboardProduit.fxml
        loadContent("/DashboardCommande.fxml");
    }

    public void goToAccueil(ActionEvent actionEvent) throws IOException {
        // Charger le contenu de DashboardProduit.fxml
        loadContent("/Accueil.fxml");
    }


    public void goToPayment(ActionEvent actionEvent) throws IOException {
        // Charger le contenu de DashboardProduit.fxml
        loadContent("/paiment.fxml");
    }

    public void goToCours(ActionEvent actionEvent) throws IOException {
        // Charger le contenu de DashboardProduit.fxml
        loadContent("/consulterAdmin.fxml");
    }



    public void goToOffre
            (ActionEvent actionEvent) throws IOException {
        // Charger le contenu de DashboardProduit.fxml
        loadContent("/DashboardAdminOffre.fxml");
    }

    public void goToCoach(ActionEvent actionEvent) {
        loadContent("/coachList.fxml");
    }

    public void goToMembre(ActionEvent actionEvent) {
        loadContent("/membreList.fxml");
    }

    public void goToProfile(ActionEvent actionEvent) {
        loadContent("/UserProfil.fxml");
    }

    public void goToLogout(ActionEvent actionEvent) {
        loadContent("/LoginGym.fxml");
    }









    private void loadContent(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent content = loader.load();
            contentPlaceholder.getChildren().clear();
            contentPlaceholder.getChildren().add(content);
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'erreur de chargement du fichier FXML
        }
    }




    public void goToReclamations(ActionEvent actionEvent) {
    }

    public void goToCoursCoach(ActionEvent actionEvent) {
    }

    public void goToMembreCoach(ActionEvent actionEvent) {
    }

    public void goToStoreAdmin(ActionEvent event) {
    }
}
