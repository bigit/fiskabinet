package ru.antelit.fiskabinet.api.bitrix.json;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class BitrixBooleanDeserializer extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        return p.getText().equals("Y") ? Boolean.TRUE : Boolean.FALSE;
    }
}
