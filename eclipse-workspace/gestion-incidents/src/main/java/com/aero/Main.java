package com.aero;

import com.aero.model.Incident;
import com.aero.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // ✅ Sprint 2 : tests des fonctionnalités
        insertIncident();                          // Insertion d’un incident
        updateIncident(1, "En cours", "Équipe Alpha");  // Modifier statut et affectation
        listAllIncidents();                        // Afficher tous les incidents

        HibernateUtil.getSessionFactory().close();
    }

    public static void insertIncident() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        // Création d’un incident avec affectation
        Incident incident = new Incident(
            "Fuite carburant",
            "Grave",
            "Piste A",
            "Signalé",
            "Équipe Bravo"
        );

        session.persist(incident);
        session.getTransaction().commit();
        session.close();
        System.out.println("✅ Incident inséré !");
    }

    public static void updateIncident(int id, String newStatut, String nouvelleEquipe) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Incident incident = session.get(Incident.class, id);
        if (incident != null) {
            incident.setStatut(newStatut);
            incident.setAssignedTo(nouvelleEquipe);
            session.update(incident);
            System.out.println("🔄 Incident ID " + id + " mis à jour.");
        } else {
            System.out.println("❌ Incident non trouvé.");
        }

        session.getTransaction().commit();
        session.close();
    }

    public static void listAllIncidents() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Incident> incidents = session.createQuery("from Incident", Incident.class).list();
        System.out.println("\n📋 Liste des incidents :");

        for (Incident i : incidents) {
            System.out.println("🔹 ID: " + i.getId() +
                " | Description: " + i.getDescription() +
                " | Gravité: " + i.getGravite() +
                " | Localisation: " + i.getLocalisation() +
                " | Statut: " + i.getStatut() +
                " | Affecté à: " + i.getAssignedTo());
        }

        session.close();
    }
}
