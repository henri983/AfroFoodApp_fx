package com.foodapp.controller;

import com.foodapp.utils.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class RegionSelectionController {

    @FXML private Button btnPanier;

    @FXML
    public void initialize() {
        refreshPanierButton();
    }

    // --- Navigation ---

    @FXML
    private void goBack(ActionEvent event) {
        try {
            // Retour vers la page d'accueil (MainView)
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/afrofoodapp/MainView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToPanier(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/afrofoodapp/CartView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshPanierButton() {
        if (btnPanier != null) {
            int count = UserSession.getInstance().getCart().values().stream().mapToInt(Integer::intValue).sum();
            btnPanier.setText("Panier (" + count + ")");
        }
    }

    // --- Sélection des Régions ---

    @FXML
    private void showCameroun(ActionEvent event) { loadMenuRegion(event, "Cameroun"); }

    @FXML
    private void showCentrafrique(ActionEvent event) { loadMenuRegion(event, "Centrafrique"); }

    @FXML
    private void showMali(ActionEvent event) { loadMenuRegion(event, "Mali"); }

    @FXML
    private void showCoteIvoire(ActionEvent event) { loadMenuRegion(event, "Côte d'Ivoire"); }

    @FXML
    private void showSenegal(ActionEvent event) { loadMenuRegion(event, "Sénégal"); }

    private void loadMenuRegion(ActionEvent event, String regionName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/afrofoodapp/ClientMenuView.fxml"));
            Parent root = loader.load();

            ClientMenuController controller = loader.getController();
            controller.filterByRegion(regionName);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}