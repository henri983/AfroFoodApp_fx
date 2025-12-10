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
            // Attention : Assure-toi que le chemin commence bien par /com/example/...
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERREUR : Impossible de charger le fichier : " + fxmlFile);
        }
    }

    @FXML
    private void handleConnexion(ActionEvent event) {
        navigate(event, "/com/example/afrofoodapp/LoginView.fxml");
    }

    @FXML
    private void handleInscription(ActionEvent event) {
        navigate(event, "/com/example/afrofoodapp/InscriptionView.fxml");
    }

    @FXML
    private void handleVoirMenu(ActionEvent event) {
        // --- MODIFICATION ICI ---
        // On dirige l'utilisateur vers la page de choix des régions (les 5 cartes)
        navigate(event, "/com/example/afrofoodapp/RegionSelectionView.fxml");
    }
}