package com.aero.chat;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.http.*;
import java.net.URI;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import org.json.*;

/**
 * Chatbot JavaFX utilisant OpenRouter (deepseek/deepseek-r1:free)
 */
public class ChatbotPane extends VBox {

    private final TextArea chatArea = new TextArea();
    private final TextField inputField = new TextField();

    // ⚠️ Remplace cette clé par la tienne
    private final String OPENROUTER_API_KEY = "-------------------------------";

    public ChatbotPane() {
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPromptText("Réponses du bot apparaîtront ici...");

        inputField.setPromptText("Posez votre question...");
        inputField.setOnAction(e -> envoyerMessage());

        Button sendButton = new Button("Ask!");
        sendButton.setOnAction(e -> envoyerMessage());

        HBox inputBox = new HBox(5, inputField, sendButton);

        this.getChildren().addAll(new Label("Free ChatBot"), chatArea, inputBox);
        this.setSpacing(10);
        this.setPadding(new javafx.geometry.Insets(10));
    }

    private void envoyerMessage() {
        String question = inputField.getText().trim();
        if (question.isEmpty()) {
            chatArea.appendText("⚠️ Veuillez entrer une question.\n");
            return;
        }

        chatArea.appendText("Vous : " + question + "\n");
        inputField.clear();

        String reponse = askOpenRouter(question);
        chatArea.appendText("Bot : " + reponse + "\n\n");
    }

    private String askOpenRouter(String userMessage) {
        try {
            // Création du JSON comme dans ton HTML
            String json = new JSONObject()
                .put("model", "deepseek/deepseek-r1:free")
                .put("messages", new JSONArray()
                    .put(new JSONObject()
                        .put("role", "user")
                        .put("content", userMessage)))
                .toString();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://openrouter.ai/api/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + OPENROUTER_API_KEY)
                .header("HTTP-Referer", "https://www.sitename.com")
                .header("X-Title", "SiteName")
                .POST(BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject responseJson = new JSONObject(response.body());
            String content = responseJson
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");

            return content.trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la requête : " + e.getMessage();
        }
    }
}
