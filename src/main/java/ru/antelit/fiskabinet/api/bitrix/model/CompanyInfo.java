package ru.antelit.fiskabinet.api.bitrix.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompanyInfo {
    private Integer id;
    private String sourceId;
    private String name;
    private String inn;
    private List<Message> messages = new ArrayList<>();

    public void addMessage(String text, boolean isError) {
        messages.add(new Message(text, isError));
    }
}

@Data
@AllArgsConstructor
class Message{
        private String text;
        private boolean isError;
}
