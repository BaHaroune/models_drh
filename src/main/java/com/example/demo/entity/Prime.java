package com.example.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "prime")
public class Prime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fonction;

    @Column(name = "prime_etranger")
    private Integer etranger;

    @Column(name = "prime_interieur")
    private Integer interieur;

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public Integer getEtranger() {
        return etranger;
    }

    public void setEtranger(Integer etranger) {
        this.etranger = etranger;
    }

    public Integer getInterieur() {
        return interieur;
    }

    public void setInterieur(Integer interieur) {
        this.interieur = interieur;
    }
}
