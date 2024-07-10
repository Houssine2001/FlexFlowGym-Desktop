package com.example.bty;

import com.example.bty.Entities.Role;
import com.example.bty.Entities.User;
import com.example.bty.Entities.Validation;

import com.example.bty.Services.*;
import com.example.bty.Utils.Session;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.time.Instant;
import java.util.Random;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/LoginGym.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.show();

//        ViewFactory viewFactory = new ViewFactory();
//        viewFactory.showLoginWindow();

        //Model.getInstance().getViewFactory().showLoginWindow();
    }

    public static void main(String[] args) {


        // IServiceUser serviceUser = new ServiceUser();
//        Role userRole = Role.ADMIN;
//
//        User u=new User("houssine","houssine@gmail.com","ibtihel1234","12345678",userRole,null);

//         //User u=new User("mnajja","mnajja@gmail.com","ibtihel1234");
//
//        //**********************tester la methode register
//   if(serviceUser.emailExists(u.getEmail()))
//        {
//            System.out.println("User already exist");
//        }
//        else
//            serviceUser.register(u);
//
////        //**********************tester la methode Authentification
//        int status = serviceUser.Authentification("houssine@gmail.com", "ibtihel1234");
//      switch (status) {
//            case 0:
//                System.out.println("Invalid user credentials");
//                break;
//            case 1:
//                System.out.println("Logged in successfully");
//                break;
//            case 2:
//                System.out.println("User is desactiver");
//                break;
//        }
//
//        Session s= Session.getInstance();
//        System.out.println(s.getLoggedInUser());
////         s.logout();
////        System.out.println(s.getLoggedInUser());
////
////         System.out.println(s.getLoggedInUser());
//
//
//
//        //*****************tester la methode ActiverOrDesactiver
//        //serviceUser.ActiverOrDesactiver(17);
//
//        //*****************tester la methode update
//        User userUpdate = new User();
//        userUpdate.setId(6);
//        userUpdate.setName("Admin1");
//        userUpdate.setEmail("Admin1@gmail.com");
//        userUpdate.setPassword("Admin1234");
//        userUpdate.setTelephone("12345678");
//        userUpdate.setRole(userRole);
//        //***********************appel de la methode update
//        //serviceUser.update(userUpdate);
//
//        //************************tester la methode delete
//        //serviceUser.delete(9);
//
//     //**********************tester la methode getAllMembers
//       // System.out.println(serviceUser.getAllMembers());
//
//        //tester la methode getAllCoaches
//        //System.out.println(serviceUser.getAllCoaches());

        //*********************tester findByEmail
        // User user = serviceUser.findByEmail("houssine@gmail.com");
        // System.out.println(user);
        //MailerService mailerService = new MailerService();
        //mailerService.sendMail("mnajjaibtihel@gmail.com");
/*

//  ********************* tastina verification par Email ******************

        int code =8264;//jebtou mel base manuelement
        IServiceValidation serviceValidation = new ServiceValidation(); //sna3et instance mel validation Service
        IServiceUser serviceUser = new ServiceUser(); // sna3et instance mel user Service
        Validation v =serviceValidation.findByCode(code); // bch nemchi njib mel base lvalidation eli 3andha lcode adheka
        if(v!=null)
        {
            User u=v.getUser();//bch njib luser eli aandou lcode adheka mel validation table
            //u.setEtat(true); // nbadalou letat true pour activer son compte
            serviceUser.desactiverAcc(u.getId()); // bch na3mlou update luser

        }
        else
        {
            System.out.println("code not exist");
        }
*/
        // ********************* tastina reninialisation mot de passe  par Email ******************

//        IServiceValidation serviceValidation = new ServiceValidation(); //sna3et instance mel validation Service
//        IServiceUser serviceUser = new ServiceUser(); // sna3et instance mel user Service
//        MailerService mailerService = new MailerService();
//
//
//        var email = "mnajjaibtihel@gmail.com";
//
//        if (serviceUser.emailExists(email)) {
//            System.out.println("User already exist");
//            Random random = new Random();
//            random.nextInt(9999); // code bin 0 w 8999 [0,9999[
//            Validation v = new Validation(random.nextInt(9999), Instant.now(), Instant.now().plusSeconds(600), serviceUser.findByEmail(email));
//            serviceValidation.ajouterValidation(v);
//            String message = "Your code is : " + v.getCode();
//            String subject = "Rénitialisation mot de passe";
//            mailerService.sendMail(email, message, subject);
//        } else {
//            System.out.println("User not exist");
//        }
//        int code = 4868;//jebtou mel base manuelement
//        Validation v = serviceValidation.findByCode(code); // bch nemchi njib mel base lvalidation eli 3andha lcode adheka
//        if (v != null) {
//            //System.out.println("code exist");
//            User u = v.getUser();//bch njib luser eli aandou lcode adheka mel validation table
//            // System.out.println(u);
//            String password = "ibtihel123456789";
//            String confirmPassword = "ibtihel123456789";
//            if (password.equals(confirmPassword)) {
//                System.out.println("passwords match");
//                //u.setPassword(password);
//                serviceUser.updatePassword(password, u.getId()); // bch na3mlou update luser
//                serviceValidation.deleteValidation(v.getCode());
//                System.out.println("password updated");
//
//
//            } else {
//                System.out.println("code not exist");
//            }
//
//
//            User user = serviceUser.findByEmail("mnajjaibtihel@gmail.com"); // jebet user eli aandou adresse : mnajjaibtihel@gmail.com
//            String password = user.getPassword(); //khdhitt lmote de passe mel user // crypté
//            if (user != null) {
//                System.out.println("user exist");
//                System.out.println(user);
//
//                if (BCrypt.checkpw("ibtihel123456789", password)) {
//                    System.out.println("password match");
//                } else {
//                    System.out.println("password not match");
//                }
//            } else {
//                System.out.println("user not exist");
//            }


        launch();

    }
}


