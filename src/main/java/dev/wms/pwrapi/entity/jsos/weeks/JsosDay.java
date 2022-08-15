package dev.wms.pwrapi.entity.jsos.weeks;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JsosDay {

    private String date;
    private List<JsosDaySubject> subjects = new ArrayList<JsosDaySubject>();
    
    
}
