package com.foodapp.controller;

import com.foodapp.dao.CommandeDAO;
import com.foodapp.model.CartItem;
import com.foodapp.model.Plat;
import com.foodapp.utils.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class CartController {
    @FXML private TableView<CartItem> tableCart;
    @FXML private TableColumn<CartItem, String> colNom;
    @FXML private TableColumn<CartItem, Double> colPrix;
    @FXML private TableColumn<CartItem, Integer> colQte;
    @FXML private TableColumn<CartItem, Double> colTotal;
    @FXML private Label lblTotal;

    private CommandeDAO commandeDAO = new CommandeDAO();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomPlat"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prixUnitaire"));
        colQte.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        loadCartData();
    }

    private void loadCartData() {
        ObservableList<CartItem> items = FXCollections.observableArrayList();
        Map<Plat, Integer> cart = UserSession.getInstance().getCart();
        double total = 0;

        for (Map.Entry<Plat, Integer> entry : cart.entrySet()) {
            items.add(new CartItem(entry.getKey(), entry.getValue()));
            total += entry.getKey().getPrix() * entry.getValue();
        }

        tableCart.setItems(items);
        lblTotal.setText(String.format("Total : %.2f €", total));
    }

    @FXML
    private void handleValider() {
        if (UserSession.getInstance().getCart().isEmpty()) {
            showAlert("Erreur", "Votre panier est vide.");
            return;
        }

        boolean success = commandeDAO.saveOrder(
                UserSession.getInstance().getCurrentUser(),
                UserSession.getInstance().getCart()
        );

        if (success) {
            showAlert("Succès", "Commande validée !");
            UserSession.getInstance().logout(); // Ou juste vider le panier : UserSession.getInstance().getCart().clear();
            goBackToMenu();
        } else {
            showAlert("Erreur", "Impossible de valider la commande.");
        }
    }

    @FXML
    private void goBackToMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ClientMenuView.fxml"));
            Stage stage = (Stage) tableCart.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
