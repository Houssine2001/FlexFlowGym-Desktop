package com.example.bty.Services;

import com.example.bty.Entities.Role;
import com.example.bty.Entities.User;
import com.example.bty.Entities.Validation;
import com.example.bty.Utils.ConnexionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceValidation implements IServiceValidation {
    private PreparedStatement pste;
    Connection cnx = ConnexionDB.getInstance().getConnexion();
    IServiceUser serviceUser = new ServiceUser();
    @Override
    public void ajouterValidation(Validation v) {
        String req = "INSERT INTO validation (`code`,`createdat`, `expiredat`,`id_user`) VALUES (?,?,?,?)";
        try {
            pste = cnx.prepareStatement(req);
            pste.setInt(1, v.getCode());
            pste.setTimestamp(2, java.sql.Timestamp.from(v.getCreated_at()));
            pste.setTimestamp(3, java.sql.Timestamp.from(v.getExpired_at()));
            pste.setInt(4, v.getUser().getId());
            pste.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Validation findByCode(int code) {
        Validation v = new Validation();
        String req = "SELECT * FROM validation WHERE code = ?";
        try (PreparedStatement pste=cnx.prepareStatement(req)) {
            pste.setInt(1, code);

            ResultSet rs = pste.executeQuery();
            while (rs.next()) {

                v.setId(rs.getInt("id"));
                v.setCode(rs.getInt("code"));
                v.setCreated_at(rs.getTimestamp("createdat").toInstant());
                v.setExpired_at(rs.getTimestamp("expiredat").toInstant());
                v.setUser(serviceUser.findByID(rs.getInt("id_user")));

            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceValidation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return v;
    }
    @Override
    public Validation findByIdUser(Integer id) {
        Validation v = new Validation();
        String req = "SELECT * FROM validation WHERE id_user = ?";
        try (PreparedStatement pste=cnx.prepareStatement(req)) {
            pste.setInt(1, id);

            ResultSet rs = pste.executeQuery();
            while (rs.next()) {

                v.setId(rs.getInt("id"));
                v.setCode(rs.getInt("code"));
                v.setCreated_at(rs.getTimestamp("createdat").toInstant());
                v.setExpired_at(rs.getTimestamp("expiredat").toInstant());
                v.setUser(serviceUser.findByID(rs.getInt("id_user")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceValidation.class.getName()).log(Level.SEVERE, null, ex);
        }
        return v;
    }
    @Override
    public void deleteValidation(int code) {
        String req = "DELETE FROM validation WHERE code = ?";
        try {
            pste = cnx.prepareStatement(req);
            pste.setInt(1, code);
            pste.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}