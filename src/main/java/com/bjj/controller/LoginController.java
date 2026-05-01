package com.bjj.controller;

import com.bjj.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void seConnecter() {
        String login = loginField.getText().trim();
        String motDePasse = passwordField.getText().trim();

        if (login.isEmpty() || motDePasse.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs !");
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM utilisateur WHERE login = ? AND mot_de_passe = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, login);
            stmt.setString(2, motDePasse);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                String prenom = rs.getString("prenom");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/bjj/main.fxml"));
                Scene scene = new Scene(loader.load(), 1100, 700);
                scene.getStylesheets().add(getClass().getResource("/com/bjj/style.css").toExternalForm());

                MainController controller = loader.getController();
                controller.setUtilisateur(prenom, role);

                Stage stage = (Stage) loginField.getScene().getWindow();
                stage.setResizable(true);
                stage.setWidth(1100);
                stage.setHeight(700);
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.setTitle("GES - Gestion des Evenements Sportifs - " + role);

            } else {
                errorLabel.setText("Identifiant ou mot de passe incorrect !");
            }

        } catch (Exception e) {
            errorLabel.setText("Erreur : " + e.getMessage());
        }
    }
}