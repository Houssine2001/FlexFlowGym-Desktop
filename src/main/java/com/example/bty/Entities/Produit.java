package com.example.bty.Entities;

import javafx.beans.property.*;

public class Produit {

    private final IntegerProperty idProduit = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty prix = new SimpleDoubleProperty();
    private final StringProperty type = new SimpleStringProperty();
    private final IntegerProperty quantite = new SimpleIntegerProperty();
    private final IntegerProperty quantiteVendues = new SimpleIntegerProperty();

    private final ObjectProperty<byte[]> image = new SimpleObjectProperty<>();

    private int quantiteVenduues;
    private int quantiteDisponible;

    public int getQuantiteVenduues() {
        return quantiteVenduues;
    }

    public void setQuantiteVenduues(int quantiteVenduues) {
        this.quantiteVenduues = quantiteVenduues;
    }

    public int getQuantiteDisponible() {
        return quantiteDisponible;
    }

    public void setQuantiteDisponible(int quantiteDisponible) {
        this.quantiteDisponible = quantiteDisponible;
    }

    public Produit() {
        // Constructeur par défaut
    }

    public Produit(int idProduit, String nom, String description, double prix, String type, int quantite, int quantiteVendues , byte[] image) {
        this.idProduit.set(idProduit);
        this.nom.set(nom);
        this.description.set(description);
        this.prix.set(prix);
        this.type.set(type);
        this.quantite.set(quantite);
        this.quantiteVendues.set(quantiteVendues);
        this.image.set(image);
    }

    public int getIdProduit() {
        return idProduit.get();
    }

    public void setIdProduit(int idProduit) {
        this.idProduit.set(idProduit);
    }

    public IntegerProperty idProduitProperty() {
        return idProduit;
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public double getPrix() {
        return prix.get();
    }

    public void setPrix(double prix) {
        this.prix.set(prix);
    }

    public DoubleProperty prixProperty() {
        return prix;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty typeProperty() {
        return type;
    }

    public int getQuantite() {
        return quantite.get();
    }

    public void setQuantite(int quantite) {
        this.quantite.set(quantite);
    }

    public IntegerProperty quantiteProperty() {
        return quantite;
    }

    public int getQuantiteVendues() {
        return quantiteVendues.get();
    }

    public void setQuantiteVendues(int quantiteVendues) {
        this.quantiteVendues.set(quantiteVendues);
    }

    public IntegerProperty quantiteVenduesProperty() {
        return quantiteVendues;
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
        return "Produit{" +
                "idProduit=" + idProduit.get() +
                ", nom='" + nom.get() + '\'' +
                ", description='" + description.get() + '\'' +
                ", prix=" + prix.get() +
                ", type='" + type.get() + '\'' +
                ", quantite=" + quantite.get() +
                ", quantiteVendues=" + quantiteVendues.get() +
                '}';
    }
}
