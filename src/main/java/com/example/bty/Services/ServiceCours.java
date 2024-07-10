package com.example.bty.Services;

import com.example.bty.Entities.Cours;
import com.example.bty.Entities.User;
import com.example.bty.Utils.ConnexionDB;

import java.io.*;
import java.sql.*;
import java.util.*;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class ServiceCours {
    private static Connection connexion;
    //private Statement ste;
    private PreparedStatement pst;
    private PreparedStatement statement;
    public ServiceCours() {
        connexion= ConnexionDB.getInstance().getConnexion();
    }

    public void addPst(Cours c, File imageFile){
        String requete="insert into cours (nom_cour,duree,intensite,cible,categorie,objectif,etat,capacite,user_id,image) values(?,?,?,?,?,?,?,?,?,?)";

        try {
            FileInputStream fis = new FileInputStream(imageFile);
            pst=connexion.prepareStatement(requete);
            pst.setString(1,c.getNom());
            pst.setString(2,c.getDuree());
            pst.setString(3,c.getIntensite());
            pst.setString(4,c.getCible());
            pst.setString(5,c.getCategorie());
            pst.setString(6,c.getObjectif());
            pst.setBoolean(7,c.isEtat());
            pst.setInt(8,c.getCapacite());
            pst.setInt(9,c.getCoach().getId());
            pst.setBinaryStream(9, fis, (int) imageFile.length());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] readImageFile(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void DeleteCours (int id) {
        String DELETE = "DELETE FROM cours WHERE id = ?";
        try (
                PreparedStatement preparedStatement = connexion.prepareStatement(DELETE)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }}


    public void UpdateCours(Cours cours) {
        String UPDATE = "UPDATE cours SET nom_cour = ?, duree = ?, intensite = ?, cible = ?, categorie = ?, objectif = ?, etat = ?, capacite = ?, user_id = ?  WHERE id = ?";
        try (
                PreparedStatement preparedStatement = connexion.prepareStatement(UPDATE)) {
            preparedStatement.setString(1, cours.getNom());
            preparedStatement.setString(2, cours.getDuree());
            preparedStatement.setString(3, cours.getIntensite());
            preparedStatement.setString(4, cours.getCible());
            preparedStatement.setString(5, cours.getCategorie());
            preparedStatement.setString(6, cours.getObjectif());
            preparedStatement.setBoolean(7, cours.isEtat());
            preparedStatement.setInt(8, cours.getCapacite());
            preparedStatement.setInt(9, cours.getCoach().getId());
            preparedStatement.setInt(10, cours.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

  /*  public List<Cours> consulterCours() {
        List<Cours> Cours = new ArrayList<>();
        // try (Connection connection = ConnexionDB.obtenirConnexion())
        String query = "SELECT * FROM cours";
        try (Statement statement = connexion.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Cours cr = new Cours();

                cr.setId(resultSet.getInt("id_cour"));
                cr.setNom(resultSet.getString("nomCour"));
                cr.setCategorie(resultSet.getString("Categorie"));
                cr.setCible(resultSet.getString("Cible"));
                cr.setDuree(resultSet.getString("Duree"));
                cr.setIntensite(resultSet.getString("Intensite"));
                cr.setObjectif(resultSet.getString("Objectif"));
                cr.setEtat(resultSet.getBoolean("etat"));
                cr.setCapacite(resultSet.getInt("Capacite"));
                // Créer un objet User pour le coach et lui attribuer l'ID uniquement
                User coach = new User();
                coach.setId(resultSet.getInt("id_user"));

                // Affecter le coach au cours
                cr.setCoach(coach);


                Cours.add(cr);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return Cours;
    } */

    public List<Cours> consulterCours() {
        List<Cours> coursList = new ArrayList<>();
        String query = "SELECT * FROM cours";
        try (Connection connection = ConnexionDB.getInstance().getConnexion();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Cours cr = new Cours();
                cr.setId(resultSet.getInt("id"));
                cr.setNom(resultSet.getString("nom_cour"));
                cr.setCategorie(resultSet.getString("categorie"));
                cr.setCible(resultSet.getString("cible"));
                cr.setDuree(resultSet.getString("duree"));
                cr.setIntensite(resultSet.getString("intensite"));
                cr.setObjectif(resultSet.getString("objectif"));
                cr.setEtat(resultSet.getBoolean("etat"));
                cr.setCapacite(resultSet.getInt("capacite"));
                int coachId = resultSet.getInt("user_id");

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
                cr.setCoach(coach);

                coursList.add(cr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return coursList;
    }




    // Méthode pour filtrer et afficher les cours par catégorie, cible ou objectif
    public static List<Cours> filtrerCours(String categorie, String cible, String objectif) {
        List<Cours> coursList = new ArrayList<>();
        String FILTER = "SELECT * FROM cours WHERE categorie = ? OR cible = ? OR objectif = ?";
        try (PreparedStatement preparedStatement = connexion.prepareStatement(FILTER)){
            preparedStatement.setString(1, categorie);
            preparedStatement.setString(2, cible);
            preparedStatement.setString(3, objectif);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    Cours cour = new Cours();
                    User user =new User();
                    cour.setId(rs.getInt("id"));
                    cour.setNom(rs.getString("nom_cour"));
                    cour.setDuree(rs.getString("duree"));
                    cour.setIntensite(rs.getString("intensite"));
                    cour.setCible(rs.getString("cible"));
                    cour.setCategorie(rs.getString("categorie"));
                    cour.setObjectif(rs.getString("objectif"));
                    cour.setEtat(rs.getBoolean("etat"));
                    cour.setCapacite(rs.getInt("capacite"));
                    user.setId(rs.getInt("user_id"));
                    coursList.add(cour);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return coursList;
    }







}
