package com.example.bty.Entities;

import javafx.beans.property.*;

public class paiment {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty carte = new SimpleStringProperty();
    private final IntegerProperty mm = new SimpleIntegerProperty();
    private final IntegerProperty yy = new SimpleIntegerProperty();
    private final IntegerProperty cvc = new SimpleIntegerProperty();

    private final IntegerProperty total = new SimpleIntegerProperty();


    public String getNom() {
        return nom.get();
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public int getMm() {
        return mm.get();
    }

    public IntegerProperty mmProperty() {
        return mm;
    }

    public void setMm(int mm) {
        this.mm.set(mm);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getCarte() {
        return carte.get();
    }

    public StringProperty carteProperty() {
        return carte;
    }

    public void setCarte(String carte) {
        this.carte.set(carte);
    }



    public int getYy() {
        return yy.get();
    }

    public IntegerProperty yyProperty() {
        return yy;
    }

    public void setYy(int yy) {
        this.yy.set(yy);
    }

    public int getCvc() {
        return cvc.get();
    }

    public IntegerProperty cvcProperty() {
        return cvc;
    }

    public void setCvc(int cvc) {
        this.cvc.set(cvc);
    }

    public int getTotal() {
        return total.get();
    }

    public IntegerProperty totalProperty() {
        return total;
    }

    public void setTotal(int total) {
        this.total.set(total);
    }

    @Override
    public String toString() {
        return "paiment{" +
                "id=" + id +
                ", nom=" + nom +
                ", email=" + email +
                ", carte=" + carte +
                ", mm=" + mm +
                ", yy=" + yy +
                ", cvc=" + cvc +
                ", total=" + total +
                '}';
    }

    public paiment() {
    }


    public paiment(int id, String nom, String email, String carte, int mm, int yy , int cvc ,int total) {
        this.id.set(id);
        this.nom.set(nom);
        this.email.set(email);
        this.carte.set(carte);
        this.mm.set(mm);
        this.yy.set(yy);
        this.cvc.set(cvc);
        this.total.set(total);
    }




}
