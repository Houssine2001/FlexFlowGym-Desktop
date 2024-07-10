package com.example.bty.Controllers.usercontroller;

import com.example.bty.Entities.Role;
import com.example.bty.Entities.User;
import com.example.bty.Services.IServiceUser;
import com.example.bty.Services.MailerService;
import com.example.bty.Services.ServiceUser;
import com.example.bty.Utils.Session;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class CoachesFormController implements Initializable {
    public AnchorPane coaches_form;
    public TextField coaches_name;
    public TextField coaches_email;
    public TextField coaches_telephone;
    public Button coaches_createBtn;
    public Button coaches_updateBtn;
    IServiceUser serviceUser00=new ServiceUser();
    MailerService mailerService = new MailerService();
    Session session = Session.getInstance();
    User u=session.getLoggedInUser();
    User user ;

    int id;

    public void coachesCreateBtn(ActionEvent actionEvent) {
        // Gather data from form fields
        String name = coaches_name.getText();
        String email = coaches_email.getText();
        //String password = coaches_password.getText();
        String telephone = coaches_telephone.getText();
        Role[] defaultRole = new Role[]{Role.COACH}; // Définissez le rôle par défaut ici

        // Check if the email already exists
        if (serviceUser00.emailExists(email)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Email Already Exists");
            alert.setHeaderText(null);
            alert.setContentText("Email already exists. Please use a different email.");
            alert.showAndWait();
        } else {
            List<Object> element=new ArrayList<>();
            for(char c = 'a'; c <= 'z'; ++c){
                element.add(c);
            }
            for(int i = 0; i < 10; ++i){
                element.add(i);
            }
            Collections.shuffle(element,new Random());
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < 8; i++){
                sb.append(element.get(i).toString());
            }
            String password = sb.toString();

            System.out.println("Password: "+password);
            String subject = "Welcome to our platform";
            String message = "<html>"
                    + "<body>"
                    + "<h1 style='color:green;'>Welcome to Flex FLow </h1>"
                    + "<p>Your account has been created successfully. Your password is: <strong>" + password + "</strong></p>"
                    + "<p>For security reasons, please change your password after your first login.</p>"
                    + "<p>Best regards,</p>"
                    + "<p><strong>Your FlexFLow Team</strong></p>"
                    + "</body>"
                    + "</html>";
            String recipient = email;
            mailerService.sendMail(recipient,message,subject);
            System.out.println("Email sent successfully!");

            User newCoach = new User(name, email, password, telephone, defaultRole,true,null);
            // Appeler la méthode d'inscription avec les valeurs récupérées
            serviceUser00.register(newCoach);
            serviceUser00.ActiverOrDesactiver(newCoach.getId());
            System.out.println("Coach created successfully! and activated ");
            // after creating the coach, redirect to the list of coaches

            Stage currentStage = (Stage) coaches_createBtn.getScene().getWindow();
            currentStage.close();

            // Rediriger vers l'interface CoachList.fxml
            // loadCoachListInterface();

        }
    }



    private void loadCoachListInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/coachList.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Coach List");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs de chargement de l'interface CoachList.fxml
        }
    }

    public CoachesFormController(){}

    public CoachesFormController(int id){
        this.id = id;
    }


    public void coachesUpdateBtn(ActionEvent actionEvent) throws IOException {
        // User loggedInUser = Session.getInstance().getLoggedInUser();

        User coach=new User(id, coaches_name.getText(),coaches_email.getText(),coaches_telephone.getText(),new Role[1],true);
        serviceUser00.update(coach);
        CoachListController c=CoachListController.getInstance();
        c.consulterCoaches();
        Stage currentStage = (Stage) coaches_updateBtn.getScene().getWindow();
        currentStage.close();


//CoachListController.consulterCoach();
        //loadCoachListInterface();
        //consulterCoach();

    }

    public void SetId(int id){
        this.id = id;
        User user = serviceUser00.findByID(id);
        System.out.println(id);
        coaches_name.setText(user.getName());
        coaches_email.setText(user.getEmail());
        coaches_telephone.setText(user.getTelephone());
    }

    public void SetUser(User userModif){
        this.user = userModif;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {





    }
}