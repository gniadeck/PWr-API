package dev.wms.pwrapi.entity.edukacja;

import java.util.ArrayList;

public class Subject {

    String id;
    String name;
    ArrayList<Group> groups = new ArrayList<Group>();
    String groupsLink;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<Group> getGroups() {
        return groups;
    }
    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
    
    public String getGroupsLink() {
        return groupsLink;
    }
    public void setGroupsLink(String groupsLink) {
        this.groupsLink = groupsLink;
    }
    @Override
    public String toString() {
        return "Subject [groupsLink=" + groupsLink.substring(0,20) + "..." + ", id=" + id + ", name=" + name + "]";
    }

    

    


    
}
