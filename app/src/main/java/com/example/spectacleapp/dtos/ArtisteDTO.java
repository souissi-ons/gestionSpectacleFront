package com.example.spectacleapp.dtos;

import com.google.gson.annotations.SerializedName;

public class ArtisteDTO {
    @SerializedName("idArt")
    private long id;

    @SerializedName("nomArt")
    private String nom;

    @SerializedName("prenomArt")
    private String prenom;

    private String specialite;

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }
}