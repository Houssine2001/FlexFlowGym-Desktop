package com.example.bty.Entities;

public class Rating {
    private int id;
    private String nomCour;
    private int userId;
    private int rating;
    private boolean liked;
    private boolean disliked;

    // Constructeur
    public Rating(String nomCour, int userId, int rating, boolean liked, boolean disliked) {
        this.nomCour = nomCour;
        this.userId = userId;
        this.rating = rating;
        this.liked = liked;
        this.disliked = disliked;
    }

    // Getters et setters
    // Vous pouvez générer automatiquement ces méthodes dans votre IDE ou les écrire manuellement

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomCour() {
        return nomCour;
    }

    public void setNomCour(String nomCour) {
        this.nomCour = nomCour;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isDisliked() {
        return disliked;
    }

    public void setDisliked(boolean disliked) {
        this.disliked = disliked;
    }
}