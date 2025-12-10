package com.foodapp.dao;

import com.foodapp.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // --- 1. CONNEXION (Login) ---
    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // NOTE : Pour l'instant, on ignore la vérification du mot de passe
                // pour permettre la connexion sur les comptes importés de PHP.
                // String dbPass = rs.getString("password_hash");

                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getBoolean("approuve"),
                        rs.getString("prenom"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getString("photo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- 2. INSCRIPTION (Register) ---
    public boolean register(String username, String email, String password, String role) {
        // On insère avec approuve = 0 (en attente) par défaut
        String sql = "INSERT INTO users (username, email, password_hash, role, approuve, created_at) VALUES (?, ?, ?, ?, 0, NOW())";

        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password); // Stocké en clair pour l'instant (faute de librairie BCrypt)
            stmt.setString(4, role);

            int result = stmt.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- 3. ADMIN : Récupérer tous les utilisateurs ---
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";

        try (Connection conn = DatabaseConnexion.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getBoolean("approuve"),
                        rs.getString("prenom"),
                        rs.getString("adresse"),
                        rs.getString("telephone"),
                        rs.getString("photo")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // --- 4. ADMIN : Approuver un utilisateur ---
    public void approveUser(int userId) {
        String sql = "UPDATE users SET approuve = 1 WHERE id = ?";
        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- 5. ADMIN : Supprimer un utilisateur ---
    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- 6. PROFIL : Mettre à jour (AVEC PHOTO) ---
    public void updateProfile(User user, String newPassword) {
        // MODIFICATION : Ajout de la colonne 'photo' dans la requête
        String sql = "UPDATE users SET prenom=?, adresse=?, telephone=?, photo=? WHERE id=?";

        // Si l'utilisateur change aussi son mot de passe
        if (newPassword != null && !newPassword.isEmpty()) {
            sql = "UPDATE users SET prenom=?, adresse=?, telephone=?, photo=?, password_hash=? WHERE id=?";
        }

        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getPrenom());
            stmt.setString(2, user.getAdresse());
            stmt.setString(3, user.getTelephone());
            stmt.setString(4, user.getPhoto()); // Sauvegarde du nom de fichier image

            if (newPassword != null && !newPassword.isEmpty()) {
                stmt.setString(5, newPassword);
                stmt.setInt(6, user.getId());
            } else {
                stmt.setInt(5, user.getId());
            }

            stmt.executeUpdate();
            System.out.println("Profil mis à jour avec succès !");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}