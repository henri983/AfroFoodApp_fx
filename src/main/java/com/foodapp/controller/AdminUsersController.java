package com.foodapp.controller;

import com.foodapp.dao.UserDAO;
import com.foodapp.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminUsersController {
    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, String> colUsername;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colStatus; // Pour afficher "Actif" ou "En attente"

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void initialize() {
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Affichage personnalisé pour le statut (booléen -> texte)
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
            loadUsers(); // Rafraîchir
            showAlert("Succès", "Utilisateur approuvé avec succès !");
        } else {
            showAlert("Erreur", "Sélectionnez un utilisateur.");
        }
    }

    @FXML
    private void handleDelete() {
        User selected = tableUsers.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Confirmation
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cet utilisateur ?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                userDAO.deleteUser(selected.getId());
                loadUsers();
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.show();
    }
}
