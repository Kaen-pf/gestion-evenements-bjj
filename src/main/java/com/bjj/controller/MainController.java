package com.bjj.controller;

import com.bjj.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class MainController {

    @FXML private Label statusLabel;
    @FXML private Label userLabel;
    @FXML private Label roleBadge;
    @FXML private StackPane contentPane;
    @FXML private Button btnAccueil;
    @FXML private Button btnAdmin;
    @FXML private Button btnSports;
    @FXML private Button btnCompetiteurs;
    @FXML private Button btnEvenements;
    @FXML private Button btnInscriptions;
    @FXML private Button btnResultats;

    private String role;
    private String prenom;
    private Button activeBtn = null;

    private static final String STYLE_ACTIF =
        "-fx-background-color: rgba(255,255,255,0.2); -fx-text-fill: white; " +
        "-fx-font-size: 13px; -fx-padding: 12 18; -fx-cursor: hand; " +
        "-fx-border-color: transparent transparent white transparent; -fx-border-width: 0 0 3 0;";

    private static final String STYLE_INACTIF =
        "-fx-background-color: transparent; -fx-text-fill: white; " +
        "-fx-font-size: 13px; -fx-padding: 12 18; -fx-cursor: hand;";

    @FXML
    public void initialize() {
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            statusLabel.setText("Erreur de connexion");
        }
    }

    public void setUtilisateur(String prenom, String role) {
        this.prenom = prenom;
        this.role = role;
        userLabel.setText(prenom);
        roleBadge.setText(role.toUpperCase());
        roleBadge.setStyle(
            (role.equals("admin") ? "-fx-background-color: #e74c3c;" : "-fx-background-color: #27ae60;") +
            "-fx-text-fill: white; -fx-font-size: 10px; -fx-font-weight: bold; " +
            "-fx-padding: 2 6; -fx-background-radius: 4;"
        );
        statusLabel.setText("Connecte en tant que " + role);

        if (btnAdmin != null) {
            btnAdmin.setVisible(role.equals("admin"));
            btnAdmin.setManaged(role.equals("admin"));
        }

        afficherAccueil();
    }

    private void setActive(Button btn) {
        if (activeBtn != null) activeBtn.setStyle(STYLE_INACTIF);
        btn.setStyle(STYLE_ACTIF);
        activeBtn = btn;
    }

    private void chargerVue(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bjj/" + fxmlFile));
            Node vue = loader.load();
            contentPane.getChildren().setAll(vue);
        } catch (Exception e) {
            statusLabel.setText("Erreur chargement : " + e.getMessage());
        }
    }

    @FXML
    public void afficherAccueil() {
        setActive(btnAccueil);
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bjj/dashboard.fxml"));
            Node vue = loader.load();
            DashboardController dc = loader.getController();
            dc.setPrenom(prenom);
            contentPane.getChildren().setAll(vue);
            statusLabel.setText("Tableau de bord");
        } catch (Exception e) {
            statusLabel.setText("Erreur dashboard : " + e.getMessage());
        }
    }

    @FXML
    private void afficherAdmin() {
        if (!role.equals("admin")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Acces refuse");
            alert.setHeaderText(null);
            alert.setContentText("Reservee aux administrateurs !");
            alert.showAndWait();
            return;
        }
        setActive(btnAdmin);
        chargerVue("utilisateur.fxml");
        statusLabel.setText("Gestion des utilisateurs");
    }

    @FXML
    private void afficherSports() {
        setActive(btnSports);
        chargerVue("sport.fxml");
        statusLabel.setText("Gestion des sports");
    }

    @FXML
    private void afficherCompetiteurs() {
        setActive(btnCompetiteurs);
        chargerVue("participant.fxml");
        statusLabel.setText("Gestion des competiteurs");
    }

    @FXML
    private void afficherEvenements() {
        setActive(btnEvenements);
        chargerVue("evenement.fxml");
        statusLabel.setText("Gestion des evenements");
    }

    @FXML
    private void afficherInscriptions() {
        setActive(btnInscriptions);
        chargerVue("inscription.fxml");
        statusLabel.setText("Gestion des inscriptions");
    }

    @FXML
    private void afficherResultats() {
        setActive(btnResultats);
        chargerVue("resultat.fxml");
        statusLabel.setText("Resultats et classements");
    }

    @FXML
    private void seDeconnecter() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bjj/login.fxml"));
            Scene scene = new Scene(loader.load(), 850, 520);
            scene.getStylesheets().add(getClass().getResource("/com/bjj/style.css").toExternalForm());
            Stage stage = (Stage) contentPane.getScene().getWindow();
            stage.setResizable(false);
            stage.setWidth(850);
            stage.setHeight(520);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("GES - Connexion");
        } catch (Exception e) {
            statusLabel.setText("Erreur deconnexion : " + e.getMessage());
        }
    }
}