package com.example.android_client;

public class User {
    private int id;
    private String name;
    private String email;
    private String prodi;
    private String fakultas;

    public User(int id, String name, String email, String prodi, String fakultas) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.prodi = prodi;
        this.fakultas = fakultas;
    }

    public User(String name, String email, String prodi, String fakultas) {
        this.name = name;
        this.email = email;
        this.prodi = prodi;
        this.fakultas = fakultas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }
}
