package com.foodapp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    // Méthode générique pour changer de page
    private void navigate(ActionEvent event, String fxmlFile) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Impossible de charger le fichier : " + fxmlFile);
        }
    }

    @FXML
    private void handleConnexion(ActionEvent event) {
        // Redirige vers la page de login
        navigate(event, "/com/example/afrofoodapp/LoginView.fxml");
    }

    @FXML
    private void handleInscription(ActionEvent event) {
        // Redirige vers la page d'inscription
        navigate(event, "/com/example/afrofoodapp/InscriptionView.fxml");
    }

    @FXML
    private void handleVoirMenu(ActionEvent event) {
        // Pour voir le menu, on demande généralement de se connecter d'abord
        // Ou tu peux diriger vers le menu directement si tu as géré le mode "invité"
        navigate(event, "/com/example/afrofoodapp/LoginView.fxml");
    }
}