package com.aero.model;

import jakarta.persistence.*;

@Entity
@Table(name = "incident_db")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
    private String gravite;
    private String localisation;
    private String statut;
    private String assignedTo;

    public Incident() {}

    public Incident(String description, String gravite, String localisation, String statut, String assignedTo) {
        this.description = description;
        this.gravite = gravite;
        this.localisation = localisation;
        this.statut = statut;
        this.assignedTo = assignedTo;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public String getGravite() { return gravite; }
    public String getLocalisation() { return localisation; }
    public String getStatut() { return statut; }
    public String getAssignedTo() { return assignedTo; }

    public void setId(int id) { this.id = id; }
    public void setDescription(String description) { this.description = description; }
    public void setGravite(String gravite) { this.gravite = gravite; }
    public void setLocalisation(String localisation) { this.localisation = localisation; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }
}

