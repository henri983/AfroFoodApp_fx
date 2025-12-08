package com.foodapp.model;

import java.sql.Timestamp;

public class Commande {
    private int id;
    private int userId;
    private Timestamp dateCommande; // Utiliser Timestamp pour gérer date + heure MySQL
    private double total;

    public Commande(int id, int userId, Timestamp dateCommande, double total) {
        this.id = id;
        this.userId = userId;
        this.dateCommande = dateCommande;
        this.total = total;
    }

    // --- Getters ---

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public Timestamp getDateCommande() {
        return dateCommande;
    }

    // Utile pour afficher la date proprement dans une colonne (String)
    public String getDateFormatted() {
        return dateCommande.toString();
        // Tu pourras utiliser SimpleDateFormat ici pour faire plus joli plus tard
    }

    public double getTotal() {
        return total;
    }
}