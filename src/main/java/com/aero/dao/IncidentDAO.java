package com.aero.dao;

import com.aero.model.Incident;
import com.aero.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class IncidentDAO {

    // Ajouter un nouvel incident
    public static void ajouterIncident(Incident incident) {
        if (incident == null) return;

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(incident);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Récupérer tous les incidents (toutes équipes)
    public static List<Incident> getAllIncidents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Incident", Incident.class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // retourne une liste vide en cas d'erreur
        }
    }

    // Récupérer incidents par équipe assignée
    public static List<Incident> getIncidentsByEquipe(String equipe) {
        if (equipe == null || equipe.trim().isEmpty()) {
            return List.of();
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "from Incident where lower(trim(assignedTo)) = :equipe";
            Query<Incident> query = session.createQuery(hql, Incident.class);
            query.setParameter("equipe", equipe.trim().toLowerCase());
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // Compter les incidents par gravité
    public static long compterIncidentsParGravite(String gravite) {
        if (gravite == null || gravite.trim().isEmpty()) {
            return 0;
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                "select count(*) from Incident where lower(gravite) = :gravite", Long.class);
            query.setParameter("gravite", gravite.trim().toLowerCase());
            Long result = query.uniqueResult();
            return result != null ? result : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Supprimer un incident
    public static void supprimerIncident(Incident incident) {
        if (incident == null) return;

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(incident);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    // Modifier un incident
    public static void modifierIncident(Incident incident) {
        if (incident == null) return;

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(incident);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }
}
