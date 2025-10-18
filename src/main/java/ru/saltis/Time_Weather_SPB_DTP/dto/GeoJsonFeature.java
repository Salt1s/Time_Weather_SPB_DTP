package ru.saltis.Time_Weather_SPB_DTP.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoJsonFeature {
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("properties")
    private GeoJsonProperties properties;
    
    @JsonProperty("geometry")
    private GeoJsonGeometry geometry;

    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public GeoJsonProperties getProperties() {
        return properties;
    }
    
    public void setProperties(GeoJsonProperties properties) {
        this.properties = properties;
    }
    
    public GeoJsonGeometry getGeometry() {
        return geometry;
    }
    
    public void setGeometry(GeoJsonGeometry geometry) {
        this.geometry = geometry;
    }
}
