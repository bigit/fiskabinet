package ru.antelit.fiskabinet.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity(name = "organization")
@Table(schema = "org", name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(columnDefinition = "bpchar", length = 12)
    private String inn;

    @OneToMany
    @JoinTable(schema = "org", name = "org_act",
    joinColumns = @JoinColumn(name = "org_id"), inverseJoinColumns = @JoinColumn(name = "act_id"))
    private List<Activity> activities;
}
