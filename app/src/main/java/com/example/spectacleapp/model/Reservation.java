package com.example.spectacleapp.model;

import java.util.Date;

public class Reservation {
    private int id;

    private int idSpectacle;
    private int idUser;
    private int nbPlaces;
    private Date dateReservation;

    private String paymentMethod;
    private String paymentStatus;

    // Constructeur aligné avec le backend
    public Reservation(int idSpectacle, int idUser, int nbPlaces) {
        this.idSpectacle = idSpectacle;
        this.idUser = idUser;
        this.nbPlaces = nbPlaces;
        this.dateReservation = new Date();
    }

    public Reservation(int id, int idSpectacle, int idUser, int nbPlaces) {
        this.id = id;
        this.idSpectacle = idSpectacle;
        this.idUser = idUser;
        this.nbPlaces = nbPlaces;
        this.dateReservation = new Date();
    }

    // Getters
    public int getIdSpectacle() { return idSpectacle; }
    public int getIdUser() { return idUser; }
    public int getNbPlaces() { return nbPlaces; }
    public Date getDateReservation() { return dateReservation; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdSpectacle(int idSpectacle) {
        this.idSpectacle = idSpectacle;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void setNbPlaces(int nbPlaces) {
        this.nbPlaces = nbPlaces;
    }

    public void setDateReservation(Date dateReservation) {
        this.dateReservation = dateReservation;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}