package ru.antelit.fiskabinet.domain;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "ofd", schema = "org")
@NamedQueries({
        @NamedQuery(name = "OfdProvider.existsByInn",
                query = "select (count(o) > 0) from OfdProvider o where o.inn = :inn")})
@Getter
@Setter
public class OfdProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String inn;
}
