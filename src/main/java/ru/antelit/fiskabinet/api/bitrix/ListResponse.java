package ru.antelit.fiskabinet.api.bitrix;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties({"total", "time"})
public class ListResponse<T> {

    @JsonProperty
    private List<T> result;
    @JsonProperty
    private Integer next;
    @JsonProperty
    private Integer start;
}
