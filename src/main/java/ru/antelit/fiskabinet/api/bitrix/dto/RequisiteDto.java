package ru.antelit.fiskabinet.api.bitrix.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RequisiteDto {

    @JsonProperty("RQ_ENITYT_ID")
    private String entityId;

    @JsonProperty("RQ_INN")
    private String inn;
}
