package ru.antelit.fiskabinet.api.bitrix.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties({"total", "time"})
public class ListResponse<T> {

    @JsonProperty("result")
    private List<T> result;
    @JsonProperty("next")
    private Integer next;
    @JsonProperty("start")
    private Integer start;
}
