package ru.antelit.fiskabinet.api.bitrix.enums;

public enum Method {

    GET("get"),
    LIST("list"),
    ADD("add"),
    UPDATE("update"),
    DELETE("delete");

    Method(String value) {
        this.value = value;
    }

    private String value;

    public String value() {
        return value;
    }
}
