package com.foodapp.controller;

import com.foodapp.dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class InscriptionController {
    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;

    private UserDAO userDAO = new UserDAO();

    //bouton retour
    @FXML
    private void handleRetour(ActionEvent event) {
        try {
            // CHARGEMENT DE LA VUE ACCUEIL

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/afrofoodapp/MainView.fxml"));
            Parent root = loader.load();

            // RÉCUPÉRATION DE LA FENÊTRE ACTUELLE (STAGE)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // CHANGEMENT DE LA SCÈNE
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erreur : Impossible de charger la vue Accueil.");
        }
    }
    @FXML
    private void handleInscription() {
        String user = txtUsername.getText();
        String email = txtEmail.getText();
        String pass = txtPassword.getText();

        if (user.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            lblError.setText("Remplissez tous les champs.");
            return;
        }

        // Hachage du mot de passe
        String hashedPassword = BCrypt.hashpw(pass, BCrypt.gensalt());

        // Enregistrement
        boolean success = userDAO.register(user, email, hashedPassword, "customer");

        if (success) {
            System.out.println("Inscription réussie ! Redirection vers le login...");
            goToLogin();
        } else {
            lblError.setText("Erreur : Cet email est peut-être déjà utilisé.");
        }
    }

    @FXML
    private void goToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/afrofoodapp/LoginView.fxml"));
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace(); // Regardez la console rouge en bas !
            System.err.println("Impossible de charger la vue Login.");
        }
    }
}