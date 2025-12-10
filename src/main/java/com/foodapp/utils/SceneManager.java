package com.foodapp.utils;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    // Chemin par défaut pour les ressources FXML (adaptez si nécessaire)
    private static final String FXML_PATH_PREFIX = "/com/example/afrofoodapp/";

    /**
     * Charge une vue FXML et affiche le Stage (fenêtre)
     * @param fxmlFileName Le nom du fichier FXML (ex: "ClientMenuView.fxml")
     * @param title Le titre de la fenêtre
     * @throws IOException Si le fichier FXML n'est pas trouvé
     */
    public static void loadView(String fxmlFileName, String title) throws IOException {

        // Construction du chemin complet de la ressource FXML
        // Le chemin est relatif au dossier 'resources' de votre projet
        String fullPath = FXML_PATH_PREFIX + fxmlFileName;

        FXMLLoader fxmlLoader = new FXMLLoader(SceneManager.class.getResource(fullPath));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}