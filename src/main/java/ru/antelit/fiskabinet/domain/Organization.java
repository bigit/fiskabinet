package ru.antelit.fiskabinet.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import ru.antelit.fiskabinet.validator.InnConstraint;

import java.util.List;

@Getter
@Setter
@Entity
@Table(schema = "org", name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128)
    @NotBlank(message = "Укажите название организации")
    private String name;

    @Column(columnDefinition = "varchar", unique = true)
    @InnConstraint
    @NotBlank(message = "Не указан ИНН")
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

    @Column(name = "fully_imported")
    private boolean fullyImported;
}
