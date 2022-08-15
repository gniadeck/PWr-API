
package dev.wms.pwrapi.dto.eportal.deserialization;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "fullname",
    "shortname",
    "idnumber",
    "summary",
    "summaryformat",
    "startdate",
    "enddate",
    "visible",
    "showactivitydates",
    "showcompletionconditions",
    "fullnamedisplay",
    "viewurl",
    "courseimage",
    "progress",
    "hasprogress",
    "isfavourite",
    "hidden",
    "showshortname",
    "coursecategory"
})
@Generated("jsonschema2pojo")
@lombok.ToString
public class Course {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("fullname")
    private String fullname;
    @JsonProperty("shortname")
    private String shortname;
    @JsonProperty("idnumber")
    private String idnumber;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("summaryformat")
    private Integer summaryformat;
    @JsonProperty("startdate")
    private Integer startdate;
    @JsonProperty("enddate")
    private Integer enddate;
    @JsonProperty("visible")
    private Boolean visible;
    @JsonProperty("showactivitydates")
    private Boolean showactivitydates;
    @JsonProperty("showcompletionconditions")
    private Object showcompletionconditions;
    @JsonProperty("fullnamedisplay")
    private String fullnamedisplay;
    @JsonProperty("viewurl")
    private String viewurl;
    @JsonProperty("courseimage")
    private String courseimage;
    @JsonProperty("progress")
    private Integer progress;
    @JsonProperty("hasprogress")
    private Boolean hasprogress;
    @JsonProperty("isfavourite")
    private Boolean isfavourite;
    @JsonProperty("hidden")
    private Boolean hidden;
    @JsonProperty("showshortname")
    private Boolean showshortname;
    @JsonProperty("coursecategory")
    private String coursecategory;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("fullname")
    public String getFullname() {
        return fullname;
    }

    @JsonProperty("fullname")
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @JsonProperty("shortname")
    public String getShortname() {
        return shortname;
    }

    @JsonProperty("shortname")
    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    @JsonProperty("idnumber")
    public String getIdnumber() {
        return idnumber;
    }

    @JsonProperty("idnumber")
    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @JsonProperty("summaryformat")
    public Integer getSummaryformat() {
        return summaryformat;
    }

    @JsonProperty("summaryformat")
    public void setSummaryformat(Integer summaryformat) {
        this.summaryformat = summaryformat;
    }

    @JsonProperty("startdate")
    public Integer getStartdate() {
        return startdate;
    }

    @JsonProperty("startdate")
    public void setStartdate(Integer startdate) {
        this.startdate = startdate;
    }

    @JsonProperty("enddate")
    public Integer getEnddate() {
        return enddate;
    }

    @JsonProperty("enddate")
    public void setEnddate(Integer enddate) {
        this.enddate = enddate;
    }

    @JsonProperty("visible")
    public Boolean getVisible() {
        return visible;
    }

    @JsonProperty("visible")
    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    @JsonProperty("showactivitydates")
    public Boolean getShowactivitydates() {
        return showactivitydates;
    }

    @JsonProperty("showactivitydates")
    public void setShowactivitydates(Boolean showactivitydates) {
        this.showactivitydates = showactivitydates;
    }

    @JsonProperty("showcompletionconditions")
    public Object getShowcompletionconditions() {
        return showcompletionconditions;
    }

    @JsonProperty("showcompletionconditions")
    public void setShowcompletionconditions(Object showcompletionconditions) {
        this.showcompletionconditions = showcompletionconditions;
    }

    @JsonProperty("fullnamedisplay")
    public String getFullnamedisplay() {
        return fullnamedisplay;
    }

    @JsonProperty("fullnamedisplay")
    public void setFullnamedisplay(String fullnamedisplay) {
        this.fullnamedisplay = fullnamedisplay;
    }

    @JsonProperty("viewurl")
    public String getViewurl() {
        return viewurl;
    }

    @JsonProperty("viewurl")
    public void setViewurl(String viewurl) {
        this.viewurl = viewurl;
    }

    @JsonProperty("courseimage")
    public String getCourseimage() {
        return courseimage;
    }

    @JsonProperty("courseimage")
    public void setCourseimage(String courseimage) {
        this.courseimage = courseimage;
    }

    @JsonProperty("progress")
    public Integer getProgress() {
        return progress;
    }

    @JsonProperty("progress")
    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    @JsonProperty("hasprogress")
    public Boolean getHasprogress() {
        return hasprogress;
    }

    @JsonProperty("hasprogress")
    public void setHasprogress(Boolean hasprogress) {
        this.hasprogress = hasprogress;
    }

    @JsonProperty("isfavourite")
    public Boolean getIsfavourite() {
        return isfavourite;
    }

    @JsonProperty("isfavourite")
    public void setIsfavourite(Boolean isfavourite) {
        this.isfavourite = isfavourite;
    }

    @JsonProperty("hidden")
    public Boolean getHidden() {
        return hidden;
    }

    @JsonProperty("hidden")
    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    @JsonProperty("showshortname")
    public Boolean getShowshortname() {
        return showshortname;
    }

    @JsonProperty("showshortname")
    public void setShowshortname(Boolean showshortname) {
        this.showshortname = showshortname;
    }

    @JsonProperty("coursecategory")
    public String getCoursecategory() {
        return coursecategory;
    }

    @JsonProperty("coursecategory")
    public void setCoursecategory(String coursecategory) {
        this.coursecategory = coursecategory;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
