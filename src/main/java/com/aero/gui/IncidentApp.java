package com.aero.gui;

import com.aero.dao.IncidentDAO;
import com.aero.model.Incident;
import com.aero.chat.ChatbotPane;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class IncidentApp extends Application {
    private Stage primaryStage;
    private Scene scene;
    private VBox mainLayout;

    private TableView<Incident> tableTousIncidents;
    private TableView<Incident> tableTechnicien;
    private String equipeTechnicien;

    // Champs réutilisables pour ajouter/modifier
    private TextField desc = new TextField();
    private TextField grav = new TextField();
    private TextField loc = new TextField();
    private TextField stat = new TextField();
    private ComboBox<String> equipeBox = new ComboBox<>();
    private Button addOrUpdateBtn = new Button("Ajouter");

    private Incident incidentSelectionne = null;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        afficherConnexion();
    }

    // Page de connexion
    private void afficherConnexion() {
        Label userLabel = new Label("Utilisateur:");
        TextField userField = new TextField();

        Label passLabel = new Label("Mot de passe:");
        PasswordField passField = new PasswordField();

        // ComboBox pour choisir l'équipe si technicien
        ComboBox<String> equipeCombo = new ComboBox<>();
        equipeCombo.getItems().setAll("équipe alpha", "équipe beta", "équipe gamma");
        equipeCombo.setPromptText("Sélectionnez votre équipe");
        equipeCombo.setVisible(false);

        userField.textProperty().addListener((obs, oldText, newText) -> {
            equipeCombo.setVisible(newText.equals("tech"));
        });

        Button loginBtn = new Button("Connexion");
        Button quitterBtn = new Button("Quitter");

        VBox layout = new VBox(10, userLabel, userField, passLabel, passField, equipeCombo, loginBtn, quitterBtn);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        scene = new Scene(layout, 450, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connexion");
        primaryStage.show();

        loginBtn.setOnAction(e -> {
            String user = userField.getText().trim();
            String pass = passField.getText().trim();

            if (user.equals("superviseur") && pass.equals("admin123")) {
                afficherInterface("SUPERVISEUR");
            } else if (user.equals("agent") && pass.equals("agent123")) {
                afficherInterface("AGENT");
            } else if (user.equals("tech") && pass.equals("tech123")) {
                String equipe = equipeCombo.getValue();
                if (equipe == null) {
                    new Alert(Alert.AlertType.WARNING, "Sélectionnez votre équipe.").showAndWait();
                    return;
                }
                afficherInterface("TECHNICIEN", equipe);
            } else {
                new Alert(Alert.AlertType.ERROR, "Utilisateur ou mot de passe incorrect.").showAndWait();
            }
        });

        quitterBtn.setOnAction(e -> primaryStage.close());
    }

    // Affichage des interfaces
    private void afficherInterface(String role) {
        afficherInterface(role, null);
    }

    private void afficherInterface(String role, String equipe) {
        mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(15));
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEFA, #B0E0E6);");

        if ("AGENT".equals(role)) {
            mainLayout.getChildren().addAll(
                    creerFormulaireAjout(),
                    creerTableTousIncidents(),
                    creerBoutonsModifierSupprimer(),
                    creerBoutonDeconnexion()
            );
        } else if ("SUPERVISEUR".equals(role)) {
            mainLayout.getChildren().addAll(
                    creerFormulaireAjout(),
                    creerTableTousIncidents(),
                    creerBoutonsModifierSupprimer(),
                    creerStatistiques(),
                    creerRapport(),
                    creerChatbot(),
                    creerBoutonDeconnexion()
            );
        } else if ("TECHNICIEN".equals(role)) {
            this.equipeTechnicien = equipe.toLowerCase();
            mainLayout.getChildren().addAll(
                    creerTableTechnicien(this.equipeTechnicien),
                    creerBoutonDeconnexion()
            );
        }

        scene.setRoot(mainLayout);
    }

    // Formulaire Ajouter/Modifier
    private Node creerFormulaireAjout() {
        desc.setPromptText("Description");
        grav.setPromptText("Gravité");
        loc.setPromptText("Localisation");
        stat.setPromptText("Statut");

        equipeBox.getItems().setAll("équipe alpha", "équipe beta", "équipe gamma");
        equipeBox.setPromptText("Équipe assignée");

        addOrUpdateBtn.setText("Ajouter");
        addOrUpdateBtn.setOnAction(e -> {
            if (equipeBox.getValue() == null) {
                new Alert(Alert.AlertType.WARNING, "Sélectionnez une équipe.").showAndWait();
                return;
            }

            if (incidentSelectionne == null) {
                Incident incident = new Incident(
                        desc.getText(), grav.getText(), loc.getText(), stat.getText()
                );
                incident.setAssignedTo(equipeBox.getValue().toLowerCase());
                IncidentDAO.ajouterIncident(incident);
                new Alert(Alert.AlertType.INFORMATION, "Incident ajouté.").showAndWait();
            } else {
                incidentSelectionne.setDescription(desc.getText());
                incidentSelectionne.setGravite(grav.getText());
                incidentSelectionne.setLocalisation(loc.getText());
                incidentSelectionne.setStatut(stat.getText());
                incidentSelectionne.setAssignedTo(equipeBox.getValue().toLowerCase());
                IncidentDAO.modifierIncident(incidentSelectionne);
                new Alert(Alert.AlertType.INFORMATION, "Incident modifié.").showAndWait();
                incidentSelectionne = null;
                addOrUpdateBtn.setText("Ajouter");
            }

            actualiserTables();
            desc.clear(); grav.clear(); loc.clear(); stat.clear(); equipeBox.setValue(null);
        });

        return new VBox(5, new Label("➕ Ajouter / Modifier un incident :"),
                desc, grav, loc, stat, equipeBox, addOrUpdateBtn);
    }

    // Boutons Modifier/Supprimer
    private Node creerBoutonsModifierSupprimer() {
        Button modifierBtn = new Button("Modifier");
        Button supprimerBtn = new Button("Supprimer");

        modifierBtn.setOnAction(e -> {
            incidentSelectionne = tableTousIncidents.getSelectionModel().getSelectedItem();
            if (incidentSelectionne != null) {
                desc.setText(incidentSelectionne.getDescription());
                grav.setText(incidentSelectionne.getGravite());
                loc.setText(incidentSelectionne.getLocalisation());
                stat.setText(incidentSelectionne.getStatut());
                equipeBox.setValue(incidentSelectionne.getAssignedTo());
                addOrUpdateBtn.setText("Enregistrer");
            }
        });

        supprimerBtn.setOnAction(e -> {
            Incident incident = tableTousIncidents.getSelectionModel().getSelectedItem();
            if (incident != null) {
                IncidentDAO.supprimerIncident(incident);
                actualiserTables();
                new Alert(Alert.AlertType.INFORMATION, "Incident supprimé.").showAndWait();
            }
        });

        return new HBox(10, modifierBtn, supprimerBtn);
    }

    // Actualiser les tables
    private void actualiserTables() {
        if (tableTousIncidents != null) {
            tableTousIncidents.getItems().setAll(IncidentDAO.getAllIncidents());
        }
        if (tableTechnicien != null && equipeTechnicien != null) {
            tableTechnicien.getItems().setAll(IncidentDAO.getIncidentsByEquipe(equipeTechnicien));
        }
    }

    // Table Tous Incidents
    private Node creerTableTousIncidents() {
        tableTousIncidents = creerTableView();
        tableTousIncidents.getItems().setAll(IncidentDAO.getAllIncidents());
        return new VBox(new Label("📋 Tous les incidents :"), tableTousIncidents);
    }

    // Table Technicien
    private Node creerTableTechnicien(String equipe) {
        tableTechnicien = creerTableView();
        tableTechnicien.getItems().setAll(IncidentDAO.getIncidentsByEquipe(equipe));
        return new VBox(new Label("📋 Incidents affectés à : " + equipe), tableTechnicien);
    }

    // Créer TableView générique
    private TableView<Incident> creerTableView() {
        TableView<Incident> table = new TableView<>();

        TableColumn<Incident, Integer> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Incident, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Incident, String> grav = new TableColumn<>("Gravité");
        grav.setCellValueFactory(new PropertyValueFactory<>("gravite"));

        TableColumn<Incident, String> loc = new TableColumn<>("Localisation");
        loc.setCellValueFactory(new PropertyValueFactory<>("localisation"));

        TableColumn<Incident, String> stat = new TableColumn<>("Statut");
        stat.setCellValueFactory(new PropertyValueFactory<>("statut"));

        TableColumn<Incident, String> equipe = new TableColumn<>("Équipe");
        equipe.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));

        table.getColumns().addAll(id, descCol, grav, loc, stat, equipe);
        return table;
    }

    // Statistiques
    private Node creerStatistiques() {
        Button btn = new Button("📊 Statistiques");
        btn.setOnAction(e -> {
            long graves = IncidentDAO.compterIncidentsParGravite("Grave");
            long mineurs = IncidentDAO.compterIncidentsParGravite("Mineure");
            new Alert(Alert.AlertType.INFORMATION,
                    "Graves : " + graves + "\nMineurs : " + mineurs).showAndWait();
        });
        return btn;
    }

    // Rapport
    private Node creerRapport() {
        TextArea area = new TextArea();
        area.setPromptText("Écrire un rapport ici...");

        Button envoyer = new Button("Envoyer");
        envoyer.setOnAction(e -> {
            if (area.getText().trim().isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Rapport vide.").showAndWait();
                return;
            }
            new Alert(Alert.AlertType.INFORMATION, "Rapport envoyé.").showAndWait();
            area.clear();
        });

        return new VBox(5, new Label("📝 Rapport :"), area, envoyer);
    }

    // Chatbot
    private Node creerChatbot() {
        return new ChatbotPane();
    }

    // Bouton de déconnexion
    private Node creerBoutonDeconnexion() {
        Button deconnexionBtn = new Button("Se déconnecter");
        deconnexionBtn.setOnAction(e -> afficherConnexion());
        HBox hbox = new HBox(deconnexionBtn);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setPadding(new Insets(10, 0, 0, 0));
        return hbox;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
