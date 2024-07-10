package com.example.bty.Entities;

public class Commande {
    private int idProduit;
    private String nomProduit;
    private int quantite;
    private int montantTotale;

    public Commande(int idProduit, String nomProduit, int quantite, int montantTotale) {
        this.idProduit = idProduit;
        this.nomProduit = nomProduit;
        this.quantite = quantite;
        this.montantTotale = montantTotale;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public int getMontantTotale() {
        return montantTotale;
    }
}
