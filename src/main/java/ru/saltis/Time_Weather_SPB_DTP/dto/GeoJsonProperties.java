package ru.saltis.Time_Weather_SPB_DTP.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoJsonProperties {
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("datetime")
    private String datetime;
    
    @JsonProperty("dead_count")
    private Integer deadCount;
    
    @JsonProperty("weather")
    private List<String> weather;
    
    @JsonProperty("road_conditions")
    private List<String> roadConditions;
    
    @JsonProperty("vehicles")
    private List<GeoJsonVehicle> vehicles;
    
    @JsonProperty("participants")
    private List<GeoJsonParticipant> participants;
    
    @JsonProperty("tags")
    private List<String> tags;

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDatetime() {
        return datetime;
    }
    
    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
    
    public Integer getDeadCount() {
        return deadCount;
    }
    
    public void setDeadCount(Integer deadCount) {
        this.deadCount = deadCount;
    }
    
    public List<String> getWeather() {
        return weather;
    }
    
    public void setWeather(List<String> weather) {
        this.weather = weather;
    }
    
    public List<String> getRoadConditions() {
        return roadConditions;
    }
    
    public void setRoadConditions(List<String> roadConditions) {
        this.roadConditions = roadConditions;
    }
    
    public List<GeoJsonVehicle> getVehicles() {
        return vehicles;
    }
    
    public void setVehicles(List<GeoJsonVehicle> vehicles) {
        this.vehicles = vehicles;
    }
    
    public List<GeoJsonParticipant> getParticipants() {
        return participants;
    }
    
    public void setParticipants(List<GeoJsonParticipant> participants) {
        this.participants = participants;
    }
    
    public List<String> getTags() {
        return tags;
    }
    
    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
