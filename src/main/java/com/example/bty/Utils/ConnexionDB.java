/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.bty.Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionDB {
    final String url ="jdbc:mysql://localhost:3306/pidevgymweb";
    final String login ="root";
    final String pwd="";
    private static ConnexionDB instance;
    Connection connexion;

    private ConnexionDB(){

        try {
            connexion =  DriverManager.getConnection(url, login, pwd);
            System.out.println("Connexion �tablie!");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static ConnexionDB getInstance(){
        if (instance == null)
            instance = new ConnexionDB();
        return instance;
    }

    public Connection getConnexion() {
        return connexion;
    }
}