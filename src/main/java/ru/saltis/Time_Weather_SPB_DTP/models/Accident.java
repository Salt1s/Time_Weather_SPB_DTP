package ru.saltis.Time_Weather_SPB_DTP.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accidents")
public class Accident {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "datetime", nullable = false)
    private LocalDateTime datetime;
    
    @Column(name = "dead_count", nullable = false)
    private Integer deadCount;

    @Column(name = "road")
    private Integer road;

    @Column(name = "weather")
    private Integer weather;

    @OneToMany(mappedBy = "accident", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Driver> driverList;

    public Accident() {}
    
    public Accident(LocalDateTime datetime, Integer deadCount) {
        this.datetime = datetime;
        this.deadCount = deadCount;
    }

    public Integer getRoad() {
        return road;
    }

    public void setRoad(Integer road) {
        this.road = road;
    }

    public Integer getWeather() {
        return weather;
    }

    public void setWeather(Integer weather) {
        this.weather = weather;
    }

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getDatetime() {
        return datetime;
    }
    
    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
    
    public Integer getDeadCount() {
        return deadCount;
    }
    
    public void setDeadCount(Integer deadCount) {
        this.deadCount = deadCount;
    }
    
    public List<Driver> getDriverList() {
        return driverList;
    }
    
    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
    }
    
    @Override
    public String toString() {
        return "Accident{" +
                "id=" + id +
                ", datetime=" + datetime +
                ", deadCount=" + deadCount +
                '}';
    }
}
