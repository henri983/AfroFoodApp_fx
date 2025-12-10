package com.foodapp.controller;

import com.foodapp.dao.PlatDAO;
import com.foodapp.model.Plat;
import com.foodapp.utils.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ClientMenuController {

    @FXML private GridPane productGrid; // Le conteneur des plats
    @FXML private Button btnPanier;

    private PlatDAO platDAO = new PlatDAO();

    @FXML
    public void initialize() {
        refreshPanierButton();
        // Par défaut, on charge tout
        loadAllPlats();
    }

    private void loadAllPlats() {
        List<Plat> plats = platDAO.getAllPlats();
        fillGrid(plats);
    }

    public void filterByRegion(String regionName) {
        productGrid.getChildren().clear();
        List<Plat> allPlats = platDAO.getAllPlats();

        List<Plat> filteredPlats = allPlats.stream()
                .filter(p -> (p.getPays() != null && p.getPays().equalsIgnoreCase(regionName)) ||
                        (p.getRegion() != null && p.getRegion().equalsIgnoreCase(regionName)))
                .toList();

        fillGrid(filteredPlats);
    }

    private void fillGrid(List<Plat> plats) {
        int column = 0;
        int row = 1;

        for (Plat plat : plats) {
            VBox card = createProductCard(plat);
            productGrid.add(card, column, row);

            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }

    private VBox createProductCard(Plat plat) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #ddd; -fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0); -fx-background-radius: 8; -fx-border-radius: 8;");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(120);
        imageView.setFitWidth(180);
        imageView.setPreserveRatio(false);

        try {
            // Assurez-vous que le chemin /com/example/... est correct selon votre structure
            String imagePath = "/com/example/afrofoodapp/images/" + plat.getImagePath();
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is != null) {
                imageView.setImage(new Image(is));
            } else {
                System.out.println("Image introuvable : " + imagePath);
            }
        } catch (Exception e) {
            System.out.println("Erreur image : " + e.getMessage());
        }

        Label lblNom = new Label(plat.getNom());
        lblNom.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        lblNom.setWrapText(true);

        Label lblPrix = new Label(String.format("%.2f €", plat.getPrix()));
        lblPrix.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        Button btnAdd = new Button("Ajouter au panier");
        btnAdd.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white; -fx-cursor: hand;");
        btnAdd.setMaxWidth(Double.MAX_VALUE);
        btnAdd.setOnAction(e -> {
            UserSession.getInstance().addToCart(plat, 1);
            refreshPanierButton();
            showAlert("Panier", plat.getNom() + " ajouté !");
        });

        box.getChildren().addAll(imageView, lblNom, lblPrix, btnAdd);
        return box;
    }

    @FXML
    private void goToPanier() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/afrofoodapp/CartView.fxml"));
            Stage stage = (Stage) productGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // C'EST LA BONNE MÉTHODE POUR LE BOUTON RETOUR
    @FXML
    private void goBack() {
        try {
            // On retourne vers la sélection des 5 régions
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/afrofoodapp/RegionSelectionView.fxml"));
            Stage stage = (Stage) productGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Impossible de charger RegionSelectionView.fxml");
        }
    }

    private void refreshPanierButton() {
        int count = UserSession.getInstance().getCart().values().stream().mapToInt(Integer::intValue).sum();
        btnPanier.setText("Panier (" + count + ")");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}