package src.main.ca.jrvs.apps.twitter.model;

import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Coordinates {

    @JsonProperty("coordinates")
    private Collection<Float> coordinates;

    @JsonProperty("type")
    private String type;

    // getters and setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Collection<Float> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Collection<Float> coordinates) {
        this.coordinates = coordinates;
    }
}
