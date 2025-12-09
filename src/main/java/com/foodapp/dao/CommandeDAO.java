package com.foodapp.dao;

import com.foodapp.model.Plat;
import com.foodapp.model.User;
import java.sql.*;
import java.util.Map;

public class CommandeDAO {

    // Méthode adaptée pour correspondre à l'appel du CartController
    public boolean saveOrder(User user, Map<Plat, Integer> cart) {
        if (cart.isEmpty()) return false;

        Connection conn = null;
        try {
            // Correction 1 : Utilisation de DatabaseConnexion (avec 'x')
            conn = DatabaseConnexion.getConnection();
            conn.setAutoCommit(false); // DÉBUT TRANSACTION

            // Correction 2 : Calcul du total depuis la Map
            double total = 0;
            for (Map.Entry<Plat, Integer> entry : cart.entrySet()) {
                total += entry.getKey().getPrix() * entry.getValue();
            }

            // 1. Insérer la commande principale
            String sqlCmd = "INSERT INTO commandes (user_id, date_commande, total) VALUES (?, NOW(), ?)";
            PreparedStatement stmtCmd = conn.prepareStatement(sqlCmd, Statement.RETURN_GENERATED_KEYS);
            stmtCmd.setInt(1, user.getId());
            stmtCmd.setDouble(2, total);
            stmtCmd.executeUpdate();

            ResultSet rs = stmtCmd.getGeneratedKeys();
            int commandeId = 0;
            if (rs.next()) {
                commandeId = rs.getInt(1);
            }

            // 2. Insérer les détails
            String sqlDetail = "INSERT INTO commande_details (commande_id, plat_id, quantite, prix) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtDetail = conn.prepareStatement(sqlDetail);

            // Correction 3 : Boucle sur la Map (Clé = Plat, Valeur = Quantité)
            for (Map.Entry<Plat, Integer> entry : cart.entrySet()) {
                Plat plat = entry.getKey();
                int quantite = entry.getValue();

                stmtDetail.setInt(1, commandeId);
                stmtDetail.setInt(2, plat.getId());
                stmtDetail.setInt(3, quantite);
                stmtDetail.setDouble(4, plat.getPrix());
                stmtDetail.addBatch();
            }
            stmtDetail.executeBatch();

            conn.commit(); // VALIDER TRANSACTION
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException e) {}
        }
    }
}