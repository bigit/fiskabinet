package ru.antelit.fiskabinet.api.bitrix;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.antelit.fiskabinet.api.bitrix.enums.Entity;
import ru.antelit.fiskabinet.api.bitrix.enums.Method;
import ru.antelit.fiskabinet.api.bitrix.enums.Scope;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BitrixRequest {

    @JsonIgnore
    private Scope scope;
    @JsonIgnore
    private Entity entity;
    @JsonIgnore
    private Method method;

    @JsonProperty("filter")
    private Map<String, ?> filter;
    @JsonProperty("select")
    private List<String> select;
    @JsonProperty("start")
    private Integer start;

    public String buildRequest() {
        StringJoiner joiner = new StringJoiner(".");
        if (scope != null) {
            joiner.add(scope.value());
        }
        if (entity != null) {
            joiner.add(entity.value());
        }
        if (method != null) {
            joiner.add(method.value());
        }
        return joiner.toString();
    }
}
