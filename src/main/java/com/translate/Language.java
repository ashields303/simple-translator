package com.translate;

public enum Language {
    ENGLISH("English", "en"),
    SPANISH("Spanish", "es"),
    FRENCH("French", "fr"),
    GERMAN("German", "de");

    private final String fullName;
    private final String code;

    Language(String fullName, String code) {
        this.fullName = fullName;
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return fullName + " (" + code + ")";
    }
}
