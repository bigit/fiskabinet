package ru.antelit.fiskabinet.api.bitrix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDto {

    @JsonProperty("ID")
    private String id;

    @JsonProperty("TITLE")
    private String title;

    @JsonProperty("ASSIGNED_BY_ID")
    private Integer assignedById;

    @JsonProperty("UF_CRM_1506421566")
    private List<String> ofd;

    @JsonProperty("UF_CRM_1506422595")
    private List<String> loginPassword;

    @JsonProperty("UF_CRM_1524819776")
    private List<String> serialNumbers;

    @JsonProperty("UF_CRM_1565683449215")
    private List<String> kabinets;

    @JsonProperty("UF_CRM_1597121179")
    private List<String> maintainAddress2;

    @JsonProperty("UF_CRM_1657547855021")
    private List<String> kkt;

    @JsonProperty("UF_CRM_1658205075471")
    private String ofd2;

    @JsonProperty("UF_CRM_1664526798211")
    private List<String> ofd3;

    private Boolean imported;
}
