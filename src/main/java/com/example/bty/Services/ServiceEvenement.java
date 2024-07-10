package com.example.bty.Services;

import com.example.bty.Entities.Evenement;
import com.example.bty.Entities.User;
import com.example.bty.Utils.ConnexionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement {
    private Connection connexion;
    private PreparedStatement pde;
    Boolean etat=true;
    public ServiceEvenement(){
        connexion= ConnexionDB.getInstance().getConnexion();
    }


    //Methode pour ajouter un evenement
    public void ajouterEvenement(Evenement e){
        String req="INSERT INTO evenement (nomEvenement,categorie,objectif,nbrplace,Date,Time,id_user,etat) VALUES (?,?,?,?,?,?,?,?)";
        try {
            pde=connexion.prepareStatement(req);
            pde.setString(1,e.getNom());
            pde.setString(2,e.getCategorie());
            pde.setString(3,e.getObjectif());
            pde.setInt(4,e.getNbre_place());
            pde.setDate(5,e.getDate());
            pde.setTime(6,e.getTime());
            pde.setInt(7,e.getCoach().getId());
            pde.setBoolean(8, e.isEtat());

            pde.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    //methode pour modifier un evenement
    public void modifierEvenement(Evenement e){
        String req="UPDATE evenement SET nomEvenement=?,categorie=?,Objectif=?,nbrPlace=?,Date=?,Time=?,id_user=?,etat=? WHERE id_evenement=?";
        try {
            pde=connexion.prepareStatement(req);
            pde.setString(1,e.getNom());
            pde.setString(2,e.getCategorie());
            pde.setString(3,e.getObjectif());
            pde.setInt(4,e.getNbre_place());
            pde.setDate(5,e.getDate());
            pde.setTime(6,e.getTime());
            pde.setInt(7,e.getCoach().getId());
            pde.setBoolean(8, e.isEtat());
            pde.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

//methode pour supprimer un evenement

    public Boolean supprimerEvenement(int id){
        String req="DELETE FROM evenement WHERE id_evenement=?";
        try {
            pde=connexion.prepareStatement(req);
            pde.setInt(1,id);
            pde.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }



    public List<Evenement> consulterEvenements() {
        List<Evenement> evenements = new ArrayList<>();
        String query = "SELECT * FROM evenement";
        try (Connection connection = ConnexionDB.getInstance().getConnexion();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Evenement E = new Evenement();
                E.setId(resultSet.getInt("id_evenement"));
                E.setNom(resultSet.getString("nomEvenement"));
                E.setCategorie(resultSet.getString("categorie"));
                E.setObjectif(resultSet.getString("Objectif"));
                E.setNbre_place(resultSet.getInt("nbrPlace"));
                E.setDate(resultSet.getDate("Date"));
                E.setTime(resultSet.getTime("Time"));
                int coachId = resultSet.getInt("id_user");

                // Récupérer le nom du coach à partir de la base de données
                String coachName = null;
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT nom FROM user WHERE id = ?")) {
                    preparedStatement.setInt(1, coachId);
                    try (ResultSet coachResultSet = preparedStatement.executeQuery()) {
                        if (coachResultSet.next()) {
                            coachName = coachResultSet.getString("nom");
                        }
                    }
                }

                // Créer un objet User pour le coach
                User coach = new User();
                coach.setId(coachId);
                coach.setName(coachName);
                E.setCoach(coach);

                evenements.add(E);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return evenements;
    }


    public List<Evenement> rechercherEvenementParNOom(String nom) {
        List<Evenement> evenements = new ArrayList<>();
        //try (Connection connection = ConnexionDB.obtenirConnexion()) {
        String query = "SELECT * FROM evenement WHERE nomEvenement = ?";
        try (PreparedStatement statement = connexion.prepareStatement(query)) {
            statement.setString(1, nom);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Evenement E = new Evenement();
                    E.setId(resultSet.getInt("id_evenement"));
                    E.setNom(resultSet.getString("nomEvenement"));
                    E.setCategorie(resultSet.getString("categorie"));
                    E.setObjectif(resultSet.getString("Objectif"));
                    E.setNbre_place(resultSet.getInt("nbrPlace"));
                    E.setDate(resultSet.getDate("Date"));
                    E.setTime(resultSet.getTime("Time"));
                    int coachId = resultSet.getInt("id_user");
                    evenements.add(E);
                }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return evenements;
    }

}