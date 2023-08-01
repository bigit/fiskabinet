package ru.antelit.fiskabinet.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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
