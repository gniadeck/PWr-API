package dev.wms.pwrapi.utils.jsonProcessing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperJSON {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String writeValueAsString(Object o) throws JsonProcessingException {
        return objectMapper.writeValueAsString(o);
    }
}
