package com.example.bty.Entities;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

public class Reservation {
    int id;

    Timestamp date_reservation;
    Evenement evenement;
    User membre;


    public Reservation() {
    }

    public Reservation(int id, Timestamp date_reservation, Evenement evenement, User membre) {
        this.id = id;
        this.date_reservation = date_reservation;

        this.evenement = evenement;
        this.membre = membre;

    }


    public Reservation(Timestamp date_reservation,  Evenement evenement, User membre) {
        this.date_reservation = date_reservation;

        this.evenement = evenement;
        this.membre = membre;

    }

    public int getId() {
        return id;
    }

    public Date getDate_reservation() {
        return date_reservation;
    }

    public void setDate_reservation(Timestamp date_reservation) {
        this.date_reservation = date_reservation;
    }



    public Evenement getEvenement() {
        return evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public User getMembre() {
        return membre;
    }

    public void setMembre(User membre) {
        this.membre = membre;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", date_reservation=" + date_reservation +
                ", evenement=" + evenement +
                ", membre=" + membre +
                '}';
    }
}