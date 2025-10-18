package ru.saltis.Time_Weather_SPB_DTP.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoJsonVehicle {
    
    @JsonProperty("participants")
    private List<GeoJsonParticipant> participants;

    public List<GeoJsonParticipant> getParticipants() {
        return participants;
    }
    
    public void setParticipants(List<GeoJsonParticipant> participants) {
        this.participants = participants;
    }
}
