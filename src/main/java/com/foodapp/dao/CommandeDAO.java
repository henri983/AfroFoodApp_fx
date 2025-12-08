package com.foodapp.dao;

import com.foodapp.model.CartItem;
import com.foodapp.model.User;
import java.sql.*;
import java.util.List;

public class CommandeDAO {

    // Traduction exacte de valider_commande.php avec Transaction
    public boolean validerCommande(User user, List<CartItem> panier) {
        if (panier.isEmpty()) return false;

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // DÉBUT TRANSACTION

            // 1. Calcul du total
            double total = panier.stream().mapToDouble(CartItem::getTotalPrice).sum();

            // 2. Insérer la commande principale
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

            // 3. Insérer les détails
            String sqlDetail = "INSERT INTO commande_details (commande_id, plat_id, quantite, prix) VALUES (?, ?, ?, ?)";
            PreparedStatement stmtDetail = conn.prepareStatement(sqlDetail);

            for (CartItem item : panier) {
                stmtDetail.setInt(1, commandeId);
                stmtDetail.setInt(2, item.getPlat().getId());
                stmtDetail.setInt(3, item.getQuantity());
                stmtDetail.setDouble(4, item.getPlat().getPrix());
                stmtDetail.addBatch(); // Optimisation
            }
            stmtDetail.executeBatch();

            conn.commit(); // VALIDER TRANSACTION
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}
        }
    }
}