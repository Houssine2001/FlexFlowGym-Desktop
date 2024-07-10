package com.example.bty.Entities;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Cours {

    int id;
    String nom;
    String duree;
    String intensite;
    String cible;
    String categorie;
    String objectif;
    boolean etat;
    int capacite;

    private final ObjectProperty<byte[]> image = new SimpleObjectProperty<>();
    User coach;



    public Cours() {
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

    public String getDuree() {
        return duree;
    }

    public void setDuree(String duree) {
        this.duree = duree;
    }

    public String getIntensite() {
        return intensite;
    }

    public void setIntensite(String intensite) {
        this.intensite = intensite;
    }

    public String getCible() {
        return cible;
    }

    public void setCible(String cible) {
        this.cible = cible;
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

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public int getCapacite() {
        return capacite;
    }

    public void setCapacite(int capacite) {
        this.capacite = capacite;
    }

    public User getCoach() {
        return coach;
    }

    public void setCoach(User coach) {
        this.coach = coach;
    }

    public Cours(int id, String nom, String duree, String intensite, String cible, String categorie, String objectif, boolean etat, int capacite, User coach, byte[] image) {
        this.id = id;
        this.nom = nom;
        this.duree = duree;
        this.intensite = intensite;
        this.cible = cible;
        this.categorie = categorie;
        this.objectif = objectif;
        this.etat = etat;
        this.capacite = capacite;
        this.coach = coach;
        this.image.set(image);
    }

    public Cours(String nom, String duree, String intensite, String cible, String categorie, String objectif, boolean etat, int capacite, User coach, byte[] image) {
        this.nom = nom;
        this.duree = duree;
        this.intensite = intensite;
        this.cible = cible;
        this.categorie = categorie;
        this.objectif = objectif;
        this.etat = etat;
        this.capacite = capacite;
        this.coach = coach;
        this.image.set(image);
    }

    public Cours(int id, String nom, String duree, String intensite, String cible, String categorie, String objectif, boolean etat, int capacite, User coach) {
        this.id = id;
        this.nom = nom;
        this.duree = duree;
        this.intensite = intensite;
        this.cible = cible;
        this.categorie = categorie;
        this.objectif = objectif;
        this.etat = etat;
        this.capacite = capacite;
        this.coach = coach;
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

    @Override
    public String toString() {
        return "Cours{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", duree='" + duree + '\'' +
                ", intensite='" + intensite + '\'' +
                ", cible='" + cible + '\'' +
                ", categorie='" + categorie + '\'' +
                ", objectif='" + objectif + '\'' +
                ", etat=" + etat +
                ", capacité=" + capacite +
                ", coach=" + coach +
                '}';
    }
}