package ru.antelit.fiskabinet.domain.dto;

import lombok.Data;
import ru.antelit.fiskabinet.domain.Organization;

@Data
public class OrgDto {

    private Integer id;
    private String name;
    private String inn;
    private String url;
    private Boolean fullyImported;
}
