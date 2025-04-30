package com.example.spectacleapp.dtos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SpectacleDTO {
    @SerializedName("id")
    private int id;
    private String titre;
    private String description;
    private String imageUrl;
    private List<SpectacleDateLieuDTO> datesLieux;
    private List<RubriqueDTO> rubriques; // Ajoutez cette ligne

    private String categorie;

    // Getters and Setters

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<SpectacleDateLieuDTO> getDatesLieux() { return datesLieux; }
    public void setDatesLieux(List<SpectacleDateLieuDTO> datesLieux) { this.datesLieux = datesLieux; }

    // Ajoutez ces getters/setters pour rubriques
    public List<RubriqueDTO> getRubriques() { return rubriques; }
    public void setRubriques(List<RubriqueDTO> rubriques) { this.rubriques = rubriques; }
}