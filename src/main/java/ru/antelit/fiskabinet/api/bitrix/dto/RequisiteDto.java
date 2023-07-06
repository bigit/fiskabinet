package ru.antelit.fiskabinet.api.bitrix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequisiteDto {

    @JsonProperty("ENTITY_ID")
    private String entityId;

    @JsonProperty("RQ_INN")
    private String inn;
}
