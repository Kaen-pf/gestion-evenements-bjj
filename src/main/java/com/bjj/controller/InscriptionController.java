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

public class InscriptionController {

    @FXML private TableView<SimpleStringProperty[]> tableInscriptions;
    @FXML private TableColumn<SimpleStringProperty[], String> colId;
    @FXML private TableColumn<SimpleStringProperty[], String> colParticipant;
    @FXML private TableColumn<SimpleStringProperty[], String> colEvenement;
    @FXML private TableColumn<SimpleStringProperty[], String> colCategorie;
    @FXML private ComboBox<Competiteur> participantCombo;
    @FXML private ComboBox<Evenement> evenementCombo;
    @FXML private TextField categorieField;
    @FXML private Label messageLabel;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue()[0].get()));
        colParticipant.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue()[1].get()));
        colEvenement.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue()[2].get()));
        colCategorie.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue()[3].get()));

        chargerParticipants();
        chargerEvenements();
        chargerInscriptions();
    }

    private void chargerParticipants() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT * FROM competiteur ORDER BY nom, prenom"
            );
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
            participantCombo.setItems(liste);
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    private void chargerEvenements() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery(
                "SELECT * FROM evenement ORDER BY nom"
            );
            ObservableList<Evenement> liste = FXCollections.observableArrayList();
            while (rs.next()) {
                Evenement ev = new Evenement();
                ev.setId(rs.getInt("id"));
                ev.setNom(rs.getString("nom"));
                ev.setLieu(rs.getString("lieu"));
                ev.setNbParticipantsMax(rs.getInt("nb_participants_max"));
                liste.add(ev);
            }
            evenementCombo.setItems(liste);
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    public void chargerInscriptions() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT i.id, CONCAT(c.prenom, ' ', c.nom) as participant, " +
                         "e.nom as evenement, i.categorie " +
                         "FROM inscription i " +
                         "JOIN competiteur c ON i.id_competiteur = c.id " +
                         "JOIN evenement e ON i.id_evenement = e.id " +
                         "ORDER BY e.nom, c.nom";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            ObservableList<SimpleStringProperty[]> liste = FXCollections.observableArrayList();
            while (rs.next()) {
                SimpleStringProperty[] row = {
                    new SimpleStringProperty(String.valueOf(rs.getInt("id"))),
                    new SimpleStringProperty(rs.getString("participant")),
                    new SimpleStringProperty(rs.getString("evenement")),
                    new SimpleStringProperty(rs.getString("categorie"))
                };
                liste.add(row);
            }
            tableInscriptions.setItems(liste);
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void inscrireParticipant() {
        if (participantCombo.getValue() == null ||
            evenementCombo.getValue() == null ||
            categorieField.getText().trim().isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Tous les champs sont obligatoires !");
            return;
        }

        int idCompetiteur = participantCombo.getValue().getId();
        int idEvenement = evenementCombo.getValue().getId();
        int maxParticipants = evenementCombo.getValue().getNbParticipantsMax();

        try {
            Connection conn = DatabaseConnection.getConnection();

            String checkDoublon = "SELECT COUNT(*) FROM inscription WHERE id_competiteur = ? AND id_evenement = ?";
            PreparedStatement stmtDoublon = conn.prepareStatement(checkDoublon);
            stmtDoublon.setInt(1, idCompetiteur);
            stmtDoublon.setInt(2, idEvenement);
            ResultSet rsDoublon = stmtDoublon.executeQuery();
            rsDoublon.next();
            if (rsDoublon.getInt(1) > 0) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Ce participant est deja inscrit a cet evenement !");
                return;
            }

            String checkMax = "SELECT COUNT(*) FROM inscription WHERE id_evenement = ?";
            PreparedStatement stmtMax = conn.prepareStatement(checkMax);
            stmtMax.setInt(1, idEvenement);
            ResultSet rsMax = stmtMax.executeQuery();
            rsMax.next();
            if (rsMax.getInt(1) >= maxParticipants) {
                messageLabel.setStyle("-fx-text-fill: red;");
                messageLabel.setText("Le nombre maximum de participants est atteint !");
                return;
            }

            String sql = "INSERT INTO inscription (id_competiteur, id_evenement, categorie) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idCompetiteur);
            stmt.setInt(2, idEvenement);
            stmt.setString(3, categorieField.getText().trim());
            stmt.executeUpdate();

            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Participant inscrit avec succes !");
            categorieField.clear();
            participantCombo.setValue(null);
            evenementCombo.setValue(null);
            chargerInscriptions();

        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void desinscrireParticipant() {
        var selected = tableInscriptions.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Selectionnez une inscription a supprimer !");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de desinscription");
        confirm.setHeaderText("Desinscrire : " + selected[1].get());
        confirm.setContentText("Cette action est irreversible. Confirmer ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String sql = "DELETE FROM inscription WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, Integer.parseInt(selected[0].get()));
                    stmt.executeUpdate();
                    messageLabel.setStyle("-fx-text-fill: green;");
                    messageLabel.setText("Participant desincrit avec succes !");
                    chargerInscriptions();
                } catch (SQLException e) {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("Erreur : " + e.getMessage());
                }
            }
        });
    }
}