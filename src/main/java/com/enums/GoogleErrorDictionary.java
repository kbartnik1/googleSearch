package com.enums;

public enum GoogleErrorDictionary {
    NO_RESULTS_FOUND("Nie znaleziono żadnych wyników wyszukiwania"),
    INSTEAD_SEARCH_FOR("Zamiast tego wyszukaj"),
    SHOWING_RESULTS_FOR("Wyświetlam wyniki");

    public String message;

    GoogleErrorDictionary(String message) {
        this.message = message;
    }

    public String getTextError() {
        return message;
    }

}
