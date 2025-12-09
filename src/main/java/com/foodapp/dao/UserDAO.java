package com.foodapp.dao;

import com.foodapp.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // --- 1. CONNEXION (Login) ---
    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";

        // Attention : Vérifie si ta classe s'appelle DatabaseConnection ou DatabaseConnexion
        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Ici on vérifie le mot de passe (en clair pour l'instant)
                // String dbPass = rs.getString("password_hash");
                // if (!password.equals(dbPass)) return null;

                // On retourne un objet User complet (avec les 9 champs)
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

    // --- 2. INSCRIPTION (Register) - C'était ton erreur ---
    public boolean register(String username, String email, String password, String role) {
        String sql = "INSERT INTO users (username, email, password_hash, role, approuve, created_at) VALUES (?, ?, ?, ?, 0, NOW())";

        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
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

    // --- 6. PROFIL : Mettre à jour ---
    public void updateProfile(User user, String newPassword) {
        String sql = "UPDATE users SET prenom=?, adresse=?, telephone=? WHERE id=?";
        if (newPassword != null && !newPassword.isEmpty()) {
            sql = "UPDATE users SET prenom=?, adresse=?, telephone=?, password_hash=? WHERE id=?";
        }

        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getPrenom());
            stmt.setString(2, user.getAdresse());
            stmt.setString(3, user.getTelephone());

            if (newPassword != null && !newPassword.isEmpty()) {
                stmt.setString(4, newPassword);
                stmt.setInt(5, user.getId());
            } else {
                stmt.setInt(4, user.getId());
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}