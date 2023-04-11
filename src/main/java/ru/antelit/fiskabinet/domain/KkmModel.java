package ru.antelit.fiskabinet.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "model")
@Table(schema = "org", name = "model")
public class KkmModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column
    private String name;
    @OneToOne
    private Vendor vendor;

    public String getFullName() {
        return vendor.getName() + " " + name;
    }
}
