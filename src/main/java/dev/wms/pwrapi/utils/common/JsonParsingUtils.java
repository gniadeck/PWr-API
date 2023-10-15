package dev.wms.pwrapi.utils.common;

import java.util.Collection;
import java.util.stream.Collectors;

public class JsonParsingUtils {

    public static <T> String collectionToString(Collection<T> collection){
        if(collection == null) return "";
        return collection.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
    }

}