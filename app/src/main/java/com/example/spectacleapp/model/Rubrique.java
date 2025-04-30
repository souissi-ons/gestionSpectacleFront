package com.example.spectacleapp.model;

public class Rubrique {
    private int idRub;
    private int idSpec;
    private int idArt;
    private double hdebutR;
    private double dureeRub;
    private String type;
    private Artiste artiste;
    private Spectacle spectacle;

    public Rubrique(int idRub, int idSpec, int idArt, double hdebutR, double dureeRub, String type) {
        this.idRub = idRub;
        this.idSpec = idSpec;
        this.idArt = idArt;
        this.hdebutR = hdebutR;
        this.dureeRub = dureeRub;
        this.type = type;
    }
    public int getIdRub() {
        return idRub;
    }

    public void setIdRub(int idRub) {
        this.idRub = idRub;
    }

    public int getIdSpec() {
        return idSpec;
    }

    public void setIdSpec(int idSpec) {
        this.idSpec = idSpec;
    }

    public int getIdArt() {
        return idArt;
    }

    public void setIdArt(int idArt) {
        this.idArt = idArt;
    }

    public double getHDebutR() {
        return hdebutR;
    }

    public void setHDebutR(double hdebutR) {
        this.hdebutR = hdebutR;
    }

    public double getDureeRub() {
        return dureeRub;
    }

    public void setDureeRub(double dureeRub) {
        this.dureeRub = dureeRub;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Artiste getArtiste() {
        return artiste;
    }

    public void setArtiste(Artiste artiste) {
        this.artiste = artiste;
    }

    public Spectacle getSpectacle() {
        return spectacle;
    }

    public void setSpectacle(Spectacle spectacle) {
        this.spectacle = spectacle;
    }

}