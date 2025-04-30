package com.example.spectacleapp.model;


import java.util.List;

public class Spectacle {
    private int idSpec;
    private String titre;
    private String description;
    private String imageUrl;
    private List<SpectacleDateLieu> datesLieux;
    private List<Rubrique> rubriques;
    private String categorie;

    // Constructors
    public Spectacle() {}

    public Spectacle(int idSpec, String titre, String description, String imageUrl) {
        this.idSpec = idSpec;
        this.titre = titre;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Spectacle(int idSpec, String titre, String description, String imageUrl, String categorie ) {
        this.idSpec = idSpec;
        this.titre = titre;
        this.description = description;
        this.imageUrl = imageUrl;
        this.categorie = categorie;
    }

    // Getters and Setters
    public int getIdSpec() { return idSpec; }
    public void setIdSpec(int idSpec) { this.idSpec = idSpec; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public List<SpectacleDateLieu> getDatesLieux() { return datesLieux; }
    public void setDatesLieux(List<SpectacleDateLieu> datesLieux) { this.datesLieux = datesLieux; }

    public List<Rubrique> getRubriques() { return rubriques; }
    public void setRubriques(List<Rubrique> rubriques) { this.rubriques = rubriques; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
}