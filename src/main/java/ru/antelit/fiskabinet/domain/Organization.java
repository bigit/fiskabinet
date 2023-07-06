package ru.antelit.fiskabinet.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UniqueElements;
import ru.antelit.fiskabinet.validator.InnUniqueConstraint;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Constraint;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@Entity(name = "organization")
@Table(schema = "org", name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128)
    @NotBlank(message = "Укажите название организации")
    private String name;

//    @Column(columnDefinition = "bpchar", length = 12)
//    @Length(min = 10, max = 12, message = "ИНН должен быть 10 или 12 цифр")
//    @InnUniqueConstraint
//    @NotBlank(message = "Не указан ИНН")
    private String inn;

    @OneToMany
    @JoinTable(schema = "org", name = "org_act",
            joinColumns = @JoinColumn(name = "org_id"), inverseJoinColumns = @JoinColumn(name = "act_id"))
    private List<Activity> activities;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserInfo owner;

    @Column(name = "source_id")
    private String sourceId;
}
