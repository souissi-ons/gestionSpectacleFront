package com.example.spectacleapp.dtos;

import com.google.gson.annotations.SerializedName;

public class LieuDTO {
    @SerializedName("idLieu")
    private Long id;

    @SerializedName("nomLieu")
    private String nom;

    private String adresse;
    private int capacite;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

}