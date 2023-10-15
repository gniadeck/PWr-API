package dev.wms.pwrapi.utils.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeFromMillisDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        long milliseconds = p.getLongValue();
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds), ZoneOffset.UTC);
    }
}
