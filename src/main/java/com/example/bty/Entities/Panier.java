package com.example.bty.Entities;

import com.example.bty.Entities.Produit;
import com.example.bty.Services.ServiceProduit;
import com.example.bty.Utils.ConnexionDB;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Panier {



    private Connection connexion;
    private PreparedStatement pst;
    private Statement ste ;
    public Panier() {
        connexion = ConnexionDB.getInstance().getConnexion();
    }

    private Map<Produit, Integer> produitsDansPanier = new HashMap<>();

    public void ajouterAuPanier(Produit produit, int quantite) {
        if (produit.getQuantite() >= quantite) {
            produitsDansPanier.put(produit, quantite);
            System.out.println("Produit ajouté au panier : " + produit.getNom() + " (Quantité: " + quantite + ")");
        } else {
            System.out.println("Quantité demandée supérieure à la quantité en stock.");
        }
    }

    public void afficherPanier(boolean confirmerAchat, ServiceProduit produitDAO,User user) {
        double montantTotal = 0.0;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Produits dans le panier : ");
        for (Map.Entry<Produit, Integer> entry : produitsDansPanier.entrySet()) {
            Produit produit = entry.getKey();
            int quantiteAchete = entry.getValue();

            System.out.println("ID: " + produit.getIdProduit() +
                    ", Nom: " + produit.getNom() +
                    ", Prix unitaire: " + produit.getPrix() +
                    ", Quantité: " + quantiteAchete +
                    ", Montant partiel: " + (produit.getPrix() * quantiteAchete) + " DH");

            montantTotal += produit.getPrix() * quantiteAchete;
        }

        System.out.println("Montant total à payer : " + montantTotal + " DH");

        if (confirmerAchat) {
            System.out.println("Voulez-vous confirmer votre achat ? (true pour confirmer, false pour annuler)");
            boolean confirmation = scanner.nextBoolean();

            if (confirmation) {



                // Mettre à jour la base de données après confirmation d'achat
                for (Map.Entry<Produit, Integer> entry : produitsDansPanier.entrySet()) {
                    Produit produit = entry.getKey();
                    int quantiteAchete = entry.getValue();
                    produitDAO.mettreAJourQuantiteVendueEtTotale(produit, quantiteAchete);
                }
                ajouterCommande(produitDAO,user);


                System.out.println("Achat confirmé. Merci !");
            } else {
                System.out.println("Achat annulé. Confirmez votre achat s'il vous plaît.");
            }
        }
    }



    public Map<Produit, Integer> getProduitsDansPanier() {
        return produitsDansPanier;
    }





    private void ajouterCommande(ServiceProduit produitDAO,User user) {
        // Obtenez la date actuelle


        // Calculez le montant total
        double montantTotal = calculerMontantTotal();



        java.util.Date dateCommande = new java.util.Date();
        Timestamp timestamp = new Timestamp(dateCommande.getTime());

        // Ajoutez la commande à la base de données en utilisant votre méthode appropriée
        produitDAO.ajouterCommande(timestamp, user, produitsDansPanier);
    }

    // Ajouter une méthode pour calculer le montant total du panier
    private double calculerMontantTotal() {
        double montantTotal = 0.0;
        for (Map.Entry<Produit, Integer> entry : produitsDansPanier.entrySet()) {
            Produit produit = entry.getKey();
            int quantiteAchete = entry.getValue();
            montantTotal += produit.getPrix() * quantiteAchete;
        }
        return montantTotal;
    }








}
