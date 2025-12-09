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

        // 1. Validation basique
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        // 2. Appel à la base de données
        User user = userDAO.login(email, password);

        if (user != null) {
            // 3. Vérification si le compte est approuvé
            if (!user.isApprouve()) {
                errorLabel.setText("Compte en attente d'approbation.");
                return;
            }

            // 4. Mise en session
            UserSession.getInstance().login(user);

            // 5. Redirection
            try {
                String fxmlFile;

                // Si c'est un admin, on l'envoie vers l'admin dashboard
                if ("admin".equalsIgnoreCase(user.getRole())) {
                    fxmlFile = "/com/example/afrofoodapp/AdminPlatsView.fxml";
                } else {
                    // Sinon (client), on l'envoie vers le Menu
                    fxmlFile = "/com/example/afrofoodapp/MenuClientView.fxml";
                }

                // Chargement de la vue
                Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));

                // Changement de scène
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText("Erreur : Fichier FXML introuvable (" + e.getMessage() + ")");
                System.err.println("Vérifiez le chemin du fichier FXML dans src/main/resources !");
            }

        } else {
            errorLabel.setText("Email ou mot de passe incorrect.");
        }
    }

    // --- Méthode pour aller vers l'inscription ---
    @FXML
    private void goToRegister() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/afrofoodapp/InscriptionView.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur : InscriptionView.fxml introuvable");
        }
    }
}