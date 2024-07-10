package com.example.bty.Entities;

import javafx.beans.property.*;

import java.sql.Timestamp;

public class Commmande {
    private final IntegerProperty idCommande = new SimpleIntegerProperty();
    private final ObjectProperty<Timestamp> dateCommande = new SimpleObjectProperty<>();
    private final IntegerProperty idProduit = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final DoubleProperty montant = new SimpleDoubleProperty();




    public Commmande(int idCommande, Timestamp dateCommande, int idProduit, String nom, double montant) {
        this.idCommande.set(idCommande);
        this.dateCommande.set(dateCommande);
        this.idProduit.set(idProduit);
        this.nom.set(nom);
        this.montant.set(montant);

    }

    public Commmande(){}

    public int getIdCommande() {
        return idCommande.get();
    }

    public IntegerProperty getIdCommandeProperty() {
        return idCommande;
    }

    public Timestamp getDateCommande() {
        return dateCommande.get();
    }

    public ObjectProperty<Timestamp> getDateCommandeProperty() {
        return dateCommande;
    }

    public int getIdProduit() {
        return idProduit.get();
    }

    public IntegerProperty getIdProduitProperty() {
        return idProduit;
    }

    public String getNom() {
        return nom.get();
    }

    public StringProperty getNomProperty() {
        return nom;
    }

    public double getMontant() {
        return montant.get();
    }

    public DoubleProperty getMontantProperty() {
        return montant;
    }


    public void setIdCommande(int idCommande) {
        this.idCommande.set(idCommande);
    }

    public void setDateCommande(Timestamp dateCommande) {
        this.dateCommande.set(dateCommande);
    }

    public void setIdProduit(int idProduit) {
        this.idProduit.set(idProduit);
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public void setMontant(double montant) {
        this.montant.set(montant);
    }


}
