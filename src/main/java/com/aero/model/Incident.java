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

    // Propriétés JavaFX (ignorées par Hibernate)
    @Transient
    private final IntegerProperty idProperty = new SimpleIntegerProperty();
    @Transient
    private final StringProperty descriptionProperty = new SimpleStringProperty();
    @Transient
    private final StringProperty graviteProperty = new SimpleStringProperty();
    @Transient
    private final StringProperty localisationProperty = new SimpleStringProperty();
    @Transient
    private final StringProperty statutProperty = new SimpleStringProperty();
    @Transient
    private final StringProperty assignedToProperty = new SimpleStringProperty();
    @Transient
    private final StringProperty rapportProperty = new SimpleStringProperty();

    // === Constructeurs ===
    public Incident() {
        bindProperties();
    }

    public Incident(String description, String gravite, String localisation, String statut) {
        this.description = description;
        this.gravite = gravite;
        this.localisation = localisation;
        this.statut = statut;
        bindProperties();
    }

    // === Liaison JavaFX <-> champs ===
    private void bindProperties() {
        idProperty.addListener((obs, oldVal, newVal) -> this.id = newVal.intValue());
        descriptionProperty.addListener((obs, oldVal, newVal) -> this.description = newVal);
        graviteProperty.addListener((obs, oldVal, newVal) -> this.gravite = newVal);
        localisationProperty.addListener((obs, oldVal, newVal) -> this.localisation = newVal);
        statutProperty.addListener((obs, oldVal, newVal) -> this.statut = newVal);
        assignedToProperty.addListener((obs, oldVal, newVal) -> this.assignedTo = newVal);
        rapportProperty.addListener((obs, oldVal, newVal) -> this.rapport = newVal);
    }

    // === Getters / Setters Hibernate ===
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

    // === Propriétés JavaFX ===
    public IntegerProperty idProperty() { return idProperty; }
    public StringProperty descriptionProperty() { return descriptionProperty; }
    public StringProperty graviteProperty() { return graviteProperty; }
    public StringProperty localisationProperty() { return localisationProperty; }
    public StringProperty statutProperty() { return statutProperty; }
    public StringProperty assignedToProperty() { return assignedToProperty; }
    public StringProperty rapportProperty() { return rapportProperty; }

    // === Méthode pour réinitialiser les propriétés après un chargement Hibernate ===
    @PostLoad
    public void syncPropertiesAfterLoad() {
        idProperty.set(id);
        descriptionProperty.set(description);
        graviteProperty.set(gravite);
        localisationProperty.set(localisation);
        statutProperty.set(statut);
        assignedToProperty.set(assignedTo);
        rapportProperty.set(rapport);
    }
}



