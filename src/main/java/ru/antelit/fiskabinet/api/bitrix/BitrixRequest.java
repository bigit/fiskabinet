package ru.antelit.fiskabinet.api.bitrix;

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
public class BitrixRequest {

    private Scope scope;
    private Entity entity;
    private Method method;

    private Map<String, ?> filter;
    private List<String> select;

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
