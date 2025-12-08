package com.foodapp.dao;

import com.foodapp.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
// Utiliser BCrypt en Java pour la prod, mais ici simple comparaison pour l'exemple
// ou reproduire password_verify de PHP.

public class UserDAO {

    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String dbHash = rs.getString("password_hash");
                // IMPORTANT: En Java, il faut une librairie comme BCrypt pour vérifier le hash PHP
                // Pour l'instant, on suppose que tu gères la vérif du mot de passe:
                // if (BCrypt.checkpw(password, dbHash)) { ... }

                // On simule la réussite pour l'exemple :
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getBoolean("approuve")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}