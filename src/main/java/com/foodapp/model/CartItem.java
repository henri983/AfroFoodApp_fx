package com.foodapp.model;

public class CartItem {
    private Plat plat;
    private int quantity;

    public CartItem(Plat plat, int quantity) {
        this.plat = plat;
        this.quantity = quantity;
    }

    // --- Getters et Setters standards ---

    public Plat getPlat() {
        return plat;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // --- Méthodes "Helper" pour l'affichage dans le TableView JavaFX ---
    // Ces méthodes permettent d'utiliser PropertyValueFactory("nomPlat") directement

    public String getNomPlat() {
        return plat.getNom();
    }

    public double getPrixUnitaire() {
        return plat.getPrix();
    }

    public double getTotalPrice() {
        return plat.getPrix() * quantity;
    }
}