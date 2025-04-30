package com.example.spectacleapp.dtos;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class SpectacleDateLieuDTO {
    private Long id;
    private Date date;

    @SerializedName("heureDebut")
    private double heureDebut;

    private int prix;
    private int capacite;
    @SerializedName("placesDisponibles")
    private int placesDisponibles;

    private LieuDTO lieu;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public double getHeureDebut() { return heureDebut; }
    public void setHeureDebut(double heureDebut) { this.heureDebut = heureDebut; }

    public int getPrix() { return prix; }
    public void setPrix(int prix) { this.prix = prix; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }

    public int getPlacesDisponibles() { return placesDisponibles; }
    public void setPlacesDisponibles(int placesDisponibles) {
        this.placesDisponibles = placesDisponibles;
    }

    public LieuDTO getLieu() { return lieu; }
    public void setLieu(LieuDTO lieu) { this.lieu = lieu; }
}