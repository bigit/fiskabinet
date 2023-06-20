package ru.antelit.fiskabinet.api.bitrix.enums;

public enum Entity {

    CATEGORY("category"),
    COMPANY("company"),
    CONTACT("contact"),
    DEAL("deal"),
    INVOICE("invoice"),
    ITEM("item"),
    LEAD("lead"),
    PAY_SYSTEM("paysystem"),
    PERSON_TYPE("persontype"),
    REQUISITE("requisite"),
    QUOTE("quote"),
    TIMELINE("timeline"),
    TASK("task"),
    TYPE("type");

    Entity(String value) {
        this.value = value;
    }

    private String value;

    public String value() {
        return value;
    }

}
