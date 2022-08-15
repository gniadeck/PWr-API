package dev.wms.pwrapi.utils.generalExceptionHandling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import dev.wms.pwrapi.dto.ExceptionMessagingDTO;

public class ResponseMessageHandler {
    /*
        class does not use provided ObjectMapperJSON static service, it is a conscious decision
            which leads to splitting the mapper associated load (single mapper may be a bottleneck)
     */
    private static final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

    public static String createResponseMessageJSON(String message) throws JsonProcessingException {
        ExceptionMessagingDTO dto = new ExceptionMessagingDTO(message);
        return objectWriter.writeValueAsString(dto);
    }
}
