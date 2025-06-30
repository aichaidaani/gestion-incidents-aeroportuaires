package com.aero.model;

import jakarta.persistence.*;

import javafx.beans.property.*;

@Entity
@Table(name = "incident")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
    private String gravite;
    private String localisation;
    private String statut;
    private String assignedTo;
    private String rapport;

    // 🔒 Transient = ignoré par Hibernate
    private transient final IntegerProperty idProperty = new SimpleIntegerProperty();
    private transient final StringProperty descriptionProperty = new SimpleStringProperty();
    private transient final StringProperty graviteProperty = new SimpleStringProperty();
    private transient final StringProperty localisationProperty = new SimpleStringProperty();
    private transient final StringProperty statutProperty = new SimpleStringProperty();
    private transient final StringProperty assignedToProperty = new SimpleStringProperty();
    private transient final StringProperty rapportProperty = new SimpleStringProperty();

    // === Constructeurs ===
    public Incident() {}

    public Incident(String description, String gravite, String localisation, String statut) {
        this.description = description;
        this.gravite = gravite;
        this.localisation = localisation;
        this.statut = statut;

        this.descriptionProperty.set(description);
        this.graviteProperty.set(gravite);
        this.localisationProperty.set(localisation);
        this.statutProperty.set(statut);
    }

    // === Hibernate : Getters/Setters ===

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.idProperty.set(id);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.descriptionProperty.set(description);
    }

    public String getGravite() {
        return gravite;
    }

    public void setGravite(String gravite) {
        this.gravite = gravite;
        this.graviteProperty.set(gravite);
    }

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
        this.localisationProperty.set(localisation);
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
        this.statutProperty.set(statut);
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
        this.assignedToProperty.set(assignedTo);
    }

    public String getRapport() {
        return rapport;
    }

    public void setRapport(String rapport) {
        this.rapport = rapport;
        this.rapportProperty.set(rapport);
    }

    // === JavaFX Property Getters ===

    public IntegerProperty idProperty() {
        return idProperty;
    }

    public StringProperty descriptionProperty() {
        return descriptionProperty;
    }

    public StringProperty graviteProperty() {
        return graviteProperty;
    }

    public StringProperty localisationProperty() {
        return localisationProperty;
    }

    public StringProperty statutProperty() {
        return statutProperty;
    }

    public StringProperty assignedToProperty() {
        return assignedToProperty;
    }

    public StringProperty rapportProperty() {
        return rapportProperty;
    }
}



