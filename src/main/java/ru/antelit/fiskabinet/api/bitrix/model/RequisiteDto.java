package ru.antelit.fiskabinet.api.bitrix.model;

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

    @JsonProperty("RQ_NAME")
    private String reqName;

    @JsonProperty("NAME")
    private String name;

    @JsonProperty("RQ_COMPANY_NAME")
    private String companyName;

    @JsonProperty("RQ_COMPANY_FULL_NAME")
    private String companyFullName;
}
