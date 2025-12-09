package com.foodapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnexion {

    // Vérifie que le nom de ta base de données est bien 'food_app_db'
    private static final String URL = "jdbc:mysql://localhost:3306/food_app_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Mets ton mot de passe ici s'il y en a un

    public static Connection getConnection() throws SQLException {
        try {
            // Charge le driver (optionnel dans les versions récentes de Java mais conseillé)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
