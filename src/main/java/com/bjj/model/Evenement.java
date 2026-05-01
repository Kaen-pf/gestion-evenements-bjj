package com.bjj.model;

import java.time.LocalDate;

public class Evenement {
    private int id;
    private String nom;
    private LocalDate dateEvenement;
    private String lieu;
    private int idSport;
    private String nomSport;
    private int nbParticipantsMax;

    public Evenement() {}

    public Evenement(int id, String nom, LocalDate dateEvenement, String lieu, int idSport, String nomSport, int nbParticipantsMax) {
        this.id = id;
        this.nom = nom;
        this.dateEvenement = dateEvenement;
        this.lieu = lieu;
        this.idSport = idSport;
        this.nomSport = nomSport;
        this.nbParticipantsMax = nbParticipantsMax;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public LocalDate getDateEvenement() { return dateEvenement; }
    public void setDateEvenement(LocalDate dateEvenement) { this.dateEvenement = dateEvenement; }

    public String getLieu() { return lieu; }
    public void setLieu(String lieu) { this.lieu = lieu; }

    public int getIdSport() { return idSport; }
    public void setIdSport(int idSport) { this.idSport = idSport; }

    public String getNomSport() { return nomSport; }
    public void setNomSport(String nomSport) { this.nomSport = nomSport; }

    public int getNbParticipantsMax() { return nbParticipantsMax; }
    public void setNbParticipantsMax(int nbParticipantsMax) { this.nbParticipantsMax = nbParticipantsMax; }

    @Override
    public String toString() {
        return nom + " - " + lieu + " (" + dateEvenement + ")";
    }
}