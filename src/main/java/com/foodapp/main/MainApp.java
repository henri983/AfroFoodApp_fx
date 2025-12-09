package com.foodapp.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // 1. Chargement de la vue de connexion (LoginView.fxml)
            // ATTENTION : Le chemin doit correspondre exactement à ton dossier resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/afrofoodapp/LoginView.fxml"));
            Parent root = loader.load();

            // 2. Création de la scène
            Scene scene = new Scene(root);

            // Tu peux ajouter une feuille de style CSS globale ici si tu en as une
            // scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

            // 3. Configuration de la fenêtre (Stage)
            primaryStage.setTitle("AfroFood App - Authentification");
            primaryStage.setScene(scene);

            // Optionnel : Empêcher le redimensionnement de la fenêtre de login
            primaryStage.setResizable(false);

            // Optionnel : Ajouter une icône à l'application
            // Assure-toi d'avoir une image dans resources/images/logo.png
            // primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));

            // 4. Affichage
            primaryStage.show();

        } catch (IOException e) {
            // En cas d'erreur (fichier FXML introuvable souvent)
            e.printStackTrace();
            System.err.println("ERREUR CRITIQUE : Impossible de charger le fichier FXML.");
            System.err.println("Vérifiez le chemin : /com/example/afrofoodapp/LoginView.fxml");
        }
    }

    // Point d'entrée standard de Java
    public static void main(String[] args) {
        launch(args);
    }
}
