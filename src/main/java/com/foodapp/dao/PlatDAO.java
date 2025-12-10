package com.foodapp.dao;

import com.foodapp.model.Plat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class PlatDAO {

    // Récupérer tous les plats
    public ObservableList<Plat> getAllPlats() {
        ObservableList<Plat> list = FXCollections.observableArrayList();

        // CORRECTION 1 : On trie par 'region' car 'pays' n'existe pas
        String sql = "SELECT * FROM plats ORDER BY region, nom";

        try (Connection conn = DatabaseConnexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // CORRECTION 2 : Mapping des colonnes
                // La colonne SQL 'region' contient le pays (ex: "cameroun")
                // On l'utilise pour remplir le champ 'pays' de l'objet Plat Java

                String paysData = rs.getString("region");
                // Si tu n'as pas de sous-région dans la BDD, on met null ou une chaine vide pour le 7ème argument
                String regionData = "";

                list.add(new Plat(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getString("image"),
                        paysData,   // On passe la donnée 'region' de la BDD ici (Cameroun, Mali...)
                        regionData  // Champ région Java laissé vide pour l'instant
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Ajouter un plat
    public void addPlat(Plat plat) {
        // CORRECTION 3 : La requête INSERT doit correspondre aux colonnes réelles (pas de colonne 'pays')
        String sql = "INSERT INTO plats (nom, description, prix, image, region) VALUES (?,?,?,?,?)";

        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plat.getNom());
            stmt.setString(2, plat.getDescription());
            stmt.setDouble(3, plat.getPrix());
            stmt.setString(4, plat.getImagePath());
            stmt.setString(5, plat.getPays()); // On enregistre le pays dans la colonne 'region' de la BDD

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un plat
    public void deletePlat(int id) {
        String sql = "DELETE FROM plats WHERE id = ?";

        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}