package ru.antelit.fiskabinet.api.bitrix.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName("fields")
public class TaskDto {

    private String id;

    private String title;

    private Integer responsibleId;

    private String description;

}
