package dev.wms.pwrapi.dto.parking;

import lombok.Data;

import java.util.List;

@Data
public class DataWithLabels {
    private List<String> labels;
    private List<String> data;
}
