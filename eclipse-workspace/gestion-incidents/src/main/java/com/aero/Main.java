package com.aero;

import com.aero.model.Incident;
import com.aero.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== 🛠️ MENU INCIDENTS ===");
            System.out.println("1. Lister tous les incidents");
            System.out.println("2. Rechercher par statut");
            System.out.println("3. Rechercher par équipe");
            System.out.println("4. Rechercher par ID");
            System.out.println("0. Quitter");
            System.out.print("👉 Choix : ");

            int choix = scanner.nextInt();
            scanner.nextLine(); // pour nettoyer le buffer

            switch (choix) {
                case 1:
                    listerTous();
                    break;
                case 2:
                    System.out.print("Statut ? (Signalé / En cours / Résolu) : ");
                    String statut = scanner.nextLine();
                    rechercherParStatut(statut);
                    break;
                case 3:
                    System.out.print("Nom de l'équipe : ");
                    String equipe = scanner.nextLine();
                    rechercherParEquipe(equipe);
                    break;
                case 4:
                    System.out.print("ID de l’incident : ");
                    int id = scanner.nextInt();
                    rechercherParId(id);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("❌ Choix invalide.");
            }
        }

        HibernateUtil.getSessionFactory().close();
        System.out.println("👋 Fin du programme.");
        scanner.close();
    }

    public static void listerTous() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Incident> list = session.createQuery("from Incident", Incident.class).list();
        System.out.println("📋 Liste de tous les incidents :");
        list.forEach(Main::afficherIncident);
        session.close();
    }

    public static void rechercherParStatut(String statut) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Incident> list = session.createQuery("from Incident where statut = :s", Incident.class)
                .setParameter("s", statut)
                .list();
        System.out.println("📋 Incidents avec statut '" + statut + "':");
        list.forEach(Main::afficherIncident);
        session.close();
    }

    public static void rechercherParEquipe(String equipe) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Incident> list = session.createQuery("from Incident where assignedTo = :e", Incident.class)
                .setParameter("e", equipe)
                .list();
        System.out.println("📋 Incidents affectés à '" + equipe + "':");
        list.forEach(Main::afficherIncident);
        session.close();
    }

    public static void rechercherParId(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Incident i = session.get(Incident.class, id);
        if (i != null) {
            System.out.println("📄 Détails de l'incident ID " + id + " :");
            afficherIncident(i);
        } else {
            System.out.println("❌ Aucun incident trouvé avec l'ID " + id);
        }
        session.close();
    }

    public static void afficherIncident(Incident i) {
        System.out.println("🔹 ID: " + i.getId() +
                " | Description: " + i.getDescription() +
                " | Gravité: " + i.getGravite() +
                " | Localisation: " + i.getLocalisation() +
                " | Statut: " + i.getStatut() +
                " | Affecté à: " + i.getAssignedTo());
    }
}
