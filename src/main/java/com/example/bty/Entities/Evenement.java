package com.example.bty.Entities;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.sql.Date;
import java.sql.Time;

public class Evenement {
    int id;
    String nom;
    String categorie;
    String objectif;
    int nbre_place;
    Date date;

    Time Time;


    User coach;
    boolean etat;

    private final ObjectProperty<byte[]> image = new SimpleObjectProperty<>();

    public Evenement(int id, String nom, byte[] image, String categorie, String objectif, Date date, java.sql.Time time, int nbrPlace) {
        this.id = id;
        this.nom = nom;
        this.image.set(image);
        this.categorie=categorie;
        this.objectif = objectif;
        this.date = date;
        this.Time = time;
        this.nbre_place = nbrPlace;
    }



    public Evenement(String nom, String categorie, String objectif, int nbre_place, Date date, Time time, User coach, boolean etat, byte[] image) {
        this.nom = nom;
        this.categorie = categorie;
        this.objectif = objectif;
        this.nbre_place = nbre_place;
        this.date = date;
        this.Time = time;
        this.coach = coach;
        this.etat = etat;
        this.image.set(image);

    }

    public byte[] getImage() {
        return image.get();
    }

    public ObjectProperty<byte[]> imageProperty() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image.set(image);
    }

    public Evenement(int id, String nom, String categorie, String objectif, int nbre_place, Date date, Time time, User coach, boolean etat, byte[] image) {
        this.id = id;
        this.nom = nom;
        this.categorie = categorie;
        this.objectif = objectif;
        this.nbre_place = nbre_place;
        this.date = date;
        Time = time;
        this.coach = coach;
        this.etat = etat;
        this.image.set(image);

    }

    public Evenement() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNbre_place() {
        return nbre_place;
    }

    public void setNbre_place(int nbre_place) {
        this.nbre_place = nbre_place;
    }



    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public User getCoach() {
        return coach;
    }

    public void setCoach(User coach) {
        this.coach = coach;
    }

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public Time getTime() {
        return Time;
    }

    public void setTime(Time time) {
        Time = time;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", date=" + date +
                ", nbre_place=" + nbre_place +
                ", categorie='" + categorie + '\'' +
                ", objectif='" + objectif + '\'' +
                ", coach=" + coach +
                ", etat=" + etat +
                ", Time='" + Time + '\'' +
                '}';
    }
}