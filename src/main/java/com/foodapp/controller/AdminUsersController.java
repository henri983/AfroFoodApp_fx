package com.foodapp.controller;

import com.foodapp.dao.UserDAO;
import com.foodapp.model.User;
import javafx.beans.property.SimpleStringProperty;
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

public class AdminUsersController {

    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colStatus;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Affichage personnalisé : True -> "Approuvé", False -> "En attente"
        colStatus.setCellValueFactory(cellData -> {
            boolean isApprouve = cellData.getValue().isApprouve();
            return new SimpleStringProperty(isApprouve ? "Approuvé" : "En attente");
        });

        loadUsers();
    }

    private void loadUsers() {
        tableUsers.setItems(FXCollections.observableArrayList(userDAO.getAllUsers()));
    }

    @FXML
    private void handleApprove() {
        User selected = tableUsers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (selected.isApprouve()) {
                showAlert("Info", "Cet utilisateur est déjà approuvé.");
                return;
            }
            userDAO.approveUser(selected.getId());
            loadUsers(); // Rafraîchir le tableau
            showAlert("Succès", "Utilisateur approuvé avec succès !");
        } else {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur.");
        }
    }

    @FXML
    private void handleDelete() {
        User selected = tableUsers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer cet utilisateur ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                userDAO.deleteUser(selected.getId());
                loadUsers();
                showAlert("Succès", "Utilisateur supprimé.");
            }
        } else {
            showAlert("Erreur", "Veuillez sélectionner un utilisateur.");
        }
    }

    // --- LA MÉTHODE MANQUANTE POUR LE BOUTON RETOUR ---
    @FXML
    private void goBackToPlats(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/afrofoodapp/AdminPlatsView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Impossible de charger AdminPlatsView.fxml");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}