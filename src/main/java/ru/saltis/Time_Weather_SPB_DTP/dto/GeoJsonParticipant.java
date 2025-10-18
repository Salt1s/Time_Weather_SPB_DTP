package ru.saltis.Time_Weather_SPB_DTP.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoJsonParticipant {
    
    @JsonProperty("role")
    private String role;
    
    @JsonProperty("gender")
    private String gender;
    
    @JsonProperty("years_of_driving_experience")
    private Integer yearsOfDrivingExperience;

    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public Integer getYearsOfDrivingExperience() {
        return yearsOfDrivingExperience;
    }
    
    public void setYearsOfDrivingExperience(Integer yearsOfDrivingExperience) {
        this.yearsOfDrivingExperience = yearsOfDrivingExperience;
    }
}
