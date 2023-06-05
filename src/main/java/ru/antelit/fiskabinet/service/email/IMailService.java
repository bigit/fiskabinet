package ru.antelit.fiskabinet.service.email;

import ru.antelit.fiskabinet.domain.UserInfo;

import java.util.UUID;

public interface IMailService {

    UUID send(String email, String recipient, String body);

}
