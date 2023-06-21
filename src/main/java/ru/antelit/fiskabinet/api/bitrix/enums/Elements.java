package ru.antelit.fiskabinet.api.bitrix.enums;

public enum Elements {

    ITEMS("items"),
    FIELDS("fields");

    Elements(String value) {
        this.value = value;
    }

    private String value;

    public String value() {
        return value;
    }

}
