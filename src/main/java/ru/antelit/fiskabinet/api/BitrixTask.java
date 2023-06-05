package ru.antelit.fiskabinet.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@JsonRootName("fields")
public class BitrixTask {

    private String title;

    private Integer responsibleId;

    private String description;

}
