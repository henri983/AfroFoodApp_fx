module com.example.afrofoodapp {
    // --- 1. Modules JavaFX obligatoires ---
    requires javafx.controls;
    requires javafx.fxml;

    // --- 2. Module pour la Base de Données ---
    requires java.sql;
    requires mysql.connector.j; // INDISPENSABLE pour le driver MySQL

    // --- 3. Autres librairies (gardées de votre fichier d'origine) ---
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // --- 4. AUTORISATIONS (C'est ici que ça bloquait) ---

    // Permet à JavaFX de lancer l'application (CORRIGE L'ERREUR DE DÉMARRAGE)
    exports com.foodapp.main;

    // Permet à JavaFX de lire vos fichiers FXML et d'injecter les contrôleurs (@FXML)
    opens com.foodapp.controller to javafx.fxml;

    // Permet aux TableView de lire les getters de vos objets (User, Plat...)
    opens com.foodapp.model to javafx.base;
    exports com.foodapp.model;
}