package dev.wms.pwrapi.entity.eportal;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MarkSummary {

    private String courseName;
    private List<Mark> marks = new ArrayList<>();


}
