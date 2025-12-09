package com.foodapp.dao;

import com.foodapp.model.Plat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class PlatDAO {

    // Récupérer tous les plats pour le tableau
    public ObservableList<Plat> getAllPlats() {
        ObservableList<Plat> list = FXCollections.observableArrayList();
        String sql = "SELECT * FROM plats ORDER BY pays, nom";

        // Correction du nom de la classe de connexion
        try (Connection conn = DatabaseConnexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Assure-toi que ton constructeur Plat dans Plat.java accepte bien ces 7 arguments
                // Si ta base de données n'a pas de colonne "region", enlève la ligne rs.getString("region")
                list.add(new Plat(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getString("image"),
                        rs.getString("pays"),
                        rs.getString("region")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Ajouter un plat (Correction : prend un objet Plat en paramètre)
    public void addPlat(Plat plat) {
        String sql = "INSERT INTO plats (nom, description, prix, image, pays, region) VALUES (?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, plat.getNom());
            stmt.setString(2, plat.getDescription());
            stmt.setDouble(3, plat.getPrix());
            stmt.setString(4, plat.getImagePath());
            stmt.setString(5, plat.getPays());
            stmt.setString(6, plat.getRegion());

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