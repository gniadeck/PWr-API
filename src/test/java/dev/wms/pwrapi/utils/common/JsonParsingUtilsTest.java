package dev.wms.pwrapi.utils.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class JsonParsingUtilsTest {

    @Test
    void shouldParseCollectionToStringWithComma(){
        var underTest = List.of("a", "b", "c", "d");
        var expectedResult = "a, b, c, d";

        assertEquals(expectedResult, JsonParsingUtils.collectionToString(underTest));
    }

    @Test
    void shouldReturnEmptyStringOnNull(){
        assertEquals("", JsonParsingUtils.collectionToString(null));
    }


}
