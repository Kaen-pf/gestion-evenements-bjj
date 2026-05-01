package com.bjj.controller;

import com.bjj.database.DatabaseConnection;
import com.bjj.model.Competiteur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;

public class ParticipantController {

    @FXML private TableView<Competiteur> tableParticipants;
    @FXML private TableColumn<Competiteur, Integer> colId;
    @FXML private TableColumn<Competiteur, String> colNom;
    @FXML private TableColumn<Competiteur, String> colPrenom;
    @FXML private TableColumn<Competiteur, Double> colPoids;
    @FXML private TableColumn<Competiteur, String> colCeinture;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField poidsField;
    @FXML private ComboBox<String> ceinturCombo;
    @FXML private Label messageLabel;

    private ObservableList<Competiteur> listeParticipants = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colPoids.setCellValueFactory(new PropertyValueFactory<>("poids"));
        colCeinture.setCellValueFactory(new PropertyValueFactory<>("ceinture"));
        tableParticipants.setItems(listeParticipants);

        ceinturCombo.setItems(FXCollections.observableArrayList(
            "Blanche", "Grise", "Jaune", "Orange", "Verte",
            "Bleue", "Violette", "Marron", "Noire"
        ));

        tableParticipants.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    nomField.setText(newVal.getNom());
                    prenomField.setText(newVal.getPrenom());
                    poidsField.setText(String.valueOf(newVal.getPoids()));
                    ceinturCombo.setValue(newVal.getCeinture());
                }
            }
        );

        chargerParticipants();
    }

    @FXML
    public void chargerParticipants() {
        listeParticipants.clear();
        try {
            Connection conn = DatabaseConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT * FROM competiteur ORDER BY nom, prenom"
            );
            while (rs.next()) {
                listeParticipants.add(new Competiteur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getDouble("poids"),
                    rs.getString("ceinture"),
                    rs.getInt("id_club")
                ));
            }
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void ajouterParticipant() {
        if (!validerFormulaire()) return;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO competiteur (nom, prenom, poids, ceinture) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomField.getText().trim());
            stmt.setString(2, prenomField.getText().trim());
            stmt.setDouble(3, Double.parseDouble(poidsField.getText().trim()));
            stmt.setString(4, ceinturCombo.getValue());
            stmt.executeUpdate();
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Participant ajoute avec succes !");
            viderFormulaire();
            chargerParticipants();
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void modifierParticipant() {
        Competiteur selected = tableParticipants.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Selectionnez un participant a modifier !");
            return;
        }
        if (!validerFormulaire()) return;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE competiteur SET nom=?, prenom=?, poids=?, ceinture=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomField.getText().trim());
            stmt.setString(2, prenomField.getText().trim());
            stmt.setDouble(3, Double.parseDouble(poidsField.getText().trim()));
            stmt.setString(4, ceinturCombo.getValue());
            stmt.setInt(5, selected.getId());
            stmt.executeUpdate();
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Participant modifie avec succes !");
            viderFormulaire();
            chargerParticipants();
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void supprimerParticipant() {
        Competiteur selected = tableParticipants.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Selectionnez un participant a supprimer !");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de suppression");
        confirm.setHeaderText("Supprimer : " + selected.getPrenom() + " " + selected.getNom());
        confirm.setContentText("Cette action est irreversible. Confirmer ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM competiteur WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, selected.getId());
                    stmt.executeUpdate();
                    messageLabel.setStyle("-fx-text-fill: green;");
                    messageLabel.setText("Participant supprime avec succes !");
                    viderFormulaire();
                    chargerParticipants();
                } catch (SQLException e) {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("Erreur : " + e.getMessage());
                }
            }
        });
    }

    private boolean validerFormulaire() {
        if (nomField.getText().trim().isEmpty() ||
            prenomField.getText().trim().isEmpty() ||
            poidsField.getText().trim().isEmpty() ||
            ceinturCombo.getValue() == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Tous les champs sont obligatoires !");
            return false;
        }
        try {
            Double.parseDouble(poidsField.getText().trim());
        } catch (NumberFormatException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Le poids doit etre un nombre !");
            return false;
        }
        return true;
    }

    private void viderFormulaire() {
        nomField.clear();
        prenomField.clear();
        poidsField.clear();
        ceinturCombo.setValue(null);
    }
}