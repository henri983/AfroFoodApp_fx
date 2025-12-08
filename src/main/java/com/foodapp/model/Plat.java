package com.foodapp.model;

public class Plat {
    private int id;
    private String nom;
    private String description;
    private double prix;
    private String imagePath;
    private String pays;
    private String region;

    public Plat(int id, String nom, String description, double prix, String imagePath, String pays, String region) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.imagePath = imagePath;
        this.pays = pays;
        this.region = region;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getDescription() { return description; }
    public double getPrix() { return prix; }
    public String getImagePath() { return imagePath; }
    public String getPays() { return pays; }
    public String getRegion() { return region; }
}