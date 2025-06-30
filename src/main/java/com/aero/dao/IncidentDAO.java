package com.aero.dao;

import com.aero.model.Incident;
import com.aero.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class IncidentDAO {

    public static void ajouterIncident(Incident incident) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(incident);
        tx.commit();
        session.close();
    }

    public static List<Incident> getAllIncidents() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Incident> list = session.createQuery("from Incident", Incident.class).list();
        session.close();
        return list;
    }

    public static List<Incident> rechercherParStatut(String statut) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<Incident> query = session.createQuery("FROM Incident WHERE statut = :s", Incident.class);
        query.setParameter("s", statut);
        List<Incident> list = query.list();
        session.close();
        return list;
    }

    public static List<Incident> getIncidentsByEquipe(String equipe) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<Incident> query = session.createQuery("FROM Incident WHERE assignedTo = :equipe", Incident.class);
        query.setParameter("equipe", equipe);
        List<Incident> list = query.list();
        session.close();
        return list;
    }
    public static List<Incident> chercherIncidentsSimilaires(String motCle) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<Incident> query = session.createQuery(
            "FROM Incident WHERE description LIKE :motCle", Incident.class);
        query.setParameter("motCle", "%" + motCle + "%");
        List<Incident> incidents = query.list();
        session.close();
        return incidents;
    }
    public static List<Incident> chercherIncidentsSimilaires(String question) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        // Extraire un mot-clé : on prend le 1er mot significatif trouvé
        String motCle = extraireMotCle(question.toLowerCase());

        List<Incident> list = session.createQuery(
            "FROM Incident WHERE description LIKE :motcle OR statut LIKE :motcle", Incident.class)
            .setParameter("motcle", "%" + motCle + "%")
            .setMaxResults(5)
            .list();

        session.close();
        return list;
    }

    // Petite méthode pour extraire un mot-clé utile
    private static String extraireMotCle(String question) {
        String[] mots = question.split("\\s+");
        for (String mot : mots) {
            if (mot.length() > 3 && !mot.equals("comment") && !mot.equals("pour")) {
                return mot;
            }
        }
        return "incident";
    }



    public static long compterIncidentsParGravite(String gravite) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query<Long> query = session.createQuery(
            "SELECT COUNT(*) FROM Incident WHERE gravite = :gravite", Long.class);
        query.setParameter("gravite", gravite);
        long count = query.uniqueResult();
        session.close();
        return count;
    }

    // Nouvelle méthode pour mettre à jour le rapport d’un incident
    public static void mettreAJourRapport(int idIncident, String rapport) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            Incident incident = session.get(Incident.class, idIncident);
            if (incident != null) {
                incident.setRapport(rapport);
                session.update(incident);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}

