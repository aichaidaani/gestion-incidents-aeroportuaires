package com.aero.gui;

import com.aero.dao.IncidentDAO;
import com.aero.chatbot.TTS;

import com.aero.model.Incident;
import com.aero.chatbot.Chatbot; // Assure-toi d’avoir cette classe

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class IncidentApp extends Application {
    private VBox mainLayout;
    private Scene scene;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        afficherEcranConnexion();
    }

    private void afficherEcranConnexion() {
        primaryStage.setTitle("Connexion");

        Label userLabel = new Label("Utilisateur:");
        TextField userField = new TextField();

        Label passLabel = new Label("Mot de passe:");
        PasswordField passField = new PasswordField();

        Button loginBtn = new Button("Connexion");
        Button quitterBtn = new Button("Quitter");

        VBox loginLayout = new VBox(10, userLabel, userField, passLabel, passField, loginBtn, quitterBtn);
        loginLayout.setPadding(new Insets(20));
        loginLayout.setAlignment(Pos.CENTER);

        scene = new Scene(loginLayout, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();

        loginBtn.setOnAction(e -> {
            String user = userField.getText();
            String pass = passField.getText();

            if (user.equals("superviseur") && pass.equals("admin123")) {
                afficherInterfaceSelonRole("SUPERVISEUR");
            } else if (user.equals("agent") && pass.equals("agent123")) {
                afficherInterfaceSelonRole("AGENT");
            } else if (user.equals("tech") && pass.equals("tech123")) {
                afficherInterfaceSelonRole("TECHNICIEN");
            } else {
                Alert alert = new Alert(AlertType.ERROR, "Utilisateur ou mot de passe incorrect.");
                alert.showAndWait();
            }
        });

        quitterBtn.setOnAction(e -> primaryStage.close());
    }

    private void afficherInterfaceSelonRole(String role) {
        mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(15));

        if (role.equals("AGENT")) {
            mainLayout.getChildren().add(creerFormulaireAjout());
        } else if (role.equals("TECHNICIEN")) {
            mainLayout.getChildren().add(creerTableIncidentsAffectes("Équipe Alpha"));
        } else if (role.equals("SUPERVISEUR")) {
            mainLayout.getChildren().addAll(
                creerFormulaireAjout(),
                creerTableTousIncidents(),
                creerBoutonStatistiques(),
                creerZoneRapport(),     // ✅ Ajout du champ rapport
                creerChatbot()          // ✅ Ajout du chatbot
            );
        }

        scene.setRoot(mainLayout);
    }



    private Node creerFormulaireAjout() {
        TextField desc = new TextField(); desc.setPromptText("Description");
        TextField grav = new TextField(); grav.setPromptText("Gravité");
        TextField loc = new TextField(); loc.setPromptText("Localisation");
        TextField stat = new TextField(); stat.setPromptText("Statut");
        Button add = new Button("Ajouter");

        add.setOnAction(e -> {
            Incident incident = new Incident(
                desc.getText(), grav.getText(), loc.getText(), stat.getText()
            );

            System.out.println("Ajouté : " + incident.getDescription() + ", " + incident.getGravite() + ", " + incident.getLocalisation());

            IncidentDAO.ajouterIncident(incident);

            // ✅ Rafraîchir le tableau s’il est visible
            if (tableTousIncidents != null) {
                tableTousIncidents.getItems().setAll(IncidentDAO.getAllIncidents());
            }

            // Réinitialiser les champs
            desc.clear(); grav.clear(); loc.clear(); stat.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Incident ajouté !");
            alert.showAndWait();
        });

        return new VBox(5, new Label("➕ Ajouter un incident :"), desc, grav, loc, stat, add);
    }


    private Node creerTableTousIncidents() {
        tableTousIncidents = creerTableView(); // 🆕 ici
        tableTousIncidents.getItems().addAll(IncidentDAO.getAllIncidents());
        return new VBox(new Label("📋 Tous les incidents :"), tableTousIncidents);
    }


    private TableView<Incident> creerTableView() {
        TableView<Incident> table = new TableView<>();

        TableColumn<Incident, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Incident, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Incident, String> gravCol = new TableColumn<>("Gravité");
        gravCol.setCellValueFactory(new PropertyValueFactory<>("gravite"));

        TableColumn<Incident, String> locCol = new TableColumn<>("Localisation");
        locCol.setCellValueFactory(new PropertyValueFactory<>("localisation"));

        TableColumn<Incident, String> statutCol = new TableColumn<>("Statut");
        statutCol.setCellValueFactory(new PropertyValueFactory<>("statut"));

        table.getColumns().addAll(idCol, descCol, gravCol, locCol, statutCol);

        return table;
    }


    private Node creerBoutonStatistiques() {
        Button statsBtn = new Button("📊 Voir statistiques");
        statsBtn.setOnAction(e -> {
            long graves = IncidentDAO.compterIncidentsParGravite("Grave");
            long mineurs = IncidentDAO.compterIncidentsParGravite("Mineure");
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Statistiques");
            alert.setHeaderText("Bilan des incidents");
            alert.setContentText("Graves : " + graves + "\nMineurs : " + mineurs);
            alert.show();
        });
        return statsBtn;
    }

    private Node creerZoneRapport() {
        TextArea rapportArea = new TextArea();
        rapportArea.setPromptText("Écrire un rapport ici...");
        rapportArea.setPrefRowCount(5);

        Button envoyerBtn = new Button("Envoyer le rapport");
        envoyerBtn.setOnAction(e -> {
            String rapport = rapportArea.getText().trim();
            if (rapport.isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING, "Le rapport ne peut pas être vide.");
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(AlertType.INFORMATION, "Rapport envoyé avec succès !");
            alert.showAndWait();
            rapportArea.clear();
        });

        return new VBox(5, new Label("📝 Rapport du superviseur :"), rapportArea, envoyerBtn);
    }

    private Node creerChatbot() {
        Label titre = new Label("🤖 Chatbot - Posez votre question");
        TextArea zoneChat = new TextArea();
        zoneChat.setEditable(false);
        zoneChat.setWrapText(true);

        TextField champQuestion = new TextField();
        champQuestion.setPromptText("Ex : Comment déclarer un incident ?");
        Button boutonEnvoyer = new Button("Envoyer");

        boutonEnvoyer.setOnAction(e -> {
            String question = champQuestion.getText();
            zoneChat.appendText("Vous : " + question + "\n");

            String reponse = Chatbot.repondre(question);
            zoneChat.appendText("Bot : " + reponse + "\n");
            TTS.parler(reponse); // 🗣️ Lire la réponse avec la voix


            if (reponse.startsWith("Je recherche")) {
                List<Incident> resultats = IncidentDAO.chercherIncidentsSimilaires(question);
                for (Incident i : resultats) {
                    zoneChat.appendText("Incident similaire : " + i.getDescription() + " (Statut : " + i.getStatut() + ")\n");
                }
            }

            champQuestion.clear();
        });

        VBox layout = new VBox(10, titre, zoneChat, champQuestion, boutonEnvoyer);
        layout.setPadding(new Insets(10));
        return layout;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
