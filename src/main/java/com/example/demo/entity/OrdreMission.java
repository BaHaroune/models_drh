package com.example.demo.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ordre_mission")
public class OrdreMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero", unique = true)
    private String numero;

    @Column(name = "annee")
    private Integer annee;

    @Column(name = "reference")
    private String reference;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "agent_nom")
    private String agentNom;

    @Column(name = "fonction")
    private String fonction;

    @Column(name = "nationalite")
    private String nationalite;

    @Column(name = "objet")
    private String objet;

    @Column(name = "destination")
    private String destination;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Column(name = "transport")
    private String transport;

    @Column(name = "observations")
    private String observations;

    @Column(name = "duree")
    private Integer duree;

    @Column(name = "montant_jour")
    private String montantJour;

    @Column(name = "montant_total")
    private String montantTotal;

    @Column(name = "type_mission")
    private String typeMission;

    @Column(name = "fait_a")
    private String faitA;

    @Column(name = "itineraire")
    private String itineraire;

    // Constructeurs
    public OrdreMission() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getAgentNom() {
        return agentNom;
    }

    public void setAgentNom(String agentNom) {
        this.agentNom = agentNom;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Integer getDuree() {
        return duree;
    }

    public void setDuree(Integer duree) {
        this.duree = duree;
    }

    public String getMontantJour() {
        return montantJour;
    }

    public void setMontantJour(String montantJour) {
        this.montantJour = montantJour;
    }

    public String getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(String montantTotal) {
        this.montantTotal = montantTotal;
    }

    public String getTypeMission() {
        return typeMission;
    }

    public void setTypeMission(String typeMission) {
        this.typeMission = typeMission;
    }

    public String getFaitA() {
        return faitA;
    }

    public void setFaitA(String faitA) {
        this.faitA = faitA;
    }

    public String getItineraire() {
        return itineraire;
    }

    public void setItineraire(String itineraire) {
        this.itineraire = itineraire;
    }
}