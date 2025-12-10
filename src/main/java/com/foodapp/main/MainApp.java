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
            // --- MODIFICATION ICI ---
            // On charge MainView.fxml au lieu de LoginView.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/afrofoodapp/MainView.fxml"));
            Parent root = loader.load();

            // 2. Création de la scène
            // On définit une taille par défaut un peu plus large pour l'accueil (ex: 900x600)
            Scene scene = new Scene(root, 900, 600);

            // 3. Configuration de la fenêtre
            primaryStage.setTitle("Saveurs d'Afrique - Accueil"); // Titre changé
            primaryStage.setScene(scene);

            // On autorise le redimensionnement pour l'accueil, ou non, selon ton choix
            primaryStage.setResizable(true);

            // 4. Affichage
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERREUR : Impossible de charger MainView.fxml.");
            System.err.println("Vérifie que le fichier est bien dans : src/main/resources/com/example/afrofoodapp/MainView.fxml");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}