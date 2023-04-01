package ru.antelit.fiskabinet.domain;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity(name = "user")
@Table(schema = "security", name = "users")
@Getter
@Setter
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @Size(min = 5, max = 24, message = "Логин должен быть не менее 5 символов")
    private String username;

    @Column
    @Email (message = "Неверно указана почта")
    @NotBlank(message = "Не указана почта")
    private String email;

    @Column(name = "phonenumber", columnDefinition = "bpchar", length = 11)
    @NotBlank(message = "Не указан номер")
    @Size(min = 11, max = 11, message = "Неверно указан номер")
    private String phone;

    @Column(name = "first_name")
    @NotBlank(message = "Нe указано имя")
    private String firstName;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "last_name")
    @NotBlank(message = "Не указана фамилия")
    private String lastName;

    @NotBlank(message = "Пароль не может быть пустым")
    @Size(min = 8, message = "Минимум 8 символов")
    private String password;

    private boolean enabled;

    /**
     *
     * @return строку содержащую имя полностью (Иванов Иван Иванович)
     */
    @SuppressWarnings("unused")
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
