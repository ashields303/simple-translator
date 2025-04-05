package com.translate;
import java.io.IOException;

import com.google.cloud.translate.v3.TranslateTextRequest;
import com.google.cloud.translate.v3.TranslateTextResponse;
import com.google.cloud.translate.v3.TranslationServiceClient;

/**
 * UserTranslate class that wraps around Google Cloud Translation API
 * and stores user's language preferences.
 */
public class UserTranslate {
    private String sourceLanguage;
    private String targetLanguage;
    private String projectId;
    private TranslationServiceClient client;

    /**
     * Constructor for UserTranslate.
     * 
     * @param sourceLanguage The source language code (e.g., "en" for English)
     * @param targetLanguage The target language code (e.g., "es" for Spanish)
     * @param projectId The Google Cloud project ID
     * @throws IOException If there's an issue creating the translation client
     */
    public UserTranslate(String sourceLanguage, String targetLanguage, String projectId) throws IOException {
        this.sourceLanguage = sourceLanguage;
        this.targetLanguage = targetLanguage;
        this.projectId = projectId;
        this.client = TranslationServiceClient.create();
    }

    /**
     * Translates the given text from source language to target language.
     * 
     * @param text The text to translate
     * @return The translated text
     */
    public String translate(String text) {
        try {
            String location = "global";
            String parent = String.format("projects/%s/locations/%s", projectId, location);

            TranslateTextRequest request = TranslateTextRequest.newBuilder()
                    .setParent(parent)
                    .setMimeType("text/plain")
                    .setSourceLanguageCode(sourceLanguage)
                    .setTargetLanguageCode(targetLanguage)
                    .addContents(text)
                    .build();

            TranslateTextResponse response = client.translateText(request);
            return response.getTranslations(0).getTranslatedText();
        } catch (Exception e) {
            System.err.println("Error during translation: " + e.getMessage());
            return text; // Return original text on error
        }
    }

    /**
     * Closes the translation client.
     */
    public void close() {
        if (client != null) {
            client.close();
        }
    }

    // Getters and setters
    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }
}