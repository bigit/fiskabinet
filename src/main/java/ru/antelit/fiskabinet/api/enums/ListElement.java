package ru.antelit.fiskabinet.api.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListElement<T> {

    @JsonProperty("result")
    private List<T> elements;

    @JsonProperty("next")
    private int next = 0;

    @JsonProperty("total")
    private int total = 0;

}
