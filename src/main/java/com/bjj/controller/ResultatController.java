package com.bjj.controller;

import com.bjj.database.DatabaseConnection;
import com.bjj.model.Competiteur;
import com.bjj.model.Evenement;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class ResultatController {

    @FXML private ComboBox<Evenement> evenementCombo;
    @FXML private TableView<SimpleStringProperty[]> tableResultats;
    @FXML private TableColumn<SimpleStringProperty[], String> colId;
    @FXML private TableColumn<SimpleStringProperty[], String> colCompetiteur1;
    @FXML private TableColumn<SimpleStringProperty[], String> colCompetiteur2;
    @FXML private TableColumn<SimpleStringProperty[], String> colResultat;
    @FXML private ComboBox<Competiteur> competiteur1Combo;
    @FXML private ComboBox<Competiteur> competiteur2Combo;
    @FXML private TextField resultatField;
    @FXML private Label messageLabel;
    @FXML private TableView<SimpleStringProperty[]> tableClassement;
    @FXML private TableColumn<SimpleStringProperty[], String> colPosition;
    @FXML private TableColumn<SimpleStringProperty[], String> colNomClassement;
    @FXML private TableColumn<SimpleStringProperty[], String> colVictoires;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0].get()));
        colCompetiteur1.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1].get()));
        colCompetiteur2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2].get()));
        colResultat.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[3].get()));

        colPosition.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0].get()));
        colNomClassement.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1].get()));
        colVictoires.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2].get()));

        chargerEvenements();
    }

    private void chargerEvenements() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM evenement ORDER BY nom");
            ObservableList<Evenement> liste = FXCollections.observableArrayList();
            while (rs.next()) {
                Evenement ev = new Evenement();
                ev.setId(rs.getInt("id"));
                ev.setNom(rs.getString("nom"));
                liste.add(ev);
            }
            evenementCombo.setItems(liste);
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    private void chargerCompetiteurs(int idEvenement) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT c.* FROM competiteur c " +
                         "JOIN inscription i ON c.id = i.id_competiteur " +
                         "WHERE i.id_evenement = ? ORDER BY c.nom";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEvenement);
            ResultSet rs = stmt.executeQuery();
            ObservableList<Competiteur> liste = FXCollections.observableArrayList();
            while (rs.next()) {
                liste.add(new Competiteur(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getDouble("poids"),
                    rs.getString("ceinture"),
                    rs.getInt("id_club")
                ));
            }
            competiteur1Combo.setItems(liste);
            competiteur2Combo.setItems(liste);
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    public void chargerResultats() {
        if (evenementCombo.getValue() == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Selectionnez un evenement !");
            return;
        }
        int idEvenement = evenementCombo.getValue().getId();
        chargerCompetiteurs(idEvenement);

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT cb.id, " +
                         "CONCAT(c1.prenom, ' ', c1.nom) as comp1, " +
                         "CONCAT(c2.prenom, ' ', c2.nom) as comp2, " +
                         "cb.resultat " +
                         "FROM combat cb " +
                         "JOIN competiteur c1 ON cb.id_competiteur1 = c1.id " +
                         "JOIN competiteur c2 ON cb.id_competiteur2 = c2.id " +
                         "WHERE cb.id_evenement = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEvenement);
            ResultSet rs = stmt.executeQuery();
            ObservableList<SimpleStringProperty[]> liste = FXCollections.observableArrayList();
            while (rs.next()) {
                SimpleStringProperty[] row = {
                    new SimpleStringProperty(String.valueOf(rs.getInt("id"))),
                    new SimpleStringProperty(rs.getString("comp1")),
                    new SimpleStringProperty(rs.getString("comp2")),
                    new SimpleStringProperty(rs.getString("resultat"))
                };
                liste.add(row);
            }
            tableResultats.setItems(liste);
            chargerClassement(idEvenement);
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    private void chargerClassement(int idEvenement) {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT CONCAT(c.prenom, ' ', c.nom) as nom, " +
                         "COUNT(cb.id) as victoires " +
                         "FROM competiteur c " +
                         "JOIN combat cb ON c.id = cb.id_competiteur1 " +
                         "WHERE cb.id_evenement = ? AND cb.resultat LIKE '%Competiteur 1%' " +
                         "GROUP BY c.id ORDER BY victoires DESC";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idEvenement);
            ResultSet rs = stmt.executeQuery();
            ObservableList<SimpleStringProperty[]> liste = FXCollections.observableArrayList();
            int position = 1;
            while (rs.next()) {
                SimpleStringProperty[] row = {
                    new SimpleStringProperty(String.valueOf(position++)),
                    new SimpleStringProperty(rs.getString("nom")),
                    new SimpleStringProperty(String.valueOf(rs.getInt("victoires")))
                };
                liste.add(row);
            }
            tableClassement.setItems(liste);
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur classement : " + e.getMessage());
        }
    }

    @FXML
    private void enregistrerResultat() {
        if (evenementCombo.getValue() == null ||
            competiteur1Combo.getValue() == null ||
            competiteur2Combo.getValue() == null ||
            resultatField.getText().trim().isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Tous les champs sont obligatoires !");
            return;
        }
        if (competiteur1Combo.getValue().getId() == competiteur2Combo.getValue().getId()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Les deux competiteurs doivent etre differents !");
            return;
        }
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO combat (id_competiteur1, id_competiteur2, id_evenement, resultat) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, competiteur1Combo.getValue().getId());
            stmt.setInt(2, competiteur2Combo.getValue().getId());
            stmt.setInt(3, evenementCombo.getValue().getId());
            stmt.setString(4, resultatField.getText().trim());
            stmt.executeUpdate();
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Resultat enregistre avec succes !");
            resultatField.clear();
            chargerResultats();
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void supprimerResultat() {
        var selected = tableResultats.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Selectionnez un resultat a supprimer !");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de suppression");
        confirm.setHeaderText("Supprimer ce resultat ?");
        confirm.setContentText("Cette action est irreversible. Confirmer ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM combat WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(selected[0].get()));
                    stmt.executeUpdate();
                    messageLabel.setStyle("-fx-text-fill: green;");
                    messageLabel.setText("Resultat supprime avec succes !");
                    chargerResultats();
                } catch (SQLException e) {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("Erreur : " + e.getMessage());
                }
            }
        });
    }
}