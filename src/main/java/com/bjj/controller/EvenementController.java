package com.bjj.controller;

import com.bjj.database.DatabaseConnection;
import com.bjj.model.Evenement;
import com.bjj.model.Sport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EvenementController {

    @FXML private TableView<Evenement> tableEvenements;
    @FXML private TableColumn<Evenement, Integer> colId;
    @FXML private TableColumn<Evenement, String> colNom;
    @FXML private TableColumn<Evenement, String> colDate;
    @FXML private TableColumn<Evenement, String> colLieu;
    @FXML private TableColumn<Evenement, String> colSport;
    @FXML private TableColumn<Evenement, Integer> colMax;
    @FXML private TextField nomField;
    @FXML private DatePicker datePicker;
    @FXML private TextField lieuField;
    @FXML private ComboBox<Sport> sportCombo;
    @FXML private TextField maxField;
    @FXML private Label messageLabel;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private ObservableList<Evenement> listeEvenements = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("dateEvenement"));
        colLieu.setCellValueFactory(new PropertyValueFactory<>("lieu"));
        colSport.setCellValueFactory(new PropertyValueFactory<>("nomSport"));
        colMax.setCellValueFactory(new PropertyValueFactory<>("nbParticipantsMax"));
        tableEvenements.setItems(listeEvenements);

        // Format JJ-MM-AAAA pour le DatePicker
        datePicker.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? formatter.format(date) : "";
            }
            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
            }
        });

        // Sélection dans le tableau → remplir le formulaire
        tableEvenements.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    nomField.setText(newVal.getNom());
                    datePicker.setValue(newVal.getDateEvenement());
                    lieuField.setText(newVal.getLieu());
                    maxField.setText(String.valueOf(newVal.getNbParticipantsMax()));
                    for (Sport s : sportCombo.getItems()) {
                        if (s.getId() == newVal.getIdSport()) {
                            sportCombo.setValue(s);
                            break;
                        }
                    }
                }
            }
        );

        chargerSports();
        chargerEvenements();
    }

    private void chargerSports() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM sport ORDER BY nom");
            ObservableList<Sport> sports = FXCollections.observableArrayList();
            while (rs.next()) {
                sports.add(new Sport(rs.getInt("id"), rs.getString("nom")));
            }
            sportCombo.setItems(sports);
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur chargement sports : " + e.getMessage());
        }
    }

    @FXML
    public void chargerEvenements() {
        listeEvenements.clear();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT e.*, s.nom as nom_sport FROM evenement e " +
                         "LEFT JOIN sport s ON e.id_sport = s.id ORDER BY e.date_evenement";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                Evenement ev = new Evenement();
                ev.setId(rs.getInt("id"));
                ev.setNom(rs.getString("nom"));
                Date d = rs.getDate("date_evenement");
                ev.setDateEvenement(d != null ? d.toLocalDate() : null);
                ev.setLieu(rs.getString("lieu"));
                ev.setIdSport(rs.getInt("id_sport"));
                ev.setNomSport(rs.getString("nom_sport"));
                ev.setNbParticipantsMax(rs.getInt("nb_participants_max"));
                listeEvenements.add(ev);
            }
        } catch (SQLException e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void ajouterEvenement() {
        if (!validerFormulaire()) return;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO evenement (nom, date_evenement, lieu, id_sport, nb_participants_max) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomField.getText().trim());
            stmt.setDate(2, Date.valueOf(datePicker.getValue()));
            stmt.setString(3, lieuField.getText().trim());
            stmt.setInt(4, sportCombo.getValue().getId());
            stmt.setInt(5, Integer.parseInt(maxField.getText().trim()));
            stmt.executeUpdate();
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Evenement ajoute avec succes !");
            viderFormulaire();
            chargerEvenements();
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void modifierEvenement() {
        Evenement selected = tableEvenements.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Selectionnez un evenement a modifier !");
            return;
        }
        if (!validerFormulaire()) return;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE evenement SET nom=?, date_evenement=?, lieu=?, id_sport=?, nb_participants_max=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nomField.getText().trim());
            stmt.setDate(2, Date.valueOf(datePicker.getValue()));
            stmt.setString(3, lieuField.getText().trim());
            stmt.setInt(4, sportCombo.getValue().getId());
            stmt.setInt(5, Integer.parseInt(maxField.getText().trim()));
            stmt.setInt(6, selected.getId());
            stmt.executeUpdate();
            messageLabel.setStyle("-fx-text-fill: green;");
            messageLabel.setText("Evenement modifie avec succes !");
            viderFormulaire();
            chargerEvenements();
        } catch (Exception e) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Erreur : " + e.getMessage());
        }
    }

    @FXML
    private void supprimerEvenement() {
        Evenement selected = tableEvenements.getSelectionModel().getSelectedItem();
        if (selected == null) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Selectionnez un evenement a supprimer !");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation de suppression");
        confirm.setHeaderText("Supprimer l'evenement : " + selected.getNom());
        confirm.setContentText("Attention ! Toutes les inscriptions et combats lies seront aussi supprimes. Confirmer ?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Connection conn = DatabaseConnection.getConnection();

                    // 1. Supprimer les combats liés
                    PreparedStatement stmt1 = conn.prepareStatement(
                        "DELETE FROM combat WHERE id_evenement = ?");
                    stmt1.setInt(1, selected.getId());
                    stmt1.executeUpdate();

                    // 2. Supprimer les classements liés
                    PreparedStatement stmt2 = conn.prepareStatement(
                        "DELETE FROM classement WHERE id_evenement = ?");
                    stmt2.setInt(1, selected.getId());
                    stmt2.executeUpdate();

                    // 3. Supprimer les inscriptions liées
                    PreparedStatement stmt3 = conn.prepareStatement(
                        "DELETE FROM inscription WHERE id_evenement = ?");
                    stmt3.setInt(1, selected.getId());
                    stmt3.executeUpdate();

                    // 4. Supprimer l'événement
                    PreparedStatement stmt4 = conn.prepareStatement(
                        "DELETE FROM evenement WHERE id = ?");
                    stmt4.setInt(1, selected.getId());
                    stmt4.executeUpdate();

                    messageLabel.setStyle("-fx-text-fill: green;");
                    messageLabel.setText("Evenement supprime avec succes !");
                    viderFormulaire();
                    chargerEvenements();
                } catch (SQLException e) {
                    messageLabel.setStyle("-fx-text-fill: red;");
                    messageLabel.setText("Erreur : " + e.getMessage());
                }
            }
        });
    }

    private boolean validerFormulaire() {
        if (nomField.getText().trim().isEmpty() ||
            datePicker.getValue() == null ||
            lieuField.getText().trim().isEmpty() ||
            sportCombo.getValue() == null ||
            maxField.getText().trim().isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Tous les champs sont obligatoires !");
            return false;
        }
        return true;
    }

    private void viderFormulaire() {
        nomField.clear();
        datePicker.setValue(null);
        lieuField.clear();
        sportCombo.setValue(null);
        maxField.clear();
    }
}