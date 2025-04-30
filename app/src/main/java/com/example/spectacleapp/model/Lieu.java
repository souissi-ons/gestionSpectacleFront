package com.example.spectacleapp.model;

import java.io.Serializable;

public class Lieu implements Serializable {
    private Long idLieu;
    private String nomLieu;
    private String adresse;
    private int capacite;


    // Getters and setters
    public Long getIdLieu() { return idLieu; }
    public void setIdLieu(Long idLieu) { this.idLieu = idLieu; }

    public String getNomLieu() { return nomLieu; }
    public void setNomLieu(String nomLieu) { this.nomLieu = nomLieu; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public int getCapacite() { return capacite; }
    public void setCapacite(int capacite) { this.capacite = capacite; }
    public Lieu(long idLieu, String nomLieu, String adresse, int capacite) {
        this.idLieu = idLieu;
        this.nomLieu = nomLieu;
        this.adresse = adresse;
        this.capacite = capacite;
    }

    public Lieu() {

    }

}
