package ru.antelit.fiskabinet.ofd;

import lombok.Getter;

@Getter
public enum CodeStatus {

    NEW(0, "Новый"),
    RESERVED(1, "Зарезервирован"),
    USED(2, "Использован"),
    ERROR(3, "Ошибочный");

    CodeStatus(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    private final Integer id;
    private final String description;

    public static CodeStatus getById(Integer id) {
        for(CodeStatus e : values()) {
            if(e.id.equals(id)) return e;
        }
        return ERROR;
    }
}
