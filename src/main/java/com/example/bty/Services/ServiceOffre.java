package com.example.bty.Services;

import com.example.bty.Entities.Offre;
import com.example.bty.Utils.ConnexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


//farah

public class ServiceOffre {
    private Connection connexion;
    private Statement ste;
    private PreparedStatement pst;
    public ServiceOffre()
    {
        connexion= ConnexionDB.getInstance().getConnexion();
    }


    //public void add(Demande d){
    //  String requete="insert into Demande (id,age,nbre_heure,maladie_chronique,but,niveau_physique, membre,offre) values ('"+d.getId()+"','"+d.getAge()+"' ,'"+d.getNbre_heure()+"' ,'"+d.getMaladie_chronique()+"' , '"+d.getBut()+"','"+d.getNiveau_physique()+"','"+d.getOffre()+"', '"+d.getMembre()+"')";

    // try {
    // ste=connexion.createStatement();
//ste.executeUpdate(requete);
    // } catch (SQLException e) {
    // throw new RuntimeException(e);
//}
    //  }




    public boolean addOffre (Offre f) {

        //try (Connection connection = DataSource.obtenirConnexion())
        String query = "INSERT INTO Offre (id,specialite,tarif_heure,id_coach) VALUES ( ?, ?, ?,?)";
        try (PreparedStatement statement = connexion.prepareStatement(query)) {
            statement.setInt(1, f.getId());
            statement.setString(2, String.valueOf(f.getspecialite()));
            statement.setFloat(3, f.getTarif_heure());
            statement.setInt(4, f.getCoach().getId());

            statement.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            e.printStackTrace();

            return false;

        }


    }

    public void DeleteOffre (int id) {
        String DELETE = "DELETE FROM Offre WHERE id = ?";
        try {
            pst = connexion.prepareStatement(DELETE);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Offre supprimée avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }}

    public void UpdateOffre(Offre f) throws SQLException {
        String UPDATE = "UPDATE Offre SET id = ?, specialite = ?, trif_heure = ?, id_coach= ? WHERE id_coach = ?";
        try {
            pst = connexion.prepareStatement(UPDATE);
            pst.setInt(1, f.getId());
            pst.setString(2, String.valueOf(f.getspecialite()));
            pst.setFloat(3, f.getTarif_heure());
            pst.setInt(4, f.getCoach().getId());
            pst.executeUpdate();
            System.out.println("Demande mise à jour avec succès !");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
