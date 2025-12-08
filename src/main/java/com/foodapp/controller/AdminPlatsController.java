package com.foodapp.controller;

import com.foodapp.dao.PlatDAO;
import com.foodapp.model.Plat;
import com.foodapp.utils.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminPlatsController {
    // Le Tableau
    @FXML private TableView<Plat> tablePlats;
    @FXML private TableColumn<Plat, String> colNom;
    @FXML private TableColumn<Plat, Double> colPrix;
    @FXML private TableColumn<Plat, String> colPays;
    @FXML private TableColumn<Plat, String> colRegion;

    // Le Formulaire
    @FXML private TextField txtNom;
    @FXML private TextField txtPrix;
    @FXML private TextField txtPays;
    @FXML private TextField txtRegion;
    @FXML private TextField txtImage; // Juste le nom du fichier pour l'instant (ex: "mali.png")
    @FXML private TextArea txtDescription;

    private PlatDAO platDAO = new PlatDAO();

    @FXML
    public void initialize() {
        // Configuration des colonnes (doit correspondre aux getters de Plat.java)
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colPays.setCellValueFactory(new PropertyValueFactory<>("pays"));
        colRegion.setCellValueFactory(new PropertyValueFactory<>("region"));

        loadPlats();
    }

    private void loadPlats() {
        // Convertit la List Java en ObservableList pour JavaFX
        tablePlats.setItems(FXCollections.observableArrayList(platDAO.getAllPlats()));
    }

    @FXML
    private void handleAjouter() {
        try {
            String nom = txtNom.getText();
            double prix = Double.parseDouble(txtPrix.getText());
            String pays = txtPays.getText();
            String region = txtRegion.getText();
            String image = txtImage.getText();
            String desc = txtDescription.getText();

            if (nom.isEmpty() || pays.isEmpty()) {
                showAlert("Erreur", "Le nom et le pays sont obligatoires.");
                return;
            }

            // ID à 0 car c'est auto-increment dans la BDD
            Plat nouveauPlat = new Plat(0, nom, desc, prix, image, pays, region);
            platDAO.addPlat(nouveauPlat);

            // Rafraîchir le tableau et vider les champs
            loadPlats();
            clearForm();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide (ex: 12.50).");
        }
    }

    @FXML
    private void handleSupprimer() {
        Plat selection = tablePlats.getSelectionModel().getSelectedItem();
        if (selection != null) {
            platDAO.deletePlat(selection.getId());
            loadPlats();
        } else {
            showAlert("Attention", "Veuillez sélectionner un plat à supprimer.");
        }
    }

    @FXML
    private void handleLogout() {
        UserSession.getInstance().logout();
        try {
            // Attention au chemin : vérifie bien ta structure dossier
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/afrofoodapp/LoginView.fxml"));
            Stage stage = (Stage) tablePlats.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearForm() {
        txtNom.clear();
        txtPrix.clear();
        txtPays.clear();
        txtRegion.clear();
        txtImage.clear();
        txtDescription.clear();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
