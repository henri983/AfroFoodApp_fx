package com.foodapp.dao;

import com.foodapp.model.User;
import org.mindrot.jbcrypt.BCrypt; // <--- Import indispensable

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // --- 1. CONNEXION (Login) Sécurisé ---
    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dbHash = rs.getString("password_hash");

                // VÉRIFICATION BCrypt :
                // On compare le mot de passe saisi (clair) avec le hash de la BDD
                if (dbHash != null && BCrypt.checkpw(password, dbHash)) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retourne null si email inconnu OU mot de passe faux
    }

    // --- 2. INSCRIPTION (Register) ---
    public boolean register(String username, String email, String passwordHash, String role) {
        // Note : Le paramètre 'passwordHash' doit déjà être haché par le contrôleur
        // via BCrypt.hashpw(password, BCrypt.gensalt())

        String sql = "INSERT INTO users (username, email, password_hash, role, approuve, created_at) VALUES (?, ?, ?, ?, 0, NOW())";

        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, passwordHash); // On enregistre le hash directement
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

    // --- 6. PROFIL : Mettre à jour (Avec hachage si changement de mdp) ---
    public void updateProfile(User user, String newPasswordClear) {
        String sql = "UPDATE users SET prenom=?, adresse=?, telephone=?, photo=? WHERE id=?";

        // Si l'utilisateur fournit un nouveau mot de passe
        if (newPasswordClear != null && !newPasswordClear.isEmpty()) {
            sql = "UPDATE users SET prenom=?, adresse=?, telephone=?, photo=?, password_hash=? WHERE id=?";
        }

        try (Connection conn = DatabaseConnexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getPrenom());
            stmt.setString(2, user.getAdresse());
            stmt.setString(3, user.getTelephone());
            stmt.setString(4, user.getPhoto());

            if (newPasswordClear != null && !newPasswordClear.isEmpty()) {
                // IMPORTANT : On hache le nouveau mot de passe ici avant de l'enregistrer
                String hashedPassword = BCrypt.hashpw(newPasswordClear, BCrypt.gensalt());

                stmt.setString(5, hashedPassword);
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