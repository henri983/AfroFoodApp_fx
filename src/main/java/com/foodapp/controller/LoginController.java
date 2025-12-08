package com.foodapp.controller;

import com.foodapp.dao.UserDAO;
import com.foodapp.model.User;
import com.foodapp.utils.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        User user = userDAO.login(email, password);

        if (user != null) {
            if (!user.isApprouve()) {
                errorLabel.setText("Compte en attente d'approbation.");
                return;
            }

            // 1. Sauvegarde en session
            UserSession.getInstance().login(user);

            // 2. Redirection selon rôle
            try {
                String fxmlFile = user.getRole().equals("admin")
                        ? "/fxml/AdminView.fxml"
                        : "/fxml/MenuClientView.fxml";

                // Charger la nouvelle scène
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent root = loader.load();

                // Obtenir la fenêtre actuelle et changer la scène
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Erreur de chargement de la vue.");
            }

        } else {
            errorLabel.setText("Email ou mot de passe incorrect.");
        }
    }
}
