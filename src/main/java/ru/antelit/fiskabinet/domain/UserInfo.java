package ru.antelit.fiskabinet.domain;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity(name = "user")
@Table(schema = "security", name = "users")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String username;

    @Column
    private String email;

    @Column(name = "phonenumber", columnDefinition = "bpchar", length = 11)
    private String phone;

    @Column(name = "first_name")
    private String firstName;
    @Column(name = "father_name")
    private String fatherName;
    @Column(name = "last_name")
    private String lastName;
    private String password;
    private boolean enabled;

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
