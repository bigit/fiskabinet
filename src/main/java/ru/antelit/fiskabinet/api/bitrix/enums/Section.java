package ru.antelit.fiskabinet.api.bitrix.enums;

public enum Section {


    COMPANY("company"),
    CONTACT("contact"),
    PRODUCT_ROWS("productrows"),
    RECURRING("recurring"),
    STATUS("status"),
    USER_FIELD("userfield");

    Section(String value) {
        this.value = value;
    }

    private final String value;

    public String value() {
        return value;
    }
}
