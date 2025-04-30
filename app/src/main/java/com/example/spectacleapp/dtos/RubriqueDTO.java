package com.example.spectacleapp.dtos;

import com.google.gson.annotations.SerializedName;

public class RubriqueDTO {
    @SerializedName("idRub")
    private Long id;
    private String type;

    @SerializedName("hdebutR") // Doit correspondre exactement au JSON/BDD
    private double hDebutR;

    @SerializedName("dureeRub")
    private double dureeRub; // Vérifiez le nom exact dans le JSON

    @SerializedName("artiste") // Vérifiez la clé exacte
    private ArtisteDTO artiste;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public double getHDebutR() { return hDebutR; }
    public void setHDebutR(double hDebutR) { this.hDebutR = hDebutR; }

    public double getDureeRub() { return dureeRub; }  // Changé de getDuree() à getDureeRub()
    public void setDureeRub(double dureeRub) { this.dureeRub = dureeRub; }  // Changé de setDuree()

    public ArtisteDTO getArtiste() { return artiste; }
    public void setArtiste(ArtisteDTO artiste) { this.artiste = artiste; }
}