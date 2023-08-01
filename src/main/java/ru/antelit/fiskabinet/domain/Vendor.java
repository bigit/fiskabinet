package ru.antelit.fiskabinet.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity(name = "vendor")
@Table(schema = "equip", name = "vendor")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(name = "sn_pattern")
    String serialNumberPattern;
}
