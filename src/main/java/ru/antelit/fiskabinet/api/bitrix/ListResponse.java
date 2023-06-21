package ru.antelit.fiskabinet.api.bitrix;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties({"total", "time"})
public class ListResponse<T> {

    @JsonProperty("result")
    private List<T> result;
    @JsonProperty("name")
    private Integer next;
    @JsonProperty("next")
    private Integer start;
}
