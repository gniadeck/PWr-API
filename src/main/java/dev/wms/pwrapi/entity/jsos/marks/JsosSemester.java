package dev.wms.pwrapi.entity.jsos.marks;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsosSemester {

    private String semesterName;
    private List<JsosMark> marks;

    
}
