package com.example.bty.Entities;

import javafx.stage.Stage;

import java.util.Date;

public class Reclamation {
    private int id;
    private Date date_reclamation;
    private String titre_reclamation;
    private String description;
    private String etat;
    //private User membre;


    public Reclamation() {
    }

    public Reclamation(int id, Date date_reclamation, String titre_reclamation, String description, String etat) {
        this.id = id;
        this.date_reclamation = date_reclamation;
        this.titre_reclamation = titre_reclamation;
        this.description = description;
        this.etat = etat;
    }

    public Reclamation(Date date_reclamation, String titre_reclamation, String description, String etat) {
        this.date_reclamation = date_reclamation;
        this.titre_reclamation = titre_reclamation;
        this.description = description;
        this.etat = etat;
    }

    public Reclamation(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate_reclamation() {
        return date_reclamation;
    }

    public void setDate_reclamation(Date date_reclamation) {
        this.date_reclamation = date_reclamation;
    }

    public String getTitre_reclamation() {
        return titre_reclamation;
    }

    public void setTitre_reclamation(String titre_reclamation) {
        this.titre_reclamation = titre_reclamation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", date_reclamation='" + date_reclamation + '\'' +
                ", titre_reclamation='" + titre_reclamation + '\'' +
                ", description='" + description + '\'' +
                ", etat='" + etat + '\'' +
                '}';
    }

    public Reclamation(String titre_reclamation, String description) {
        this.titre_reclamation = titre_reclamation;
        this.description = description;
    }


}