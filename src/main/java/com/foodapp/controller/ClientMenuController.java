package com.foodapp.controller;

import com.monrestaurant.dao.PlatDAO;
import com.monrestaurant.model.Plat;
import com.monrestaurant.utils.UserSession;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ClientMenuController {
    @FXML private GridPane productGrid; // Le conteneur des plats
    @FXML private Button btnPanier;

    private PlatDAO platDAO = new PlatDAO();

    @FXML
    public void initialize() {
        refreshPanierButton();
        loadPlats();
    }

    private void loadPlats() {
        List<Plat> plats = platDAO.getAllPlats();
        int column = 0;
        int row = 1;

        for (Plat plat : plats) {
            VBox card = createProductCard(plat);
            productGrid.add(card, column, row);
            column++;
            if (column == 3) { // 3 colonnes max
                column = 0;
                row++;
            }
        }
    }

    private VBox createProductCard(Plat plat) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: #ddd; -fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 0);");

        // Tente de charger l'image, sinon met un placeholder
        ImageView imageView = new ImageView();
        try {
            // Assure-toi d'avoir une image par défaut ou les vraies images dans resources
            // String imagePath = "/images/" + plat.getImagePath();
            // imageView.setImage(new Image(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) { }
        imageView.setFitHeight(120);
        imageView.setFitWidth(180);

        Label lblNom = new Label(plat.getNom());
        lblNom.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label lblPrix = new Label(String.format("%.2f €", plat.getPrix()));
        lblPrix.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");

        Button btnAdd = new Button("Ajouter au panier");
        btnAdd.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        btnAdd.setOnAction(e -> {
            UserSession.getInstance().addToCart(plat, 1);
            refreshPanierButton();
        });

        box.getChildren().addAll(imageView, lblNom, lblPrix, btnAdd);
        return box;
    }

    @FXML
    private void goToPanier() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/CartView.fxml"));
            Stage stage = (Stage) productGrid.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshPanierButton() {
        int count = UserSession.getInstance().getCart().values().stream().mapToInt(Integer::intValue).sum();
        btnPanier.setText("Panier (" + count + ")");
    }
}
