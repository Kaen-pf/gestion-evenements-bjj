package com.bjj.model;

public class Competiteur {
    private int id;
    private String nom;
    private String prenom;
    private double poids;
    private String ceinture;
    private int idClub;

    public Competiteur() {}

    public Competiteur(int id, String nom, String prenom, double poids, String ceinture, int idClub) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.poids = poids;
        this.ceinture = ceinture;
        this.idClub = idClub;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public double getPoids() { return poids; }
    public void setPoids(double poids) { this.poids = poids; }

    public String getCeinture() { return ceinture; }
    public void setCeinture(String ceinture) { this.ceinture = ceinture; }

    public int getIdClub() { return idClub; }
    public void setIdClub(int idClub) { this.idClub = idClub; }

    @Override
    public String toString() {
        return prenom + " " + nom + " (" + ceinture + ")";
    }
}