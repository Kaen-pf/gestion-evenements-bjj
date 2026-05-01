package com.bjj.controller;

import com.bjj.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import java.sql.*;

public class DashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label nbCompetiteurs;
    @FXML private Label nbEvenements;
    @FXML private Label nbInscriptions;
    @FXML private Label nbSports;
    @FXML private VBox listeEvenements;
    @FXML private VBox listeTopCompetiteurs;
    @FXML private Label labelAucunEv;
    @FXML private Label labelAucunTop;

    public void setPrenom(String prenom) {
        welcomeLabel.setText("Bonjour " + prenom + " !");
        chargerStatistiques();
        chargerProchainEvenements();
        chargerTopCompetiteurs();
    }

    private void chargerStatistiques() {
        try {
            Connection conn = DatabaseConnection.getConnection();

            ResultSet rs1 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM competiteur");
            if (rs1.next()) nbCompetiteurs.setText(String.valueOf(rs1.getInt(1)));

            ResultSet rs2 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM evenement");
            if (rs2.next()) nbEvenements.setText(String.valueOf(rs2.getInt(1)));

            ResultSet rs3 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM inscription");
            if (rs3.next()) nbInscriptions.setText(String.valueOf(rs3.getInt(1)));

            ResultSet rs4 = conn.createStatement().executeQuery("SELECT COUNT(*) FROM sport");
            if (rs4.next()) nbSports.setText(String.valueOf(rs4.getInt(1)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void chargerProchainEvenements() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT nom, DATE_FORMAT(date_evenement, '%d-%m-%Y'), lieu " +
                         "FROM evenement WHERE date_evenement >= CURDATE() " +
                         "ORDER BY date_evenement ASC LIMIT 5";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            listeEvenements.getChildren().clear();
            boolean hasData = false;
            int index = 0;

            while (rs.next()) {
                hasData = true;
                String nom = rs.getString(1);
                String date = rs.getString(2);
                String lieu = rs.getString(3);
                String bgColor = index % 2 == 0 ? "#ffffff" : "#f8f9fa";

                HBox ligne = new HBox();
                ligne.setStyle("-fx-padding: 14 20; -fx-background-color: " + bgColor +
                               "; -fx-border-color: transparent transparent #ecf0f1 transparent;");

                Label barre = new Label();
                barre.setStyle("-fx-background-color: #2a5298; -fx-min-width: 4; -fx-max-width: 4; " +
                               "-fx-min-height: 40; -fx-background-radius: 2;");

                VBox infos = new VBox(3);
                infos.setStyle("-fx-padding: 0 0 0 12;");
                HBox.setHgrow(infos, Priority.ALWAYS);

                Label labelNom = new Label(nom);
                labelNom.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

                Label labelDetails = new Label(date + "  |  " + (lieu != null ? lieu : "N/A"));
                labelDetails.setStyle("-fx-font-size: 11px; -fx-text-fill: #95a5a6;");

                infos.getChildren().addAll(labelNom, labelDetails);
                ligne.getChildren().addAll(barre, infos);
                listeEvenements.getChildren().add(ligne);
                index++;
            }

            labelAucunEv.setVisible(!hasData);
            listeEvenements.setVisible(hasData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void chargerTopCompetiteurs() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT CONCAT(c.prenom, ' ', c.nom), COUNT(cb.id) as victoires " +
                         "FROM competiteur c " +
                         "JOIN combat cb ON c.id = cb.id_competiteur1 " +
                         "WHERE cb.resultat LIKE '%Competiteur 1%' " +
                         "GROUP BY c.id ORDER BY victoires DESC LIMIT 5";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            listeTopCompetiteurs.getChildren().clear();
            boolean hasData = false;
            int pos = 1;

            while (rs.next()) {
                hasData = true;
                String nom = rs.getString(1);
                int victoires = rs.getInt(2);
                String bgColor = pos % 2 == 0 ? "#f8f9fa" : "#ffffff";
                String medailleColor = pos == 1 ? "#f39c12" : pos == 2 ? "#95a5a6" : pos == 3 ? "#e67e22" : "#2a5298";
                String medailleText = pos == 1 ? "1er" : pos == 2 ? "2eme" : pos == 3 ? "3eme" : String.valueOf(pos);

                HBox ligne = new HBox(15);
                ligne.setStyle("-fx-padding: 14 20; -fx-background-color: " + bgColor +
                               "; -fx-border-color: transparent transparent #ecf0f1 transparent; " +
                               "-fx-alignment: CENTER_LEFT;");

                Label badge = new Label(medailleText);
                badge.setStyle("-fx-background-color: " + medailleColor +
                               "; -fx-text-fill: white; -fx-font-size: 11px; -fx-font-weight: bold; " +
                               "-fx-padding: 4 8; -fx-background-radius: 6; -fx-min-width: 45; -fx-alignment: CENTER;");

                Label labelNom = new Label(nom);
                labelNom.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
                HBox.setHgrow(labelNom, Priority.ALWAYS);

                Label labelVictoires = new Label(victoires + " victoire" + (victoires > 1 ? "s" : ""));
                labelVictoires.setStyle("-fx-font-size: 12px; -fx-text-fill: #27ae60; -fx-font-weight: bold;");

                ligne.getChildren().addAll(badge, labelNom, labelVictoires);
                listeTopCompetiteurs.getChildren().add(ligne);
                pos++;
            }

            labelAucunTop.setVisible(!hasData);
            listeTopCompetiteurs.setVisible(hasData);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}