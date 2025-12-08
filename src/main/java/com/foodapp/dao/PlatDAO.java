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

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Plat(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getDouble("prix"),
                        rs.getString("image"),
                        rs.getString("pays")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Ajouter un plat
    public void addPlat(String nom, String desc, double prix, String image, String pays) {
        String sql = "INSERT INTO plats (nom, description, prix, image, pays) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nom);
            stmt.setString(2, desc);
            stmt.setDouble(3, prix);
            stmt.setString(4, image);
            stmt.setString(5, pays);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Supprimer un plat
    public void deletePlat(int id) {
        String sql = "DELETE FROM plats WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}