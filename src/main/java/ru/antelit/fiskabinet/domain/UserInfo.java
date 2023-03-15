package ru.antelit.fiskabinet.domain;

import lombok.Data;

@Data
public class UserInfo {
    private String login;
    private String email;
    private String phone;
    private String firstName;
    private String fatherName;
    private String lastName;

}
