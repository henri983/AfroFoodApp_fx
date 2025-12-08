package com.monrestaurant.model;

public class User {
    private int id;
    private String username;
    private String email;
    private String role; // "admin" ou "customer"
    private boolean approuve;
    private String prenom;
    private String adresse;
    private String telephone;
    private String photo;

    public User(int id, String username, String email, String role, boolean approuve,
                String prenom, String adresse, String telephone, String photo) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.approuve = approuve;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = telephone;
        this.photo = photo;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public boolean isApprouve() { return approuve; }
    public String getPrenom() { return prenom; }
    public String getAdresse() { return adresse; }
    public String getTelephone() { return telephone; }
    public String getPhoto() { return photo; }

    // Setters (pour la mise à jour du profil)
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setPhoto(String photo) { this.photo = photo; }
}