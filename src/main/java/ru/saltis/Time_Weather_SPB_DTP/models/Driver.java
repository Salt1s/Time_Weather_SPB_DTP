package ru.saltis.Time_Weather_SPB_DTP.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "years_exp", nullable = false, length = 10)
    private String yearsExp;
    
    // Связь многие-к-одному с Accident
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acc_id", nullable = false)
    @JsonIgnore
    private Accident accident;
    
    // Конструкторы
    public Driver() {}
    
    public Driver(String gender, Accident accident) {
        this.gender = gender;
        this.accident = accident;
    }
    
    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public Accident getAccident() {
        return accident;
    }
    
    public void setAccident(Accident accident) {
        this.accident = accident;
    }
    
    public String getYearsExp() {
        return yearsExp;
    }
    
    public void setYearsExp(String yearsExp) {
        this.yearsExp = yearsExp;
    }
    
    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", gender='" + gender + '\'' +
                ", accidentId=" + (accident != null ? accident.getId() : null) +
                '}';
    }
}
