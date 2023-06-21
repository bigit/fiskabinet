package ru.antelit.fiskabinet.api.bitrix.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyDto {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("TITLE")
    private String title;

    @JsonProperty("ASSIGNED_BY_ID")
    private Integer assignedById;

//    @JsonProperty("UF_CRM_1506421566")
//    private String ofd;

    @JsonProperty("UF_CRM_1506422595")
    private List<String> loginPassword;

    @JsonProperty("UF_CRM_1524819776")
    private List<String> serialNumbers;

    @JsonProperty("UF_CRM_1565683449215")
    private String kabinets;

    @JsonProperty("UF_CRM_1596523038359")
    private List<String> maintainAddress;

    @JsonProperty("UF_CRM_1597121179")
    private List<String> maintainAddress2;

    @JsonProperty("UF_CRM_1657547855021")
    private List<String> kkt;

    @JsonProperty("UF_CRM_1658205075471")
    private String ofd2;

    @JsonProperty("UF_CRM_1664526798211")
    private String ofd3;
}
