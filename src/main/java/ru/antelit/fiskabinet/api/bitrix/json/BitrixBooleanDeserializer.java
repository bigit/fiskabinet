package ru.antelit.fiskabinet.api.bitrix.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

@SuppressWarnings("unused")
public class BitrixBooleanDeserializer extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext context) throws IOException {
        return p.getText().equals("Y") ? Boolean.TRUE : Boolean.FALSE;
    }
}
