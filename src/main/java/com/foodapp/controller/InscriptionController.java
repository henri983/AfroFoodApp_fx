package com.foodapp.controller;

import com.foodapp.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class InscriptionController {
    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleInscription() {
        String user = txtUsername.getText();
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        if (user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            lblError.setText("Remplissez tous les champs.");
            return;
        }

        // On inscrit en tant que 'customer'
        boolean success = userDAO.register(user, email, pass, "customer");

        if (success) {
            // Retour au Login
            goToLogin();
        } else {
            lblError.setText("Erreur: Email déjà utilisé ?");
        }
    }

    @FXML
    private void goToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/afrofoodapp/LoginView.fxml"));
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
