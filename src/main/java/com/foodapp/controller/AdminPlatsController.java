package com.foodapp.controller;

import com.foodapp.dao.PlatDAO;
import com.foodapp.model.Plat;
import com.foodapp.utils.UserSession;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class AdminPlatsController {

    // --- 1. LES VARIABLES FXML (Doivent correspondre aux fx:id du FXML) ---
    @FXML private TableView<Plat> tablePlats;
    @FXML private TableColumn<Plat, String> colNom;
    @FXML private TableColumn<Plat, Double> colPrix;
    @FXML private TableColumn<Plat, String> colPays;
    @FXML private TableColumn<Plat, String> colRegion;

    @FXML private TextField txtNom;
    @FXML private TextArea txtDescription;
    @FXML private TextField txtPrix;
    @FXML private TextField txtPays;   // Correspond à la colonne 'region' de ta BDD
    @FXML private TextField txtRegion; // Champ supplémentaire (non stocké dans la BDD actuelle si pas de colonne)
    @FXML private TextField txtImage;

    private PlatDAO platDAO = new PlatDAO();

    // --- 2. INITIALISATION ---
    @FXML
    public void initialize() {
        // Liaison des colonnes du tableau aux attributs de la classe Plat
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colPays.setCellValueFactory(new PropertyValueFactory<>("pays"));
        colRegion.setCellValueFactory(new PropertyValueFactory<>("region"));

        loadPlats();
    }

    private void loadPlats() {
        tablePlats.setItems(platDAO.getAllPlats());
    }

    // --- 3. ACTIONS DES BOUTONS ---

    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            String nom = txtNom.getText();
            String desc = txtDescription.getText();
            double prix = Double.parseDouble(txtPrix.getText());
            String pays = txtPays.getText();
            String region = txtRegion.getText();
            String image = txtImage.getText();

            if (nom.isEmpty() || pays.isEmpty()) {
                showAlert("Erreur", "Le nom et le pays sont obligatoires.");
                return;
            }

            // Note : Dans ta BDD, 'region' stocke le pays. On passe donc 'pays' dans le champ pays de l'objet.
            Plat nouveauPlat = new Plat(0, nom, desc, prix, image, pays, region);
            platDAO.addPlat(nouveauPlat);

            loadPlats(); // Rafraîchir le tableau
            clearForm(); // Vider les champs
            showAlert("Succès", "Plat ajouté !");

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un chiffre (ex: 12.50).");
        }
    }

    @FXML
    private void handleSupprimer(ActionEvent event) {
        Plat selected = tablePlats.getSelectionModel().getSelectedItem();
        if (selected != null) {
            platDAO.deletePlat(selected.getId());
            loadPlats();
        } else {
            showAlert("Info", "Sélectionnez une ligne à supprimer.");
        }
    }

    // --- 4. NAVIGATION (C'est ici que tu avais l'erreur) ---

    @FXML
    private void goToUsers(ActionEvent event) {
        try {
            // Assure-toi d'avoir créé AdminUsersView.fxml (même vide) sinon ça plantera ici
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/afrofoodapp/AdminUsersView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la page Utilisateurs.");
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UserSession.getInstance().logout();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/afrofoodapp/LoginView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- 5. UTILITAIRES ---

    private void clearForm() {
        txtNom.clear();
        txtDescription.clear();
        txtPrix.clear();
        txtPays.clear();
        txtRegion.clear();
        txtImage.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}