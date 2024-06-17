package ru.antelit.fiskabinet.domain;

import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(schema = "equip", name = "vendor")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(name = "sn_pattern")
    String serialNumberPattern;

    @Entity
    @Table(name = "ofd", schema = "org")
    @NamedQueries({
            @NamedQuery(name = "OfdProvider.existsByInn",
                    query = "select (count(o) > 0) from OfdProvider o where o.inn = :inn")})
    @Getter
    @Setter
    public static class OfdProvider {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column
        private String name;

        @Column
        private String inn;
    }
}
