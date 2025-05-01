package com.example.spectacleapp.dtos;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ReservationDTO {

    @SerializedName("id")
    private Long id;

    @SerializedName("spectacleId")
    private Long spectacleId;

    @SerializedName("spectacleDateLieuId")
    private Long spectacleDateLieuId;

    @SerializedName("userId")
    private Long userId;

    @SerializedName("nom")
    private String nom;

    @SerializedName("prenom")
    private String prenom;

    @SerializedName("email")
    private String email;

    @SerializedName("telephone")
    private String telephone;

    @SerializedName("nbPlaces")
    private int nbPlaces;

    @SerializedName("paymentInfo")
    private PaymentInfoDTO paymentInfo;

    @SerializedName("dateReservation")
    private String dateReservation;


    @SerializedName("paymentMethod")
    private String paymentMethod;
    @SerializedName("paymentStatus")

    private String paymentStatus;



    public ReservationDTO() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        this.dateReservation = sdf.format(new Date());
    }




    public ReservationDTO(long dateLieuId, int places, String nom, String prenom, String telephone) {
        this.spectacleDateLieuId = dateLieuId;
        this.nbPlaces = places;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone=telephone;
        this.dateReservation = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
    }

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateReservation() {
        return dateReservation;
    }

    public void setDateReservation(String dateReservation) {
        this.dateReservation = dateReservation;
    }

    public Long getSpectacleId() { return spectacleId; }
    public void setSpectacleId(Long spectacleId) { this.spectacleId = spectacleId; }

    public Long getSpectacleDateLieuId() { return spectacleDateLieuId; }
    public void setSpectacleDateLieuId(Long spectacleDateLieuId) {
        this.spectacleDateLieuId = spectacleDateLieuId;
    }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public int getNbPlaces() { return nbPlaces; }
    public void setNbPlaces(int nbPlaces) { this.nbPlaces = nbPlaces; }

    public PaymentInfoDTO getPaymentInfo() { return paymentInfo; }
    public void setPaymentInfo(PaymentInfoDTO paymentInfo) { this.paymentInfo = paymentInfo; }

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