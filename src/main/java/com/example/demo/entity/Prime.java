package com.example.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "prime")
public class Prime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fonction;
    private Double etranger;
    private Double interieur;

    // --- Getters et Setters ---
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFonction() {
        return fonction;
    }
    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public Double getEtranger() {
        return etranger;
    }
    public void setEtranger(Double etranger) {
        this.etranger = etranger;
    }

    public Double getInterieur() {
        return interieur;
    }
    public void setInterieur(Double interieur) {
        this.interieur = interieur;
    }
}
