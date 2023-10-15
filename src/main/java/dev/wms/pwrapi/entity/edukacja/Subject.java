package dev.wms.pwrapi.entity.edukacja;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Subject {

    String id;
    String name;
    List<Group> groups = new ArrayList<Group>();
    String groupsLink;

}
