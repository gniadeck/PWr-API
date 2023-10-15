package dev.wms.pwrapi.dto.usos;


import lombok.*;

import java.math.BigDecimal;

@Builder
@Data
@Setter
@Getter
public class UsosCourse {
    private String name;
    private String code;
    private BigDecimal mark;
    private String teacher;
    private Integer ECTS;
}