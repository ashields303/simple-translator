package com.translate;

import org.apache.commons.text.StringEscapeUtils;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions; // Make sure to import this

public class TranslateInput {
    
    private Language sourceLanguage;
    private Language targetLanguage;

    private static final Language[] availableLanguages = Language.values();

    private TranslateOptions translateOptions;
    private Translate translateClient;
    
    public TranslateInput(){
        this(Language.ENGLISH, Language.SPANISH);
    }
    
    public TranslateInput(Language sourceln, Language targetln){
        // You may want to use the parameters here instead of hardcoding English and Spanish.
        this.sourceLanguage = sourceln;
        this.targetLanguage = targetln;
        this.translateOptions = TranslateOptions.getDefaultInstance();
        this.translateClient = translateOptions.getService();        
    }

    public String TranslateInput(String input){
        String translatedText = translateClient.translate(input, 
            Translate.TranslateOption.sourceLanguage(sourceLanguage.getCode()), 
            Translate.TranslateOption.targetLanguage(targetLanguage.getCode()))
            .getTranslatedText();
        // Unescape HTML entities so special characters render correctly
        return StringEscapeUtils.unescapeHtml4(translatedText);
    }

    public Language getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(Language sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public Language getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(Language targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public static Language[] getAvailableLanguages() {
        return availableLanguages;
    }
}
