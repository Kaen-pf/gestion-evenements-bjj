package com.bjj.model;

public class Club {
    private int id;
    private String nom;
    private String ville;
    private String responsable;

    public Club() {}

    public Club(int id, String nom, String ville, String responsable) {
        this.id = id;
        this.nom = nom;
        this.ville = ville;
        this.responsable = responsable;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }

    @Override
    public String toString() {
        return nom + " - " + ville;
    }
}