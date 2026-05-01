package com.bjj.controller;

import com.bjj.database.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class UtilisateurController {

    @FXML private TableView<ObservableList<String>> tableUtilisateurs;
    @FXML private TableColumn<ObservableList<String>, String> colId;
    @FXML private TableColumn<ObservableList<String>, String> colLogin;
    @FXML private TableColumn<ObservableList<String>, String> colNom;
    @FXML private TableColumn<ObservableList<String>, String> colPrenom;
    @FXML private TableColumn<ObservableList<String>, String> colRole;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private ComboBox<String> roleCombo;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        colLogin.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        colNom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        colPrenom.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
        colRole.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));

        roleCombo.setItems(FXCollections.observableArrayList("admin", "organisateur"));

        tableUtilisateurs.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    loginField.setText(newVal.get(1));
                    nomField.setText(newVal.get(2));
                    prenomField.setText(newVal.get(3));
                    roleCombo.setValue(newVal.get(4));
                    passwordField.clear();
                }
            }
        );

        chargerUtilisateurs();
    }

    @FXML
    public void chargerUtilisateurs() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT id, login, nom, prenom, role FROM utilisateur ORDER BY nom"
            );
            ObservableList<ObservableList<String>> liste = FXCollections.observableArrayList();
            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(String.valueOf(rs.getInt("id")));
                row.add(rs.getString("login"));
                row.add(rs.getString("nom"));
                row.add(rs.getString("prenom"));
                row.add(rs.getString("role"));
                liste.add(row);
            }
            tableUtilisateurs.setItems(liste);
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void ajouterUtilisateur() {
        if (!validerFormulaire(true)) return;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO utilisateur (login, mot_de_passe, nom, prenom, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, loginField.getText().trim());
            stmt.setString(2, passwordField.getText().trim());
            stmt.setString(3, nomField.getText().trim());
            stmt.setString(4, prenomField.getText().trim());
            stmt.setString(5, roleCombo.getValue());
            stmt.executeUpdate();
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Utilisateur ajoute avec succes !");
            viderFormulaire();
            chargerUtilisateurs();
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void modifierUtilisateur() {
        ObservableList<String> selected = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Selectionnez un utilisateur a modifier !");
            return;
        }
        if (!validerFormulaire(false)) return;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql;
            PreparedStatement stmt;
            if (!passwordField.getText().trim().isEmpty()) {
                sql = "UPDATE utilisateur SET login=?, mot_de_passe=?, nom=?, prenom=?, role=? WHERE id=?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, loginField.getText().trim());
                stmt.setString(2, passwordField.getText().trim());
                stmt.setString(3, nomField.getText().trim());
                stmt.setString(4, prenomField.getText().trim());
                stmt.setString(5, roleCombo.getValue());
                stmt.setInt(6, Integer.parseInt(selected.get(0)));
            } else {
                sql = "UPDATE utilisateur SET login=?, nom=?, prenom=?, role=? WHERE id=?";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, loginField.getText().trim());
                stmt.setString(2, nomField.getText().trim());
                stmt.setString(3, prenomField.getText().trim());
                stmt.setString(4, roleCombo.getValue());
                stmt.setInt(5, Integer.parseInt(selected.get(0)));
            }
            stmt.executeUpdate();
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Utilisateur modifie avec succes !");
            viderFormulaire();
            chargerUtilisateurs();
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void supprimerUtilisateur() {
        ObservableList<String> selected = tableUtilisateurs.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Selectionnez un utilisateur a supprimer !");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de suppression");
        confirm.setHeaderText("Supprimer l'utilisateur : " + selected.get(1));
        confirm.setContentText("Cette action est irreversible. Confirmer ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM utilisateur WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(selected.get(0)));
                    stmt.executeUpdate();
                    messageLabel.setStyle("-fx-text-fill: green;");
                    messageLabel.setText("Utilisateur supprime avec succes !");
                    viderFormulaire();
                    chargerUtilisateurs();
                } catch (SQLException e) {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("Erreur : " + e.getMessage());
                }
            }
        });
    }

    private boolean validerFormulaire(boolean nouveauUtilisateur) {
        if (loginField.getText().trim().isEmpty() ||
            nomField.getText().trim().isEmpty() ||
            prenomField.getText().trim().isEmpty() ||
            roleCombo.getValue() == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Tous les champs sont obligatoires !");
            return false;
        }
        if (nouveauUtilisateur && passwordField.getText().trim().isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Le mot de passe est obligatoire !");
            return false;
        }
        return true;
    }

    private void viderFormulaire() {
        loginField.clear();
        passwordField.clear();
        nomField.clear();
        prenomField.clear();
        roleCombo.setValue(null);
    }
}