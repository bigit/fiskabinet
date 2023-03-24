package ru.antelit.fiskabinet.domain;

import lombok.Data;

import java.util.List;

@Data
public class Organization {

    private Integer id;
    private String name;
    private String inn;
    private List<Activity> activities;
}
