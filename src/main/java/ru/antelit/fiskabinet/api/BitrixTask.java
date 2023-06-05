package ru.antelit.fiskabinet.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@Data
@JsonRootName("fields")
public class BitrixTask {

    private String title;

    private Integer responsibleId;

    private String description;

}
