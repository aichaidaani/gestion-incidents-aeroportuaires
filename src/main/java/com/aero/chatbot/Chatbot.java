package com.aero.chatbot;

public class Chatbot {
    public static String repondre(String question) {
        String q = question.toLowerCase();

        if (q.contains("déclarer") || q.contains("déclaration")) {
            return "Pour déclarer un incident, utilisez le formulaire en tant qu’AGENT.";
        } else if (q.contains("résoudre") || q.contains("résolution")) {
            return "Les incidents sont résolus par les TECHNICIENS affectés par le SUPERVISEUR.";
        } else if (q.contains("statistique") || q.contains("rapport")) {
            return "En tant que SUPERVISEUR, cliquez sur le bouton '📊 Voir statistiques'.";
        } else if (q.contains("traiter") || q.contains("technicien")) {
            return "Les techniciens traitent les incidents selon leur affectation.";
        } else if (q.contains("historique") || q.contains("similaire")) {
            return "Je recherche dans les incidents passés...";
        } else {
            return "Je suis désolé, je n’ai pas compris. Pouvez-vous reformuler ?";
        }
    }
}

