package com.example.bty.Entities;

public class Participation {
    int id;
    User user;
    Cours cours;
    String nomParticipant;


    public Participation() {
    }

    public Participation(int id, User user, Cours cours, String nomParticipant) {
        this.id = id;
        this.user = user;
        this.cours = cours;
        this.nomParticipant = nomParticipant; // Modification du constructeur pour inclure nomParticipant
    }

    public Participation(User user, Cours cours, String nomParticipant) {
        this.user = user;
        this.cours = cours;
        this.nomParticipant = nomParticipant; // Modification du constructeur pour inclure nomParticipant
    }

    public String getNomParticipant() {
        return nomParticipant;
    }

    public void setNomParticipant(String nomParticipant) {
        this.nomParticipant = nomParticipant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cours getcours() {
        return cours;
    }

    public void setcours(Cours cours) {
        this.cours = cours;
    }
}
