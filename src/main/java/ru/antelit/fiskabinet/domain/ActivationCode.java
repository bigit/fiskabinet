package ru.antelit.fiskabinet.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
}
