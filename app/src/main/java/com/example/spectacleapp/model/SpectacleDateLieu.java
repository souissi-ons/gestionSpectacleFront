package com.example.spectacleapp.model;

import java.util.Date;
import java.util.List;

public class SpectacleDateLieu {
    private Long id;
    private Date date;
    private double heureDebut;
    private int prix;
    private int placesDisponibles;
    private Lieu lieu;
    private List<Reservation> reservations;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public double getHeureDebut() { return heureDebut; }
    public void setHeureDebut(double heureDebut) { this.heureDebut = heureDebut; }

    public int getPrix() { return prix; }
    public void setPrix(int prix) { this.prix = prix; }

    public int getPlacesDisponibles() { return placesDisponibles; }
    public void setPlacesDisponibles(int placesDisponibles) { this.placesDisponibles = placesDisponibles; }

    public Lieu getLieu() { return lieu; }
    public void setLieu(Lieu lieu) { this.lieu = lieu; }

    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
}