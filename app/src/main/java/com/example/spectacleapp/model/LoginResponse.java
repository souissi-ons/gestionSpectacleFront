package com.example.spectacleapp.model;

public class LoginResponse {
    private String token;
    private String email;
    private long id;

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(int id) {
        this.id = id;
    }
}