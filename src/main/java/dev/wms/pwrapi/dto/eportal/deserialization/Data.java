
package dev.wms.pwrapi.dto.eportal.deserialization;

import java.util.HashMap;
import java.util.List;
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
    "courses",
    "nextoffset"
})
@Generated("jsonschema2pojo")
@lombok.ToString
public class Data {

    @JsonProperty("courses")
    private List<Course> courses = null;
    @JsonProperty("nextoffset")
    private Integer nextoffset;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("courses")
    public List<Course> getCourses() {
        return courses;
    }

    @JsonProperty("courses")
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @JsonProperty("nextoffset")
    public Integer getNextoffset() {
        return nextoffset;
    }

    @JsonProperty("nextoffset")
    public void setNextoffset(Integer nextoffset) {
        this.nextoffset = nextoffset;
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
