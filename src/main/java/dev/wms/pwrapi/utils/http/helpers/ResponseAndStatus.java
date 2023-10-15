package dev.wms.pwrapi.utils.http.helpers;

import lombok.*;

@Value(staticConstructor = "of")
public class ResponseAndStatus {
    int statusCode;
    String responseBody;
}
