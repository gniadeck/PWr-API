package dev.wms.pwrapi.utils.map;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TimestampedContainer <T> {

    private T data;
    private LocalDateTime timestamp;

    public TimestampedContainer(T data) {
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public void setTimestampToNow() {
        this.timestamp = LocalDateTime.now();
    }

    public T getDataAndUpdateTimestamp(LocalDateTime newTimestamp) {
        this.timestamp = newTimestamp;
        return data;
    }

    public T getDataAndUpdateTimestamp() {
        return getDataAndUpdateTimestamp(LocalDateTime.now());
    }
}
