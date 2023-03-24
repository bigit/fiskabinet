package ru.antelit.fiskabinet.domain;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.Data;

@Data
public class UserInfo {
    private Integer id;
    private String username;
    private String email;
    private String phone;
    private String firstName;
    private String fatherName;
    private String lastName;

    /**
     *
     * @return строку содержащую имя полностью (Иванов Иван Иванович)
     */
    public String getFullName() {
        return lastName + firstName + fatherName;
    }

    /**
     * Формирует фамилию и инициалы пользователя
     * @return строку содержащюю фамилию и инициалы (Иванов И.И.)
     */
    public String getShortName() {
        return String.join(" ",
                lastName,
                StringUtils.left(firstName, 1) + ". ",
                StringUtils.left(fatherName, 1) + ".");
    }
}
