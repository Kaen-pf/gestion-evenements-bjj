package com.bjj.controller;

import com.bjj.database.DatabaseConnection;
import com.bjj.model.Sport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class SportController {

    @FXML private TableView<Sport> tableSports;
    @FXML private TableColumn<Sport, Integer> colId;
    @FXML private TableColumn<Sport, String> colNom;
    @FXML private TextField nomField;
    @FXML private Label messageLabel;

    private ObservableList<Sport> listeSports = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tableSports.setItems(listeSports);

        tableSports.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    nomField.setText(newVal.getNom());
                }
            }
        );

        chargerSports();
    }

    @FXML
    public void chargerSports() {
        listeSports.clear();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM sport ORDER BY nom";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                listeSports.add(new Sport(rs.getInt("id"), rs.getString("nom")));
            }
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void ajouterSport() {
        String nom = nomField.getText().trim();
        if (nom.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Le nom du sport est obligatoire !");
            return;
        }
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO sport (nom) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nom);
            stmt.executeUpdate();
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Sport ajoute avec succes !");
            nomField.clear();
            chargerSports();
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void modifierSport() {
        Sport selected = tableSports.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Selectionnez un sport a modifier !");
            return;
        }
        String nom = nomField.getText().trim();
        if (nom.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Le nom du sport est obligatoire !");
            return;
        }
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE sport SET nom = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nom);
            stmt.setInt(2, selected.getId());
            stmt.executeUpdate();
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Sport modifie avec succes !");
            nomField.clear();
            chargerSports();
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void supprimerSport() {
        Sport selected = tableSports.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Selectionnez un sport a supprimer !");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de suppression");
        confirm.setHeaderText("Supprimer le sport : " + selected.getNom());
        confirm.setContentText("Cette action est irreversible. Confirmer ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM sport WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, selected.getId());
                    stmt.executeUpdate();
                    messageLabel.setStyle("-fx-text-fill: green;");
                    messageLabel.setText("Sport supprime avec succes !");
                    nomField.clear();
                    chargerSports();
                } catch (SQLException e) {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("Erreur : " + e.getMessage());
                }
            }
        });
    }
}