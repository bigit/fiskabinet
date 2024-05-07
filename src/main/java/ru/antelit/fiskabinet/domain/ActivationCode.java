package ru.antelit.fiskabinet.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Getter
@Setter
@Entity
@Table(schema = "cds", name = "code")
public class ActivationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Length(max = 16)
    private String value;

    @Column
    @Enumerated(EnumType.ORDINAL)
    private CodeStatus status;

    @ManyToOne
    private OfdProvider provider;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    @Column
    private Integer duration;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;
}
