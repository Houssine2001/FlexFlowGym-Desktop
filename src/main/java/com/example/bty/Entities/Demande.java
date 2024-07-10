package com.example.bty.Entities;

import java.sql.Time;

public class Demande {
    public boolean isMaladieCHronique;
    private String nom;

    private int id;
    private int age;
    private int nbre_heure;
    private boolean maladie_chronique;
    private String but;
    private String niveau_physique;
    private User membre;
    private Offre offre;
    private Etat etat;
    private Byte image;
    private String lesjours;
    private Time horaire;

    public Demande(String id, String age, String nbre_heure, String maladie_chronique, String but, String niveau_physique, String m, String f, String etat, Time lesjours, String horaire) {
    }

    public Demande(Object nom, String idDemande, Object but, Object niveauPhysique, Object maladieChronique, Object age, Object idUser, Object idOffre, Object etat, Object nombreHeure, Object horaire, Object lesjours) {
    }

    public Time getHoraire() {
        return horaire;
    }

    public void setHoraire(Time horaire) {
        this.horaire = horaire;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setEtat(Etat etat) {
        this.etat = etat;
    }

    public int getId() {
        return id;
    }

    public Byte getImage() {
        return image;
    }

    public void setImage(Byte image) {
        this.image = image;
    }

    public String getLesjours() {
        return lesjours;
    }

    public void setLesjours(String lesjours) {
        this.lesjours = lesjours;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNbre_heure() {
        return nbre_heure;
    }

    public void setNbre_heure(int nbre_heure) {
        this.nbre_heure = nbre_heure;
    }

    public boolean isMaladie_chronique() {
        return maladie_chronique;
    }

    public void setMaladie_chronique(boolean maladie_chronique) {
        this.maladie_chronique = maladie_chronique;
    }

    public String getBut() {
        return but;
    }

    public void setBut(String but) {
        this.but = but;
    }

    public String getNiveau_physique() {
        return niveau_physique;
    }

    public void setNiveau_physique(String niveau_physique) {
        this.niveau_physique = niveau_physique;
    }

    public User getMembre() {
        return membre;
    }

    public void setMembre(User membre) {
        this.membre = membre;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }


    public Demande(int id_demande, int age, int nbre_heure, boolean maladie_chronique, String but, String niveau_physique, User membre, Offre offre, Etat etat, String lesjours, Time horaire) {
        this.id = id_demande;
        this.age = age;
        this.nbre_heure = nbre_heure;
        this.maladie_chronique = maladie_chronique;
        this.but = but;
        this.niveau_physique = niveau_physique;
        this.membre = membre;
        this.offre = offre;
        this.etat = Etat.REFUSER; // Par défaut, l'état est initialisé à refu
        this.lesjours = lesjours;
        this.horaire = horaire;
    }

    public Demande(int age, int nbre_heure, boolean maladie_chronique, String but, String niveau_physique, User membre, Offre offre) {
        this.age = age;
        this.nbre_heure = nbre_heure;
        this.maladie_chronique = maladie_chronique;
        this.but = but;
        this.niveau_physique = niveau_physique;
        this.membre = membre;
        this.offre = offre;
    }

    @Override
    public String toString() {
        return "Demande{" +
                "id=" + id +
                ", age=" + age +
                ", nbre_heure=" + nbre_heure +
                ", maladie_chronique=" + maladie_chronique +
                ", but='" + but + '\'' +
                ", niveau_physique='" + niveau_physique + '\'' +
                ", membre=" + membre +
                ", offre=" + offre +
                '}';
    }

    public boolean getMaladie_chronique() {
        return maladie_chronique;
    }

    public int getId_demande() {
        return id;
    }

    public Etat getEtat() {
        return etat;
    }

    // Méthode pour accepter la demande
    public void accepterDemande() {
        this.etat = Etat.ACCEPTER;
    }

    // Méthode pour refuser la demande
    public void refuserDemande() {
        this.etat = Etat.REFUSER;

    }


}