module com.example.afrofoodapp {
    // --- 1. Modules JavaFX obligatoires ---
    requires javafx.controls;
    requires javafx.fxml;

    // --- 2. Module pour la Base de Données ---
    requires java.sql;
    requires mysql.connector.j;

    // --- 3. Autres librairies ---
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    // --- 4. AJOUTEZ CETTE LIGNE POUR CORRIGER L'ERREUR ---
    requires jbcrypt;
    requires com.gluonhq.charm.glisten;
    requires javafx.base;
    requires javafx.graphics;
    // -----------------------------------------------------

    // --- 5. Exportations et Ouvertures ---
    exports com.foodapp.main;
    opens com.foodapp.controller to javafx.fxml;
    opens com.foodapp.model to javafx.base;
    exports com.foodapp.model;
}