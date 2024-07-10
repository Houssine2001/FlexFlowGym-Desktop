package com.example.bty.Services;

import com.example.bty.Entities.Reclamation;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.bty.Entities.User;
import com.example.bty.Utils.ConnexionDB;
import com.example.bty.Utils.Session;

public class ServiceReclamation {

    private Connection connexion;
    private Statement ste;
    private PreparedStatement preparedStatement;
    Date Datenow = Date.valueOf(LocalDate.now());


    public ServiceReclamation() {
        connexion = ConnexionDB.getInstance().getConnexion();
    }


//inutile
//    public void addReclamation(Reclamation reclamation) {
//        String requete = "insert into reclamation (date_reclamation, titre_reclamation, description, etat) values ('"
//                + reclamation.getDate_reclamation() + "', '" + reclamation.getTitre_reclamation() + "', '"
//                + reclamation.getDescription() + "', '" + reclamation.getEtat() + "')";
//
//        try {
//            Statement statement = connexion.createStatement();
//            statement.executeUpdate(requete);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }





    public void addReclamationv2(Reclamation reclamation) {
        reclamation.setEtat("Non traite");
//        Session s=Session.getInstance();
//        User u = s.getLoggedInUser();
//        reclamation.setMembre(u);
        String requete = "INSERT INTO reclamation (date_reclamation, titre_reclamation, description, etat) VALUES (?, ?, ?, ?)"; //,id_user     ?

        try {
            preparedStatement = connexion.prepareStatement(requete);
            preparedStatement.setDate(1, Datenow);
            preparedStatement.setString(2, reclamation.getTitre_reclamation());
            preparedStatement.setString(3, reclamation.getDescription());
            preparedStatement.setString(4, reclamation.getEtat());
            // preparedStatement.setInt(5,reclamation.getMembre().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //unutile
//    public void deleteReclamation(int id) {
//        String requete = "delete from reclamation where id = " + id;
//        try {
//            Statement statement = connexion.createStatement();
//            statement.executeUpdate(requete);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//    }


    public void updateReclamationEtat(int id, String nouvelEtat) {
        String requete = "UPDATE reclamation SET etat = '" + nouvelEtat + "' WHERE id = " + id;

        try {
            Statement statement = connexion.createStatement();
            statement.executeUpdate(requete);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//afficheg inutile (sans liste)
//    public void afficherToutesReclamations() {
//        String requete = "SELECT * FROM reclamation";
//        try {
//            Statement statement = connexion.createStatement();
//            ResultSet resultSet = statement.executeQuery(requete);
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String dateReclamation = resultSet.getString("date_reclamation");
//                String titreReclamation = resultSet.getString("titre_reclamation");
//                String description = resultSet.getString("description");
//                String etat = resultSet.getString("etat");
//                System.out.println("ID: " + id + ", Date: " + dateReclamation + ", Titre: " + titreReclamation + ", Description: " + description + ", Etat: " + etat);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }

//    public List<Reclamation> readallreclamation() {
//        String requete = "select * from reclamation";
//        List<Reclamation> list = new ArrayList<>();
//        try {
//            ste = connexion.createStatement();
//            ResultSet rs = ste.executeQuery(requete);
//            while (rs.next()) {
//                list.add(new Reclamation(rs.getInt("id"), rs.getDate("date_reclamation"), rs.getString("titre_reclamation"), rs.getString("description"), rs.getString("etat")));
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return list;
//    }


    public void updateReclamationEtatv2(Reclamation reclamation, String nouvelEtat) {
        String requete = "UPDATE reclamation SET etat = ? WHERE id = ?";

        try {
            PreparedStatement preparedStatement = connexion.prepareStatement(requete);
            preparedStatement.setString(1, nouvelEtat);
            preparedStatement.setInt(2, reclamation.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}








