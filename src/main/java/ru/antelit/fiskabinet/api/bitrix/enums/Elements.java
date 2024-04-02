package ru.antelit.fiskabinet.api.bitrix.enums;

@SuppressWarnings("unused")
public enum Elements {

    ITEMS("items"),
    FIELDS("fields");

    Elements(String value) {
        this.value = value;
    }

    private final String value;

    public String value() {
        return value;
    }

}
