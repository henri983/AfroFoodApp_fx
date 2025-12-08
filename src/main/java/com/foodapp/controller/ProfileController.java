package com.foodapp.controller;

import com.foodapp.dao.UserDAO;
import com.foodapp.model.User;
import com.foodapp.utils.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class ProfileController {
    @FXML private TextField txtUsername; // En lecture seule souvent
    @FXML private TextField txtEmail;    // En lecture seule souvent
    @FXML private TextField txtPrenom;
    @FXML private TextField txtTel;
    @FXML private TextArea txtAdresse;

    @FXML private PasswordField txtNewPass;
    @FXML private PasswordField txtConfirmPass;

    private UserDAO userDAO = new UserDAO();
    private User currentUser;

    @FXML
    public void initialize() {
        currentUser = UserSession.getInstance().getCurrentUser();
        if (currentUser != null) {
            // Remplir les champs
            txtUsername.setText(currentUser.getUsername());
            txtEmail.setText(currentUser.getEmail());
            txtPrenom.setText(currentUser.getPrenom()); // Peut être null, JavaFX gère
            txtTel.setText(currentUser.getTelephone());
            txtAdresse.setText(currentUser.getAdresse());

            // Empêcher la modif du pseudo/email si c'est ta règle
            txtUsername.setEditable(false);
            txtEmail.setEditable(false);
        }
    }

    @FXML
    private void handleSave() {
        // 1. Mettre à jour l'objet local
        currentUser.setPrenom(txtPrenom.getText());
        currentUser.setTelephone(txtTel.getText());
        currentUser.setAdresse(txtAdresse.getText());

        // 2. Vérifier mot de passe
        String newPass = txtNewPass.getText();
        String confirm = txtConfirmPass.getText();

        if (!newPass.isEmpty() && !newPass.equals(confirm)) {
            showAlert("Erreur", "Les mots de passe ne correspondent pas.");
            return;
        }

        // 3. Envoyer en BDD
        userDAO.updateProfile(currentUser, newPass.isEmpty() ? null : newPass);

        showAlert("Succès", "Profil mis à jour !");

        // Mettre à jour la session si nécessaire (déjà fait car currentUser est une réf)
    }

    @FXML
    private void handleRetour() {
        try {
            // Redirection vers le menu client (ou Admin selon le rôle)
            String fxml = "/com/example/afrofoodapp/ClientMenuView.fxml";
            if ("admin".equals(currentUser.getRole())) {
                fxml = "/com/example/afrofoodapp/AdminPlatsView.fxml";
            }

            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
