package ru.antelit.fiskabinet.api.bitrix.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

@SuppressWarnings("unused")
public class BitrixBooleanSerializer extends JsonSerializer<Boolean> {

    @Override
    public void serialize(Boolean value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value ? "Y" : "N");
    }
}
