package ru.antelit.fiskabinet.domain.dto;

import lombok.Data;

@Data
public class ActivationCodeDto {

    private Long id;
    private String value;
    private Integer providerId;
    private Integer status;
    private Integer organizationId;
    private Integer duration;
}
